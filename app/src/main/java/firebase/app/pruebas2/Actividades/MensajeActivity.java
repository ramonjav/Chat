package firebase.app.pruebas2.Actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import firebase.app.pruebas2.Entidades.firebase.logica.Mensaje;
import firebase.app.pruebas2.Entidades.firebase.logica.User;
import firebase.app.pruebas2.Entidades.logica.LMensaje;
import firebase.app.pruebas2.Entidades.logica.LUser;
import firebase.app.pruebas2.adapters.AdapterMensaje;
import firebase.app.pruebas2.R;
import firebase.app.pruebas2.persistencia.UserDAO;

public class MensajeActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Chat");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button btnEnviar;
    private EditText txtMensaje;
    private RecyclerView rvMensaje;
    private AdapterMensaje adapter;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);


        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        txtMensaje = (EditText)findViewById(R.id.txtMensaje);
        rvMensaje = (RecyclerView)findViewById(R.id.rvMensajes);



        adapter = new AdapterMensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensaje.setLayoutManager(l);
        rvMensaje.setAdapter(adapter);


    btnEnviar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String men = txtMensaje.getText().toString();
            if(!men.isEmpty()){
                Mensaje mensaje = new Mensaje();
                mensaje.setMensaje(men);
                mensaje.setKeyEmisor(UserDAO.getIntacia().getkeyUser());
                myRef.push().setValue(mensaje);
                txtMensaje.setText("");
            }
     }
    });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });



        myRef.addChildEventListener(new ChildEventListener() {

            Map<String, LUser> mapUT = new HashMap<>();
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final Mensaje m = dataSnapshot.getValue(Mensaje.class);
                final LMensaje lmensaje = new LMensaje(dataSnapshot.getKey(), m);
               final int position =  adapter.addmensaje(lmensaje);

                if(mapUT.get(m.getKeyEmisor()) != null){
                    lmensaje.setLuser(mapUT.get(m.getKeyEmisor()));
                    adapter.actualizarmensaje(position, lmensaje);

                }else{
                    UserDAO.getIntacia().OptenerUser(m.getKeyEmisor(), new UserDAO.IdevolverLuser() {
                        @Override
                        public void devolver(LUser lUser) {
                            mapUT.put(m.getKeyEmisor(), lUser);
                            lmensaje.setLuser(lUser);
                            adapter.actualizarmensaje(position, lmensaje);
                        }

                        @Override
                        public void devolverErro(String error) {

                            Toast.makeText(MensajeActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setScrollbar(){

        rvMensaje.scrollToPosition(adapter.getItemCount()-1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mensajeria_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerra:
                Toast.makeText(this, "Cerrando sesion", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MensajeActivity.this, LoginActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
