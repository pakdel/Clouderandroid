package com.amirpakdel.clouderandroid;

import android.content.Context;

/**
 * Created by amir on 11/13/14.
 */
public class YarnPlotsLayout extends BasePlotsLayout {
    public YarnPlotsLayout(Context context, String name) {
        super(context, name);
    }

    @Override
    protected void setPlots(Context context, String name) {

        plots.add(new BasePlot(context,
                "Applications Running (Cumulative)",
                "select apps_running_cumulative where category=YARN_POOL and serviceName=" + name + " and queueName=root"
        ));
        plots.add(new BasePlot(context,
                "Applications Failed (Cumulative)",
                "select apps_failed_cumulative_rate where category=YARN_POOL and serviceName=" + name + " and queueName=root"
        ));
        plots.add(new BasePlot(context,
                "Applications Killed (Cumulative)",
                "select apps_killed_cumulative_rate where category=YARN_POOL and serviceName=" + name + " and queueName=root"
        ));
        plots.add(new BasePlot(context,
                "Total Containers Running Across NodeManagers",
                "select total_containers_running_across_nodemanagers where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Pending Containers (Cumulative)",
                "select pending_containers_cumulative where category=YARN_POOL and serviceName=" + name + " and queueName=root"
        ));
        plots.add(new BasePlot(context,
                "Total Containers Failed Across NodeManagers",
                "select total_containers_failed_rate_across_nodemanagers where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Important Events and Alerts",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "ResourceManager Critical Event and Alert Rate",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where category=ROLE and serviceName=" + name + " and roleType=RESOURCEMANAGER"
        ));
        plots.add(new BasePlot(context,
                "Alerts Across NodeManagers",
                "select integral(alerts_rate_across_nodemanagers) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Critical Events Across NodeManagers",
                "select integral(events_critical_rate_across_nodemanagers) where entityName=" + name
        ));

    }
}
