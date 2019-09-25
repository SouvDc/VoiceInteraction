package com.cnbot.irobotvoice.unit;

import android.util.Log;

import com.cnbot.irobotvoice.unit.utils.HttpUtil;


/**
 * Copyright (c) 2016--2019/9/9  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin unit对话服务
 * @FileName: ChatService.java
 * @author: dc
 * @date: 2019/9/9 15:13
 * @version: 1.0
 */

public class ChatService {


    public static String utterance(String text) {
        // 请求URL
        String talkUrl = "https://aip.baidubce.com/rpc/2.0/unit/service/chat";
        try {
            // 请求参数
            String params = "{\"log_id\":\"UNITTEST_10000\",\"version\":\"2.0\",\"service_id\":\"S21524\",\"session_id\":\"\",\"request\":{\"query\":\"" +
                    text +
                    "\",\"user_id\":\"88888\"},\"dialog_state\":{\"contexts\":{\"SYS_REMEMBERED_SKILLS\":[\"1057\"]}}}";
            Log.e("DC", "params = " + params);
            String accessToken = IConstant.accessToken;
            String result = HttpUtil.post(talkUrl, accessToken, "application/json", params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DC", "unit请求异常 = " + e.getMessage());
        }
        return null;
    }
    public static void main(String[] args) {
//        utterance();
    }
}
