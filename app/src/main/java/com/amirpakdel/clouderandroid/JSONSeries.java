package com.amirpakdel.clouderandroid;

import android.util.Log;

import com.androidplot.xy.XYSeries;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by amir on 10/5/14.
 */
public class JSONSeries implements XYSeries {
    //                                     "2014-10-04'T'01:36:05.195Z";
    private static String timeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
    private String title;
    private JSONArray data;

    public JSONSeries(String title, JSONArray data) {
        this.title = title;
        this.data = (data != null) ? data : new JSONArray();
    }

    public static String getFormattedDate(Date date) {
        return sdf.format(date);
    }

    public void setData(JSONArray data) {
        this.data = data;

//        try {
//            Log.d("JSONSeries: setData", data.toString(2));
//        } catch (JSONException e) {
//            Log.d("JSONSeries: setData", data.toString());
//        }
    }

    @Override
    public int size() {
        return data.length();
    }

    @Override
    public Number getX(int index) {
        // FIXME performance issue
//        return index;
        try {
//            Log.d("getX", ": " + Integer.parseInt( data.getJSONObject(index).getString("timestamp").substring(11, 13) ));
//            return Integer.parseInt( data.getJSONObject(index).getString("timestamp").substring(11, 13) );
            return sdf.parse(data.getJSONObject(index).getString("timestamp")).getTime();
        } catch (JSONException e) {
            Log.e("JSONSeries", e.toString(), e);
            return 0;
        } catch (ParseException e) {
            Log.e("JSONSeries", e.toString(), e);
            return 0;
        }
    }

    @Override
    public Number getY(int index) {
        try {
            return data.getJSONObject(index).getDouble("value");
        } catch (JSONException e) {
            Log.e("JSONSeries", e.toString(), e);
            return 0;
        }
    }

    @Override
    public String getTitle() {
        return title;
    }
}
