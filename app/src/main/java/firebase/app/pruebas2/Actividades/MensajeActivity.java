package firebase.app.pruebas2.Actividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

import firebase.app.pruebas2.Entidades.firebase.logica.Mensaje;
import firebase.app.pruebas2.Entidades.logica.LMensaje;
import firebase.app.pruebas2.Entidades.logica.LUser;
import firebase.app.pruebas2.adapters.AdapterMensaje;
import firebase.app.pruebas2.R;
import firebase.app.pruebas2.persistencia.UserDAO;

public class MensajeActivity extends AppCompatActivity implements ChildEventListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Chat");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button btnEnviar;
    private EditText txtMensaje;
    private RecyclerView rvMensaje;
    private AdapterMensaje adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);


        btnEnviar = findViewById(R.id.btnEnviar);
        txtMensaje = findViewById(R.id.txtMensaje);
        rvMensaje = findViewById(R.id.rvMensajes);

        adapter = new AdapterMensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensaje.setLayoutManager(l);
        rvMensaje.setAdapter(adapter);

        comprobarEditView(txtMensaje, btnEnviar);
        Buttonenviar(btnEnviar,txtMensaje,myRef);

        adapterRegister(adapter);
        myRef.addChildEventListener(this);
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


    public void comprobarEditView(final EditText editText, final Button button){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().trim().length() > 0){
                    button.setVisibility(View.VISIBLE);
                }else{
                    button.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void Buttonenviar(Button button, final EditText editText, final DatabaseReference databaseReference){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String men = editText.getText().toString();
                if(!men.isEmpty()){
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(men);
                    mensaje.setKeyEmisor(UserDAO.getIntacia().getkeyUser());
                    databaseReference.push().setValue(mensaje);
                    editText.setText("");
                }
            }
        });
    }

    public void adapterRegister(AdapterMensaje adapterMensaje){

        adapterMensaje.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
       final Map<String, LUser> mapUT = new HashMap<>();


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
}
