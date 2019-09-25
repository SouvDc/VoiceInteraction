package com.cnbot.irobotvoice.listener;

/**
 * Copyright (c) 2016--2019/2/21  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 小i 语义引擎分析结果
 * @FileName: IRobotNlpResultListener.java
 * @author: dc
 * @date: 2019/2/21 11:23
 * @version: 1.0
 */

public interface IRobotNlpResultListener {

    /**
     * 语义分析结果
     * @param text
     */
    void robotNlpResult(String text);


    /**
     * 语义分析错误
     * @param error
     */
    void robotNlpError(String error);
}
