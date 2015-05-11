package com.amirpakdel.clouderandroid;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.volley.Response;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by amir on 11/2/14.
 */

// TODO set default range format
// TODO start loading at creation time
// DONE round up queries from / to up to minutes => cache better

public class BasePlot extends XYPlot {
    public static Format percentFormat = new DecimalFormat("0 '%'");
    public static Format bitrateFormat = new Format() {
        @Override
        public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
            String format = "%.1f B/s";
            float traffic = ((Number) obj).floatValue();
            if (traffic > 1024) {
                traffic /= 1024;
                if (traffic > 1024) {
                    traffic /= 1024;
                    format = "%.1f M/s";
                } else {
                    // Still KB
                    format = "%.1f K/s";
                }
            }
            return toAppendTo.append(String.format(format, traffic));
        }

        @Override
        public Object parseObject(String source, @NonNull ParsePosition pos) {
            return null;

        }
    };
    // FIXME performance issue
    public static Format dateFormat = new Format() {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        @Override
        public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
            // FIXME performance issue
            // because our timestamps are in seconds and SimpleDateFormat expects milliseconds
            // we multiply our timestamp by 1000:
//                long timestamp = ((Number) obj).longValue() * 1000;
            long timestamp = ((Number) obj).longValue();
            Date date = new Date(timestamp);
            return dateFormat.format(date, toAppendTo, pos);
        }

        @Override
        public Object parseObject(String source, @NonNull ParsePosition pos) {
            return null;

        }
    };
    private static int timeSpan;
    //    public static LineAndPointFormatter blueLine = new LineAndPointFormatter(Color.BLUE, null, null, null);
    private static List<LineAndPointFormatter> lineFormats;
    private String query;

    public BasePlot(Context context, String title, String query) {
        super(context, title);

        this.query = query;

        Point size = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getSize(size);
        this.getRootView().setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (Math.min(size.x, size.y) / 1.4)

        ));
//        this.getGraphWidget().setHeight(500);

        this.getBackgroundPaint().setColor(Color.LTGRAY);
//        this.getTitleWidget().setHeight(40);
//        this.getTitleWidget().setWidth(300);
//        this.getTitleWidget().setPadding(0,0,0,0);
//        this.getTitleWidget().setMargins(0,0,0,0);
        this.getTitleWidget().getLabelPaint().setTextSize(30);
        this.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
        this.getTitleWidget().setSize(new SizeMetrics(
                32, SizeLayoutType.ABSOLUTE,
                1.0f, SizeLayoutType.RELATIVE));

        this.getGraphWidget().setPaddingTop(0);
        this.getGraphWidget().setMarginTop(20);
        this.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        this.getGraphWidget().getDomainGridLinePaint().setColor(Color.BLACK);
        this.getGraphWidget().getDomainGridLinePaint().
                setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
        this.getGraphWidget().getRangeGridLinePaint().setColor(Color.BLACK);
        this.getGraphWidget().getRangeGridLinePaint().
                setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
        this.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        this.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);

        this.getGraphWidget().getBackgroundPaint().setColor(Color.LTGRAY);

        this.getGraphWidget().setPaddingRight(5);
        this.getGraphWidget().setMarginRight(10);
        this.getGraphWidget().setPaddingBottom(5);
        this.getGraphWidget().setMarginBottom(40);
//        this.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);
        this.getDomainLabelWidget().getLabelPaint().setTextSize(20);
//        this.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.RED);
//        this.getGraphWidget().getDomainOriginLabelPaint().setTextSize(50);
        this.getGraphWidget().getDomainLabelPaint().setTextSize(20);
        this.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);

        this.getGraphWidget().setPaddingLeft(0);
        this.getGraphWidget().setMarginLeft(55);
//        this.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
//        this.getRangeLabelWidget().getLabelPaint().setTextSize(15);
        this.getGraphWidget().getRangeLabelPaint().setTextSize(20);
        this.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);

//        this.setTicksPerRangeLabel(3);
        this.getGraphWidget().setDomainLabelOrientation(-45);

//        plot.setDomainLabel("Year");
//        plot.setRangeLabel("# of Sightings");


        this.setDomainValueFormat(BasePlot.dateFormat);
        this.setDomainStep(XYStepMode.SUBDIVIDE, 5);

//        this.setRangeBoundaries(0, 1, BoundaryMode.GROW);
        this.setRangeStep(XYStepMode.SUBDIVIDE, 5);

        this.getLegendWidget().getTextPaint().setTextSize(20);
        this.getLegendWidget().getTextPaint().setColor(Color.BLACK);
        this.getLegendWidget().setHeight(25);
    }

