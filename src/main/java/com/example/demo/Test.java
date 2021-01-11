package com.example.demo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.pojo.CalendarDTO;
import com.example.demo.pojo.CalendarsDTO;
import com.example.demo.pojo.ResultDTO;
import com.example.demo.utils.GsonUtils;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String res = "{\"resCode\":0,\"msg\":null,\"data\":{\"calendars\":[{\"dutyDate\":\"2020-12-10\",\"weekDesc\":\"周四\",\"status\":\"AVAILABLE\"},{\"dutyDate\":\"2020-12-11\",\"weekDesc\":\"周五\",\"status\":\"AVAILABLE\"},{\"dutyDate\":\"2020-12-12\",\"weekDesc\":\"周六\",\"status\":\"SOLD_OUT\"},{\"dutyDate\":\"2020-12-13\",\"weekDesc\":\"周日\",\"status\":\"SOLD_OUT\"},{\"dutyDate\":\"2020-12-14\",\"weekDesc\":\"周一\",\"status\":\"AVAILABLE\"},{\"dutyDate\":\"2020-12-15\",\"weekDesc\":\"周二\",\"status\":\"AVAILABLE\"},{\"dutyDate\":\"2020-12-16\",\"weekDesc\":\"周三\",\"status\":\"AVAILABLE\"}],\"totalWeek\":12,\"fhTimestamp\":1607560200000,\"refreshMilliseconds\":22257226}}";
        JSONObject jsonObject = JSONUtil.parseObj(res);
        JSONArray array = jsonObject.getJSONObject("data").getJSONArray("calendars");
        List<CalendarDTO> calendarDTOS = array.toList(CalendarDTO.class);

        ResultDTO<CalendarsDTO> baseResponseBO = GsonUtils.jsonToBean(res, new TypeToken<ResultDTO<CalendarsDTO>>() {});
        ResultDTO<CalendarsDTO> calendarsDTO = JSONUtil.toBean(res, ResultDTO.class);
        System.out.println(calendarDTOS);
        System.out.println(baseResponseBO.getData().getCalendars());
        System.out.println(calendarsDTO.getData().getCalendars());
    }
}
