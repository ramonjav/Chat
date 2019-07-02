package firebase.app.pruebas2.Actividades;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import firebase.app.pruebas2.Service.service;
import firebase.app.pruebas2.persistencia.UserDAO;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    User user;
    String tok = FirebaseInstanceId.getInstance().getToken();

    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);


            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null) {
                DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
                reference.addListenerForSingleValueEvent(this);

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
            case R.id.cerra:
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
            startActivity(new Intent(MenuActivity.this, MensajeActivity.class));
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
            View hView = navigationView.getHeaderView(0);
            TextView correo = hView.findViewById(R.id.txt_correo_menu);
            TextView name = hView.findViewById(R.id.txt_nombre_menu);
            correo.setText(user.getEmal());
            name.setText(user.getNombre());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }



    private void returnLogin(){
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UserDAO.getInstancia().isUsuarioLogeado()){
            //el usuario esta logeado y hacemos algo
        }else{
            returnLogin();
        }
    }
}
