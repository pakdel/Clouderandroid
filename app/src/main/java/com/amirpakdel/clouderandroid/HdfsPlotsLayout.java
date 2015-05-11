package com.amirpakdel.clouderandroid;

import android.content.Context;

/**
 * Created by amir on 11/13/14.
 */
public class HdfsPlotsLayout extends BasePlotsLayout {
    public HdfsPlotsLayout(Context context, String name) {
        super(context, name);
    }

    @Override
    protected void setPlots(Context context, String name) {

        plots.add(new BasePlot(context,
                "HDFS Capacity",
                "select dfs_capacity, dfs_capacity_used, dfs_capacity_used_non_hdfs where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Bytes Read Across DataNodes",
                "select total_bytes_read_rate_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Bytes Written Across DataNodes",
                "select total_bytes_written_rate_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Blocks Read Across DataNodes",
                "select total_blocks_read_rate_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Blocks Written Across DataNodes",
                "select total_blocks_written_rate_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Transceivers Across DataNodes",
                "select total_xceivers_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Transceivers Across DataNodes",
                "select xceivers_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Packet Ack Round Trip Average Time Across DataNodes",
                "select packet_ack_round_trip_time_nanos_avg_time_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Send Data Packet Transfer Average Time Across DataNodes",
                "select send_data_packet_transfer_nanos_avg_time_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Send Data Packet Blocked On Network Average Time Across DataNodes",
                "select send_data_packet_blocked_on_network_nanos_avg_time_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Disk Flushes Across DataNodes",
                "select flush_nanos_rate_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Average Disk Flush Time Across DataNodes",
                "select flush_nanos_avg_time_across_datanodes where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Important Events and Alerts",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where serviceType=HDFS AND clusterId=$CLUSTERID and category = SERVICE"
        ));
        plots.add(new BasePlot(context,
                "Alerts Across DataNodes",
                "select integral(alerts_rate_across_datanodes) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Critical Events Across DataNodes",
                "select integral(events_critical_rate_across_datanodes) where entityName=" + name
        ));

    }
}
