package com.cnbot.irobotvoice;

/**
 * 描述：
 * 作者：dc on 2019/9/23 15:44
 * 邮箱：597210600@qq.com
 */
public interface IVoiceConstant {
    /**
     * 在这里配置语音识别、语音合成、语义分析的平台
     */

    String BAIDU_ASR = "baidu";
    String IFLYOS_ASR = "iflyos";
    String ALI_ASR = "ali";

    String BAIDU_TTS = "baidu";
    String IFLYOS_TTS = "iflyos";
    String ALI_TTS = "ali";

    String AIUI_NLP = "讯飞AIUI";
    String UNIT_NLP = "百度UNIT";
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
    String NLP_MODEL = UNIT_NLP;
}
