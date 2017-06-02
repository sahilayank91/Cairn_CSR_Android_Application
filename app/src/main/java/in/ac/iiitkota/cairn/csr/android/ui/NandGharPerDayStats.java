package in.ac.iiitkota.cairn.csr.android.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import in.ac.iiitkota.cairn.csr.android.AppSingleton;
import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.model.DayAttendance;
import in.ac.iiitkota.cairn.csr.android.model.NandgramAttendance;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

public class NandGharPerDayStats extends AppCompatActivity {

    private CustomCalendarView calendarView;
    private Button btnProceed;
    private TextView selectedDate;
    private TextView morningHeadcount;
    private TextView eveningHeadcount;
    private ImageView morningHeadcountImage;
    private ImageView eveningHeadcountImage;
    private CardView calendarHolder;
    private CardView headcountHolder;
    private String date;
    private Long nandgram_id;

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
        eveningHeadcount = (TextView) findViewById(R.id.evening_headcount_text);
        eveningHeadcountImage = (ImageView) findViewById(R.id.evening_headcount_image);
        headcountHolder = (CardView) findViewById(R.id.head_count_holder);
        calendarHolder = (CardView) findViewById(R.id.calender_holder);
        selectedDate = (TextView) findViewById(R.id.selected_date);
        nandgram_id = getIntent().getLongExtra("nandgram_id",-1);
        parentScrollView = (NestedScrollView) findViewById(R.id.parent_scrollview);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        selectedDateStr = simpleDateFormat.format(new Date(calendarView.getDate()));
        date = simpleDateFormat1.format(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDateStr = dayOfMonth + "-" + (month + 1) + "-" + year;
                date = year + "-" + (month+1) + "-" + dayOfMonth;


            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCalenderVisible) {
                    isCalenderVisible = false;
                    calendarHolder.setVisibility(View.GONE);
                    parentScrollView.scrollTo(0, 0);
                    btnProceed.setText("Change date");
                    selectedDate.setText(selectedDateStr);
                    new getDayAttendance().execute(date);
                    headcountHolder.setVisibility(View.VISIBLE);
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


    class getDayAttendance extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... s) {

            String result = "";
            try {
                result = Server.performServerCall(getResources().getString(R.string.get_day_attendance)+nandgram_id.toString()+"/" +s[0]+"/","GET");
                Log.e("result",result);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                DayAttendance dayAttendance = new DayAttendance(new JSONObject(s));
                morningHeadcount = (TextView)findViewById(R.id.morning_headcount_text);
                eveningHeadcount = (TextView)findViewById(R.id.evening_headcount_text);
                morningHeadcount.setText(String.valueOf(dayAttendance.getHead_count_slot1()));
                eveningHeadcount.setText(String.valueOf(dayAttendance.getHead_count_slot2()));
                String morning_image_url = AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.images) + dayAttendance.getSlot1_image();
                Picasso.with(getApplicationContext())
                        .load(morning_image_url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                if (bitmap == null) {
                                    return;
                                }

                                morningHeadcountImage.setImageBitmap(bitmap);

                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch swatch = palette.getDominantSwatch();
                                                if (swatch == null) {
                                                    //Toast.makeText(context, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                morningHeadcountImage.setBackgroundColor(swatch.getRgb());
                                            }
                                        });
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });



                String evening_image_url = AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.images) + dayAttendance.getSlot2_image();
                Picasso.with(getApplicationContext())
                        .load(evening_image_url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                if (bitmap == null) {
                                    return;
                                }

                                eveningHeadcountImage.setImageBitmap(bitmap);

                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch swatch = palette.getDominantSwatch();
                                                if (swatch == null) {
                                                    //Toast.makeText(context, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                eveningHeadcountImage.setBackgroundColor(swatch.getRgb());
                                            }
                                        });
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


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
