package in.ac.iiitkota.cairn.csr.android.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.ShareWithAdapter;
import in.ac.iiitkota.cairn.csr.android.interfaces.BottomSheetClickListener;
import in.ac.iiitkota.cairn.csr.android.interfaces.CheckboxCheckChangeListener;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment;
import in.ac.iiitkota.cairn.csr.android.utilities.PathUti;
import in.ac.iiitkota.cairn.csr.android.utilities.RealPathUtil;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NewMilestoneActivity extends AppCompatActivity implements CheckboxCheckChangeListener, BottomSheetClickListener {


    private static final int PICK_IMAGE = 2;
    private MultiAutoCompleteTextView milestoneAutoCompleteTextView;


    private Button submit;
    boolean edit = false;
    boolean hasPublic;
    boolean image_selected = false;
    boolean update_image = false;
    Long edit_post_id = 0l;
    Long edit_post_lives = 0l;
    ProgressBar progressBar;
    RecyclerView recyclerView;


    ImageView milestone_image;
    private HoloCircleSeekBar picker;
    String realPath = null;

    private static final int CAMERA_REQUEST = 1;
    private Uri cameraImageUri;
    private boolean isImageCaptured;

    private ModalBottomSheetFragment bottomSheetFragment;
    File capture_image_file;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_milestone);
        edit = getIntent().getBooleanExtra("edit", false);
        hasPublic = UserData.getInstance(getApplicationContext()).getUser().getAccount_level() > 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        milestoneAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multi_auto_complete_text_view);
        milestoneAutoCompleteTextView.setTokenizer(new SpaceTokenizer());
        picker = (HoloCircleSeekBar) findViewById(R.id.picker);
        submit = (Button) findViewById(R.id.submit_milestone_button);
        progressBar = (ProgressBar) findViewById(R.id.milestone_progressbar);
        progressBar.setVisibility(View.GONE);

        setUpShareOptions();


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, prepareSuggestions());

        //adapter = new AutoCompleteMilestoneAdapter(this, listMilestones);
        milestoneAutoCompleteTextView.setThreshold(1);
        milestoneAutoCompleteTextView.setAdapter(arrayAdapter);

        //prepareListMilestones();
        milestoneAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                Toast.makeText(NewMilestoneActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });


        milestone_image = (ImageView) findViewById(R.id.milestone_image);
        milestone_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!image_selected) {
//                    selectImageFromGallery();
                    bottomSheetFragment = ModalBottomSheetFragment.newInstance("Modal Bottom Sheet");
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

                    bottomSheetFragment.setBottomSheetClickListener(NewMilestoneActivity.this);

                } else {
                    milestone_image.getLayoutParams().height = 80;
                    milestone_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_rectangle_shape));
                    milestone_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                    milestone_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    realPath = null;
                    image_selected = false;
                    update_image = true;
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, File> file_params = new HashMap<>();
                if (realPath != null && !realPath.trim().equals(""))
                    file_params.put("img", new File(realPath));

                HashMap<String, String> form_params = new HashMap<>();
                if (milestoneAutoCompleteTextView.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.type_desc, Toast.LENGTH_LONG).show();

                    return;
                }
                String myparam = "";


                    myparam = convertToUTF8(milestoneAutoCompleteTextView.getText().toString());



                form_params.put("description", myparam);
                form_params.put("author", String.valueOf(UserData.getInstance(getApplicationContext()).getUser_id()));
                form_params.put("head_count", String.valueOf(picker.getValue()));


                if (!edit) {
                    ArrayList<Long> share_teams = new ArrayList<>();
                    String team_ids = "";
                    share_teams.clear();
                    if (!edit) {
                        for (int i = 0; i < recyclerView.getLayoutManager().getChildCount(); i++) {

                            CheckBox checkbox = (CheckBox) recyclerView.getLayoutManager().getChildAt(i).findViewById(R.id.checkbox_team);
                            if (checkbox.isChecked()) {
                                if (hasPublic) {
                                    if (i == 0) {
                                        share_teams.add(0l);
                                    } else
                                        share_teams.add(NewsFeedFragment.teams.get(i - 1).getTeam_id());
                                } else {
                                    share_teams.add(NewsFeedFragment.teams.get(i).getTeam_id());
                                }


                            }
                        }
                        if(share_teams.size()==0){
                            Toast.makeText(getApplicationContext(), R.string.select_team_share, Toast.LENGTH_LONG).show();
                        return;
                        }
                        for (int i = 0; i < share_teams.size(); i++) {
                            team_ids += share_teams.get(i);
                            if (i < share_teams.size() - 1)
                                team_ids += ",";
                        }
                        form_params.put("teams", team_ids);
                    }
                    new AddMilestone(file_params, form_params).execute();
                } else {

                    form_params.put("post_id", String.valueOf(edit_post_id));
                    form_params.put("update_image", String.valueOf(update_image ? 1 : 0));
                    new UpdateMilestone(file_params, form_params).execute();
                }
            }
        });
        if (!edit) getSupportActionBar().setTitle(R.string.milestone_add);
        else {
            getSupportActionBar().setTitle(R.string.milestone_edit);
            findViewById(R.id.sharing_card).setVisibility(View.GONE);
            milestoneAutoCompleteTextView.setText(getIntent().getStringExtra("edit_title"));
            edit_post_lives = getIntent().getLongExtra("edit_lives", 0L);
            picker.setInitPosition(edit_post_lives.intValue());
            CardView sharing_card = (CardView) findViewById(R.id.sharing_card);
            sharing_card.setVisibility(View.GONE);


            edit_post_id = getIntent().getLongExtra("edit_post_id", 0l);

            if (edit && getIntent().getStringExtra("edit_image") != null && !getIntent().getStringExtra("edit_image").isEmpty()) {
                String newsfeed_image_url = getApplicationContext().getResources().getString(R.string.images) + getIntent().getStringExtra("edit_image");

                Picasso.
                        with(getApplicationContext()).
                        load(newsfeed_image_url).
                        into(milestone_image);
                image_selected = true;

            }


        }
        picker.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            int limit = 50;

            int value = 0;

            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) {
                value = i;
            }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {

            }


            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                if (value > limit * 0.7 && value < 3000) {
                    limit = limit * 2;
                    picker.setMax(limit);
                    picker.setValue(value);

                }
                if (value <= limit * 0.4 && limit > 50) {

                    limit /= 2;
                    picker.setMax(limit);
                    picker.refreshDrawableState();
                    picker.setValue(value);

                }
            }
        });
    }

    private void setUpShareOptions() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_share_with);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ShareWithAdapter adapter = new ShareWithAdapter(NewsFeedFragment.prepareTeamList(), hasPublic);
        recyclerView.setAdapter(adapter);

        adapter.setCheckboxCheckChangeListener(this);

    }

    public void selectImageFromGallery() {
        if (!isStoragePermissionGranted()) {
            Toast.makeText(getApplicationContext(), R.string.grant_permissions, Toast.LENGTH_LONG).show();
            return;
        } else {


            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), PICK_IMAGE);
        }
    }

    private void startCameraActivity() {
        if (!isStoragePermissionGranted()) {
            Toast.makeText(getApplicationContext(),R.string.grant_permissions, Toast.LENGTH_LONG).show();
            return;
        }


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String filename="img_"+String.valueOf(System.currentTimeMillis());
        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        capture_image_file = new File(dir,   filename  + ".jpg");
        cameraImageUri = Uri.fromFile(capture_image_file);
//
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, filename);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(intent, CAMERA_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, options);
                options.inSampleSize = calculateInSampleSize(options, 100, 100);
                options.inJustDecodeBounds = false;
                Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, options);
                milestone_image.setImageBitmap(image);
                milestone_image.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                milestone_image.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;

                milestone_image.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
   //     realPath=getRealPathFromUri(selectedImage);



            try {
            realPath= PathUti.getPath(this,selectedImage);
//                if (Build.VERSION.SDK_INT < 19)
//                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, selectedImage);
//                    // SDK > 19 (Android 4.4)
//                else
//                    realPath = RealPathUtil.getRealPathFromURI_API19(this, selectedImage);
//            } catch (Exception e) {
//                e.printStackTrace();
//              try{
//                  realPath = selectedImage.getPath();
              }
              catch (Exception e1){
                  e1.printStackTrace();
                  Toast.makeText(this, R.string.image_capture_fail, Toast.LENGTH_SHORT).show();
              }
