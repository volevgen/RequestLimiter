package com.example.requestlimiter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class RequestLimiterService
{
    private Map<String,List<Long>> requestsIp2Time = new HashMap<>();

    @Value("${paramN}")
    Integer paramN;

    @Value("${paramX}")
    Integer paramX;

    private void updateTimes(List<Long> requestTimes)
    {
        long now = System.currentTimeMillis();
        requestTimes.add(now);
        long fromTime = now - TimeUnit.MINUTES.toMillis( paramX );
        requestTimes.removeIf(time -> (fromTime > time));
    }


    boolean checkRequestCount(String ip) {
        List<Long> requestTimes = requestsIp2Time.getOrDefault(ip, new LinkedList<>());
        synchronized (requestTimes)
        {
            updateTimes(requestTimes);
            requestsIp2Time.putIfAbsent(ip,requestTimes);
            return requestTimes.size() <= paramN;
        }
    }
}
