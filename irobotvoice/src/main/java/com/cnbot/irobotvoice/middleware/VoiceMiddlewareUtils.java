package com.cnbot.irobotvoice.middleware;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cnbot.aiui.bean.AiuiNlpResult;
import com.cnbot.aiui.utils.AIUINlpUtil;
import com.cnbot.irobotvoice.IVoiceConstant;
import com.cnbot.irobotvoice.ali.api.IRobotAliTranAsrTools;
import com.cnbot.irobotvoice.ali.api.IRobotAliTtsTools;
import com.cnbot.irobotvoice.baidu.asr.listener.IRecogListener;
import com.cnbot.irobotvoice.baidu.asr.util.BaiduRecUtil;
import com.cnbot.irobotvoice.baidu.asr.util.RecogResult;
import com.cnbot.irobotvoice.baidu.tts.BaiduTTS;
import com.cnbot.irobotvoice.bean.UnitBean;
import com.cnbot.irobotvoice.iflyos.IflyosRecognize;
import com.cnbot.irobotvoice.iflyos.listener.IAsrErrorListener;
import com.cnbot.irobotvoice.listener.IRobotAsrResultListener;
import com.cnbot.irobotvoice.listener.IRobotTtsResultListener;
import com.cnbot.irobotvoice.unit.AuthService;
import com.cnbot.irobotvoice.unit.ChatService;
import com.cnbot.zlog.Zlog;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;


/**
 * Copyright (c) 2016--2019/8/12  Hunan Cnbot Co., Ltd. All Rights Reserved.
 *
 * @descriptoin 语音中间件（封装多语音平台）
 * @FileName: VoiceMiddlewareUtils.java
 * @author: dc
 * @date: 2019/8/12 14:47
 * @version: 1.0
 */

public class VoiceMiddlewareUtils {
    private static final String TAG = VoiceMiddlewareUtils.class.getSimpleName();
    /**
     * 是否启动nlp, 主要在区分有的语义分析走的是http接口，而非sdk
     */
    private boolean isStartUpNlp = false;
    /**
     * 百度语音识别工具类
     */
    private BaiduRecUtil baiduRecUtil = null;
    /**
     * ali语音识别
     */
    private IRobotAliTranAsrTools iRobotAsrTools;
    /**
     * ali语音合成
     */
    private IRobotAliTtsTools iRoboTtsTools;

    /**
     * 语音交互事件管理器
     */
    private IVoiceEventListener iVoiceEventListener;
    /**
     * iflyos aiui 语义分析
     */
    private AIUINlpUtil aiuiNlpUtil;
    /**
     * 百度语音合成对象
     */
    private BaiduTTS baiduTTS = null;
    /**
     * 当前插入使用的识别引擎
     * baidu  使用百度引擎   iflyos 讯飞引擎   ali  阿里引擎
     */
    private String currentAsrEngine;
    /**
     * 当前插入使用的合成引擎
     * baidu  使用百度引擎   iflyos 讯飞引擎   ali  阿里引擎
     */
    private String currentTtsEngine;
    /**
     * 当前插入使用的合成引擎
     * baidu  使用百度unit   aiui 讯飞引擎
     */
    private String currentNlpEngine;
    /*****************************  iflyos引擎  *****************************/
    private IflyosRecognize iflyosAsr = null;
    /************************  百度语义合成引擎  ****************************/



    public static class Instance {
        private static VoiceMiddlewareUtils instance = new VoiceMiddlewareUtils();
    }

    /**
     * 单利，获取语音中间组件对象
     * @return
     */
    public static VoiceMiddlewareUtils get() {
        return Instance.instance;
    }

    /**
     * 注册监听器
     *
     * @param listener
     */
    public void setVoiceEventListener(IVoiceEventListener listener) {
        iVoiceEventListener = listener;
    }

    /**
     * 移除监听器
     */
    public void removeVoiceEventListener() {
        iVoiceEventListener = null;
    }


    /************************** 公共组件方法  **************************/

