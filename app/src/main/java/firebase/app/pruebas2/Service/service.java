package firebase.app.pruebas2.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import firebase.app.pruebas2.Actividades.MenuActivity;
import firebase.app.pruebas2.R;

public class service extends FirebaseMessagingService {
    String TAG = "Mensaje";
    String tok = FirebaseInstanceId.getInstance().getToken();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTitle() +"/ "+remoteMessage.getNotification().getBody());

            mostrarnotificacion(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void mostrarnotificacion(String body, String title){

        Intent intent = new Intent(service.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,  pendingIntent.getCreatorPackage())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        //If you want to send messages to this application instance or
        //manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       //sendRegistrationToServer(token);
    }
}