//    public JSONSeries data;

    private static LineAndPointFormatter getLineFormat(int index) {
        if (lineFormats == null) {
            lineFormats = new ArrayList<>(2);
//            lineFormats.add(new LineAndPointFormatter(Color.BLUE, null, null, null));
            lineFormats.add(new LineAndPointFormatter(Color.argb(196, 0, 0, 200), null, null, null));
            lineFormats.add(new LineAndPointFormatter(Color.argb(196, 64, 64, 196), null, null, null));
            lineFormats.add(new LineAndPointFormatter(Color.GREEN, null, null, null));
        }
        return lineFormats.get(index % lineFormats.size());
    }

    public void setTimeSpanAndRefresh(int orientation) {
//        Log.d("setTimeSpanAndRefresh", "Setting TS of " + this.getTitle() + " to "  + orientation);
        // Checks the orientation of the screen
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            timeSpan = -90;
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        } else {
            timeSpan = -45;
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }

        refresh();
    }

    public void refresh() {
//        Log.d("Here 2.0", "Refreshing");
        // Prepare request
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("query", query));
//                "select stats(bytes_receive_rate_across_network_interfaces, total), stats(bytes_transmit_rate_across_network_interfaces, total) where category = CLUSTER"));
        params.add(new BasicNameValuePair("contentType", "application/json"));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);

//        Log.d("Params: to", JSONSeries.getFormattedDate(cal.getTime()));
        params.add(new BasicNameValuePair("to", JSONSeries.getFormattedDate(cal.getTime())));
//        cal.add(Calendar.HOUR, timeSpan);
        cal.add(Calendar.MINUTE, timeSpan);
//        Log.d("Params: from", JSONSeries.getFormattedDate(cal.getTime()));
        params.add(new BasicNameValuePair("from", JSONSeries.getFormattedDate(cal.getTime())));

        final BasePlot thisPlot = this;

        ClouderaManagerRequest getRequest = new ClouderaManagerRequest(
                Application.getClouderaManager(),
                "timeseries",
                params,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                                    Log.d("ClouderaManagerRequest: Response", response.toString(2).substring(0, 50));
//                                    Log.d("ClouderaManagerRequest: Response",
//                                            response
//                                                    .getJSONArray("items")
//                                                    .getJSONObject(0)
//                                                    .getJSONArray("timeSeries")
//                                                    .getJSONObject(0)
//                                                    .getJSONArray("data")
//                                                    .toString(2).substring(0, 150));
//
//                            data = new JSONSeries("Net RX", null);
//                            thisPlot.addSeries(data, getLineFormat());
//
//                            data.setData(response
//                                    .getJSONArray("items")
//                                    .getJSONObject(0)
//                                    .getJSONArray("timeSeries")
//                                    .getJSONObject(0)
//                                    .getJSONArray("data"));
////                                    plot1.setDomainStep(XYStepMode.SUBDIVIDE, years.length);

                            JSONArray timeSeries = response.getJSONArray("items").getJSONObject(0).getJSONArray("timeSeries");
                            // In case of a single Data Series, we do not need a legend
                            if (timeSeries.length() == 1) {
                                getLayoutManager().remove(getLegendWidget());
                            }
//                            services = new ArrayList<JSONArray>(clusters.length());
                            // Its not the best, but works well in small series
                            timeSeriesLoop:
                            for (int i = 0; i < timeSeries.length(); i++) {
                                String metricName = Application.getMetricName(timeSeries.getJSONObject(i).getJSONObject("metadata").getString("metricName"));
                                for (XYSeries tmpSeries : thisPlot.getSeriesSet()) {
                                    if (tmpSeries.getTitle().equals(metricName)) {
//                                        Log.d("   refresh", "Found " + metricName);
                                        ((JSONSeries) tmpSeries).setData(timeSeries.getJSONObject(i).getJSONArray("data"));
                                        continue timeSeriesLoop;
                                    }
                                }
                                thisPlot.addSeries(
                                        new JSONSeries(metricName, timeSeries.getJSONObject(i).getJSONArray("data")),
                                        getLineFormat(i)
                                );
                            }
                            redraw();
                        } catch (JSONException e) {
                            Log.d("ClouderaManagerRequest: Response", response.toString().substring(0, 150));
                            Log.e("ClouderaManagerRequest: Response", e.toString(), e);
                        }
                    }
                },
                null
        );
        // add it to the RequestQueue
        Application.addToVolleyRequestQueue(getRequest);
    }


}
