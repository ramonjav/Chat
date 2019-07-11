package firebase.app.pruebas2.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import firebase.app.pruebas2.Entidades.firebase.logica.User;
import firebase.app.pruebas2.Entidades.logica.LUser;
import firebase.app.pruebas2.R;
import firebase.app.pruebas2.Utilidades.Constantes;
import firebase.app.pruebas2.holders.HolderUser;

import static firebase.app.pruebas2.Utilidades.Constantes.NODO_USUARIOS;
import static firebase.app.pruebas2.Utilidades.ListDatos.listusers;

public class VerUsuarioActivity extends AppCompatActivity{

    private RecyclerView listUser;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);

        listUser = findViewById(R.id.rvUsers);

        LinearLayoutManager l = new LinearLayoutManager(this);

        listUser.setLayoutManager(l);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(NODO_USUARIOS)
                .limitToLast(50);

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<User, HolderUser>(options) {
            @Override
            public HolderUser onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_view_user, parent, false);

                return new HolderUser(view);
            }

            @Override
            protected void onBindViewHolder(HolderUser holder, int position, User model) {

                final LUser lUser = new LUser(getSnapshots().getSnapshot(position).getKey(), model);

                    holder.getName().setText(model.getNombre());
                    holder.getCorreo().setText(model.getEmal());

                holder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(VerUsuarioActivity.this, MensajeActivity.class);
                        intent.putExtra(Constantes.KEY, lUser.getKey());
                        startActivity(intent);
                        Toast.makeText(VerUsuarioActivity.this, lUser.getKey(), Toast.LENGTH_SHORT).show();
                    }
                });

                holder.getButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listusers.add(lUser);
                        Toast.makeText(VerUsuarioActivity.this,"hhhh", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

       listUser.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
