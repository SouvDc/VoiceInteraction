package com.cnbot.irobotvoice.iflyos.listener;


/**
 * Copyright (c) 2016--2019/9/24  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 语音识别过程监听接口
 * @FileName: IAsrProcessListener.java
 * @author: dc
 * @date: 2019/9/24 18:07
 * @version: 1.0
 */

public interface IAsrProcessListener {

    /**
     * 识别的最终结果
     * @param text
     */
    void onAsrResult(String text);

    /**
     * 识别错误
     * @param code 错误码 {@link com.iflytek.cloud.ErrorCode}
     * @param errorDes
     */
    void onAsrError(int code, String errorDes);

    /**
     * 动态识别过程
     * @param text
     */
    void onAsrProcess(String text);

    /**
     * 开始说话
     */
    void onSpeechBegin();

    /**
     * 说话结束
     */
    void onSpeechEnd();
}
