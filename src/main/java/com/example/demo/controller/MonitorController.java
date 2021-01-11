package com.example.demo.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/monitor")
@Slf4j
public class MonitorController {
    private boolean switchFlag = false;
    private JsonMapper jsonMapper = new JsonMapper();
    private static Map<String,String> skuMap = Maps.newHashMap();
    private static Map<String,Long> notifyMap = Maps.newHashMap();

    static {
        skuMap.put("100006622009","茅台 53%vol 500ml陈年茅台酒（15）");
        skuMap.put("100006622007","茅台 53%vol 500ml陈年茅台酒（30）");
        skuMap.put("100016241002","贵州茅台酒 第十届贵州酒博会纪念酒 53度 500ml");
        skuMap.put("100014530230","茅台飞天 2019年 53度 500ml");
        skuMap.put("100006622027","茅台 庚子鼠年生肖酒 酱香型白酒 53度 500ml*6瓶 整箱装");
        skuMap.put("100006622035","茅台 庚子鼠年生肖酒 酱香型白酒 53度 500ml");
    }

    @RequestMapping("/start")
    public String start() {
        switchFlag = true;
        new Thread(() -> {
            check();
        }).start();
        return "开关已打开";
    }

    @RequestMapping("/close")
    public String close() {
        switchFlag = false;
        return "开关已关闭";
    }

    @PostConstruct
    public void init() {
        switchFlag = true;
        new Thread(() -> {
            check();
        }).start();
    }

    @RequestMapping("/allSku")
    public String getAllSku(){
        return JSONUtil.toJsonStr(skuMap);
    }

    @RequestMapping("/addSku")
    public String addSku(String skuId, String title){
        skuMap.put(skuId, title);
        return JSONUtil.toJsonStr(skuMap);
    }

    @RequestMapping("/delSku")
    public String delSku(String skuId){
        skuMap.remove(skuId);
        return JSONUtil.toJsonStr(skuMap);
    }

    public void check() {
        log.info("开启监控......");
        while (true && switchFlag) {
            try {
                String skuString = Joiner.on(",").join(skuMap.keySet());
                checkSkus(skuString);
                Thread.sleep(ThreadLocalRandom.current().nextInt(5000, 9000));
            } catch (Exception e) {
                log.error("异常:", e);
            }
        }
        log.info("监控关闭......");
    }

    private void checkSkus(String skuString) throws Exception {
        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
        int num = ThreadLocalRandom.current().nextInt(1000000, 9999999);
        String url = new StringBuilder("https://c0.3.cn/stocks?callback=jQuery").append(num).append("&type=getstocks&skuIds=").append(skuString).append("&area=1_72_2819_0&_=").append(System.currentTimeMillis()).toString();
        String s = HttpUtil.get(url, 3000);
        log.info(s);
        String s1 = s.replaceAll("jQuery" + num + "\\(", "");
        String s2 = s1.replaceAll("\\)", "");
        JsonNode jsonNode = jsonMapper.readTree(s2);
        Iterator<String> fieldNames = jsonNode.fieldNames();
        while (fieldNames.hasNext()){
            String skuId = fieldNames.next();
            JsonNode node = jsonNode.get(skuId);
            Integer skuState = node.get("skuState").asInt();
            Integer stockState = node.get("StockState").asInt();
            if (skuState.equals(1) && (stockState.equals(33) || stockState.equals(40) || stockState.equals(36))) {
                //再次单个调用确认
                if (skuString.contains(",")){
                    checkSkus(skuId);
                }else {
                    notifyWX(skuId);
                }
            }
        }
    }

    private void notifyWX(String skuId) {
        Long lastTime = notifyMap.get(skuId);
        //一分钟内只提醒一次
        if (null != lastTime && System.currentTimeMillis() - lastTime <= 61 * 1000){
            return;
        }
        notifyMap.put(skuId, System.currentTimeMillis());
        String url = "https://item.jd.com/"+skuId+".html";
        HttpUtil.get("https://sc.ftqq.com/SCU135702T4d9eb61083345ff9bc246a0e755e0b0d5fd879eaa898e.send?text="+skuMap.get(skuId)+" 放货了!&desp="+URLEncoder.encode(url));
        log.info("检测到放货sku[{}]商品名字[{}].",skuId,skuMap.get(skuId));
    }
}
