package com.cnbot.voiceinteraction;

import android.app.Application;

import com.cnbot.irobotvoice.IVoiceConstant;
import com.cnbot.irobotvoice.iflyos.IflyosRecognize;

/**
 * Copyright (c) 2016--2019/9/24  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin application
 * @FileName: VoiceApplication.java
 * @author: dc
 * @date: 2019/9/24 17:35
 * @version: 1.0
 */
public class VoiceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppHelper.init(this.getApplicationContext());

        if(IVoiceCofig.ASR_MODEL.equals(IVoiceConstant.IFLYOS_ASR)){
            // 讯飞平台，需要在application中进行初始化配置
            // 初始化讯飞语音识别
            IflyosRecognize.get().createSpeeck(VoiceApplication.this);
        }
    }
}
