package com.amirpakdel.clouderandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ScrollView;

// TODO support more than one CM Manager
// TODO choose between manager / host / api_server /cm
// TODO choose between ArrayList and LinkedList

public class MainActivity extends Activity
        implements DrawerAdapter.OnSetServiceListener {

    private DrawerLayout drawer;
    private DrawerAdapter drawerAdapter;

    private ActionBarDrawerToggle drawerToggle;
    private ScrollView plotsScroll;
    private BasePlotsLayout plotsLayout;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.title = "ClouderAndroid";
        // DrawerLayout
        this.drawer = new DrawerLayout(this);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                    getActionBar().setTitle("ClouderAndroid");
                getActionBar().setTitle(title);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("ClouderAndroid - CDH");
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawer.setDrawerListener(drawerToggle);

        //DEBUG
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        plotsScroll = new ScrollView(this);
        drawer.addView(plotsScroll);

        // DrawerAdapter
        this.drawerAdapter = new DrawerAdapter(this);
        drawerAdapter.setOnSetServiceListener(this);
        ExpandableListView drawerExpandableListView = new ExpandableListView(this);
        drawerExpandableListView.setAdapter(drawerAdapter);
        drawerExpandableListView.setOnChildClickListener(drawerAdapter);
        DrawerLayout.LayoutParams drawerListViewParams = new DrawerLayout.LayoutParams(400, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.START);
        drawerExpandableListView.setLayoutParams(drawerListViewParams);
        drawerExpandableListView.setBackgroundColor(Color.WHITE);
        drawer.addView(drawerExpandableListView);
        Application.getClouderaManager().setDrawerAdapter(drawerAdapter);
        // DrawerAdapter

        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

        setContentView(drawer);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();

        plotsLayoutHelper(new ClusterPlotsLayout(this, "CLUSTER"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                plotsLayout.refreshPlots();
                // FIXME if refresh cluster status on the left menu is set
                Application.getClouderaManager().refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
        plotsLayout.setTimeSpanAndRefresh(getResources().getConfiguration().orientation);
    }

    private boolean plotsLayoutHelper(BasePlotsLayout newPlotsLayout) {
        drawer.closeDrawers();
        plotsLayout = newPlotsLayout;
        plotsScroll.removeAllViews();
        plotsScroll.addView(plotsLayout);
        return true;
    }

    @Override
    public boolean onSetService(String serviceType, String serviceName) {
        // FIXME
        title = serviceName;
        drawer.closeDrawers();
        switch (serviceType) {
            case "CLUSTER":
                return plotsLayoutHelper(new ClusterPlotsLayout(this, serviceName));
            case "FLUME":
                return plotsLayoutHelper(new FlumePlotsLayout(this, serviceName));
            case "HBASE":
                return plotsLayoutHelper(new HbasePlotsLayout(this, serviceName));
            case "HDFS":
                return plotsLayoutHelper(new HdfsPlotsLayout(this, serviceName));
            case "HIVE":
                return plotsLayoutHelper(new HivePlotsLayout(this, serviceName));
            case "HUE":
                return plotsLayoutHelper(new HuePlotsLayout(this, serviceName));
            case "IMPALA":
                return plotsLayoutHelper(new ImpalaPlotsLayout(this, serviceName));
            case "OOZIE":
                return plotsLayoutHelper(new OoziePlotsLayout(this, serviceName));
            case "SOLR":
                return plotsLayoutHelper(new SolrPlotsLayout(this, serviceName));
            case "SPARK":
                return plotsLayoutHelper(new SparkPlotsLayout(this, serviceName));
            case "YARN":
                return plotsLayoutHelper(new YarnPlotsLayout(this, serviceName));
            case "ZOOKEEPER":
                return plotsLayoutHelper(new ZookeeperPlotsLayout(this, serviceName));
        }
        return false;
    }
}
