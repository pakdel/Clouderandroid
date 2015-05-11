package com.amirpakdel.clouderandroid;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amir on 10/4/14.
 */

// TODO Do not need "?" when there is no params

public class ClouderaManagerRequest extends JsonObjectRequest {

    private static Response.Listener<JSONObject> defaultListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                Log.d("ClouderaManagerRequest: Response Fallback", response.toString(2).substring(0, 50));
            } catch (JSONException e) {
                Log.d("ClouderaManagerRequest: Response Fallback", response.toString().substring(0, 50));
            }
        }
    };
    private static Response.ErrorListener defaultErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ClouderaManagerRequest: Error.Response Fallback", error.toString(), error);
        }
    };
    private String username;
    private String password;

    public ClouderaManagerRequest(ClouderaManager cm, String api, List<NameValuePair> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, cm.getBaseUrl() + api + ((params != null) ? "?" + URLEncodedUtils.format(params, "UTF-8") : ""),
                null,
                (listener != null) ? listener : defaultListener,
                (errorListener != null) ? errorListener : defaultErrorListener);
        username = cm.getUsername();
        password = cm.getPassword();
    }

    public ClouderaManagerRequest(String baseUrl, String api, String initUsername, String initPassword, List<NameValuePair> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, baseUrl + api + ((params != null) ? "?" + URLEncodedUtils.format(params, "UTF-8") : ""),
                null,
                (listener != null) ? listener : defaultListener,
                (errorListener != null) ? errorListener : defaultErrorListener);
        username = initUsername;
        password = initPassword;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // "Content-Type" header to "application/json"
//        Log.d("ClouderaManagerRequest: ", " 1 getHeaders");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " +
                        Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP)
        );
//        Log.d("ClouderaManagerRequest: ", " 2 getHeaders: Authorization: '" + headers.get("Authorization").toString() + "'");
        return headers;
    }
}
