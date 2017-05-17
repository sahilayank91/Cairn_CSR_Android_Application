package in.ac.iiitkota.cairn.csr.android.ui.fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.iiitkota.cairn.csr.android.adapter.StatisticsSectionPagerAdapter;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.model.UserProfile;
import in.ac.iiitkota.cairn.csr.android.utilities.PathUti;
import in.ac.iiitkota.cairn.csr.android.utilities.RealPathUtil;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

import static android.app.Activity.RESULT_OK;
import static in.ac.iiitkota.cairn.csr.android.R.id.milestone_image;
import static in.ac.iiitkota.cairn.csr.android.ui.NewMilestoneActivity.calculateInSampleSize;

import in.ac.iiitkota.cairn.csr.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    View view;
    TextView department, member_since, summary, name;
    ImageView profileImage;
    private static final int PICK_IMAGE = 2;
    String realPath=null;


    private ImageView profileEditIcon;
    private SmilesFragment week_fragment, month_fragment, year_fragment;
    private File gallery_image_file;
    private Uri galleryImageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) view = inflater.inflate(R.layout.fragment_profile, container, false);
        else return view;


        department = (TextView) view.findViewById(R.id.profile_department_name);
        member_since = (TextView) view.findViewById(R.id.profile_member_since);
        summary = (TextView) view.findViewById(R.id.profile_summary);
        name = (TextView) view.findViewById(R.id.profile_name);
        profileImage = (ImageView) view.findViewById(R.id.profile_image);

        profileEditIcon = (ImageView) view.findViewById(R.id.profile_edit_icon);

        profileEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_edit_profile);
                //dialog.setCancelable(false);

                final EditText etDepartment = (EditText) dialog.findViewById(R.id.et_department);
                final EditText etSummary = (EditText) dialog.findViewById(R.id.et_summary);

                Button btnUpdate = (Button) dialog.findViewById(R.id.btn_update);
                Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newDepartment = etDepartment.getText().toString();
                        String newSummary = etSummary.getText().toString();
                        //
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });
        week_fragment =new SmilesFragment();
      month_fragment =new SmilesFragment();
        year_fragment =new SmilesFragment();
        StatisticsSectionPagerAdapter sectionsPagerAdapter = new StatisticsSectionPagerAdapter(getActivity().getFragmentManager(), "user", UserData.getInstance(getContext()).getUser_id());

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager_profile);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewPager.setAdapter(sectionsPagerAdapter);
        indicator.setViewPager(viewPager);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFromGallery();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetProfile().execute();
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
                String profile_image_url = getContext().getResources().getString(R.string.profile_images) + profile.getUser_id() + ".png";
                Picasso.with(getContext()).load(profile_image_url).placeholder(R.drawable.profile_image_place_holder).into(profileImage);

            }


        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            if(isAdded()){
                String url = getResources().getString(R.string.retrieve_profile) + UserData.getInstance(getContext()).getUser_id();

                try {
                    if(isAdded())
                        response = Server.performServerCall(url, "GET");
                    else cancel(true);
                    profile = new UserProfile(new JSONObject(response));
                    succes = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }
    }
    public void selectImageFromGallery() {
        if(!isStoragePermissionGranted()){
            Toast.makeText(getContext(),"Please grant the required permissions",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT,galleryImageUri);
            intent.setType("image/*");
            String filename="img_"+String.valueOf(System.currentTimeMillis());
            File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
              gallery_image_file = new File(dir, filename + ".jpg");
            galleryImageUri=Uri.fromFile(gallery_image_file);



            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage), null, options);
                options.inSampleSize = calculateInSampleSize(options, 100, 100);
                options.inJustDecodeBounds = false;
                Bitmap image = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectedImage), null, options);
               profileImage.setImageBitmap(image);




                realPath= PathUti.getPath(getContext(),selectedImage);
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
                Toast.makeText(getContext(), "Unable to capture image!", Toast.LENGTH_SHORT).show();
            }

        new UpdateProfilePic().execute();



        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

            //resume tasks needing this permission
            selectImageFromGallery();
        }
    }
    class UpdateProfilePic extends AsyncTask<String,String,String>{
        boolean success=false;
        HashMap<String,File> file_params=new HashMap<>();
        HashMap<String,String> form_params=new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            form_params.put("user_id",String.valueOf(UserData.getInstance(getContext()).getUser_id()));
            if(realPath==null)cancel(true);
            file_params.put("img",new File(realPath));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(success){
                Toast.makeText(getContext(),"Profile Picture updated",Toast.LENGTH_LONG).show();;

            }
            else
            {
                Toast.makeText(getContext(),"Sorry there was an error!",Toast.LENGTH_LONG).show();;
            }
        }

        @Override
        protected String doInBackground(String...strings) {

            String response= null;
            try {
                // new MultipartUtility().performMultipartServerCall(getResources().getString(R.string.add_post),form_params,file_params);
                Server.multipartRequest(getResources().getString(R.string.update_profile_picture),form_params,file_params,"img","image/jpg");
                success=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
