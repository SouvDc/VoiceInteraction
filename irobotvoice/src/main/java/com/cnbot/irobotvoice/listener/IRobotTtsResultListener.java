package com.cnbot.irobotvoice.listener;

/**
 * Copyright (c) 2016--2019/2/21  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 小i，语音tts结果监听器
 * @FileName: IRobotAsrResultListener.java
 * @author: dc
 * @date: 2019/2/21 10:25
 * @version: 1.0
 */

public interface IRobotTtsResultListener {


    /**
     * 识别错误
     * @param error
     */
    void ttsError(String error);

    /**
     * 开始识别
     */
    void ttsBefore();

    /**
     * 识别结束
     */
    void ttsEnd();
}
