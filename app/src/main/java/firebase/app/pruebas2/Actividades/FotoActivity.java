package firebase.app.pruebas2.Actividades;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import firebase.app.pruebas2.Entidades.firebase.logica.Mensaje;
import firebase.app.pruebas2.R;
import firebase.app.pruebas2.Utilidades.Constantes;
import firebase.app.pruebas2.persistencia.MensajeDAO;
import firebase.app.pruebas2.persistencia.UserDAO;

public class FotoActivity extends AppCompatActivity {

    ImageView imageView;
    EditText text;
    Button btnEnviar;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    Uri uri;

    int isvalided = 0;

    String KEY_RECEPTOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        imageView = findViewById(R.id.imageView2);
        btnEnviar = findViewById(R.id.btnEnviar);
        text = findViewById(R.id.txtMensaje);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            uri = Uri.parse(bundle.getString(Constantes.URI));
            imageView.setImageURI(uri);
            KEY_RECEPTOR = bundle.getString(Constantes.KEY);
        }else{finish();}

        storage = FirebaseStorage.getInstance();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isvalided++;
                subirfoto(isvalided);
                finish();
            }
        });
    }

    public void subirfoto(int c){
        if(c==1){
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(uri.getLastPathSegment());
            fotoReferencia.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    String url = task.getResult().toString();
                    EnviarMensaje(url);
                    finish();
                }
            });
        }
    }

    public void EnviarMensaje(String url){
        if(!url.isEmpty()){
            Mensaje mensaje = new Mensaje();
            mensaje.setMensaje(text.getText().toString());
            mensaje.setUrlFoto(url);
            mensaje.setConFoto(true);
            mensaje.setKeyEmisor(UserDAO.getInstancia().getkeyUser());
            MensajeDAO.getInstancia().NewMenssage(UserDAO.getInstancia().getkeyUser(),KEY_RECEPTOR,mensaje);

            text.setText("");
        }
    }
}
