package com.example.requestlimiter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@WebMvcTest
@ComponentScan
class RequestLimiterControllerTest
{

    @Resource
    private MockMvc mockMvc;

    @Resource
    RequestLimiterService requestLimiterService;

    @Value("${paramN}")
    Integer paramN;

    @Value("${testThreadsCount}")
    Integer testThreadsCount;

    @Test
    void test() throws Exception {

        Callable<Boolean> task = () -> {
            final String threadName = Thread.currentThread().getName();
            for(int i=0;i<=paramN * 2;i++)
            {
                try
                {
                    mockMvc.perform(MockMvcRequestBuilders.get("/request").with(new RequestPostProcessor()
                    {
                        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request)
                        {
                            request.setRemoteAddr(threadName);
                            return request;
                        }
                    }).accept(MediaType.APPLICATION_JSON)).andExpect( i < paramN ? status().isOk() : status().isBadGateway());
                } catch (Exception e) {
                    return false;
                }

            }
            return true;
        };

        ExecutorService executor = Executors.newFixedThreadPool(testThreadsCount);
        assertEquals(executor.submit(task).get(),true);
    }
}