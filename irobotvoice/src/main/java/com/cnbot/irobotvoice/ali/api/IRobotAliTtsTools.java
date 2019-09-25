package com.cnbot.irobotvoice.ali.api;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechSynthesizer;
import com.alibaba.idst.util.SpeechSynthesizerCallback;
import com.cnbot.irobotvoice.listener.IRobotTtsResultListener;
import com.cnbot.irobotvoice.utils.TimeUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (c) 2016--2019/5/20  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin ali tts
 * @FileName: IRobotAliTtsTools.java
 * @author: dc
 * @date: 2019/5/20 20:09
 * @version: 1.0
 */

public class IRobotAliTtsTools {
    public static final String TAG = IRobotAliTtsTools.class.getSimpleName();

    private NlsClient client;
    private SpeechSynthesizer speechSynthesizer;
    private AudioPlayer audioPlayer;


    /**
     * 小i ，合成结果监听器
     */
    private IRobotTtsResultListener iRobotTtsResultListener;

    /**
     * 正式运行服务器地址
     */
    private final String RELEASE_BASE_TTS_URL = "ws://10.130.17.71:8101/ws/v1";

    /**
     * debug运行服务器地址
     */
    private final String DEBUG_BASE_TTS_URL = "wss://nls-gateway.cn-shanghai.aliyuncs.com/ws/v1";

    /**
     * 正式运行服务器token
     */
    private final String RELEASE_BASE_TTS_TOKEN = "default";

    /**
     * debug运行服务器TOKEN
     */
private final String DEBUG_BASE_TTS_TOKEN = "d1c6dc808b5841f2ace05fface836668";    /**
     * 正式运行服务器KEY
     */
    private final String RELEASE_BASE_TTS_KEY = "default";

    /**
     * debug运行服务器KEY
     */
    private final String DEBUG_BASE_TTS_KEY = "DyP26Qr9v4ZEUxWE";


    /**
     * 服务器地址, 根据buildConfig的变量DEBUG_SERVER_URL 获取不同的服务器地址
     */
    private String ttsUrl, ttsToken, ttsKey;

    private static IRobotAliTtsTools instance = null;

    public static IRobotAliTtsTools getInstance(boolean isDebug){
        if(instance == null){
            synchronized (IRobotAliTtsTools.class){
                if(instance == null){
                    instance = new IRobotAliTtsTools(isDebug);
                }
            }
        }
        return instance;
    }

    public static IRobotAliTtsTools get(){
        return instance;
    }

    public IRobotAliTtsTools(boolean isDebugUrl) {

        ttsUrl = isDebugUrl == false ?   RELEASE_BASE_TTS_URL :  DEBUG_BASE_TTS_URL ;
        ttsToken = isDebugUrl == false ?   RELEASE_BASE_TTS_TOKEN :  DEBUG_BASE_TTS_TOKEN ;
        ttsKey = isDebugUrl == false ?   RELEASE_BASE_TTS_KEY :  DEBUG_BASE_TTS_KEY ;
        Log.e(TAG, "tts服务器地址 = " + ttsUrl + "   " + ttsKey + "  " + ttsToken);
        initTts();
    }

    /**
     * 初始化
     */
    private void initTts(){

        audioPlayer = new AudioPlayer();
        // 第一步，创建client实例，client只需要创建一次，可以用它多次创建transcriber
        client = new NlsClient();
//        initParamter();
    }

