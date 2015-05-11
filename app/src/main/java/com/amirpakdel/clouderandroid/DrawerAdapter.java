package com.amirpakdel.clouderandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amir on 11/9/14.
 */
public class DrawerAdapter extends BaseExpandableListAdapter
        implements ExpandableListView.OnChildClickListener {

    private OnSetServiceListener onSetServiceListener;
    private JSONArray clusters;
    //    private List<JSONArray> services;
    private SparseArray<JSONArray> services;
    private Context context;

    public DrawerAdapter(Context context) {
        this.context = context;
        this.services = new SparseArray<>();
        this.onSetServiceListener = new OnSetServiceListener() {
            @Override
            public boolean onSetService(String serviceType, String serviceName) {
                Log.d("DrawerAdapter", "No OnSetServiceListener is set.");
                return false;
            }
        };
    }

    public void setOnSetServiceListener(OnSetServiceListener onSetServiceListener) {
        this.onSetServiceListener = onSetServiceListener;
    }

    //    public JSONArray getClusters() {
//        return clusters;
//    }
    public void setClusters(JSONArray clusters) {
        this.clusters = clusters;
    }

    //    public SparseArray<JSONArray> getServices() {
//        return services;
//    }
    public void putService(int i, JSONArray service) {
        this.services.put(i, service);
    }

    @Override
    public int getGroupCount() {
        if (clusters == null) return 0;
        return clusters.length();
    }

    @Override
    public int getChildrenCount(int i) {
        // Here Cluster
        // Element 0 is always the CLUSTER itself
        if (services == null) return 1;
        return services.get(i).length() + 1;
    }

    @Override
    public Object getGroup(int i) {
        try {
            return clusters.getJSONObject(i);
        } catch (JSONException e) {
            // FIXME
//                e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getChild(int i, int i2) {
        // Here Cluster
        try {
            if (i2 == 0) {
                return new JSONObject("{}");
            }
            return services.get(i).getJSONObject(i2 - 1);
        } catch (JSONException e) {
            // FIXME
//                e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        // Here Cluster
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        // TODO maybe true
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = new TextView(context);
            ((TextView) view).setTextSize(20);
            view.setPadding(50, 20, 10, 20);
        }
        String clusterName;
//            String clusterMode;
        try {
            clusterName = clusters.getJSONObject(i).getString("name");
//                clusterMode = clusters.getJSONObject(i).getBoolean("maintenanceMode")? "maintenance":"OK";
        } catch (JSONException e) {
            // FIXME
//                e.printStackTrace();
//                Log.e("getGroupView", "JSONExceptopn", e);
            clusterName = "Failed to find cluster " + i;
//                clusterMode = "ERROR";
        }
//            Spannable text1 = new SpannableString( ((b) ?"V ":"> ") + cluster);
//            text1.setSpan(new RelativeSizeSpan(1.5f), 0, drug.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((TextView) convertView).setText(text1, TextView.BufferType.SPANNABLE);
//            ((TextView) view).setText(((b) ?"V ":"> ") + cluster);
        ((TextView) view).setText(" " + clusterName);
//            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(Application.getIcon(clusterMode), null, null, null);

        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
//                view = new TextView(context);
//                ((TextView) view).setTextSize(18);
//                view.setPadding(70,20,10,20);
            view = new ServiceView(context);
            ((ServiceView) view).setTextSize(18);
            view.setPadding(30, 20, 10, 20);
        }
        String serviceName;
        String serviceType;
        String healthSummary;
        try {
            // Here Cluster
            if (i2 == 0) {
                serviceName = clusters.getJSONObject(i).getString("name") + " summary";
                serviceType = "CLUSTER";
                // FIXME maintenanceMode is not CONCERNING
                healthSummary = (clusters.getJSONObject(i).getBoolean("maintenanceMode")) ? "CONCERNING" : "GOOD";
            } else {
                serviceName = services.get(i).getJSONObject(i2 - 1).getString("name");
                serviceType = services.get(i).getJSONObject(i2 - 1).getString("type");
                healthSummary = services.get(i).getJSONObject(i2 - 1).getString("healthSummary");
            }

        } catch (JSONException e) {
            // FIXME
//                e.printStackTrace();
            serviceName = "Failed to find cluster " + i;
            serviceType = "ERROR";
            healthSummary = "BAD";
        }
//            Spannable text1 = new SpannableString( ((b) ?"V ":"> ") + cluster);
        //text1.setSpan(new RelativeSizeSpan(1.5f), 0, drug.getName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((TextView) convertView).setText(text1, TextView.BufferType.SPANNABLE);
//            ((TextView) view).setText(" " + serviceName);
        ((ServiceView) view).setText(" " + serviceName);
        ((ServiceView) view).setServiceType(serviceType);
        ((ServiceView) view).setHealthSummary(healthSummary);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    private String getServiceType(int i, int i2) {
        try {
            // Here Cluster
            if (i2 == 0) {
                return "CLUSTER";
            }
            return services.get(i).getJSONObject(i2 - 1).getString("type");
        } catch (JSONException e) {
            // FIXME
//            e.printStackTrace();
            return "";
        }
    }

    private String getServiceName(int i, int i2) {
        try {
            // Here Cluster
            if (i2 == 0) {
                return clusters.getJSONObject(i).getString("name");
            }
            return services.get(i).getJSONObject(i2 - 1).getString("name");
        } catch (JSONException e) {
            // FIXME
//            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
//        Log.d("onItemClick", " i: " + i + " i2: " + i2 + ", l: " + l + " => " + getServiceType(i, i2) + " / " + getServiceName(i, i2));
        return this.onSetServiceListener.onSetService(getServiceType(i, i2), getServiceName(i, i2));
//        return false;
    }

    public interface OnSetServiceListener {
        boolean onSetService(String serviceType, String serviceName);
    }

    private class ServiceView extends TextView {
        String serviceType;
        //        private Drawable icon;
//        private String serviceState; //"STARTED"
        private String healthSummary; //"GOOD"

        public ServiceView(Context context) {
            super(context);
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        //        public void setIcon(Drawable icon) {
//            this.icon = icon;
//        }
        public void setHealthSummary(String healthSummary) {
            this.healthSummary = healthSummary;
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
//            Rect r = new Rect();
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            switch (healthSummary) {
                case "GOOD":
                    paint.setColor(0x8000AA00);
                    break;
                case "CONCERNING":
                    paint.setColor(0x80CC6600);
                    break;
                case "BAD":
                    paint.setColor(0x80AA0000);
                    break;
                default:
                    paint.setColor(0x80331111);
                    break;
            }

            canvas.drawRect(2f, 0f, 17f, canvas.getHeight(), paint);
            this.setCompoundDrawablesWithIntrinsicBounds(Application.getIcon(serviceType), null, null, null);

            super.onDraw(canvas);
        }
    }
}
