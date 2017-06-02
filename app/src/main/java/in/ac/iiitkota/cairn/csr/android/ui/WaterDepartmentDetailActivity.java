package in.ac.iiitkota.cairn.csr.android.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import in.ac.iiitkota.cairn.csr.android.R;

/**
 * Created by Sahil on 2/6/17.
 */

public class WaterDepartmentDetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "position";
    ImageView location,project,web;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_dept_details);

        location = (ImageView)findViewById(R.id.locations);
        project = (ImageView)findViewById(R.id.cumm_proj);
        web = (ImageView)findViewById(R.id.web);


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WaterDepartmentDetailActivity.this,"You selected locations",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WaterDepartmentDetailActivity.this,DepartmentLocationList.class);
                startActivity(intent);
            }
        });


        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WaterDepartmentDetailActivity.this,"You selected Cummulative Projects",Toast.LENGTH_SHORT).show();

            }
        });


        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WaterDepartmentDetailActivity.this,"You selected Web Portal",Toast.LENGTH_SHORT).show();
                Intent internetIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://piramalwater.net/index.php#"));
                internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
                internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(internetIntent);
            }
        });
    }
}
