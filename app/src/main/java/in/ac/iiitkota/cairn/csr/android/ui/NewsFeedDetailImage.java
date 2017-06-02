package in.ac.iiitkota.cairn.csr.android.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.AppSingleton;
import in.ac.iiitkota.cairn.csr.android.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class NewsFeedDetailImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_news_feed_detail_image);
        String image=getIntent().getStringExtra("image");
        String caption=getIntent().getStringExtra("caption");
        final ImageView image_view=(ImageView) findViewById(R.id.newsfeed_detail_image);
        TextView caption_view=(TextView)findViewById(R.id.newsfeed_detail_caption);
        caption_view.setText(caption);
        String newsfeed_image_url = AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.images) + image;
        Picasso.with(getApplicationContext())
                .load(newsfeed_image_url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        if (bitmap == null) {
                            return;
                        }

                        image_view.setImageBitmap(bitmap);

                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch swatch = palette.getDominantSwatch();
                                        if (swatch == null) {
                                            //Toast.makeText(context, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        image_view.setBackgroundColor(swatch.getRgb());
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
    }
}
