package com.example.demo.task;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.example.demo.pojo.*;
import com.example.demo.utils.GsonUtils;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class JingFengMonitor {

    @PostConstruct
    public void monitor(){
        String url = "http://100000552840.yuyue.n.weimob.com/api3/interactive/advance/microbook/mobile/getAvailableCalendar";
        String cookies = "rprm_cuid=3124274306btvhslvm38; saas.express.session=s%3AZS5mkQfB1qL8JwtQi3ylbCJ175n5eK0v.v%2FyfCOpp1wc%2FkPUEFQX%2FvA%2F2RwS9vq8FOm8RUC3a5WA";
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sno", "12556");
        hashMap.put("pid", "100000552840");
        long lastNotifyTime = System.currentTimeMillis() - 3 * 60 * 1000;
        while (true){
            try {
                HttpRequest httpRequest = HttpRequest.post(url);
                httpRequest.cookie(cookies);
                httpRequest.header(Header.REFERER, "http://100000552840.yuyue.n.weimob.com/saas/yuyue/100000552840/12556/calendar?pno=", false);
                httpRequest.header(Header.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) MicroMessenger/6.8.0(0x16080000) MacWechat/2.4.2(0x12040210) NetType/WIFI WindowsWechat", false);
                httpRequest.header(Header.ACCEPT, "application/json, text/plain, */*");
                httpRequest.header(Header.CONNECTION, "keep-alive");
                httpRequest.header(Header.ACCEPT_ENCODING, "gzip, deflate, br");
                httpRequest.header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
                httpRequest.header(Header.CONTENT_TYPE, "application/json;charset=UTF-8");
                hashMap.put("calendarDate", System.currentTimeMillis());
                String res = httpRequest.body(JSONUtil.toJsonPrettyStr(hashMap)).timeout(6000).execute().body();
                log.info(res);
                JingFengResultDTO<Items> resultDTO = GsonUtils.jsonToBean(res, new TypeToken<JingFengResultDTO<Items>>() {
                });
                for (ItemsDTO itemsDTO : resultDTO.getData().getItems()) {
                    if (!Integer.valueOf(0).equals(itemsDTO.getStockNum()) && itemsDTO.getStockNum() > 5 && System.currentTimeMillis()-lastNotifyTime > 3*60*1000) {
                        HttpUtil.get("https://sc.ftqq.com/SCU135702T4d9eb61083345ff9bc246a0e755e0b0d5fd879eaa898e.send?text=京丰放货:日期["+itemsDTO.getBookDivideTimes()+"],数量["+itemsDTO.getStockNum()+"]!&desp="+ itemsDTO);
                        log.info("发送提醒."+itemsDTO);
                        lastNotifyTime = System.currentTimeMillis();

                    }
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000,9000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