//            }
            image_selected = true;
            update_image = true;

        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //photo = (Bitmap) data.getExtras().get("data");
            try {

                Bitmap bm = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), cameraImageUri);

//                FileInputStream fis = null;
//
//                fis = new FileInputStream(cameraImageUri.toString());


//                Bitmap bm = BitmapFactory.decodeStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

//            cursor.close();
                image_selected = true;
                update_image = true;

                milestone_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                milestone_image.setImageBitmap(bm);


                realPath = capture_image_file.getAbsolutePath();

                isImageCaptured = true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.image_capture_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public Bitmap decodeFile(String filePath) {
//
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, o);
//
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 1024;
//
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
//                break;
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
//
//        return bitmap;
//    }

    private String[] prepareSuggestions() {
        return new String[]{
                getResources().getString(R.string.water_supplied),
                getResources().getString(R.string.purifier_setup),
                getResources().getString(R.string.milkplant_setup),
                getResources().getString(R.string.public_toilet_setup),
                getResources().getString(R.string.mobile_health_van),
                getResources().getString(R.string.fire_awareness),
                getResources().getString(R.string.skill),
                getResources().getString(R.string.nat_res_management),
                getResources().getString(R.string.jeevan_amrit),
                getResources().getString(R.string.nandghar),
                getResources().getString(R.string.health),
                getResources().getString(R.string.jan_sanvaad),
                getResources().getString(R.string.education),
                getResources().getString(R.string.milk)

        };
    }

    @Override
    public void onTeamCheckChange(CompoundButton compoundView, boolean isChecked, int position) {
        //handle team stats
    }

    @Override
    public void onBottomSheetItemClick(View view, int position) {

        bottomSheetFragment.dismiss();

        if (position == 0) {
            //camera
            if (realPath == null) {
                startCameraActivity();
            } else {
                //milestone_image.getLayoutParams().height = 80;
                milestone_image.setImageResource(R.drawable.ic_add_black_24dp);
                milestone_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                realPath = null;
            }
        } else {
            //gallery
            if (realPath == null) {
                selectImageFromGallery();
            } else {
                //milestone_image.getLayoutParams().height = 80;
                milestone_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_rectangle_shape));
                milestone_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
                milestone_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                realPath = null;
            }
        }
    }


    class AddMilestone extends AsyncTask<String, String, String> {
        boolean success = false;
        HashMap<String, File> file_params;
        HashMap<String, String> form_params;


        AddMilestone(HashMap<String, File> file_params, HashMap<String, String> form_params) {
            this.file_params = file_params;
            this.form_params = form_params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressBar.setVisibility(View.VISIBLE);
            submit.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (success) {
                Toast.makeText(getApplicationContext(), "Milestone updated", Toast.LENGTH_LONG).show();

                submit.setEnabled(false);
                finish();
            } else {
                submit.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Sorry there was an error!", Toast.LENGTH_LONG).show();

            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String response = null;
            try {
                // new MultipartUtility().performMultipartServerCall(getResources().getString(R.string.add_post),form_params,file_params);
                response = Server.multipartRequest(getResources().getString(R.string.add_post), form_params, file_params, "img", "image/jpg");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    //    public String getRealPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//
//        //This method was deprecated in API level 11
//        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//
//        CursorLoader cursorLoader = new CursorLoader(
//                this,
//                contentUri, proj, null, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//
//        int column_index =
//                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //resume tasks needing this permission
            selectImageFromGallery();
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

    class UpdateMilestone extends AsyncTask<String, String, String> {
        boolean success = false;
        HashMap<String, File> file_params;
        HashMap<String, String> form_params;

        UpdateMilestone(HashMap<String, File> file_params, HashMap<String, String> form_params) {
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
                Toast.makeText(getApplicationContext(), "Milestone updated", Toast.LENGTH_LONG).show();
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
                Server.multipartRequest(getResources().getString(R.string.update_post), form_params, file_params, "img", "image/jpg");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        public CharSequence terminateToken(CharSequence text) {


            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }
    public static String convertFromUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    /* convert from internal Java String Format -> UTF-8 encoded HTML/JSP-Pages  */
    public static String convertToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = this.getContentResolver().query(tempUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private Uri getImageUri( Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}