    /**
     * 插入语音识别引擎模块
     * @param context 上下文
     * @param asrPlatfrom baidu  使用百度引擎   iflyos 讯飞引擎   ali  阿里引擎
     * @param model ali版本分为内外和外网版本（因机场项目存在两个版本问题故保留该参数，后期如果确定一个版本的情况这个参数可以去除）
     */
    public void insertAsrModel(Context context, String asrPlatfrom, boolean model) {
        currentAsrEngine = asrPlatfrom;
        if (asrPlatfrom.equals(IVoiceConstant.BAIDU_ASR)) {
            initBaduAsrUtil(context);
        } else if (asrPlatfrom.equals(IVoiceConstant.IFLYOS_ASR)) {
            initIflyos(context);
        } else if (asrPlatfrom.equals(IVoiceConstant.ALI_ASR)) {
            initAliAsr(model);
        }
    }

    /**
     * 插入语音合成引擎模块
     * @param context 上下文
     * @param ttsPlatfrom  baidu  使用百度引擎   iflyos 讯飞引擎   ali  阿里引擎
     * @param model ali版本分为内外和外网版本（因机场项目存在两个版本问题故保留该参数，后期如果确定一个版本的情况这个参数可以去除）
     */
    public void insertTtsModel(Context context, String ttsPlatfrom, boolean model) {
        currentTtsEngine = ttsPlatfrom;
        if (ttsPlatfrom.equals(IVoiceConstant.BAIDU_TTS)) {
            initBaiduTts(context);
        } else if (ttsPlatfrom.equals(IVoiceConstant.IFLYOS_TTS)) {

        } else if (ttsPlatfrom.equals(IVoiceConstant.ALI_TTS)) {
            initAliTts(model);
        }
    }

    /**
     * 插入语义合成引擎模块
     * @param context 上下文
     * @param nlpPlatfrom unit  使用百度引擎   aiui 讯飞引擎
     */
    public void insertNlpModel(Context context, String nlpPlatfrom) {
        currentNlpEngine = nlpPlatfrom;
        if (nlpPlatfrom.equals(IVoiceConstant.AIUI_NLP)) {
            initAiuiNlp(context);
        } else if (nlpPlatfrom.equals(IVoiceConstant.UNIT_NLP)) {
            initUnit();
        }
    }


    /**********start 初始化语音识别引擎*****************/
    /**
     * 初始化讯飞语音识别
     * @param context 上下文
     */
    private void initIflyos(Context context) {
        Zlog.wtf(TAG, "initIflyos ");
        iflyosAsr = IflyosRecognize.get();
        iflyosAsr.initRecognizer(context, new IflyosAsrListener());
    }



    /**
     * 初始化百度语音识别
     * @param context 上下文
     */
    private void initBaduAsrUtil(Context context) {
        Zlog.wtf(TAG, "initBaduAsrUtil ");
        //集成第一步 初始化EventManager类并注册自定义输出事件
        //1.1 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        baiduRecUtil = BaiduRecUtil.getInstance(context, new BaiduAsrEventListener());
        baiduRecUtil.initASR();
    }


    /**
     * 初始化阿里语音识别
     * @param isAsrModel true为外网版本  false为机场内网版本
     */
    private void initAliAsr(boolean isAsrModel) {
        Zlog.wtf(TAG, "初始化ali语音识别");
        iRobotAsrTools = IRobotAliTranAsrTools.getInstance(isAsrModel);
        iRobotAsrTools.setiRobotAsrResultListener(new IRobotAsrResultCallback());
    }

    /**********end 初始化语音识别引擎*****************/


    /**********start 初始化语音合成引擎*****************/
    /**
     * 初始化阿里tts
     * @param isTtsModel  true为外网版本  false为机场内网版本
     */
    private void initAliTts(boolean isTtsModel) {
        Zlog.wtf(TAG, "初始化initAliTts");
        iRoboTtsTools = IRobotAliTtsTools.getInstance(isTtsModel);
        iRoboTtsTools.setiRobotTtsResultListener(new IRobotTtsResultCallback());
    }

