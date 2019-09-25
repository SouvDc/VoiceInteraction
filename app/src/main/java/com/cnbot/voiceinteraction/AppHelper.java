package com.cnbot.voiceinteraction;

import android.content.Context;

/**
 * Copyright (c) 2016--2019/1/16  Hunan Cnbot Co., Ltd. All Rights Reserved.
 **/

/**
 *@descriptoin 提供application级别的上下文和全局的Handler
 *@FileName: AppHelper.java
 *@author: ww
 *@date: 2019/1/16 16:26
 *@version: 1.0
 */

public class AppHelper {

	private static Context sContext;


	/**
	 * please init it in your custom application
	 * @param context
	 */
	public static void init(Context context) {
		sContext = context;

	}



	private static void checkNotNull(Object obj, String msg) {
		if (obj == null){
			throw new NullPointerException(msg);
		}

	}

	private static void checkNotNull(Object obj) {
		if (obj == null) {
			throw new NullPointerException("you must init the util in your application");
		}
	}

	public static Context getContext() {
		checkNotNull(sContext);
		return sContext;
	}


}
