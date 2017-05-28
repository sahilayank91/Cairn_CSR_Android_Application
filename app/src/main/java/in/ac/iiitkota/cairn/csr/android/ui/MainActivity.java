package in.ac.iiitkota.cairn.csr.android.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.service.textservice.SpellCheckerService;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Locale;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.SharedPreferenceSingleton;

import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.FeedbackFragment;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.MessagesThreadFragment;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.MyActivityFragment;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    FragmentManager fragmentManager;

    private BottomNavigationView bottomNavigationView;
    private NewsFeedFragment newsFeedFragment;
    private MyActivityFragment myActivityFragment;
    private MessagesThreadFragment messagesThreadFragment;
    private ProfileFragment profileFragment;
    private FeedbackFragment feedbackFragment;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String selectedLanguage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView nav_user_name= (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_user_name.setText(UserData.getInstance(getApplicationContext()).getUser().getName());
        TextView nav_user_email= (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_user_email.setText(UserData.getInstance(getApplicationContext()).getUser().getEmail());
        setUpBottomBar();
        fragmentManager = getSupportFragmentManager();

        //Getting configuration file for the application
        Configuration config = getBaseContext().getResources().getConfiguration();


        Log.w("shared pref lang",PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG",""));
        String lang = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG","");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            setLangRecreate(lang);
        }

        if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("LANG","")==null){
            updateLanguage();
        }
        if (newsFeedFragment == null) newsFeedFragment = new NewsFeedFragment();
        replaceFragment(newsFeedFragment);

    }

    private void setUpBottomBar() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_newsfeed:
                        if (newsFeedFragment == null)
                            newsFeedFragment = new NewsFeedFragment();
                        replaceFragment(newsFeedFragment);
                        break;
                    case R.id.action_my_activity:
                        if (myActivityFragment == null)
                            myActivityFragment = new MyActivityFragment();
                        replaceFragment(myActivityFragment);
                        break;
                    case R.id.action_messages:
                        if (messagesThreadFragment == null)
                            messagesThreadFragment = new MessagesThreadFragment();
                        replaceFragment(messagesThreadFragment);
                        break;
                    case R.id.action_profile:
                        if (profileFragment == null)
                            profileFragment = new ProfileFragment();
                        replaceFragment(profileFragment);
                        break;
                    case R.id.action_feedback:
                        if (feedbackFragment == null)
                            feedbackFragment = new FeedbackFragment();
                        replaceFragment(feedbackFragment);
                        break;
                }

                return true;
            }
        });
    }

    private void addFragment(Fragment fragmentToAdd) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_container, fragmentToAdd)
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_milestone) {
            startActivity(new Intent(this, NewMilestoneActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_share) {
            //
        } else if (id == R.id.nav_logout) {
            getApplicationContext().getSharedPreferences(SharedPreferenceSingleton.SETTINGS_NAME, MODE_PRIVATE).edit().clear().commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_change_password) {
            startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
        } else if (id == R.id.nav_nandgram_list) {
            startActivity(new Intent(MainActivity.this, NandGharListActivity.class));
        }else if(id==R.id.nav_change_lang){
            //ChangeLanguage Activity Here
            updateLanguage();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateLanguage(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the alert dialog title
        builder.setTitle("Choose your Language");
        // Initializing an array of Languages
        final String[] language = new String[]{
                "English",
                "हिन्दी"
        };

        builder.setSingleChoiceItems(
                language, // Items list
                -1, // Index of checked item (-1 = no selection)
                new DialogInterface.OnClickListener() // Item click listener
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the alert dialog selected item's text
                        String selectedItem = Arrays.asList(language).get(i);
                        // Display the selected item's text on snack bar

                        if(selectedItem=="English"){
                            selectedLanguage="en";
                        }else if(selectedItem=="हिन्दी"){
                            selectedLanguage="hi";
                        }

                        if(selectedLanguage.equalsIgnoreCase("en")){

                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();

                        }else if(selectedLanguage.equalsIgnoreCase("hi")){

                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").commit();

                        }
                    }
                });

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just dismiss the alert dialog after selection
                // Or do something now
                setLangRecreate(selectedLanguage);
            }
        });


        // Create the alert dialog
        AlertDialog dialog = builder.create();

        // Finally, display the alert dialog
        dialog.show();



    }


    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }


    private void addFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void replaceFragment(Fragment new_fragment, String tag) {

        if (isTagInBackStack(tag)) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragmentManager.findFragmentByTag(tag));
            transaction.commit();
        } else {
            addFragment(new_fragment, tag);
        }

    }

    public boolean isTagInBackStack(String tag) {
        int x;
        boolean toReturn = false;
        int backStackCount = fragmentManager.getBackStackEntryCount();
        System.out.println("backstack" + backStackCount);

        for (x = 0; x < backStackCount; x++) {
            if (tag == fragmentManager.getBackStackEntryAt(x).getName()) {
                toReturn = true;
            }
        }

        return toReturn;
    }
}
