package com.bm.corelibs.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bm.corelibs.logic.BaseLogic.NListener;
import com.bm.corelibs.logic.NetworkParams;

public class HttpVolleyRequest<T> {

	private NListener<T> mListener;
	private Activity mAct;
//	private boolean isCache = false; // 是否开启缓存
//	private boolean isExit = true; // 判断缓存文件是否存在
//	private String urlKey;
//	private HashMap<String, String> params;
//	private int method;

	public HttpVolleyRequest() {
	}

	public HttpVolleyRequest(Activity mAct) {
		this.mAct = mAct;
	}

//	public HttpVolleyRequest(Activity mAct) {
//		this.isCache = isCache;
//		this.mAct = mAct;
//	}

//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//
//		@SuppressWarnings("unchecked")
//		@Override
//		public void handleMessage(Message msg) {
//			// 防止网络请求在延时前就已结束，此时缓存已经过期了
//			T t = null;
//			if (method == Method.GET) {
//				t = (T) App.getInstance().getCache()
//						.getAsObject(getCacheKey((String) msg.obj));
//			} else {
//				t = (T) App.getInstance().getCache()
//						.getAsObject(getPostCacheKey((String) msg.obj, params));
//			}
//			successListener.onResponse(t);
//		}
//	};

	/**
	 * Volley Get方法
	 * 
	 * @param url
	 *            请求地址
	 * @param parentClass
	 *            父类数据结构
	 * @param class1
	 *            子类数据结构
	 * @param listener
	 *            成功监听
	 * @param errorListener
	 *            错误监听
	 */
	public void HttpVolleyRequestGet(String url, Class<T> parentClass,
			Class<?> class1, NListener<T> listener) {
		
		GsonRequest<T> request = new GsonRequest<T>(Method.GET, url,
				parentClass, class1, SuccessListener(), ErrorListener());
		RequestManager.getRequestQueue().add(request);
		mListener = listener;
//		this.urlKey = url;
//		this.method = Method.GET;
		// 把缓存带出去
//		if (isCache) {
//			T t = (T) App.getInstance().getCache()
//					.getAsObject(getCacheKey(url));
//			if (t != null) {
//				handler.sendMessageDelayed(handler.obtainMessage(0, url), 300);
//			} else {
//				isExit = false;
//			}
//		} else {
//			isExit = false;
//		}
	}

	/**
	 * Volley Post方法
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            上传的Map数据结构
	 * @param parentClass
	 *            父类数据结构
	 * @param class1
	 *            子类数据结构
	 * @param listener
	 *            成功监听
	 * @param errorListener
	 *            错误监听
	 */
	public void HttpVolleyRequestPost(String url,
			HashMap<String, String> params, Class<T> parentClass,
			Class<?> class1, NListener<T> listener, String token) {
		FakeX509TrustManager.allowAllSSL();
		GsonRequest<T> request = new GsonRequest<T>(Method.POST, url, params,
				parentClass, class1, SuccessListener(), ErrorListener());
		
		if(token != null && token.length() > 0) {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "Bearer " + token);
			request.setHeader(headers);
		}

		RequestManager.getRequestQueue().add(request);

		mListener = listener;
//		this.urlKey = url;
//		this.method = Method.POST;
//		this.params = params;
		// 把缓存带出去
//		if (isCache) {
//			T t = (T) App.getInstance().getCache()
//					.getAsObject(getPostCacheKey(url, params));
//			if (t != null) {
//				handler.sendMessageDelayed(handler.obtainMessage(0, url), 300);
//			} else {
//				isExit = false;
//			}
//		} else {
//			isExit = false;
//		}

	}

	/**
	 * 返回成功Response
	 * 
	 * @return
	 */
	private Listener<T> SuccessListener() {
		return new Listener<T>() {

			@Override
			public void onResponse(T response) {
				if (response == null) {
					return;
				}
				
				if (mListener != null) {
					mListener.onResponse(response);
				}
				
//				if ("1".equals(((BaseData<T>) response).status)) {
//					if (isCache) {
//						if (!isExit) {
//							if (successListener != null) {
//								successListener.onResponse(response);
//							}
//						}
//						if (method == Method.GET) {
//							App.getInstance()
//									.getCache()
//									.put(getCacheKey(urlKey),
//											(BaseData<T>) response,
//											DataCache.TIME_DAY);
//						} else {
//							App.getInstance()
//									.getCache()
//									.put(getPostCacheKey(urlKey, params),
//											(BaseData<T>) response,
//											DataCache.TIME_DAY);
//						}
//
//					} else {
//						if (successListener != null) {
//							successListener.onResponse(response);
//						}
//					}
//				} else if ("0".equals(((BaseData<T>) response).status)) {
//
//					if (successListener != null) {
//						successListener.onResponse(response);
//					}
//				}
			}
		};
	}

	/**
	 * 返回错误Response 集中处理错误提示消息
	 * 
	 * @return
	 */
	private Response.ErrorListener ErrorListener() {
		return new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (mAct != null) {
					// toast显示错误信息
					Toast.makeText(mAct,
							VolleyErrorHelper.getMessage(error, mAct),
							Toast.LENGTH_SHORT).show();
				}

				if (mListener != null)
					mListener.onErrResponse(error);
			}
		};
	}

	/**
	 * 完全匹配
	 * 
	 * @param url
	 * @return
	 */
//	private String getCacheKey(String url) {
//		// if (url.contains("&")) {
//		// return url.substring(0, url.indexOf("&"));
//		// } else {
//		// return url;
//		// }
//		return url;
//	}
//
//	private String getPostCacheKey(String url, HashMap<String, String> map) {
//		return url + getParams(map);
//	}

	/**
	 * 根据Map 转成parmas字符串
	 * 
	 * @param localTreeMap
	 * @return
	 */
	public String getParams(HashMap<String, String> map) {
		String str1 = "";
		if (null == map) {
			return str1;
		}
		// 参数为空
		if (map.isEmpty()) {
			return str1;
		}
		Iterator<String> localIterator = map.keySet().iterator();
		while (true) {
			if (!localIterator.hasNext()) {
				return str1.substring(0, -1 + str1.length());
			}
			String str2 = localIterator.next();
			str1 = str1 + str2 + "_" + map.get(str2) + ",";
		}
	}
}
