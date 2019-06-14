package firebase.app.pruebas2.Service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FireBaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        String TAG = "Miaun: ";
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
}
