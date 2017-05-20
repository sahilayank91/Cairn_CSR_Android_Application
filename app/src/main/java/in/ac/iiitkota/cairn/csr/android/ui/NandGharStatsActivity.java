package in.ac.iiitkota.cairn.csr.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.AttendanceSectionPagerAdapter;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.AttendanceFragment;
import me.relex.circleindicator.CircleIndicator;


public class NandGharStatsActivity extends AppCompatActivity {
    private AttendanceFragment week_fragment, month_fragment, year_fragment;

    private LinearLayout statsTextHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nandghar_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.nandghar_stat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        week_fragment = new AttendanceFragment();
        month_fragment = new AttendanceFragment();
        year_fragment = new AttendanceFragment();
        AttendanceSectionPagerAdapter sectionsPagerAdapter = new AttendanceSectionPagerAdapter(getFragmentManager(), "user", UserData.getInstance(this).getUser_id());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_profile);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(sectionsPagerAdapter);
        indicator.setViewPager(viewPager);

        statsTextHolder = (LinearLayout) findViewById(R.id.day_viz_stats_holder);
        statsTextHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NandGharStatsActivity.this, NandGharPerDayStats.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
