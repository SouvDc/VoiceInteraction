package com.cnbot.irobotvoice.ali.api;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechTranscriberWithRecorder;
import com.alibaba.idst.util.SpeechTranscriberWithRecorderCallback;
import com.cnbot.irobotvoice.listener.IRobotAsrResultListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Copyright (c) 2016--2019/4/19  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 实时长语音识别简易版
 * @FileName: IRobotAliAsrTools.java
 * @author: dc
 * @date: 2019/4/19 16:03
 * @version: 1.0
 */

public class IRobotAliTranAsrTools {
    private String TAG = IRobotAliTranAsrTools.class.getSimpleName();
    /**
     * 正式运行服务器地址
     */
    private final String RELEASE_BASE_ASR_URL = "ws://10.130.17.71:8101/ws/v1";
    /**
     * debug运行服务器地址
     */
    private final String DEBUG_BASE_ASR_URL = "ws://asr.demo.xiaoi.net/ws/v1";
    /**
     * 阿里公网运行服务器地址
     */
    private final String DEBUG_BASE_ASR_URL_ALI = "wss://nls-gateway.cn-shanghai.aliyuncs.com/ws/v1";
    /**
     * 正式运行服务器token
     */
    private final String RELEASE_BASE_ASR_TOKEN = "default";
    /**
     * debug运行服务器TOKEN
     */
    private final String DEBUG_BASE_ASR_TOKEN = "d1c6dc808b5841f2ace05fface836668";
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

    private SpeechTranscriberWithRecorder speechRecognizer;
    /**
     * 语音识别结果监听器
     */
    private IRobotAsrResultListener iRobotAsrResultListener = null;
    private static IRobotAliTranAsrTools instance = null;

    public static IRobotAliTranAsrTools getInstance(boolean isDebug){
        if(instance == null){
            synchronized (IRobotAliTranAsrTools.class){
                if(instance == null){
                    instance = new IRobotAliTranAsrTools(isDebug);
                }
            }
        }
        return instance;
    }