    /**
     * 初始化百度语音合成
     */
    private void initBaiduTts(Context context) {
        Zlog.wtf(TAG, "初始化百度语音合成 ");
        baiduTTS = new BaiduTTS(context);
        baiduTTS.initialTts();
    }
    /**********end 初始化语音合成引擎*****************/


    /**
     * 是否注册nlp交互
     *
     * @param isNlp
     */
    public void isRegNlpModel(boolean isNlp) {
        isStartUpNlp = isNlp;
    }


    /***************************************  ali 语音识别引擎 *************************************/

    /**
     * 1、公共组件方法启动识别
     */
    public void startAsr() {
        Zlog.wtf(TAG, "公共组件方法启动识别");
        stopAsr();
        stopTts();
        try {
            if (currentAsrEngine.equals("baidu")) {
                startBaiduAsr();
            } else if (currentAsrEngine.equals("iflyos")) {
                startIflyosAsr();
            } else if (currentAsrEngine.equals("ali")) {
                startAliAsr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2、公共组件方法,停止识别
     */
    public void stopAsr() {
        Zlog.wtf(TAG, "公共组件方法,停止识别");
        try {
            if (currentAsrEngine.equals("baidu")) {
                stopBaiduAsr();
            } else if (currentAsrEngine.equals("iflyos")) {
                stopIflyosAsr();
            } else if (currentAsrEngine.equals("ali")) {
                stopAliAsr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 3、公共组件方法，开始语音合成
     */
    public void startTts(String text) {
        Zlog.wtf(TAG, "公共组件方法,开始语音合成");
        stopTts();
        try {
            if (currentTtsEngine.equals("baidu")) {
                startBaiduTts(text);
            } else if (currentTtsEngine.equals("iflyos")) {

            } else if (currentTtsEngine.equals("ali")) {
                startAliTts(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 4、公共组件方法，停止语音合成
     */
    public void stopTts() {
        Zlog.wtf(TAG, "公共组件方法,停止语音合成");
        try {
            if (currentTtsEngine.equals("baidu")) {
                stopBaiduTts();
            } else if (currentTtsEngine.equals("iflyos")) {

            } else if (currentTtsEngine.equals("ali")) {
                stopAliTts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 5、公共组件方法, 销毁asr
     */
    public void releaseAsr() {
        Zlog.wtf(TAG, "公共组件方法,销毁asr");
        try {
            if (currentAsrEngine.equals("baidu")) {
                releaseBaiduAsr();
            } else if (currentAsrEngine.equals("iflyos")) {
                releaseIflyosAsr();
            } else if (currentAsrEngine.equals("ali")) {
                releaseAliAsr();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 停止阿里语音识别
     */
    public void stopAliAsr() {
        Zlog.wtf(TAG, "停止ali 识别");
        if (iRobotAsrTools != null) {
            iRobotAsrTools.stopAsr();
        }
    }

    /**
     * 停止讯飞语音识别
     */
    private void stopIflyosAsr() {
        Zlog.wtf(TAG, "停止讯飞语音识别 ");
        if (iflyosAsr != null) {
            iflyosAsr.stopRecognize();

        }
    }

    /**
     * 停止百度语音识别
     * SDK会识别不会再识别停止后的录音。
     */
    protected void stopBaiduAsr() {
        Zlog.wtf(TAG, "停止百度语音识别 ");
        if (baiduRecUtil != null) {
            baiduRecUtil.stopASR();
        }
    }


    /**
     * 开始进行nlp分析
     * @param text
     */
    public void startNlp(String text){
        try {
            if (currentNlpEngine.equals(IVoiceConstant.AIUI_NLP)) {
                startAiuiNlp(text);
            } else if (currentNlpEngine.equals(IVoiceConstant.UNIT_NLP)) {
                startUnitNlp(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 停止tts
     */
    public void stopAliTts() {
        Zlog.wtf(TAG, "stopAliTts");
        if (iRoboTtsTools != null) {
            iRoboTtsTools.stopTts();
        }
    }

    /**
     * 关闭百度语音合成
     */
    private void stopBaiduTts() {
        Zlog.wtf(TAG, "关闭百度语音合成");
        if (baiduTTS != null) {
            baiduTTS.stop();
        }
    }

    /**
     * 启动阿里语音识别
     */
    private void startAliAsr() {
        Zlog.wtf(TAG, "启动阿里语音识别");
        if (iRobotAsrTools != null) {
            iRobotAsrTools.startAsr();
        }
    }

    /**
     * 启动讯飞语音识别
     */
    private void startIflyosAsr() {
        Zlog.wtf(TAG, "启动讯飞语音识别 ");
        if (iflyosAsr != null) {
            iflyosAsr.startRecognize();
        }
    }

    /**
     * 开始百度语音识别
     */
    protected void startBaiduAsr() {
        Zlog.wtf(TAG, "开始百度语音识别 ");
        if (baiduRecUtil != null) {
            baiduRecUtil.startASR();


        }
    }

    /**
     * 开始合成
     *
     * @param text
     */
    public void startAliTts(String text) {
        Zlog.wtf(TAG, "开始ali 语音合成 = " + text);
        if (!TextUtils.isEmpty(text)) {
            iRoboTtsTools.startTts(text);
        }
    }

    /**
     * 开始启动百度语音合成
     *
     * @param text
     */
    private void startBaiduTts(String text) {
        Zlog.wtf(TAG, "开始启动百度语音合成 =  " + text);
        if (baiduTTS != null && !TextUtils.isEmpty(text)) {
            baiduTTS.speak(text);
        }
    }

    /**
     * 销毁asr
     */
    public void releaseAliAsr() {
        Zlog.wtf(TAG, "销毁ali asr");
        if (iRobotAsrTools != null) {
            iRobotAsrTools.removeiRobotAsrResultListener();
            iRobotAsrTools.releaseAsr();
            iRobotAsrTools = null;
        }
    }

    /**
     * 销毁讯飞语音识别
     */
    private void releaseIflyosAsr() {
        Zlog.wtf(TAG, "销毁讯飞语音识别 ");
        if (iflyosAsr != null) {
            iflyosAsr.destroy();
        }
    }

    protected void releaseBaiduAsr() {
        Zlog.wtf(TAG, "releaseBaiduAsr ");
        if (baiduRecUtil != null) {
            baiduRecUtil.releaseASR();
        }
    }

    /**
     * 6、公共组件方法, 销毁tts
     */
    public void releaseTts() {
        Zlog.wtf(TAG, "公共组件方法,销毁tts");
        try {
            if (currentTtsEngine.equals("baidu")) {
                releaseBaiduTts();
            } else if (currentTtsEngine.equals("iflyos")) {
            } else if (currentTtsEngine.equals("ali")) {
                releaseAliTts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁tts
     */
    public void releaseAliTts() {
        Zlog.wtf(TAG, "销毁ali tts = ");
        if (iRoboTtsTools != null) {
            iRoboTtsTools.removeiRobotTtsResultListener();
            iRoboTtsTools.releaseTts();
            iRoboTtsTools = null;
        }
    }

    /**
     * 销毁百度语音合成
     */
    private void releaseBaiduTts() {
        Zlog.wtf(TAG, "销毁百度语音合成");
        if (baiduTTS != null) {
            baiduTTS.release();
        }
    }

    /**
     * 取消讯飞语音识别
     */
    private void cancalIflyosAsr() {
        Zlog.wtf(TAG, "取消讯飞语音识别 ");
        if (iflyosAsr != null) {
            iflyosAsr.cancalRecognize();
        }
    }

    /****************************end 百度语音识别引擎*******************************/

    /**
     * 取消百度语音识别
     * SDK会取消本次识别，回到原始状态。
     */
    protected void cancelBaiduAsr() {
        if (baiduRecUtil != null) {
            baiduRecUtil.cancelASR();
        }
    }

    /**
     * 初始化aiui
     */
    public void initAiuiNlp(Context context) {
        aiuiNlpUtil = AIUINlpUtil.getInstance(context);
        aiuiNlpUtil.createAgent();
        aiuiNlpUtil.setListener(new AiuiNlpResult() {
            @Override
            public void aiuiNlpResult(int rc, String result) {
                Log.e(TAG, "得到讯飞语义结果:" + result);
                String aiuiResult;
                if (rc == 4) {
                    aiuiResult = "您问的问题太高深了，可把小兴给难倒了！";
                } else {
                    // AIUI语义结果
                    aiuiResult = result;
                }
                if (iVoiceEventListener != null) {
                    iVoiceEventListener.resultNlp(aiuiResult);
                }
            }

            @Override
            public void aiuiNlpError(String error, int code) {
                if (iVoiceEventListener != null) {
                    iVoiceEventListener.errorNlp(error, code);
                }
            }
        });
    }

    /**
     * 启动AIUI语义分析
     *
     * @param text
     */
    private void startAiuiNlp(String text) {
        Zlog.wtf(TAG, "启动AIUI语义分析 = " + text);
        if (aiuiNlpUtil != null) {
            aiuiNlpUtil.startTextNlp(text);
        }
    }

    /**
     * 启动unit语义分析
     *
     * @param text
     */
    private void startUnitNlp(String text) {
        String unitResult = ChatService.utterance(text);
        Zlog.wtf(TAG, "unit返回结果 = " + unitResult);
        if (!TextUtils.isEmpty(unitResult)) {
            UnitBean chatResponseBean = new Gson().fromJson(unitResult, UnitBean.class);
            showResult(chatResponseBean);
        } else {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.resultNlp("抱歉，UNIT请求异常，请检查网络之后再重试");
            }
        }

    }

    /**
     * 解析unit结果内容
     *
     * @param chatResponseBean
     */
    private void showResult(UnitBean chatResponseBean) {
        String content = "抱歉，我的大脑出现了问题，要休息一会";
        if (chatResponseBean != null) {
            if (chatResponseBean.getError_code() == 0 && (chatResponseBean.getResult().getResponse_list().size() > 0)) {
                List<UnitBean.ResultBean.ResponseListBean.ActionListBean> actionListBean = chatResponseBean.getResult().getResponse_list().get(0).getAction_list();
                if (actionListBean.size() > 0) {
                    int random = new Random().nextInt(actionListBean.size());
                    UnitBean.ResultBean.ResponseListBean.ActionListBean action = actionListBean.get(random);
                    if (action.getType().equals("guide")) {
                        int guideSize = action.getRefine_detail().getOption_list().size();
                        String guideContent = "";
                        for (int i = 0; i < guideSize; i++) {
                            guideContent += actionListBean.get(random).getRefine_detail().getOption_list().get(i).getOption() + "\r\n";
                        }
                        content = action.getSay() + "\r\n" + guideContent;
                    } else {
                        content = action.getSay();
                    }
                }
            } else if (chatResponseBean.getError_code() == 100 || chatResponseBean.getError_code() == 111 || chatResponseBean.getError_code() == 110) {
                Zlog.wtf(TAG, "unit Token 失效" + chatResponseBean.getError_code());
                boolean init = initUnit();
                if (init && iVoiceEventListener != null) {
                    iVoiceEventListener.errorUnitNlp("unit Token 失效", chatResponseBean.getError_code());
                }
            }
        }

        if (iVoiceEventListener != null) {
            iVoiceEventListener.resultNlp(content);
        }
        Zlog.wtf(TAG, "语义结果 = " + content);
    }

    /************************************** 百度UNIT ***********************************/

    /**
     * 初始化unit
     */
    public boolean initUnit() {
        Zlog.wtf(TAG, "初始化unit ");
        String auth = AuthService.getAuth();
        if (TextUtils.isEmpty(auth)) {
            Zlog.wtf(TAG, "百度UNIT获取失败 ");
            if (iVoiceEventListener != null) {
                // TODO: 2019/9/9
                iVoiceEventListener.errorInitNlp("百度UNIT_Token获取失败", -1);
            }
        } else {
            Zlog.wtf(TAG, "百度UNIT获取成功 = " + auth);
            return true;
        }
        return false;
    }


    /***********************************  AIUI语义  ******************************/



    /**
     * 语音识别结果监听器
     */
    public class IRobotAsrResultCallback implements IRobotAsrResultListener {

        @Override
        public void asrResultFinal(final String text) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.resultAsr(text);
            }
        }

        @Override
        public void asrError(String error, int code) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorAsr(error, code);
            }
        }

        @Override
        public void asrResult(String text) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.resultProcessAsr(text);
            }
        }

        @Override
        public void asrBefore() {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.beforeAsr();
            }
        }

        @Override
        public void asrEnd() {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.finishAsr();
            }
        }
    }

    /**
     * ******************************百度语音识别回调start**************************************
     */
    public class BaiduAsrEventListener implements IRecogListener {

        @Override
        public void onAsrReady() {
            //可以开始说话
            if (iVoiceEventListener != null) {
                iVoiceEventListener.beforeAsr();
            }
        }

        @Override
        public void onAsrBegin() {
            //已经开始说话

        }

        @Override
        public void onAsrEnd() {
            //停止说话
            if (iVoiceEventListener != null) {
                iVoiceEventListener.finishAsr();
            }
        }

        @Override
        public void onAsrPartialResult(String[] results, RecogResult recogResult) {
            //临时识别结果
            if (iVoiceEventListener != null) {
                Zlog.wtf(TAG, "临时识别结果 = " + results[0]);
                iVoiceEventListener.resultProcessAsr(results[0]);
            }
        }


        @Override
        public void onAsrFinalResult(String[] results, RecogResult recogResult) {
            //最终识别结果，长语音每一句话会回调一次;不开启长语音，仅回调一次；可能有多个结果，取第一个
            if (iVoiceEventListener != null) {
                Zlog.wtf(TAG, "最终识别结果 = " + results[0]);
                iVoiceEventListener.resultAsr(results[0]);
            }
        }

        @Override
        public void onAsrFinish(RecogResult recogResult) {
            //识别结束.可能有多个结果，取第一个

        }

        @Override
        public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecogResult recogResult) {
            //百度识别结果出错
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorAsr(descMessage, subErrorCode);
                Zlog.wtf(TAG, "百度语音识别出错" + descMessage);
            }
        }

        @Override
        public void onAsrLongFinish() {
            //长语音
            Log.d(TAG, "百度语音识别开启了长语音，识别结束");
        }


        @Override
        public void onAsrExit() {
            //引擎完成整个识别，空闲中
        }

        @Override
        public void onAsrError(int error, String desc) {
            //百度识别出错
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorAsr(desc, error);
                Zlog.wtf(TAG, "百度语音识别出错" + desc);
            }
        }
    }

    /**
     * ali语音合成结果监听器
     */
    public class IRobotTtsResultCallback implements IRobotTtsResultListener {

        @Override
        public void ttsError(String error) {
            Log.e(TAG, "合成错误 " + error);
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorTts(error, -1);
            }
        }

        @Override
        public void ttsBefore() {
            Log.e(TAG, "开始合成");
            if (iVoiceEventListener != null) {
                iVoiceEventListener.beforeTts();
            }
        }

        @Override
        public void ttsEnd() {
            Log.e(TAG, "结束合成");
            if (iVoiceEventListener != null) {
                iVoiceEventListener.finishTts();
            }
        }
    }

    /**
     * 语音识别结果及错误监听器
     */
    public class IflyosAsrListener implements IAsrErrorListener {

        @Override
        public void onAsrResult(String text) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.resultAsr(text);
            }
        }

        @Override
        public void onAsrError(int code, String errorDes) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorAsr(errorDes, code);
            }
        }

        @Override
        public void onAsrProcess(String text) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.resultProcessAsr(text);
            }
        }

        @Override
        public void onSpeechBegin() {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.beforeAsr();
            }
        }

        @Override
        public void onSpeechEnd() {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.finishAsr();
            }
        }

        @Override
        public void onRecordFailure(String error) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorAsr(error, -1);
            }
        }

        @Override
        public void onEmptyData(String error) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorAsr(error, -1);
            }
        }

        @Override
        public void onNetError(String error) {
            if (iVoiceEventListener != null) {
                iVoiceEventListener.errorAsr(error, -1);
            }
        }
    }


}
