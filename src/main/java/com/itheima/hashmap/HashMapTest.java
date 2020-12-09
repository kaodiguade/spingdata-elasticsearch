package com.itheima.hashmap;

import com.itheima.scope.PublicTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapTest {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("杨过", "2018");
        map.put("小龙女", "2019");

        Map concurrentHashMap = new ConcurrentHashMap<String,String>();
        System.out.println(map.get("杨过"));

        for (String key :  map.keySet()) {
            int i = key.hashCode();
            System.out.println("hashCode:"+i);
        }
    }
}
