package in.ac.iiitkota.cairn.csr.android.helper;

import android.content.Context;
import android.content.Intent;

/**
 * Created by SS Verma on 03-04-2017.
 */

public class ContentShareHelper {

    public static void shareContent(Context context, String contentText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contentText);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

}
