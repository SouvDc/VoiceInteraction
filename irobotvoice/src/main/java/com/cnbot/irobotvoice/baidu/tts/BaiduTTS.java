package com.cnbot.irobotvoice.baidu.tts;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.cnbot.irobotvoice.baidu.tts.control.InitConfig;
import com.cnbot.irobotvoice.baidu.tts.control.MySyntherizer;
import com.cnbot.irobotvoice.baidu.tts.listener.MessageListener;
import com.cnbot.irobotvoice.baidu.tts.utils.AutoCheck;
import com.cnbot.irobotvoice.baidu.tts.utils.OfflineResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2016--2019/9/17  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 百度tts合成
 * @FileName: BaiduTTS.java
 * @author: dc
 * @date: 2019/9/17 14:18
 * @version: 1.0
 */
public class BaiduTTS {
    private final String TAG = "BaiduTTS";

    protected String appId = "17323773";

    protected String appKey = "oniFUxGq2VarOR0SMYQSiCe1";

    protected String secretKey = "cNmogGbCLeTBCZGBY9nElEtgAvWnbhkG";

    /**
     * TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
     */
    protected TtsMode ttsMode = TtsMode.MIX;

    protected String offlineVoice = OfflineResource.VOICE_FEMALE;

    /**
     * 主控制类，所有合成控制方法从这个类开始
     */
    protected MySyntherizer synthesizer;

    private Context context;

    public BaiduTTS(Context context) {
        this.context = context;
    }


    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    public  void initialTts() {
        // 日志打印在logcat中
        LoggerProxy.printable(true);
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new MessageListener();

        Map<String, String> params = getParams();

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(context).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
                        Log.e(TAG, "AutoCheckMessage" + message);
                    }
                }
            }
        });
        // 此处可以改为MySyntherizer 了解调用过程
        synthesizer = new MySyntherizer(context, initConfig);
    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>(8);
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "10");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "6");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件
        // 离线语音识别需要对包名保持一致，包名是app的包名，而不是model的包名
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(context, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.e(TAG, "【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }



    /****************************************************************************************/

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    public void speak(String text) {

        // 需要合成的文本text的长度不能超过1024个GBK字节。
        if (!TextUtils.isEmpty(text)) {
            // 合成前可以修改参数：
            // Map<String, String> params = getParams();
            // synthesizer.setParams(params);
            if(synthesizer != null) {
                int result = synthesizer.speak(text);
                checkResult(result, "speak");
            }
        }
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    public void pause() {
        if(synthesizer != null) {
            int result = synthesizer.pause();
            checkResult(result, "pause");
        }
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    public void resume() {
        if(synthesizer != null) {
            int result = synthesizer.resume();
            checkResult(result, "resume");
        }
    }

    /**
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    public void stop() {
        if(synthesizer != null) {
            int result = synthesizer.stop();
            checkResult(result, "stop");
        }
    }

    /**
         * 销毁。
         */
    public void release() {
        if(synthesizer != null) {
            synthesizer.release();
            synthesizer = null;
        }
    }

    public void checkResult(int result, String method) {
        if (result != 0) {
//            toPrint("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
            Log.e(TAG, "error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
            Toast.makeText(context, "百度合成出错 = " + "error code :" + result + " method:" + method , Toast.LENGTH_SHORT).show();
        }
    }

}
