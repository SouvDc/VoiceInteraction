package com.cnbot.irobotvoice.ali.api;

import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechRecognizerWithRecorder;
import com.alibaba.idst.util.SpeechRecognizerWithRecorderCallback;
import com.cnbot.irobotvoice.listener.IRobotAsrResultListener;
import com.cnbot.irobotvoice.utils.TimeUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Copyright (c) 2016--2019/4/19  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 一句话识别简易版
 * @FileName: IRobotAliAsrTools.java
 * @author: dc
 * @date: 2019/4/19 16:03
 * @version: 1.0
 */

public class IRobotAlibabaAsrTools {
    private String TAG = IRobotAlibabaAsrTools.class.getSimpleName();
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
    private final String DEBUG_BASE_ASR_TOKEN = "603dbad8ee21421192b6e90b2eb79c1b";
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
    private static IRobotAlibabaAsrTools instance = null;

    public static IRobotAlibabaAsrTools getInstance(boolean isDebug){
        if(instance == null){
            synchronized (IRobotAlibabaAsrTools.class){
                if(instance == null){
                    instance = new IRobotAlibabaAsrTools(isDebug);
                }
            }
        }
        return instance;
    }

    public IRobotAlibabaAsrTools(boolean isDebugUrl) {
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

        //UI在主线程更新
//        Handler handler= new MyHandler(this);
        // 第二步，新建识别回调类
        SpeechRecognizerWithRecorderCallback callback = new MyCallback();

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
        speechRecognizer.setMaxEndSilence(800);
        speechRecognizer.setParams("{\"format\":\"pcm\"}");

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


    private class MyCallback implements SpeechRecognizerWithRecorderCallback {

        @Override
        public void onVoiceData(byte[] bytes, int i) {
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
//            Log.e(TAG, "onVoiceVolume:  " + i);
        }

        @Override
        public void onTaskFailed(String s, int i) {
            isAsr = false;
            // 请求失败
            Log.e(TAG, "onASRError: 录音出错 " + s + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrError( s, i);
            }
            if(i == 10000001){
                //重新初始化
                initAsr();
            }
        }

        @Override
        public void onRecognizedStarted(String s, int i) {
            // 识别开始
            isAsr = true;
            Log.e("开始拾音", TimeUtil.getFormatToday(TimeUtil.FORMAT_DATE_TIME_SECOND_2));
            Log.e(TAG, "onStartRecord: 开始录音 Thread: " + Thread.currentThread().getName());
            if(iRobotAsrResultListener != null) {
                iRobotAsrResultListener.asrBefore();
            }
        }

        @Override
        public void onRecognizedCompleted(String s, int i) {
            Log.e("结束拾音", TimeUtil.getFormatToday(TimeUtil.FORMAT_DATE_TIME_SECOND_2));

            isAsr = false;
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
}
