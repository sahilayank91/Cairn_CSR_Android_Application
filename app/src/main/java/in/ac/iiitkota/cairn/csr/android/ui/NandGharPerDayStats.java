package in.ac.iiitkota.cairn.csr.android.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.ac.iiitkota.cairn.csr.android.R;

public class NandGharPerDayStats extends AppCompatActivity {

    private CustomCalendarView calendarView;
    private Button btnProceed;
    private TextView selectedDate;
    private TextView morningHeadcount;
    private TextView eveningHedcount;
    private ImageView morningHeadcountImage;
    private ImageView eveningHeadcountImage;
    private CardView calendarHolder;
    private CardView headcountHolder;

    private String selectedDateStr;
    private boolean isCalenderVisible = true;

    private NestedScrollView parentScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nand_ghar_per_day_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("NandGhar per day stats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        morningHeadcount = (TextView) findViewById(R.id.morning_headcount_text);
        morningHeadcountImage = (ImageView) findViewById(R.id.morning_headcount_image);
        eveningHedcount = (TextView) findViewById(R.id.evening_headcount_text);
        eveningHeadcountImage = (ImageView) findViewById(R.id.evening_headcount_image);
        headcountHolder = (CardView) findViewById(R.id.head_count_holder);
        calendarHolder = (CardView) findViewById(R.id.calender_holder);
        selectedDate = (TextView) findViewById(R.id.selected_date);

        parentScrollView = (NestedScrollView) findViewById(R.id.parent_scrollview);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd / MM / yyyy");
        selectedDateStr = simpleDateFormat.format(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDateStr = dayOfMonth + " / " + (month + 1) + " / " + year;
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCalenderVisible) {
                    isCalenderVisible = false;
                    calendarHolder.setVisibility(View.GONE);
                    headcountHolder.setVisibility(View.VISIBLE);
                    parentScrollView.scrollTo(0, 0);
                    btnProceed.setText("Change date");
                    selectedDate.setText(selectedDateStr);
                    //
                    //morningHeadcount.setText("24");
                    //...
                } else {
                    isCalenderVisible = true;
                    calendarHolder.setVisibility(View.VISIBLE);
                    headcountHolder.setVisibility(View.GONE);
                    btnProceed.setText("Proceed");
                }
            }
        });


        parentScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                calendarView.requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        calendarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        parentScrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        parentScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;

            }
        });

    }


    public void setHeadCountHolder(){


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

    @Override
    public void onBackPressed() {

        if (calendarHolder.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            isCalenderVisible = true;
            calendarHolder.setVisibility(View.VISIBLE);
            calendarHolder.setVisibility(View.VISIBLE);
            headcountHolder.setVisibility(View.GONE);
            btnProceed.setText(R.string.proceed);
        }

        //super.onBackPressed();
    }

}
