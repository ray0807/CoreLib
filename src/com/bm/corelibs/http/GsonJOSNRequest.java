package com.bm.corelibs.http;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bm.corelibs.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonJOSNRequest<T> extends JsonObjectRequest {

	/**
	 * Gson parser
	 */

	public Gson mGson = new GsonBuilder().registerTypeAdapter(java.util.Date.class, new DateDeserializerUtils())
			.registerTypeAdapter(java.util.Date.class, new DateSerializerUtils()).enableComplexMapKeySerialization()
			.setDateFormat(DateFormat.LONG).create();

	private final Class<T> parentClass;
	/**
	 * Class type for the response
	 */
	private final Class<?> mClass;

	/**
	 * Callback for response delivery
	 */
	private final Listener<T> mListener;

	private Map<String, String> param = null;

	/**
	 * @param method
	 *            Request type.. Method.GET etc
	 * @param url
	 *            path for the requests
	 * @param objectClass
	 *            expected class type for the response. Used by gson for
	 *            serialization.
	 * @param listener
	 *            handler for the response
	 * @param errorListener
	 *            handler for errors
	 */
	public GsonJOSNRequest(int method, String url, Class<T> parentClass, Class<?> objectClass, Listener<T> listener,
			ErrorListener errorListener) {

		super(method, url, null, jListener, errorListener);
		this.parentClass = parentClass;
		this.mClass = objectClass;
		this.mListener = listener;
		setTimeOut();
	}

	public GsonJOSNRequest(int method, String url, Map<String, String> params, Class<T> parentClass, Class<?> class1,
			Listener<T> listener, ErrorListener errorListener) {

		super(method, url, new JSONObject(params), jListener, errorListener);
		this.param = params;
		this.parentClass = parentClass;
		this.mClass = class1;
		this.mListener = listener;
		setTimeOut();
		
	}

	// 30秒超时，最大请求次数为1
	private void setTimeOut() {
		setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return param;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//		String json;
//		try {
//			json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//			if(Configuration.isShowNetworkJson())
//				Log.e("result", json);
//		} catch (UnsupportedEncodingException e) {
//			return Response.error(new ParseError(e));
//		}	
//		Type objectType;
//		if (mClass != null) {
//			objectType = type(parentClass, mClass);
//		} else {
//			objectType = parentClass;
//		}
//
//		return (Response<JSONObject>) Response.success(mGson.fromJson(json, objectType),
//				HttpHeaderParser.parseCacheHeaders(response));
//	}

	static ParameterizedType type(final Class<?> raw, final Type... args) {
		return new ParameterizedType() {

			@Override
			public Type getRawType() {
				return raw;
			}

			@Override
			public Type[] getActualTypeArguments() {
				return args;
			}

			@Override
			public Type getOwnerType() {
				return null;
			}
		};
	}
	
	private static Response.Listener<JSONObject> jListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject arg0) {
			Log.e("JSONObject", arg0.toString());
		}
	};
	
}
