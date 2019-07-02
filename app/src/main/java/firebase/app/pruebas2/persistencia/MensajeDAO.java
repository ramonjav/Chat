package firebase.app.pruebas2.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebase.app.pruebas2.Entidades.firebase.logica.Mensaje;
import firebase.app.pruebas2.Utilidades.Constantes;

public class MensajeDAO {

    private static MensajeDAO mensajeDAO;

    private FirebaseDatabase database;
    private DatabaseReference referenciaMesnaje;

    public static MensajeDAO getInstancia(){
        if(mensajeDAO == null) mensajeDAO = new MensajeDAO();
        return mensajeDAO;
    }

    private MensajeDAO(){
        database = FirebaseDatabase.getInstance();
        referenciaMesnaje = database.getReference(Constantes.NODO_MENSAJES);
    }

    public void  NewMenssage(String key_Emisor, String key_Receptor, Mensaje mensaje){
        DatabaseReference reference_Emisor = referenciaMesnaje.child(key_Emisor).child(key_Receptor);
        DatabaseReference reference_Receptor = referenciaMesnaje.child(key_Receptor).child(key_Emisor);
        reference_Emisor.push().setValue(mensaje);
        reference_Receptor.push().setValue(mensaje);

    }
}
