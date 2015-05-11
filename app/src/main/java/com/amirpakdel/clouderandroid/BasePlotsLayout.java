package com.amirpakdel.clouderandroid;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by amir on 11/13/14.
 */
public abstract class BasePlotsLayout extends LinearLayout {
    List<BasePlot> plots;

    public BasePlotsLayout(Context context, String name) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        // TODO LinkedList or ArrayList
//        plots = new ArrayList<BasePlot>(4);
        plots = new LinkedList<>();

        this.setPlots(context, name);

        for (BasePlot plot : plots) {
            plot.setTimeSpanAndRefresh(getResources().getConfiguration().orientation);
            this.addView(plot);
        }
    }
//    protected abstract void setSummary(Context context, String name);

    protected abstract void setPlots(Context context, String name);

    public void refreshPlots() {
        for (BasePlot tmpPlot : plots) {
            tmpPlot.refresh();
        }
    }

    public void setTimeSpanAndRefresh(int orientation) {
        for (BasePlot tmpPlot : plots) {
            tmpPlot.setTimeSpanAndRefresh(getResources().getConfiguration().orientation);
        }
    }


}
