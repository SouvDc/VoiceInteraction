package com.cnbot.irobotvoice.baidu.tts.listener;

/**
 * Created by DengJia on 2018/7/17.
 * Modified by DengJia on 2018/7/19.
 *
 * baidu语音识别接口，将识别结果传回主界面。
 */

public interface SpeechAsrListener {
    /**
     *
     * asr识别结果
     * @param asr
     */
    void asrResult(String asr);

    /**
     * 录音中
     * @param recording
     */
    void asrRecording(boolean recording);

    /**
     * 识别中
     * @param recognizing
     */
    void asrRecognizing(boolean recognizing);

    /**
     * 思考中
     * @param thinking
     */
    void asrThinking(boolean thinking);

    /**
     * 识别结束，界面显示点击说话
     * @param finish
     */
    void asrFinish(boolean finish);

}
