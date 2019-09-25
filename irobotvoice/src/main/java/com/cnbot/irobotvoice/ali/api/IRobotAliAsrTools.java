package com.cnbot.irobotvoice.ali.api;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechRecognizerWithRecorder;
import com.alibaba.idst.util.SpeechRecognizerWithRecorderCallback;
import com.cnbot.irobotvoice.listener.IRobotAsrResultListener;



/**
 * Copyright (c) 2016--2019/4/19  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin
 * @FileName: IRobotAliAsrTools.java
 * @author: dc
 * @date: 2019/4/19 16:03
 * @version: 1.0
 */

public class IRobotAliAsrTools {
    private String TAG = IRobotAliAsrTools.class.getSimpleName();
    /**
     * 正式运行服务器地址
     */
    private final String RELEASE_BASE_ASR_URL = "ws://10.130.17.71:8101/ws/v1";

    /**
     * debug运行服务器地址
     * ;
     */
    private final String DEBUG_BASE_ASR_URL = "ws://asr.demo.xiaoi.net/ws/v1";

    /**
     * 阿里公网运行服务器地址
     */
    private final String DEBUG_BASE_TTS_URL_ALI = "wss://nls-gateway.cn-shanghai.aliyuncs.com/ws/v1";

    /**
     * 正式运行服务器token
     */
    private final String RELEASE_BASE_ASR_TOKEN = "default";

    /**
     * debug运行服务器TOKEN
     */
    private final String DEBUG_BASE_ASR_TOKEN = "273a4bfc3ab2426aaa0f20bcf37497ff";

    /**
     * 正式运行服务器KEY
     */
    private final String RELEASE_BASE_ASR_KEY = "default";

    /**
     * debug运行服务器KEY
     */
    private final String DEBUG_BASE_ASR_KEY = "DyP26Qr9v4ZEUxWE";

    /**
     * 服务器地址, 根据buildConfig的变量DEBUG_SERVER_URL 获取不同的服务器地址
     */
    private String asrURL, asrToken, asrKey;

    private NlsClient client;

    private SpeechRecognizerWithRecorder speechRecognizer;
    /**
     * 语音识别结果监听器
     */
    private IRobotAsrResultListener iRobotAsrResultListener = null;


    private static IRobotAliAsrTools instance = null;

    public static IRobotAliAsrTools getInstance(boolean isDebug){
        if(instance == null){
            synchronized (IRobotAliAsrTools.class){
                if(instance == null){
                    instance = new IRobotAliAsrTools(isDebug);
                }
            }
        }
        return instance;
    }

    public IRobotAliAsrTools(boolean isDebugUrl) {

        asrURL = isDebugUrl == false ?   RELEASE_BASE_ASR_URL :  DEBUG_BASE_TTS_URL_ALI ;
        asrToken = isDebugUrl == false ?   RELEASE_BASE_ASR_TOKEN :  DEBUG_BASE_ASR_TOKEN ;
        asrKey = isDebugUrl == false ?   RELEASE_BASE_ASR_KEY :  DEBUG_BASE_ASR_KEY ;

        Log.e(TAG, "ASR服务器地址 = " + asrURL);
    }


    public void initAsr(){
        //第一步，创建client实例，client只需要创建一次，可以用它多次创建recognizer
        client = new NlsClient();
        initParamter();
    }

    /**
     * 初始化配置
     */
    private void initParamter(){

        // 第二步，新建识别回调类
        SpeechRecognizerWithRecorderCallback callback = new AsrResultCallback();
        // 第三步，创建识别request
        speechRecognizer = client.createRecognizerWithRecorder(callback);

        // 第四步，设置相关参数
        // Token有有效期，请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
        speechRecognizer.setToken(asrToken);
        // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey
        speechRecognizer.setAppkey(asrKey);
        speechRecognizer.setUrl(asrURL);
        // 设置返回中间结果，更多参数请参考官方文档
        // 开启ITN
        speechRecognizer.enableInverseTextNormalization(true);
        // 开启标点
        speechRecognizer.enablePunctuationPrediction(false);
        // 不返回中间结果
        speechRecognizer.enableIntermediateResult(false);
        // 设置打开服务端VAD
        speechRecognizer.enableVoiceDetection(true);
        speechRecognizer.setMaxStartSilence(3000);
        speechRecognizer.setMaxEndSilence(600);
    }


    /**
     * 注册结果监听器
     * @param iRobotAsrResultListener
     */
    public void setiRobotAsrResultListener(IRobotAsrResultListener iRobotAsrResultListener) {
        this.iRobotAsrResultListener = iRobotAsrResultListener;
    }

    /**
     * 移除监听器
     */
    public void removeiRobotAsrResultListener() {
        this.iRobotAsrResultListener = null;
    }



    /**
     * Asr语音识别结果
     */
    public class AsrResultCallback implements SpeechRecognizerWithRecorderCallback {

        @Override
        public void onVoiceData(byte[] bytes, int i) {
            Log.e(TAG, "onVoiceData:  ");
        }

        @Override
        public void onVoiceVolume(int i) {
            Log.e(TAG, "onVoiceVolume:  " + i);
        }

        @Override
        public void onTaskFailed(String s, int i) {
            // 请求失败
            Log.e(TAG, "onASRError: 录音出错 " + s + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrError(s, i);
            }
        }

        @Override
        public void onRecognizedStarted(String s, int i) {
            // 识别开始
            Log.e(TAG, "onStartRecord: 开始录音 Thread: " + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrBefore();
            }
        }

        @Override
        public void onRecognizedCompleted(String s, int i) {
            // 第七步，识别结束，得到最终完整结果
//            stopAsr();
            Log.e(TAG, "onASRResult: 语音识别结果 Thread: " + s + Thread.currentThread().getName());
            String result = "";
            if(iRobotAsrResultListener != null) {
                if (!s.equals("")){
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    if (jsonObject.containsKey("payload")){
                        result = jsonObject.getJSONObject("payload").getString("result");
                    }
                }
                iRobotAsrResultListener.asrResultFinal(result);
            }
        }

        @Override
        public void onRecognizedResultChanged(String s, int i) {

        }

        @Override
        public void onChannelClosed(String s, int i) {
            // 请求结束，关闭连接
            Log.e(TAG, "onStopRecord: 结束录音 Thread: " + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrEnd();
            }
        }
    }

    public void startAsr(){
        if(speechRecognizer != null) {
            Log.e(TAG, "启动asr");
//            initParamter();
            speechRecognizer.start();
        }
    }

    public void stopAsr(){
        if(speechRecognizer != null) {
            Log.e(TAG, "停止asr");
            speechRecognizer.stop();
        }
    }


    public void releaseAsr(){
        stopAsr();
        if (client != null) {
            Log.e(TAG, "释放asr");
            client.release();
        }
    }


}
