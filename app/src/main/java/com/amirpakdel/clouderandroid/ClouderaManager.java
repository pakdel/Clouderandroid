package com.amirpakdel.clouderandroid;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amir on 10/4/14.
 */

// TODO Add a static test function (get version)

public class ClouderaManager {
    private String baseUrl;
    private String username;
    private String password;

    private DrawerAdapter drawerAdapter;

    // FIXME should be static final, unless loaded from web
//    private Drawable icons;
//    private BitmapDrawable icons;
//    private Bitmap icons;

    public ClouderaManager(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;

//        this.clusters = null;
//        this.services = null;

        // Here
//        ImageLoader.ImageContainer images =  Application.getImage("http://192.168.1.126:7180/"+"static/cms/icons/sprite16x16.png",
//                ImageLoader.getImageListener(new ImageView(Application.getAppContext()),0,0) );
//        Bitmap allImages = images.getBitmap();
//        icons = Application.getAppContext().getResources().getDrawable(R.drawable.sprite16x16);
//        BitmapFactory.Options opt = new BitmapFactory.Options();
////        opt.inDensity = Application.getAppContext().getResources().getDisplayMetrics();
//        Bitmap allIcons = BitmapFactory.decodeResource(Application.getAppContext().getResources(), R.drawable.sprite16x16, opt);
////        allIcons.setDensity(Bitmap.DENSITY_NONE);
//        Log.d("All Icons ", ": " + allIcons.getWidth());
////        Log.d("All Icons ", ": " + allIcons.getDensity());
//        Log.d("All Icons ", ": " + Application.getAppContext().getResources().getDisplayMetrics().density);
//        Log.d("All Icons ", ": " + Application.getAppContext().getResources().getDisplayMetrics().densityDpi);
//        Bitmap myIcon = Bitmap.createBitmap(allIcons, 0, 3*32, 16, 16);
//        icons = new BitmapDrawable( Application.getAppContext().getResources(), myIcon );
        this.loadClusters();
    }

    public void refresh() {
        this.loadClusters();
    } //Just an alias to loadClusters()

    private void loadClusters() {
        ClouderaManagerRequest getClustersRequest = new ClouderaManagerRequest(
                baseUrl,
                "clusters",
                username, password,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ClouderaManager: loadClusters", "Found " + response.getJSONArray("items").length() + " cluster(s)");
                            JSONArray clusters = response.getJSONArray("items");
                            drawerAdapter.setClusters(clusters);
//                            services = new ArrayList<JSONArray>(clusters.length());
//                            services = new SparseArray<JSONArray>();
                            for (int i = 0; i < clusters.length(); i++) {
                                Log.d("ClouderaManager: loadCluster ", "Loading services of " + clusters.getJSONObject(i).getString("name"));
                                loadServices(i, clusters.getJSONObject(i).getString("name"));
                            }
                            // TODO
                            // Refresh the drawer
                        } catch (JSONException e) {
                            Log.d("ClouderaManager: Response", response.toString().substring(0, 150));
                            Log.e("ClouderaManager: Response", e.toString(), e);
                        }
                    }
                },
                null
        );
        Application.addToVolleyRequestQueue(getClustersRequest);
    }

    private void loadServices(final int clusterIndex, final String clusterName) {
        Log.d("ClouderaManager: Getting", "clusters/" + clusterName + "/services");
        ClouderaManagerRequest getServicesRequest = new ClouderaManagerRequest(
                baseUrl,
                "clusters/" + clusterName + "/services",
                username, password,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ClouderaManager: Getting", "clusters/" + clusterName + "/services");

//                                services.set(clusterIndex, response.getJSONArray("items"));
                            drawerAdapter.putService(clusterIndex, response.getJSONArray("items"));
                            // TODO
                            // Refresh the drawer on the last service?
                            if (drawerAdapter != null) {
                                drawerAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Log.d("ClouderaManager: Response", response.toString().substring(0, 150));
                            Log.e("ClouderaManager: Response", e.toString(), e);
                        }
                    }
                },
                null
        );

        Application.addToVolleyRequestQueue(getServicesRequest);

    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        this.loadClusters();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.loadClusters();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.loadClusters();
    }

    public void setDrawerAdapter(DrawerAdapter drawerAdapter) {
        this.drawerAdapter = drawerAdapter;
    }
}