    /**
     * 开始合成，但是在家需要排查start之前是否需要stoptts
     * @param text
     */
    public void  startTts(String text){
        try {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            stopTts();
            Log.e(TAG, "tts被调用");
            // 第二步，定义语音合成回调类
            MyCallback callback = new MyCallback();
            // 第三步，创建语音合成对象
            speechSynthesizer = client.createSynthesizerRequest(callback);
            callback.setSynthesizer(speechSynthesizer);

            // 第四步，设置token和appkey
            // Token有有效期，请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
            speechSynthesizer.setToken(ttsToken);
            // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey
            speechSynthesizer.setAppkey(ttsKey);
            speechSynthesizer.setUrl(ttsUrl);


            Log.i(TAG, "Set chosen voice " + SpeechSynthesizer.VOICE_SIYUE);
            speechSynthesizer.setVoice(SpeechSynthesizer.VOICE_SIYUE);
            // 设置语速
            Log.i(TAG, "User set speechRate " + 0);
            speechSynthesizer.setSpeechRate(0);
            // 设置要转为语音的文本
            speechSynthesizer.setText(text);
            // 设置语音数据采样率
            speechSynthesizer.setSampleRate(SpeechSynthesizer.SAMPLE_RATE_16K);
            // 设置语音编码，pcm编码可以直接用audioTrack播放，其他编码不行
            speechSynthesizer.setFormat(SpeechSynthesizer.FORMAT_PCM);


        /*// 第五步，设置相关参数，以下选项都会改变最终合成的语音效果，可以按文档调整试听效果
        // 设置人声
        Log.i(TAG, "Set chosen voice " + SpeechSynthesizer.VOICE_XIAOMEI);
        speechSynthesizer.setVoice(SpeechSynthesizer.VOICE_XIAOMEI);
        // 设置语速
        Log.i(TAG, "User set speechRate " + 0);
        speechSynthesizer.setSpeechRate(0);
        // 设置要转为语音的文本
        speechSynthesizer.setText(text);
        // 设置语音数据采样率
        speechSynthesizer.setSampleRate(SpeechSynthesizer.SAMPLE_RATE_16K);
        // 设置语音编码，pcm编码可以直接用audioTrack播放，其他编码不行
        speechSynthesizer.setFormat(SpeechSynthesizer.FORMAT_PCM);
*/
            // 第六步，开始合成
            try {
                if (speechSynthesizer != null) {
                    if (speechSynthesizer.start() < 0) {
                        speechSynthesizer.stop();
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isTts = true;
            Log.d(TAG, "speechSynthesizer start done");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 停止tts
     */
    public void stopTts(){
        try {
            if (speechSynthesizer != null) {
                speechSynthesizer.cancel();
                audioPlayer.stop();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    // 语音识别回调类，得到语音识别结果后在这里处理
    // 注意不要在回调方法里执行耗时操作
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BJNewAirportRobot" + File.separator;
    public static final String IMAGE_TEMP_PATH = BASE_PATH + "TTS";
    private static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-ssss");
    String fileName = "";
    boolean isTts = false;
    boolean isSaveFile = false;


    private class MyCallback implements SpeechSynthesizerCallback {
        private WeakReference<SpeechSynthesizer> synthesizerWeakReference;

        public void setSynthesizer(SpeechSynthesizer speechSynthesizer) {
            synthesizerWeakReference = new WeakReference<>(speechSynthesizer);
        }

        /**
         * 语音合成开始的回调
         * @param msg
         * @param code
         */
        @Override
        public void onSynthesisStarted(String msg, int code)
        {
            isTts = true;
            Log.e(TAG, "TTS拾音合成开始:" + TimeUtil.getFormatToday(TimeUtil.FORMAT_DATE_TIME_SECOND_2));
            Log.d(TAG,"OnSynthesisStarted " + msg + ": " + String.valueOf(code));
            if(iRobotTtsResultListener != null){
                iRobotTtsResultListener.ttsBefore();
            }
        }


        /**
         * 第七步，获取音频数据的回调，在这里把音频写入播放器
         * @param data
         * @param code
         */
        @Override
        public void onBinaryReceived(byte[] data, int code)
        {

            Log.i(TAG, "binary received length: " + data.length);
            if(audioPlayer != null) {
                Log.i(TAG,"语音播报");
                audioPlayer.setAudioData(data);
            }
            if(isTts){
                // 进行文件保存
                if(!isSaveFile) {
                    String time = format.format(new Date());
                    fileName = "tts-" + time + "-" + ".pcm";
                    Log.e(TAG, "保存的音频文件 ： " + fileName);
                    isSaveFile = true;
                }
//                ByteUtils.saveFileToByte(IMAGE_TEMP_PATH, fileName, data, false);
            }

        }


        /**
         * 语音合成完成的回调，在这里关闭播放器
         * @param msg
         * @param code
         */
        @Override
        public void onSynthesisCompleted(final String msg, int code)
        {
            //
            isTts = true;
            Log.e(TAG, "TTS拾音合成结束:" + TimeUtil.getFormatToday(TimeUtil.FORMAT_DATE_TIME_SECOND_2));
            Log.d(TAG,"OnSynthesisCompleted " + msg + ": " + String.valueOf(code));
            // 第八步，结束合成
            if(synthesizerWeakReference != null ) {
                if(synthesizerWeakReference.get() != null) {
                    synthesizerWeakReference.get().stop();
                }
            }
            if(iRobotTtsResultListener != null){
                iRobotTtsResultListener.ttsEnd();
            }
        }


        /**
         *  调用失败的回调
         * @param msg
         * @param code
         */
        @Override
        public void onTaskFailed(String msg, int code)
        {
            stopSave();
            Log.d(TAG,"OnTaskFailed " + msg + ": " + String.valueOf(code));
            // 第八步，结束合成
            if(synthesizerWeakReference != null) {
                if(synthesizerWeakReference.get() != null) {
                    synthesizerWeakReference.get().stop();
                }
            }
            if(iRobotTtsResultListener != null){
                iRobotTtsResultListener.ttsError(msg);
            }
        }

        /**
         * 连接关闭的回调
         * @param msg
         * @param code
         */
        @Override
        public void onChannelClosed(String msg, int code) {
            stopSave();
            Log.d(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }
    }

    /**
     * 注册tts监听器
     * @param iRobotTtsResultListener
     */
    public void setiRobotTtsResultListener(IRobotTtsResultListener iRobotTtsResultListener) {
        this.iRobotTtsResultListener = iRobotTtsResultListener;
    }

    /**
     * 注销tts监听器
     */
    public void removeiRobotTtsResultListener() {
        this.iRobotTtsResultListener = null;
    }


    /**
     * 释放tts
     */
    public void releaseTts(){
        try {
            // 最终，释放客户端
            if (audioPlayer != null) {
                audioPlayer.stop();
                audioPlayer.release();
            }
            if (client != null) {
                client.release();
            }
            if(speechSynthesizer != null){
                speechSynthesizer.cancel();
                speechSynthesizer = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void stopSave(){
//        ByteUtils.saveFileToByte(IMAGE_TEMP_PATH, fileName, null, true);
        isTts = false;
        isSaveFile = false;
    }
}
