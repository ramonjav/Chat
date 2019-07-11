package firebase.app.pruebas2.Actividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import firebase.app.pruebas2.Entidades.firebase.logica.User;
import firebase.app.pruebas2.R;

public class RegistrarActivity extends AppCompatActivity {

    private TextInputEditText nombre, correo, contrasena, contrasenarepetida;
    private Button boton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String token = FirebaseInstanceId.getInstance().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        nombre = findViewById(R.id.txtname);
        correo = findViewById(R.id.txtcorreo);
        contrasena = findViewById(R.id.txtPass);
        contrasenarepetida = findViewById(R.id.txtPassRepat);
        boton = findViewById(R.id.btnAcep);
        mAuth = FirebaseAuth.getInstance();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String corre = correo.getText().toString();
                final String no = nombre.getText().toString();
                if(isValidEmail(corre) && validarcontraseña() && validarnombre(no)){
                    String con = contrasena.getText().toString();

                    mAuth.createUserWithEmailAndPassword(corre, con)
                            .addOnCompleteListener(RegistrarActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(RegistrarActivity.this, "Se registto correctamente", Toast.LENGTH_LONG).show();
                                        User user = new User();
                                        user.setEmal(corre);
                                        user.setNombre(no);
                                        user.setToken(token);
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());

                                        reference.setValue(user);

                                        regresar();

                                    } else {
                                        Toast.makeText(RegistrarActivity.this, "Error al registrar", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public  boolean validarcontraseña(){
        String contra = contrasena.getText().toString();
        String contrarep = contrasenarepetida.getText().toString();
        if(contra.equals(contrarep)){
            if(contra.length() >= 6 && contra.length()<= 16){
                return true;
            }else return false;
        }else return false;
    }

    public boolean validarnombre(String na){
        return !na.isEmpty();
    }

    public void regresar(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}
