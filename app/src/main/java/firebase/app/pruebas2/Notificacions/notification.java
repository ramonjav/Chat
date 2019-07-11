package firebase.app.pruebas2.Notificacions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import firebase.app.pruebas2.Utilidades.Constantes;
import firebase.app.pruebas2.Utilidades.Contantes_Notification;
import firebase.app.pruebas2.Volleys.Volleys;


public class notification {

    static public void sendNotification(final Context context, String body, String title, String key, String token){
        RequestQueue colaDePeticiones;
        Volleys volleys;

        volleys = Volleys.getInstance(context);
        colaDePeticiones = volleys.getRequestQueue();

//////////////////////Map de notificacion//////////////////////
        Map<String, String> notification = new HashMap<>();
        notification.put(Contantes_Notification.BODY, body);
        notification.put(Contantes_Notification.TITLE, title);
/////////////////////Map de data//////////////////////////////
        Map<String, String> data = new HashMap<>();
        data.put(Contantes_Notification.KEYUSER, key);
        data.put(Constantes.TOKEN, token);
//////////////////////Map principal/////////////////////////////
        Map<String, Map> parametros = new HashMap<>();
        parametros.put(Contantes_Notification.NOTIFICATION, notification);
        parametros.put(Contantes_Notification.DATA, data);

        JSONObject jsonObject = new JSONObject(parametros);

        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.POST,
                "https://apcpruebas.es/david/notificaciones/enviarNoti.php",
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // array disponible
                        Log.i("Respuesta", "Respuesta del servidor correcta");
                        try {
                            Toast.makeText(context, "Notificación enviada correctamente", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("datos", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        colaDePeticiones.add(peticion);
    }
}
