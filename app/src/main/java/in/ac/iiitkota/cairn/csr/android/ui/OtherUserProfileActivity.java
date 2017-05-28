package in.ac.iiitkota.cairn.csr.android.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.StatisticsSectionPagerAdapter;
import in.ac.iiitkota.cairn.csr.android.model.UserProfile;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import in.ac.iiitkota.cairn.csr.android.utilities.Server;
import me.relex.circleindicator.CircleIndicator;

public class OtherUserProfileActivity extends AppCompatActivity {

    ImageView profileImage ;

    private TextView name,department,summary,member_since;
    Long user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle extras = getIntent().getExtras();
        user_id = extras.getLong("user_id"); //clicked item user_id
        StatisticsSectionPagerAdapter sectionsPagerAdapter = new StatisticsSectionPagerAdapter(getFragmentManager(), "user", user_id );
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_profile);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(sectionsPagerAdapter);
        indicator.setViewPager(viewPager);

         profileImage = (ImageView) findViewById(R.id.profile_image);
         name = (TextView) findViewById(R.id.profile_name);
         department=(TextView) findViewById(R.id.profile_department_name);
         summary=(TextView) findViewById(R.id.profile_summary);
         member_since=(TextView) findViewById(R.id.profile_member_since);


    new GetProfile().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    class GetProfile extends AsyncTask<String, String, String> {
        UserProfile profile = null;
        boolean succes = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (succes) {
                department.setText(profile.getDepartment());
                member_since.setText(new SimpleDateFormat("dd MMM yyyy").format(profile.getMember_sinceDate()));
                summary.setText(profile.getSummary());
                name.setText(profile.getName());
                String profile_image_url = getApplicationContext().getResources().getString(R.string.profile_images) + profile.getUser_id() + ".png";
                Picasso.with(getApplicationContext()).load(profile_image_url).placeholder(R.drawable.profile_image_place_holder).into(profileImage);

            }


        }

        @Override
        protected String doInBackground(String... strings) {
            String url = getResources().getString(R.string.retrieve_profile) + user_id;
            String response = null;
            try {

                    response = Server.performServerCall(url, "GET");

                profile = new UserProfile(new JSONObject(response));
                succes = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }
    }



}
