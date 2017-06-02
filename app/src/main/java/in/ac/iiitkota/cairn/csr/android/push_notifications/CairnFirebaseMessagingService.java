package in.ac.iiitkota.cairn.csr.android.push_notifications;

/**
 * Created by joey on 11/1/17.
 */


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Belal on 5/27/2016.
 */

public class CairnFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

//

//
//    private void sendNotification(Map<String, String> data) {
//        Intent intent = new Intent(this, LoginActivity.summy_headcount);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        String display_message = "";
//        Notification notification=new Notification();
//        if (data.containsKey("message"))
//            display_message = data.get("message");
//
//
//
//        notification.setText(display_message);
//        if (data.containsKey("type")) {
//            String type = data.get("type");
//            notification.setType(type);
//            if( data.containsKey("time"))notification.setTimestamp(Long.valueOf(data.get("time")));
//            else notification.setTimestamp(new Date().getTime());
//            if( data.containsKey("device_id"))notification.setDevice_id(Long.valueOf(data.get("device_id")));
//            else{
//                notification.setDevice_id(-1l);
//            }
//            switch (type) {
//                case "advertisement": {
//                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_ryoking_logo);
//
//                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                            .setSmallIcon(getNotificationIcon())
//                            .setLargeIcon(largeIcon)
//                            .setContentTitle("Ryoking")
//                            .setContentText(display_message)
//                            .setAutoCancel(true)
//                            .setSound(defaultSoundUri)
//                            .setContentIntent(pendingIntent);
//
//                    NotificationManager notificationManager =
//                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    if(UserData.getInstance(getApplicationContext()).isNotif_promotion())
//                        notificationManager.notify((int)System.currentTimeMillis(), notificationBuilder.build());
//                }
//                break;
//                case "alert": {
//                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_ryoking_logo);
//                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                            .setSmallIcon(getNotificationIcon())
//                            .setLargeIcon(largeIcon)
//                            .setContentTitle("Ryoking Alert!")
//                            .setContentText(display_message)
//                            .setAutoCancel(true)
//                            .setSound(defaultSoundUri)
//                            .setContentIntent(pendingIntent);
//
//                    if( data.containsKey("location_lat"))notification.setLocation_latitude(Double.valueOf(data.get("location_lat")));
//                    if( data.containsKey("location_long"))notification.setLocation_longitude(Double.valueOf(data.get("location_long")));
//                    if( data.containsKey("location_address"))notification.setLocation_address(data.get("location_address"));
//
//
//                    NotificationManager notificationManager =
//                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    if(display_message.contains("left")&& !UserData.getInstance(getApplicationContext()).isNotif_geofence()){
//
//                    }
//                    else if(display_message.contains("entered")&& !UserData.getInstance(getApplicationContext()).isNotif_geofence()){
//
//                    }
//
//                   else if(display_message.contains("speeding")&& !UserData.getInstance(getApplicationContext()).isNotif_overspeed()){
//
//                    }
//
//                   else if(display_message.contains("braking")&& !UserData.getInstance(getApplicationContext()).isNotif_hard_brake()){
//
//                    }
//
//                   else if(display_message.contains("acceleration")&& !UserData.getInstance(getApplicationContext()).isNotif_hard_acc()){
//
//                    }
//
//                    else notificationManager.notify((int)System.currentTimeMillis(), notificationBuilder.build());
//                }
//                break;
//                case "logout": {
//                    getApplicationContext().getSharedPreferences(SharedPreferenceSingleton.SETTINGS_NAME, MODE_PRIVATE).edit().clear().commit();
//                }
//                break;
//                case "cleardb":{
//                    TripDatasource sqlite_trips=new TripDatasource(getApplicationContext());
//                    sqlite_trips.open();
//                    sqlite_trips.deleteAllTripData();
//                    sqlite_trips.close();
//                    NotificationDatasource sqlite_notifs=new NotificationDatasource(getApplicationContext());
//                    sqlite_notifs.open();
//                    sqlite_notifs.deleteAllNotifications();
//                    sqlite_notifs.close();
//                    DayTripDatasource sqlite_summary=new DayTripDatasource(getApplicationContext());
//                    sqlite_summary.open();
//                    sqlite_summary.deleteAllTripData();
//                    sqlite_summary.close();
//                }break;
//                case "update_devices":{
//                   new UpdateDevices().execute();
//                }break;
//
//
//            }
//            NotificationDatasource sqlite=new NotificationDatasource(getApplicationContext());
//            sqlite.open();
//            sqlite.addNotification(notification);
//            sqlite.close();
//        }
//
//
//    }
//    private int getNotificationIcon() {
//        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
//        return useWhiteIcon ? R.drawable.ic_ryo_silhouette : R.drawable.ic_ryoking_notif_logo;
//    }
//    summy_headcount UpdateDevices extends AsyncTask<String,String,String>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                UserData.getInstance(getApplicationContext()).initDeviceData(s,getApplicationContext());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String response="";
//            try {
//              response=  Server.performServerCall(getApplicationContext().getResources().getString(R.string.fetch_devices_url)+UserData.getInstance(getApplicationContext()).getUser_id(),"GET");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//    }
}
