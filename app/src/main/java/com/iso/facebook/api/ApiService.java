package com.iso.facebook.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iso.facebook.common.CheckNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class ApiService {
    private static final String TAG = "Api Call";

    private final Context mContext;
    private final RequestQueue mRequestQueue;

    public ApiService(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
    }

    public void get(String url, String bearerToken, ApiCallback callback) {
        makeRequest(Request.Method.GET, url, null, bearerToken, callback);
    }

    public void post(String url, JSONObject requestBody, String bearerToken, ApiCallback callback) {
        makeRequest(Request.Method.POST, url, requestBody, bearerToken, callback);
    }

    public void put(String url, JSONObject requestBody, String bearerToken, ApiCallback callback) {
        makeRequest(Request.Method.PUT, url, requestBody, bearerToken, callback);
    }

    public void patch(String url, String bearerToken, ApiCallback callback) {
        makeRequest(Request.Method.PATCH, url, null, bearerToken, callback);
    }

    public void delete(String url, String bearerToken, ApiCallback callback) {
        makeRequest(Request.Method.DELETE, url, null, bearerToken, callback);
    }


    private void makeRequest(int method, final String url, final JSONObject requestBody, final String bearerToken, final ApiCallback callback) {
        if (!CheckNetwork.isNetworkAvailable(mContext)) {
            Log.e(TAG, "No internet connection");
            callback.onError("No internet connection");
            return;
        }

        Log.d(TAG, "Making " + methodToString(method) + " request to: " + endPoints.baseUrl + url);

        StringRequest stringRequest = new StringRequest(method, endPoints.baseUrl + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response received: " + response);
                        try {
                            Object jsonResponse = new JSONTokener(response).nextValue();
                            if (jsonResponse instanceof JSONObject) {
                                callback.onSuccess((JSONObject) jsonResponse);
                            } else if (jsonResponse instanceof JSONArray) {
                                callback.onSuccess((JSONArray) jsonResponse);
                            } else {
                                callback.onError("Unknown response type");
                            }
                        } catch (JSONException e) {
                            callback.onError("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleErrorResponse(error, callback);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                if (requestBody != null) {
                    Log.d(TAG, "Request body: " + requestBody);
                    return requestBody.toString().getBytes();
                }
                return super.getBody();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (bearerToken != null && !bearerToken.isEmpty()) {
                    Log.d(TAG, "Authorization: " + bearerToken);
                    headers.put("Authorization", "Bearer " + bearerToken);
                }
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        mRequestQueue.add(stringRequest);
    }

    private String methodToString(int method) {
        switch (method) {
            case Request.Method.GET:
                return "GET";
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.DELETE:
                return "DELETE";
            case Request.Method.PATCH:
                return "PATCH";
            default:
                return "UNKNOWN";
        }
    }

    private void handleErrorResponse(VolleyError error, ApiCallback callback) {
        if (error instanceof NoConnectionError) {
            Log.e(TAG, "No internet connection");
            callback.onError("No internet connection");
        } else
            if (error instanceof TimeoutError) {
            Log.e(TAG, "Request timeout");
            callback.onError("Request timeout");
        } else if (error instanceof NetworkError) {
            Log.e(TAG, "Network error");
            callback.onError("Network error");
        } else if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            String message = "Unknown error";
            if (error.networkResponse.data != null) {
                try {
                    message = new String(error.networkResponse.data);
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing error response", e);
                }
            }
            Log.e(TAG, "Status code: " + statusCode + ", Message: " + message);
            callback.onError("Status code: " + statusCode + ", Message: " + message);
        } else {
            Log.e(TAG, "Unknown error");
            callback.onError("Unknown error");
        }
    }

    public interface ApiCallback {
        void onSuccess(JSONObject response);

        void onSuccess(JSONArray response);

        void onError(String errorMessage);
    }


}
