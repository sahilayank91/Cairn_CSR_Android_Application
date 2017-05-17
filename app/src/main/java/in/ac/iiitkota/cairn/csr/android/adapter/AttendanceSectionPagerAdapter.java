package in.ac.iiitkota.cairn.csr.android.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v13.app.FragmentStatePagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import in.ac.iiitkota.cairn.csr.android.AppSingleton;
import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.model.Statistics;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.AttendanceFragment;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

/**
 * Created by SS Verma on 26-03-2017.
 */

public class AttendanceSectionPagerAdapter extends FragmentStatePagerAdapter {
    public AttendanceFragment week_fragment;
    public AttendanceFragment month_fragment;
    public AttendanceFragment year_fragment;
    public Statistics statistics;

    public String domain;
    public Long object_id;
    private FragmentManager fragmentManager;

    public AttendanceSectionPagerAdapter(FragmentManager fm, String domain, Long object_id) {
        super(fm);
        this.domain = domain;
        this.object_id = object_id;
        // new GetSmileData(week_fragment,month_fragment,year_fragment,statistics,domain,object_id).execute();
        new GetSmileData(statistics, domain, object_id).execute();
        week_fragment = new AttendanceFragment();
        month_fragment = new AttendanceFragment();
        year_fragment = new AttendanceFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                if(week_fragment==null){
//                    week_fragment=new AttendanceFragment();
//                 //   week_fragment.scope="week";
//                }
//


                return week_fragment;
            case 1:
//                if(month_fragment==null){
//                    month_fragment=new AttendanceFragment();
//                  //  month_fragment.scope="month";
//                }
                return month_fragment;
            case 2:
//                if(year_fragment==null){
//                    year_fragment=new AttendanceFragment();
//                  //  year_fragment.scope="year";
//                }
                return year_fragment;

        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;
    }

    public class GetSmileData extends AsyncTask<String, String, String> {
        //        AttendanceFragment week_fragment;
//         AttendanceFragment month_fragment;
//        AttendanceFragment year_fragment;
        Statistics statistics;
        public String domain;
        public Long object_id;

        public GetSmileData(Statistics statistics
                , String domain, Long object_id) {

            this.domain = domain;
            this.object_id = object_id;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                statistics = new Statistics(jsonObject);

                week_fragment.setData("week", statistics.getWeek(), statistics.getWeek_dates());
                if (week_fragment != null && week_fragment.isAdded()) {
                    week_fragment.resetViewport();

                }

                month_fragment.setData("month", statistics.getMonth(), statistics.getMonth_dates());
                if (month_fragment != null && month_fragment.isAdded()) {
                    month_fragment.resetViewport();
                }

                year_fragment.setData("year", statistics.getYear(), statistics.getYear_dates());
                if (year_fragment != null && year_fragment.isAdded()) {

                    year_fragment.resetViewport();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            Context context = AppSingleton.getInstance().getApplicationContext();
            String response = "";
            try {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);

                String url = context.getResources().getString(R.string.retrieve_user_statistics) + domain + "/" + object_id + "/";

                response = Server.performServerCall(url, "GET");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
