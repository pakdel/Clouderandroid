package com.amirpakdel.clouderandroid;

import android.content.Context;

/**
 * Created by amir on 11/13/14.
 */
public class FlumePlotsLayout extends BasePlotsLayout {
    public FlumePlotsLayout(Context context, String name) {
        super(context, name);
    }

    @Override
    protected void setPlots(Context context, String name) {

        plots.add(new BasePlot(context,
                "Important Events and Alerts",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Alerts Across Flume Agents",
                "select integral(alerts_rate_across_agents) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Critical Events Across Flume Agents",
                "select integral(events_critical_rate_across_agents) where entityName=" + name
        ));

    }
}
