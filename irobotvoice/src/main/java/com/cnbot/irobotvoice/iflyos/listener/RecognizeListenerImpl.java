package com.cnbot.irobotvoice.iflyos.listener;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cnbot.irobotvoice.iflyos.utils.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

/**
 *  @项目名：  EcologyRobot4
 *  @包名：    com.cnbot.aiui.listener
 *  @文件名:   RecognizeListenerImpl
 *  @创建者:   ww
 *  @创建时间:  2019/3/14 16:48
 *  @描述：    语音识别接口实现类
 */
public class RecognizeListenerImpl implements RecognizerListener {
	private final String TAG = RecognizeListenerImpl.class.getSimpleName();

	/**
	 * 识别拼接结果
	 */
	private String mCurrentAsrStr = "";
	private IAsrErrorListener mIAsrProcessListener;

	public RecognizeListenerImpl(IAsrErrorListener l) {
		mIAsrProcessListener = l;
	}

	@Override
	public void onVolumeChanged(int volume, byte[] data) {
		//Log.d(TAG, "当前正在说话，音量大小：" + volume);
	}

	@Override
	public void onResult(final RecognizerResult result, boolean isLast) {
		if (mIAsrProcessListener == null) {
			throw new NullPointerException("you must set asr listener first");
		}
		//1、最后一次的数据一定为空，不用处理/2、如果没有检测到语音，直接会走onEndOfSpeech，其实不用对结果判断空
		if (isLast) {
			mIAsrProcessListener.onAsrResult(mCurrentAsrStr);
			mCurrentAsrStr = "";
			return;
		}
		if (result == null || TextUtils.isEmpty(result.getResultString())) {
			mIAsrProcessListener.onAsrError(-1, "RecognizerResult is null");
			return;
		}
		String text = JsonParser.parseCloudGrammarResult(result.getResultString());
		mCurrentAsrStr += text;
		mIAsrProcessListener.onAsrProcess(mCurrentAsrStr);

		Log.d(TAG, "onResult:" + result == null ? null : result.getResultString() + ",isLast:" + isLast + ",result:" + text);
	}

	@Override
	public void onEndOfSpeech() {
		// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
		Log.d(TAG, "onEndOfSpeech: 结束说话");
		if (mIAsrProcessListener != null) {
			mIAsrProcessListener.onSpeechEnd();
		}
	}

	@Override
	public void onBeginOfSpeech() {
		// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
		Log.d(TAG, "onBeginOfSpeech: 开始说话");
		if (mIAsrProcessListener != null) {
			mIAsrProcessListener.onSpeechBegin();
		}
	}

	@Override
	public void onError(SpeechError error) {
		Log.e(TAG, "onError Code：" + error.getErrorCode() + ",error:" + error.getErrorDescription());
		if (mIAsrProcessListener != null) {
			final int code = error.getErrorCode();
			final String str = error.getErrorDescription();
			mIAsrProcessListener.onAsrError(code, str);
			if (code == ErrorCode.ERROR_AUDIO_RECORD)
				mIAsrProcessListener.onRecordFailure(str);
			else if (code == ErrorCode.MSP_ERROR_NO_DATA)
				mIAsrProcessListener.onEmptyData(str);
			else if (code >= ErrorCode.ERROR_NO_NETWORK && code <= ErrorCode.ERROR_NET_EXCEPTION) {
				mIAsrProcessListener.onNetError(str);
			}
		}
	}

	@Override
	public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
		// 若使用本地能力，会话id为null
		//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
		//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
		//		Log.d(TAG, "session id =" + sid);
		//	}
	}

}
