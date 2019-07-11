package firebase.app.pruebas2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import firebase.app.pruebas2.Entidades.firebase.logica.User;
import firebase.app.pruebas2.R;
import firebase.app.pruebas2.Utilidades.Constantes;
import firebase.app.pruebas2.adapters.AdapterUserMenu;
import firebase.app.pruebas2.persistencia.UserDAO;

import static firebase.app.pruebas2.Utilidades.ListDatos.listusers;
import static firebase.app.pruebas2.gestion_ficheros.Gestion_Ficheros.guardardatos;
import static firebase.app.pruebas2.gestion_ficheros.Gestion_Ficheros.leerdatos;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private User user;
    private String tok = FirebaseInstanceId.getInstance().getToken();
    private AdapterUserMenu adapter;

    private ListView list;

    private String tipo;
    private String key;

    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = findViewById(R.id.lista_acceso);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        leerdatos(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(this);

        }
            adapter = new AdapterUserMenu(MenuActivity.this, listusers);
            list.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            tipo = bundle.getString(Constantes.NODO_USUARIOS);

            if(tipo.equals("Notificacion")){
                key = bundle.getString(Constantes.KEY);
                next();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrar_menu:
                Toast.makeText(this, "Cerrando sesion", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
           startActivity(new Intent(MenuActivity.this,VerUsuarioActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,tok,Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          user = dataSnapshot.getValue(User.class);
          if(user != null){
              View hView = navigationView.getHeaderView(0);
              TextView correo = hView.findViewById(R.id.txt_correo_menu);
              TextView name = hView.findViewById(R.id.txt_nombre_menu);
              correo.setText(user.getEmal());
              name.setText(user.getNombre());
          }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

public void next(){
        Intent i = new Intent(MenuActivity.this, MensajeActivity.class);
        i.putExtra(Constantes.KEY, key);
        startActivity(i);
}

    private void returnLogin(){
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        if(UserDAO.getInstancia().isUsuarioLogeado()){
            //el usuario esta logeado y hacemos algo
        }else{
            returnLogin();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        guardardatos(this);

        System.out.println(tok);
    }
}
