package com.example.demo.task;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.example.demo.pojo.CalendarDTO;
import com.example.demo.pojo.CalendarsDTO;
import com.example.demo.pojo.ResultDTO;
import com.example.demo.utils.GsonUtils;
import com.google.common.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

//@Component
@Slf4j
public class Task_114 {


//    @Scheduled(initialDelay = 1000, fixedRate = 3000)
    public void run(){
        String url = "https://www.114yygh.com/web/product/list?_time=";

        String cookies = "pgv_pvi=3741152256; __jsluid_s=69d551284c28cb741f74b06efe495aaf; pgv_si=s6975501312; secure-key=d582e779-b331-4868-ac0e-94bbfbd0e92c; hyde_session=a50Vv55cMzKW5xA9FfZCTzUp2361TV6U_5358356; cmi-user-ticket=JOXNoZZLkZyaGrKYUb6iK93cjTX-aee1-SjMVQ..; hyde_session_tm=1607507122896";

        HttpRequest httpRequest = HttpRequest.post(url + System.currentTimeMillis());
        httpRequest.header(Header.REFERER, "https://www.114yygh.com/hospital/91/e24a4910ede3556e481cfed0fe475545/200047652/source", false);
        httpRequest.header(Header.USER_AGENT, "Mozilla/5.0 (Macintosh;Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36", false);
        httpRequest.header("Request-Source", "PC", false);
        httpRequest.header("Sec-Fetch-Dest", "empty", false);
        httpRequest.header("Sec-Fetch-Dest", "empty", false);
        httpRequest.header("Sec-Fetch-Mode", "cors", false);
        httpRequest.header("Sec-Fetch-Site", "same-origin", false);
        httpRequest.cookie(cookies);
        httpRequest.header(Header.ACCEPT, "application/json, text/plain, */*");
        httpRequest.header(Header.ACCEPT_ENCODING, "gzip, deflate, br");
        httpRequest.header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        httpRequest.header(Header.CONNECTION, "keep-alive");
        httpRequest.header(Header.CONTENT_LENGTH, "105");
        httpRequest.header(Header.CONTENT_TYPE, "application/json;charset=UTF-8");
        httpRequest.header(Header.HOST, "www.114yygh.com");
        httpRequest.header(Header.ORIGIN, "https://www.114yygh.com");
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("firstDeptCode", "e24a4910ede3556e481cfed0fe475545");
            hashMap.put("hosCode", "91");
            hashMap.put("secondDeptCode", "200047652");
            hashMap.put("week", "1");

            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("firstDeptCode", "8830eefc0f7e8a23e215a621ea5c5413");
            hashMap2.put("hosCode", "91");
            hashMap2.put("secondDeptCode", "200042798");
            hashMap2.put("week", "1");
            while (true){
                long currentTime = System.currentTimeMillis();
                String res = httpRequest.body(JSONUtil.toJsonPrettyStr(hashMap)).timeout(6000).execute().body();
                ResultDTO<CalendarsDTO> resultDTO = GsonUtils.jsonToBean(res, new TypeToken<ResultDTO<CalendarsDTO>>() {});
                log.info("{},耗时[{}]ms",resultDTO.getData().getCalendars().stream().filter(calendarDTO -> "2020-12-13".equals(calendarDTO.getDutyDate())).findFirst().get(), System.currentTimeMillis()-currentTime);
                if (resultDTO.getData().getCalendars().stream().filter(c -> "2020-12-13".equals(c.getDutyDate())).map(CalendarDTO::getStatus).findFirst().orElse("NO_INVENTORY").equals("AVAILABLE")) {
                    log.info("周末有号!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                long currentTime2 = System.currentTimeMillis();
                String res2 = httpRequest.body(JSONUtil.toJsonPrettyStr(hashMap2)).timeout(6000).execute().body();
                ResultDTO<CalendarsDTO> resultDTO2 = GsonUtils.jsonToBean(res2, new TypeToken<ResultDTO<CalendarsDTO>>() {});
                log.info("{},耗时[{}]ms",resultDTO2.getData().getCalendars().stream().filter(calendarDTO -> "2020-12-13".equals(calendarDTO.getDutyDate())).findFirst().get(), System.currentTimeMillis()-currentTime2);
                if (resultDTO2.getData().getCalendars().stream().filter(c -> "2020-12-13".equals(c.getDutyDate())).map(CalendarDTO::getStatus).findFirst().orElse("NO_INVENTORY").equals("AVAILABLE")) {
                    log.info("周末有号!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                Thread.sleep(20*1000);
            }
        } catch (Exception e) {
            log.error("有异常");
        }

    }
}
