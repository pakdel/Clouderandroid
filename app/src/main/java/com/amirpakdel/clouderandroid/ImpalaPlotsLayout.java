package com.amirpakdel.clouderandroid;

import android.content.Context;

/**
 * Created by amir on 11/13/14.
 */
public class ImpalaPlotsLayout extends BasePlotsLayout {
    public ImpalaPlotsLayout(Context context, String name) {
        super(context, name);
    }

    @Override
    protected void setPlots(Context context, String name) {

        plots.add(new BasePlot(context,
                "Total Queries Across Impala Daemons",
                "select total_num_queries_rate_across_impalads where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Queries Across Impala Daemons",
                "select num_queries_rate_across_impalads where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Query Fragments Across Impala Daemons",
                "select total_num_fragments_rate_across_impalads where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Query Fragments Across Impala Daemons",
                "select num_fragments_rate_across_impalads where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Total Assignments Across Impala Daemons",
                "select total_assignments_rate_across_impalads where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Total Assignments Across Impala Daemons",
                "select assignments_rate_across_impalads where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Assignment Locality",
                "select 100 calls.out calls.sh CDH5.dashboards.json dashboards.json total_local_assignments_rate_across_impalads / total_assignments_rate_across_impalads where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Important Events and Alerts",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "StateStore Critical Event and Alert Rate",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where category=ROLE and serviceName=" + name + " and roleType=STATESTORE"
        ));
        plots.add(new BasePlot(context,
                "Catalog Server Critical Event and Alert Rate",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where category=ROLE and serviceName=" + name + " and roleType=CATALOGSERVER"
        ));
        plots.add(new BasePlot(context,
                "Alerts Across Impala Daemons",
                "select integral(alerts_rate_across_impalads) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Critical Events Across Impala Daemons",
                "select integral(events_critical_rate_across_impalads) where entityName=" + name
        ));

    }
}
