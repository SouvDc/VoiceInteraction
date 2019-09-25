package com.cnbot.irobotvoice.iflyos.listener;

/**
 * Copyright (c) 2016--2019/9/24  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 对于该错误的细分
 * @FileName: IAsrErrorListener.java
 * @author: dc
 * @date: 2019/9/24 18:07
 * @version: 1.0
 */

public interface IAsrErrorListener extends IAsrProcessListener {


    /**
     * 录音失败
     * @see com.iflytek.cloud.ErrorCode#ERROR_AUDIO_RECORD
     */
    void onRecordFailure(String error);

    /**
     * 没有说话，无内容
     * @see com.iflytek.cloud.ErrorCode#MSP_ERROR_NO_DATA
     */
    void onEmptyData(String error);


    /**
     * 网络相关错误
     * @see  com.iflytek.cloud.ErrorCode#ERROR_NO_NETWORK
     * @see  com.iflytek.cloud.ErrorCode#ERROR_NETWORK_TIMEOUT
     * @see  com.iflytek.cloud.ErrorCode#ERROR_NET_EXCEPTION
     */
    void onNetError(String error);


}
