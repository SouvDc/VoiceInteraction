package com.cnbot.voiceinteraction;

import static com.cnbot.irobotvoice.IVoiceConstant.AIUI_NLP;
import static com.cnbot.irobotvoice.IVoiceConstant.BAIDU_ASR;
import static com.cnbot.irobotvoice.IVoiceConstant.BAIDU_TTS;

/**
 * Copyright (c) 2016--2019/9/24  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 语音能力平台配置
 * @FileName: IVoiceCofig.java
 * @author: dc
 * @date: 2019/9/24 18:06
 * @version: 1.0
 */

public interface IVoiceCofig {
    /**
     * 在这里配置语音识别、语音合成、语义分析的平台
     */
    /**
     * asr 平台
     */
    String ASR_MODEL = BAIDU_ASR;

    /**
     * tts 平台
     */
    String TTS_MODEL = BAIDU_TTS;

    /**
     * 语义 平台
     */
    String NLP_MODEL = AIUI_NLP;

}
