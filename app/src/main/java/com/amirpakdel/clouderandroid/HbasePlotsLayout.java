package com.amirpakdel.clouderandroid;

import android.content.Context;

/**
 * Created by amir on 11/13/14.
 */
public class HbasePlotsLayout extends BasePlotsLayout {
    public HbasePlotsLayout(Context context, String name) {
        super(context, name);
    }

    @Override
    protected void setPlots(Context context, String name) {

        plots.add(new BasePlot(context,
                "Total Regions Across RegionServers",
                "select total_regions_across_regionservers where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Regions Across RegionServers",
                "select regions_across_regionservers where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Read and Write Requests",
                "select total_read_requests_rate_across_regionservers, total_write_requests_rate_across_regionservers where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Important Events and Alerts",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "HBase Master Critical Event and Alert Rate",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where category=ROLE and serviceName=" + name + " and roleType=MASTER"
        ));
        plots.add(new BasePlot(context,
                "Alerts Across RegionServers",
                "select integral(alerts_rate_across_regionservers) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Critical Events Across RegionServers",
                "select integral(events_critical_rate_across_regionservers) where entityName=" + name
        ));

    }
}
