package in.ac.iiitkota.cairn.csr.android.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.face_detection.FaceOverlayView;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddAttendance extends AppCompatActivity implements LocationListener{

    private static final int CAMERA_REQUEST = 1;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private Bitmap capturedPhoto;
    private ImageView captureImage;
    private Button submit;
    private ProgressBar progressBar,locationProgressBar;
    private Uri selectedImage;
    String realPath = null;

    private boolean isImageCaptured;
    private FaceOverlayView mFaceOverlayView;
    TextView face_detection_text;
    private int head_count=-1;
    private LocationManager mLocationManager;
    private boolean located;
    private GoogleMap googleMap;
    private Marker marker;
    LatLng attendance_location =null;

    private Long nandgram_id;
    final int distance=100;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        submit=(Button)findViewById(R.id.submit_attendance);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        locationProgressBar=(ProgressBar)findViewById(R.id.locationProgressBar);
        progressBar.setVisibility(View.GONE);
        locationProgressBar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nandgram_id=getIntent().getLongExtra("nandgram_id",-1);
        captureImage = (ImageView) findViewById(R.id.capture_image);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isImageCaptured) {
                    startCameraActivity();
                } else {
                    captureImage.setScaleType(ImageView.ScaleType.CENTER);
                    captureImage.setImageResource(R.drawable.ic_menu_camera);
                    isImageCaptured = false;
                }

            }
        });
        TextView summary=(TextView) findViewById(R.id.summary);
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        summary.setText("5 faces Detected\nPhotograph taken at "+sdf.format(new Date())+"\n At Barmer,Rajasthan");

        mFaceOverlayView = (FaceOverlayView) findViewById( R.id.face_overlay );
        face_detection_text=(TextView)findViewById(R.id.face_detection_text);
        face_detection_text.setVisibility(View.GONE);
        mFaceOverlayView.setVisibility(View.GONE);
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView));
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {

                googleMap = gMap;
                getUserLocation();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, File> file_params = new HashMap<>();
                if (realPath != null && !realPath.trim().equals(""))
                    file_params.put("img", new File(realPath));

                HashMap<String, String> form_params = new HashMap<>();


                form_params.put("nandgram_id",String.valueOf(nandgram_id) );
                form_params.put("user_id",String.valueOf(UserData.getInstance(getApplicationContext()).getUser_id()));
                if(realPath==null||realPath.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please click a picture!",Toast.LENGTH_LONG).show();
                }
                else if(head_count<=0){
                    Toast.makeText(getApplicationContext(),"No faces detected, Please try again!",Toast.LENGTH_LONG).show();
                    return;
                }
                else
                form_params.put("head_count",String.valueOf(head_count));
                if(attendance_location !=null){
                    form_params.put("longitude", String.valueOf(attendance_location.longitude));
                    form_params.put("latitude", String.valueOf(attendance_location.latitude));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Still fetching location! Please wait!",Toast.LENGTH_LONG).show();
                    return;
                }
                new InsertAttendance(file_params,form_params).execute();
            }
        });

    }


    private void startCameraActivity() {

        if (!isStoragePermissionGranted()) {
            Toast.makeText(getApplicationContext(), "Please grant the required permissions", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Attendance Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");

        selectedImage = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);




        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //photo = (Bitmap) data.getExtras().get("data");
            try {

                capturedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);

                head_count=mFaceOverlayView.setBitmap(capturedPhoto);

                captureImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                captureImage.setImageBitmap(capturedPhoto);



              realPath=getRealPathFromURI(getApplicationContext(),selectedImage);

                if(head_count==0){
                    face_detection_text.setText("No faces detected");
                }
                else if(head_count==1){
                    face_detection_text.setText("1 face detected");
                }
                else{
                    face_detection_text.setText(head_count+" faces detected");
                }

                face_detection_text.setVisibility(View.VISIBLE);
                mFaceOverlayView.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),head_count+" faces detected",Toast.LENGTH_LONG).show();

                isImageCaptured = true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to capture image!", Toast.LENGTH_SHORT).show();
            }
        }
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
    private void getUserLocation() {





        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setMessage("Permission requrired to access your attendance_location.Kindly enable GPS.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddAttendance.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
                return;
            }
            else{
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }

        }
        else{
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = mLocationManager.getBestProvider(criteria, false);



            if(provider!=null&&!provider.equals("null"))   mLocationManager.requestLocationUpdates(provider, 0, 0, this);

        }






    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // attendance_location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        String provider = mLocationManager.getBestProvider(criteria, false);



                        if(provider!=null&&!provider.equals("null"))   mLocationManager.requestLocationUpdates(provider, 0, 0, this);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void setMarker(double latitude, double longitude,String title){
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_icon);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b,40, 40, false);

        marker= googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(smallMarker)));
    }
    private void createGeofence(double latitude, double longitude, int radius,
                                String geofenceType, String title,boolean animate) {
        googleMap.clear();


        googleMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude)).radius(radius)
                .strokeColor(R.color.colorAccent).strokeWidth(7));
        if(!animate)    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                latitude, longitude), 15));
        else googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                latitude, longitude), 15));
        located=true;

    }

    @Override
    public void onLocationChanged(Location location) {

        createGeofence(location.getLatitude(), location.getLongitude(), distance, "CIRCLE", "Nandgram", false);
        setMarker(location.getLatitude(),location.getLongitude(),"Your Location");
        attendance_location=new LatLng(location.getLatitude(),location.getLongitude());
        locationProgressBar.setVisibility(View.GONE);

        located = true;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getApplicationContext(),"Please Enable GPS!",Toast.LENGTH_LONG).show();
    }

    class InsertAttendance extends AsyncTask<String, String, String> {
        boolean success = false;
        HashMap<String, File> file_params;
        HashMap<String, String> form_params;

        InsertAttendance(HashMap<String, File> file_params, HashMap<String, String> form_params) {
            this.file_params = file_params;
            this.form_params = form_params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            submit.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (success) {
                Toast.makeText(getApplicationContext(), "Attendance updated", Toast.LENGTH_LONG).show();
                ;
                submit.setEnabled(false);
               finish();
            } else {
                submit.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Sorry there was an error!", Toast.LENGTH_LONG).show();
                ;
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String response = null;
            try {
                // new MultipartUtility().performMultipartServerCall(getResources().getString(R.string.add_post),form_params,file_params);
                Server.multipartRequest(getResources().getString(R.string.insert_attendance), form_params, file_params, "img", "image/jpg");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
 if(mLocationManager!=null)       mLocationManager.removeUpdates(this);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

}
