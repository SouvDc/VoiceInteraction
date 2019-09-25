package com.cnbot.irobotvoice.iflyos;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.cnbot.irobotvoice.iflyos.listener.IAsrErrorListener;
import com.cnbot.irobotvoice.iflyos.listener.RecognizeListenerImpl;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * @project ecologyrobot2
 * @author ww
 * @date 2019/3/13
 * @description  科大讯飞语音识别引擎【单例模式】包括听写、语法识别功能，音频数据转换成文本数据
 */
public class IflyosRecognize {
	private final String TAG = IflyosRecognize.class.getSimpleName();

	private String mEngineType = "cloud";
	// 返回结果格式，支持：xml,json
	private String mResultType = "json";

	private RecognizerListener mRecognizerListener;

	private IflyosRecognize() {}

	public static class Instance {
		private static IflyosRecognize sInstance = new IflyosRecognize();
	}

	public static IflyosRecognize get() {

		return Instance.sInstance;
	}

	/**
	 * 创建讯飞语音 在application中调用该方法。
	 * 注意此方法必须调用
	 * @param context
	 */
	public void createSpeeck(Context context){
		SpeechUtility.createUtility(context, "appid=" + "56c3d3bd");
	}

	/**
	 * 初始化讯飞语义识别对象
	 * @param context
	 */
	public void initRecognizer(Context context) {
		if (SpeechRecognizer.getRecognizer() != null) {
			return;
		}

		// 初始化识别对象
		SpeechRecognizer.createRecognizer(context, new InitListener() {

			@Override
			public void onInit(int code) {
				if (code == ErrorCode.SUCCESS) {
					setRecognizerParam();
					Log.e(TAG, "initRecognizer: 初始化成功：" + code);
				} else {
					Log.e(TAG, "initRecognizer: 初始化失败,错误码：" + code);
				}
			}
		});
	}

	/**
	 * 初始化讯飞语义识别对象
	 * @param context
	 */
	public void initRecognizer(Context context, IAsrErrorListener l) {
		initRecognizer(context);
		initRecognizeListener(l);
	}

	/**
	 * 参数设置
	 * @param
	 * @return
	 */
	private void setRecognizerParam() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		if (recognizer == null)
			return;
		// 清空参数
		recognizer.setParameter(SpeechConstant.PARAMS, null);
		// 设置识别引擎
		recognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		recognizer.setParameter(SpeechConstant.RESULT_TYPE, mResultType);
		// 设置云端识别使用的语法id

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		recognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/asr.wav");
		recognizer.setParameter(SpeechConstant.ASR_PTT, "0");

		// 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
		//取值范围{1000～10000}
		recognizer.setParameter(SpeechConstant.VAD_BOS, "3000");
		//设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
		//自动停止录音，范围{0~10000}
		recognizer.setParameter(SpeechConstant.VAD_EOS, "2000");
		//设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		//        mAsr.setParameter(SpeechConstant.ASR_PTT,"1");
	}

	//获取识别资源路径
	private String getResourcePath(Context context) {
		StringBuffer tempBuffer = new StringBuffer();
		//识别通用资源
		tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
		//识别8k资源-使用8k的时候请解开注释
		//		tempBuffer.append(";");
		//		tempBuffer.append(ResourceUtil.generateResourcePath(this, RESOURCE_TYPE.assets, "asr/common_8k.jet"));
		return tempBuffer.toString();
	}

	/**
	 * 开启语义识别
	 */
	public void startRecognize() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		if (recognizer != null && !recognizer.isListening()) {
			recognizer.startListening(mRecognizerListener);
		}

	}

	private void initRecognizeListener(IAsrErrorListener l) {
		if (mRecognizerListener == null)
			mRecognizerListener = new RecognizeListenerImpl(l);
	}


	/**
	 * 停止语义识别
	 */
	public void stopRecognize() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		if (recognizer != null && recognizer.isListening()) {
			recognizer.stopListening();
		}
	}

	/**
	 * 取消当前会话
	 */
	public void cancalRecognize() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		if (recognizer != null) {
			recognizer.cancel();
		}
	}

	/**
	 * 资源释放
	 */
	public void destroy() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		if (null != recognizer) {
			// 退出时释放连接
			recognizer.cancel();
			recognizer.destroy();
		}
	}

}
