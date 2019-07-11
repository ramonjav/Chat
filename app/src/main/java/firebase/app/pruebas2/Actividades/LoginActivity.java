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

import firebase.app.pruebas2.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email, contrasena;
    private Button button, registar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.txtcorreo);
        contrasena = findViewById(R.id.txtPass);
        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btnAcep);
        registar = findViewById(R.id.button);

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = email.getText().toString();
                if(isValidEmail(correo) && validarcontraseña()){
                    String contra = contrasena.getText().toString();

                    mAuth.signInWithEmailAndPassword(correo, contra)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this,"Bienvenido su majestad", Toast.LENGTH_LONG).show();
                                        next();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this,"Error, credenciales incorrectas", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public  boolean validarcontraseña(){
        String contra = contrasena.getText().toString();
            if(contra.length() >= 6 && contra.length()<= 16){
                return true;
            }else return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            next();
        }
    }

    public void next(){

        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        finish();
    }

}
