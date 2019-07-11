package firebase.app.pruebas2.Actividades;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private static String fichero = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Chat/Images/";
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

        final File file = new File(fichero);

        if(!file.exists()){
            file.mkdirs();
        }

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isvalided++;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = "Image_" + timeStamp + ".jpg";
                subirfoto(isvalided, fileName, "/Chat/Images/");
                finish();
               saveImage(((BitmapDrawable)imageView.getDrawable()).getBitmap(), fileName);
               // storage.getReference().child("imagenes_chat").child("40").delete();
            }
        });
    }

    public void subirfoto(int c, final String name, final String fichero){
        if(c==1){
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(name);
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
                    EnviarMensaje(url, name, fichero);
                    finish();
                }
            });
        }
    }

    public void EnviarMensaje(String url, String name, String fichero){
        if(!url.isEmpty()){
            Mensaje mensaje = new Mensaje();
            mensaje.setMensaje(text.getText().toString());
            mensaje.setUrlFoto(url);
            mensaje.setConFoto(true);
            mensaje.setUbicacion(fichero+name);
            mensaje.setNameFoto(name);
            mensaje.setKeyEmisor(UserDAO.getInstancia().getkeyUser());
            MensajeDAO.getInstancia().NewMenssage(UserDAO.getInstancia().getkeyUser(),KEY_RECEPTOR,mensaje);

            text.setText("");
        }
    }

    public void saveImage(Bitmap bitmap, String fileName){

        File file = new File(fichero, fileName);
        if(!file.exists()){
            Log.d("pathImage", file.toString());
            FileOutputStream fos = null;
            try{
               //file.mkdirs();
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                Log.d("pathImage", file.toString());
            }catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
        Toast.makeText(FotoActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
    }
}
