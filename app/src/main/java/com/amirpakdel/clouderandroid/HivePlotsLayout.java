package com.amirpakdel.clouderandroid;

import android.content.Context;

/**
 * Created by amir on 11/13/14.
 */
public class HivePlotsLayout extends BasePlotsLayout {
    public HivePlotsLayout(Context context, String name) {
        super(context, name);
    }

    @Override
    protected void setPlots(Context context, String name) {

        plots.add(new BasePlot(context,
                "CPU Cores Used",
                "select cpu_system_rate + cpu_user_rate where category=ROLE and serviceName=" + name
        ));
        plots.add(new BasePlot(context,
                "Important Events and Alerts",
                "select integral(alerts_rate), integral(events_critical_rate), integral(events_important_rate) where entityName=" + name
        ));
        plots.add(new BasePlot(context,
                "Critical Events and Alerts",
                "select integral(alerts_rate), integral(events_critical_rate) where category=role and serviceName=" + name
        ));
        plots.add(new BasePlot(context,
                "Hive Metastore Server Canary Duration",
                "select canary_duration where category=ROLE and roleType=HIVEMETASTORE and serviceName=" + name
        ));

    }
}
