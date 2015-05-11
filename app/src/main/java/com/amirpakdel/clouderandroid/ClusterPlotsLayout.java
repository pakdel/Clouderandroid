package com.amirpakdel.clouderandroid;

import android.content.Context;

import com.androidplot.xy.BoundaryMode;

/**
 * Created by amir on 11/13/14.
 */
public class ClusterPlotsLayout extends BasePlotsLayout {
    public ClusterPlotsLayout(Context context, String name) {
        super(context, name);
    }

    @Override
    protected void setPlots(Context context, String name) {
        BasePlot plot;

        plot = new BasePlot(context, "Cluster CPU", "select cpu_percent_across_hosts where category = CLUSTER");
        plot.setRangeValueFormat(BasePlot.percentFormat);
        plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        plots.add(plot);

        plot = new BasePlot(context, "Cluster Disk IO", "select stats(read_bytes_rate_across_disks, total), stats(write_bytes_rate_across_disks, total) where category = CLUSTER");
        plot.setRangeValueFormat(BasePlot.bitrateFormat);
        plots.add(plot);

        plot = new BasePlot(context, "Cluster Network IO", "select stats(bytes_receive_rate_across_network_interfaces, total), stats(bytes_transmit_rate_across_network_interfaces, total) where category = CLUSTER");
        plot.setRangeValueFormat(BasePlot.bitrateFormat);
        plots.add(plot);

        plot = new BasePlot(context, "HDFS IO", "select stats(bytes_read_rate_across_datanodes, total), stats(bytes_written_rate_across_datanodes, total) where category = SERVICE and serviceType = HDFS");
        plot.setRangeValueFormat(BasePlot.bitrateFormat);
        plots.add(plot);
    }
}
