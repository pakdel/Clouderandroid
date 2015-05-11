package com.amirpakdel.clouderandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amir on 10/4/14.
 */

// FIXME ClouderaManager cm needs to be reset after settings change

public class Application extends android.app.Application {
    private static Context context;
    private static SharedPreferences pref;
    // Caution: The preference manager does not currently store a strong reference to the listener.
    // You must store a strong reference to the listener, or it will be susceptible to garbage collection.
    // We recommend you keep a reference to the listener in the instance data of an object that will exist as long as you need the listener.
    private static SharedPreferences.OnSharedPreferenceChangeListener prefChanged;
    private static ClouderaManager cm;
    private static RequestQueue queue;
    //    private static ImageLoader  imageLoader;
    private static Map<String, Drawable> icons;
    private static Map<String, String> metricNames;

    public static Context getAppContext() {
        return context;
    }

//    public static SharedPreferences getPreferences() {
//        return pref;
//    }

    public static ClouderaManager getClouderaManager() {
        return cm;
    }

    public static void addToVolleyRequestQueue(Request req) {
        queue.add(req);
    }

//    public static ImageLoader.ImageContainer getImage(String imageUrl, ImageLoader.ImageListener listener) {
//        imageLoader.get(imageUrl, listener);
//    }

    public static Drawable getIcon(String name) {
        return icons.get(name);
    }

    public static String getMetricName(String metricName) {
        String niceName = metricNames.get(metricName);
        if (niceName == null) {
            return metricName;
        }
        return niceName;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        // FIXME
//      PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
//      PreferenceManager.setDefaultValues(this, R.xml.pref_general, true);
        PreferenceManager.setDefaultValues(Application.context, R.xml.pref_general, false);


        // Volley
        queue = Volley.newRequestQueue(context);
//        imageLoader = new ImageLoader(queue, new BitmapLruCache());
        // Load icons
        icons = new HashMap<>();

        icons.put("SPARK", context.getResources().getDrawable(R.drawable.spark));

        Bitmap allIcons = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite16x16);
        int stepSize = (int) (context.getResources().getDisplayMetrics().density * 16);
        icons.put("OK", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                3 * 2 * stepSize, stepSize, stepSize
        )));
        icons.put("ERROR", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                4 * 2 * stepSize, stepSize, stepSize
        )));

        icons.put("HBASE", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                8 * 2 * stepSize, stepSize, stepSize
        )));
        icons.put("HDFS", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                9 * 2 * stepSize, stepSize, stepSize
        )));
        icons.put("HUE", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                10 * 2 * stepSize, stepSize, stepSize
        )));
        icons.put("YARN", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                11 * 2 * stepSize, stepSize, stepSize
        )));

        icons.put("ZOOKEEPER", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                13 * 2 * stepSize, stepSize, stepSize
        )));

        icons.put("OOZIE", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                27 * 2 * stepSize, stepSize, stepSize
        )));

        icons.put("HIVE", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                29 * 2 * stepSize, stepSize, stepSize
        )));

        icons.put("FLUME", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                34 * 2 * stepSize, stepSize, stepSize
        )));
        icons.put("maintenance", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                35 * 2 * stepSize, stepSize, stepSize
        )));

        icons.put("IMPALA", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                37 * 2 * stepSize, stepSize, stepSize
        )));

        icons.put("SOLR", new BitmapDrawable(Application.getAppContext().getResources(), Bitmap.createBitmap(allIcons, 0,
                46 * 2 * stepSize, stepSize, stepSize
        )));


        metricNames = new HashMap<>(6);
        metricNames.put("stats(read_bytes_rate_across_disks, total)", "Read");
        metricNames.put("stats(write_bytes_rate_across_disks, total)", "Write");
        metricNames.put("stats(bytes_receive_rate_across_network_interfaces, total)", "RX");
        metricNames.put("stats(bytes_transmit_rate_across_network_interfaces, total)", "TX");
        metricNames.put("stats(bytes_read_rate_across_datanodes, total)", "Read");
        metricNames.put("stats(bytes_written_rate_across_datanodes, total)", "Write");
        metricNames.put("integral(alerts_rate)", "Alert");
        metricNames.put("integral(events_critical_rate)", "Critical");
        metricNames.put("integral(events_important_rate)", "Importatnt");

        cm = new ClouderaManager(
                pref.getString("manager", getString(R.string.pref_default_manager)),
                pref.getString("username", getString(R.string.pref_default_username)),
                pref.getString("password", getString(R.string.pref_default_password))
        );
        prefChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                // Assert cm is not null
                Log.d("prefChanged", "Updating " + s);
                switch (s) {
                    case "manager":
                        cm.setBaseUrl(pref.getString("manager", getString(R.string.pref_default_manager)));
                        break;
                    case "username":
                        cm.setUsername(pref.getString("username", getString(R.string.pref_default_username)));
                        break;
                    case "password":
                        cm.setPassword(pref.getString("password", getString(R.string.pref_default_password)));
                        break;
                    default:
                        Log.e("prefChanged", s + " is not a know preference!");
                        break;
                }
            }
        };
        pref.registerOnSharedPreferenceChangeListener(prefChanged);

    }

//    public class BitmapLruCache
//            extends LruCache<String, Bitmap>
//            implements ImageLoader.ImageCache {
//
//        public BitmapLruCache() {
//            this((int) (Runtime.getRuntime().maxMemory() / 1024) / 8);
//        }
//
//        public BitmapLruCache(int sizeInKiloBytes) {
//            super(sizeInKiloBytes);
//        }
//
//        @Override
//        protected int sizeOf(String key, Bitmap value) {
//            return value.getRowBytes() * value.getHeight() / 1024;
//        }
//
//        @Override
//        public Bitmap getBitmap(String url) {
//            return get(url);
//        }
//
//        @Override
//        public void putBitmap(String url, Bitmap bitmap) {
//            put(url, bitmap);
//        }
//    }

}
