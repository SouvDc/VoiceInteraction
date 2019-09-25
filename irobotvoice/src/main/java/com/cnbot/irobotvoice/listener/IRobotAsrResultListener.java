package com.cnbot.irobotvoice.listener;

/**
 * Copyright (c) 2016--2019/2/21  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 小i，语音识别结果监听器
 * @FileName: IRobotAsrResultListener.java
 * @author: dc
 * @date: 2019/2/21 10:25
 * @version: 1.0
 */

public interface IRobotAsrResultListener {

    /**
     * 最终结果
     * @param text
     */
    void asrResultFinal(String text);

    /**
     * 识别错误
     * @param error
     * @param code
     */
    void asrError(String error, int code);

    /**
     * 过程结果
     */
    void asrResult(String text);

    /**
     * 开始识别
     */
    void asrBefore();

    /**
     * 识别结束
     */
    void asrEnd();
}
