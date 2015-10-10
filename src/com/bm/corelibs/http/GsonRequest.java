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
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.bm.corelibs.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonRequest<T> extends JsonRequest<T> {

	/**
	 * Gson parser
	 */

	public Gson mGson = new GsonBuilder().registerTypeAdapter(java.util.Date.class, new DateDeserializerUtils()).registerTypeAdapter(java.util.Date.class, new DateSerializerUtils()).enableComplexMapKeySerialization().setDateFormat(DateFormat.LONG).create();

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
	private Map<String, String> headers;

	/**
	 * @param method
	 *            Request type.. Method.GET etc
	 * @param url
	 *            path for the requests
	 * @param objectClass
	 *            expected class type for the response. Used by gson for serialization.
	 * @param listener
	 *            handler for the response
	 * @param errorListener
	 *            handler for errors
	 */
	public GsonRequest(int method, String url, Class<T> parentClass, Class<?> objectClass, Listener<T> listener, ErrorListener errorListener) {

		super(method, url, "", listener, errorListener);
		this.parentClass = parentClass;
		this.mClass = objectClass;
		this.mListener = listener;
		setTimeOut();
	}

	public GsonRequest(int method, String url, Map<String, String> params, Class<T> parentClass, Class<?> class1, Listener<T> listener, ErrorListener errorListener) {

		super(method, url, new JSONObject(params).toString(), listener, errorListener);
		this.param = params;
		this.parentClass = parentClass;
		this.mClass = class1;
		this.mListener = listener;
		setTimeOut();
	}

	public GsonRequest(int method, String url, String params, Class<T> parentClass, Class<?> class1, Listener<T> listener, ErrorListener errorListener) {

		super(method, url, params, listener, errorListener);
		this.parentClass = parentClass;
		this.mClass = class1;
		this.mListener = listener;
		setTimeOut();

	}
	
	public void setHeader(Map<String, String> headers){
		this.headers = headers;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers == null ? super.getHeaders() : headers;
	}

	// 30秒超时，最大请求次数为1
	private void setTimeOut() {
		setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));
	}

	// @Override
	// protected Map<String, String> getParams() throws AuthFailureError {
	// return param;
	// }

	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		String json;
		try {
			json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			if (Configuration.isShowNetworkJson())
				Log.e("result", json);
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
		Type objectType;
		if (mClass != null) {
			objectType = type(parentClass, mClass);
		} else {
			objectType = parentClass;
		}

		return (Response<T>) Response.success(mGson.fromJson(json, objectType), HttpHeaderParser.parseCacheHeaders(response));
	}

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

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	public void deliverError(VolleyError error) {
		Log.e("error", error.toString());
		super.deliverError(error);
	}

}
