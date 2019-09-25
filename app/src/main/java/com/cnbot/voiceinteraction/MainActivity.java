package com.cnbot.voiceinteraction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cnbot.voiceinteraction.adapter.ChatHistoryAdapter;
import com.cnbot.voiceinteraction.bean.Msg;
import com.cnbot.irobotvoice.middleware.IVoiceEventListener;
import com.cnbot.irobotvoice.middleware.VoiceMiddlewareUtils;
import com.cnbot.zlog.Zlog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText etInputStr;
    private Button btnSend, btnVoiceClick;
    private RecyclerView rvContent;
    private ChatHistoryAdapter adapter;
    private final List<Msg> history = new ArrayList<>();

    private VoiceMiddlewareUtils voiceMiddlewareUtils = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initVoice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(voiceMiddlewareUtils != null){
            voiceMiddlewareUtils.removeVoiceEventListener();
            voiceMiddlewareUtils.releaseAsr();
            voiceMiddlewareUtils.releaseTts();
            voiceMiddlewareUtils = null;
        }
    }

    /**
     * 初始化语音交互所有流程
     */
    private void initVoice() {
        voiceMiddlewareUtils = VoiceMiddlewareUtils.get();
        voiceMiddlewareUtils.setVoiceEventListener(new IVoiceMiddleListener());
        voiceMiddlewareUtils.insertAsrModel(AppHelper.getContext(),IVoiceCofig.ASR_MODEL, true);
        voiceMiddlewareUtils.insertTtsModel(AppHelper.getContext(),IVoiceCofig.TTS_MODEL, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                voiceMiddlewareUtils.insertNlpModel(AppHelper.getContext(),IVoiceCofig.NLP_MODEL);
            }
        }).start();

    }


    private void initView() {
        etInputStr = findViewById(R.id.main_input_edit);
        btnSend = findViewById(R.id.main_send_bt);
        btnVoiceClick = findViewById(R.id.btn_voice_click);

        rvContent = findViewById(R.id.main_recyclerView);
        rvContent.setHasFixedSize(true);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatHistoryAdapter(this, history);
        rvContent.setAdapter(adapter);

        btnVoiceClick.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_send_bt:
                String input = etInputStr.getText().toString();
                if(!TextUtils.isEmpty(input)){
                    startNlp(input);
                } else {
                    String ttsTip = "请输入你想要咨询的问题";
                    startTts(ttsTip);
                }
                break;
            case R.id.btn_voice_click:
                startAsr();
                break;
            default:
                break;
        }
    }

    /************************************ 公共方法开始 ***********************************/
    /**
     * 开始启动语音识别
     */
    private void startAsr(){
        stopAsr();
        if(voiceMiddlewareUtils != null){
            voiceMiddlewareUtils.startAsr();
        }
        btnVoiceClick.setText("请说话");
    }

    /**
     * 停止语音识别
     */
    private void stopAsr(){
        if(voiceMiddlewareUtils != null){
            voiceMiddlewareUtils.stopAsr();
        }
        btnVoiceClick.setText("点击说话");
    }

    /**
     * 开始AIUI平台的语义分析
     * @param text
     */
    private void startNlp(final String text){
        btnVoiceClick.setText("正在思考");
        if(voiceMiddlewareUtils != null ){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    voiceMiddlewareUtils.startNlp(text);
                }
            }).start();
        }
    }

    /**
     * 语音合成
     * @param text 合成的文本内容
     */
    private void startTts(String text){
        if(voiceMiddlewareUtils != null){
            voiceMiddlewareUtils.startTts(text);
        }
    }



    /************************************ 公共方法结束 ***********************************/




    /************************************ 回调方法处理开始 ***********************************/

    /**
     * 语音识别错误处理
     * @param error
     * @param code
     */
    private void asrErrorHandle(String error, int code) {
        Zlog.wtf(TAG, "识别出错 = " + error + "    错误码 = " + code);
        notifyOutputMsg("识别出错 = " + error + "    错误码 = " + code);
        stopAsr();
    }


    /**
     * 语音识别最终结果处理
     * @param text
     */
    private void asrResultFinalHandle(String text) {
        String asrResultStr = text;
        Zlog.wtf(TAG, "语音识别最终结果处理 = " + asrResultStr );
        // 进行语义分析
        if(!TextUtils.isEmpty(text)) {
            notifyInputMsg(text);
            startNlp(text);
        } else {
            String tip = "抱歉，我没有听到你说了什么，请再试一次吧";
            notifyOutputMsg(tip);
        }
    }


    /**
     * tts结束
     */
    /**
     * tts错误
     * @param error
     * @param i
     */
    private void ttsErrorHandle(String error, int i) {
        Zlog.wtf(TAG, "tts错误 = " + error + "    错误码 = " + i);
    }


    /************************************ 回调方法处理结束 ***********************************/


    /************************************ 结果的界面展示开始  ***********************************/
    /**
     * 语义分析结果
     * @param text
     */
    private void showNlpResult(String text) {
        Log.e(TAG, "语义分析结果 = " + text);
        notifyOutputMsg(text);
    }

    /**
     * 在语义分析之前，将输入信息更新到界面上
     *
     * @param input 百度asr输入或输入框输入
     */
    private void notifyInputMsg(String input) {
        btnVoiceClick.setText("");
        history.add(new Msg(input, Msg.INPUT_TYPE));
        adapter.notifyDataSetChanged();
        rvContent.scrollToPosition(history.size() - 1);
    }

    /**
     * 在语义分析结束之后，将输入信息更新到界面上，并播放百度tts
     *
     * @param output 灵聚nlp或者讯飞nlp识别结果
     */
    private void notifyOutputMsg(final String output) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnVoiceClick.setText("点击说话");
                history.add(new Msg(output, Msg.OUTPUT_TYPE));
                adapter.notifyDataSetChanged();
                rvContent.scrollToPosition(history.size() - 1);
            }
        });
        startTts(output);
    }


    /************************************ 结果的界面展示结束  ***********************************/








    /**
     * 整个语音交互的重要的回调方法事件
     */
    public class IVoiceMiddleListener implements IVoiceEventListener {

        private void ttsFinishHandle() {

        }

        @Override
        public void beforeAsr() {
            Zlog.wtf(TAG, "IVoiceMiddleListener_beforeAsr");
            btnVoiceClick.setText("正在录音");
        }

        @Override
        public void finishAsr() {
            Zlog.wtf(TAG, "IVoiceMiddleListener_finishAsr");
        }

        @Override
        public void errorAsr(String error, int codeError) {
            asrErrorHandle(error, codeError);
        }

        @Override
        public void resultAsr(String result) {
            asrResultFinalHandle(result);
        }

        @Override
        public void resultProcessAsr(String result) {
//            asrResultProcessHandle(result);
        }

        @Override
        public void beforeTts() {

        }

        @Override
        public void finishTts() {
            ttsFinishHandle();
        }

        @Override
        public void errorTts(String error, int codeError) {
            ttsErrorHandle(error, codeError);
        }

        @Override
        public void resultNlp(String result) {
            showNlpResult(result);
        }

        @Override
        public void errorNlp(String error, int codeError) {
            Zlog.wtf(TAG, "NLP to AIUI error = " + error);
            String errorStr = "我的大脑出现问题了,请换个问题试下吧!" + error;
            notifyOutputMsg(errorStr);
        }

        @Override
        public void errorInitNlp(String error, int codeError) {
//            startTts(error);
            Zlog.wtf(TAG, "errorInitNlp = " + error);
        }

        @Override
        public void errorUnitNlp(String error, int codeError) {
            Zlog.wtf(TAG, "NLP to UNIT error = " + error);
        }
    }


}
