package com.bm.corelibs;

/**
 * 项目配置
 * @Copyright Copyright © 2014 蓝色互动. All rights reserved.
 *
 * @author ZhuFan
 * @Date: 2015-6-1
 */
public class Configuration {
	/** 是否是调试环境 */
	private static boolean debug;
	/** 是否打印网络参数Log */
	private static boolean showNetworkParams;
	/** 是否打印网络结果Log */
	private static boolean showNetworkJson;
	private static boolean UIVersion;
	
	/** 是否是调试环境 */
	public static boolean isDebug() {
		return debug;
	}
	
	/** 设置调试环境 */
	public static void setDebug(boolean debug) {
		Configuration.debug = debug;
	}
	
	/** 是否打印网络参数Log */
	public static boolean isShowNetworkParams() {
		return showNetworkParams;
	}
	
	/** 设置是否打印网络参数Log */
	public static void setShowNetworkParams(boolean showNetworkParams) {
		Configuration.showNetworkParams = showNetworkParams;
	}
	
	/** 是否打印网络结果Log */
	public static boolean isShowNetworkJson() {
		return showNetworkJson;
	}
	
	/** 设置是否打印网络结果Log */
	public static void setShowNetworkJson(boolean showNetworkJson) {
		Configuration.showNetworkJson = showNetworkJson;
	}

	public static boolean isUIVersion() {
		return UIVersion;
	}

	public static void setUIVersion(boolean uIVersion) {
		UIVersion = uIVersion;
	}

}
