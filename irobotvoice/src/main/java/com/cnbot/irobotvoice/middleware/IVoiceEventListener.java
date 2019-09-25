package com.cnbot.irobotvoice.middleware;

/**
 * Copyright (c) 2016--2019/8/12  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 语音交互中间组建的事件回调方法定义
 * @FileName: IVoiceEventListener.java
 * @author: dc
 * @date: 2019/8/12 17:19
 * @version: 1.0
 */

public interface IVoiceEventListener {

    /**
     * 开始识别
     */
    void beforeAsr();

    /**
     * 识别结束
     */
    void finishAsr();

    /**
     * 识别错误
     * @param error 错误描述
     * @param codeError 错误码
     */
    void errorAsr(String error, int codeError);

    /**
     * 识别结果
     * @param result 识别结果
     */
    void resultAsr(String result);

    /**
     * 动态识别过程
     * @param result
     */
    void resultProcessAsr(String result);

    /**
     * 开始合成
     */
    void beforeTts();

    /**
     * 合成结束
     */
    void finishTts();

    /**
     * 合成错误
     * @param error 错误描述
     * @param codeError 错误码
     */
    void errorTts(String error, int codeError);

    /**
     * 语义分析结果
     * @param result
     */
    void resultNlp(String result);

    /**
     * 语义分析错误
     * @param error
     * @param codeError
     */
    void errorNlp(String error, int codeError);

    /**
     * 语义分析初始化错误
     * @param error
     * @param codeError
     */
    void errorInitNlp(String error, int codeError);

    /**
     * unit语义分析错误
     * @param error
     * @param codeError
     */
    void errorUnitNlp(String error, int codeError);
}