    public IRobotAliTranAsrTools(boolean isDebugUrl) {
        asrURL = isDebugUrl == false ?   RELEASE_BASE_ASR_URL :  DEBUG_BASE_ASR_URL_ALI ;
        asrToken = isDebugUrl == false ?   RELEASE_BASE_ASR_TOKEN :  DEBUG_BASE_ASR_TOKEN ;
        asrKey = isDebugUrl == false ?   RELEASE_BASE_ASR_KEY :  DEBUG_BASE_ASR_KEY ;

        Log.e(TAG, "ASR服务器地址 = " + asrURL + "   token = " + asrToken + "    key = " + asrKey);
        initAsr();
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

    public void initAsr(){
        //第一步，创建client实例，client只需要创建一次，可以用它多次创建recognizer
        client = new NlsClient();
        Log.e(TAG, "initAsr");
    }

    private void initParameter(){

        // 第二步，新建识别回调类
        SpeechTranscriberWithRecorderCallback callback = new AsrResultCallback();

        // 第三步，创建识别request
        speechRecognizer = client.createTranscriberWithRecorder(callback);

        // 第四步，设置相关参数
        // Token有有效期，请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
        speechRecognizer.setToken(asrToken);
        // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey
        speechRecognizer.setAppkey(asrKey);
        speechRecognizer.setUrl(asrURL);
        // 设置返回中间结果，更多参数请参考官方文档
        speechRecognizer.enableIntermediateResult(true);
        // 开启标点
        speechRecognizer.enablePunctuationPrediction(true);
        // 开启ITN
        speechRecognizer.enableInverseTextNormalization(true);
        speechRecognizer.setMaxSentenceSilence(1000);
        // 设置打开服务端VAD
        // 设置静音断句长度
//        speechRecognizer.setMaxSentenceSilence(1000);
//        speechRecognizer.setParams("{\"format\":\"pcm\"}");

    }


    // 此方法内启动录音和识别逻辑，为了代码简单便于理解，没有加防止用户重复点击的逻辑，用户应该在真实使用场景中注意
    public void startAsr(){
        initParameter();
        // 设置定制模型和热词
//         speechRecognizer.setCustomizationId("hangban_vocab");
         speechRecognizer.setVocabularyId("hangban_vocab");
        if(speechRecognizer != null) {
            speechRecognizer.start();
        }
    }

    public void stopAsr(){
        // 第八步，停止本次识别
        if(speechRecognizer != null) {
            Log.e(TAG, "停止识别");
            speechRecognizer.stop();
        }
    }

    public void releaseAsr() {
        if (speechRecognizer != null) {
            speechRecognizer.stop();
        }
        // 最终，退出时释放client
        if(client != null) {
            client.release();
        }

    }
    // 语音识别回调类，得到语音识别结果后在这里处理
    // 注意不要在回调方法里执行耗时操作
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BJNewAirportRobot" + File.separator;
    public static final String IMAGE_TEMP_PATH = BASE_PATH + "VOICE";
    private static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-ssss");
    String fileName = "";
    boolean isAsr = false;
    boolean isSaveFile = false;


    /**
     * Asr语音识别结果
     */
    public class AsrResultCallback implements SpeechTranscriberWithRecorderCallback {
        @Override
        public void onVoiceData(byte[] bytes, int i) {
            Log.e(TAG, "音频数据");
            if(isAsr){
                // 进行文件保存
                if(!isSaveFile) {
                    String time = format.format(new Date());
                    fileName = "asr-" + time + "-" + ".pcm";
                    Log.e(TAG, "保存的音频文件 ： " + fileName);
                    isSaveFile = true;
                }
//                ByteUtils.saveFileToByte(IMAGE_TEMP_PATH, fileName, bytes, false);
            }
        }

        @Override
        public void onVoiceVolume(int i) {

        }

        @Override
        public void onTranscriptionStarted(String s, int i) {
            // 识别开始
            isAsr = true;
            Log.e(TAG, "onStartRecord: 开始录音 Thread: " + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrBefore();
            }
        }

        @Override
        public void onTranscriptionCompleted(String s, int i) {
            Log.e(TAG, "onTranscriptionCompleted:  Thread: " + Thread.currentThread().getName());
            isAsr = false;
        }

        @Override
        public void onTranscriptionResultChanged(String s, int i) {
//            Log.e(TAG, "onTranscriptionResultChanged:  Thread: " + Thread.currentThread().getName());
            Log.d(TAG,"OnTranscriptionResultChanged " + s + ": " + String.valueOf(i));
        }

        @Override
        public void onSentenceBegin(String s, int i) {
            Log.e(TAG, "onSentenceBegin:  Thread: " + Thread.currentThread().getName());

        }

        @Override
        public void onSentenceEnd(String s, int i) {
            isAsr = false;
            // 第七步，识别结束，得到最终完整结果
            stopAsr();
            Log.e(TAG, "onSentenceEnd: 语音识别结果 Thread: " + s + Thread.currentThread().getName());
            String result = "";
            if(iRobotAsrResultListener != null) {
                if (!s.equals("")){
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    if (jsonObject.containsKey("payload")){
                        result = jsonObject.getJSONObject("payload").getString("result");
                    }
                }
                if(!TextUtils.isEmpty(result)) {
                    iRobotAsrResultListener.asrResultFinal(result);
                } else {
                    iRobotAsrResultListener.asrError("抱歉，我没有听清楚，请再说一遍吧",41010105);
                }
            }
        }

        @Override
        public void onTaskFailed(String s, int i) {
            isAsr = false;
            // 请求失败
            Log.e(TAG, "onTaskFailed: 录音出错 " + s + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrError(s,i);
            }
        }

        @Override
        public void onChannelClosed(String s, int i) {
            isAsr = false;
            // 请求结束，关闭连接
            Log.e(TAG, "onChannelClosed: 结束录音 Thread: " + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrEnd();
            }
        }
    }
}
