package firebase.app.pruebas2.persistencia;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import firebase.app.pruebas2.Entidades.firebase.logica.User;
import firebase.app.pruebas2.Entidades.logica.LUser;
import firebase.app.pruebas2.Utilidades.Constantes;

public class UserDAO {

   public interface IdevolverLuser{
       public void devolver(LUser lUser);
       public void devolverErro (String error);
   }

    private static UserDAO userDAO;

    private FirebaseDatabase database;
    private DatabaseReference referenciauser;

    public static UserDAO getInstancia(){
        if(userDAO == null) userDAO = new UserDAO();
        return userDAO;
    }

    private UserDAO(){
        database = FirebaseDatabase.getInstance();
        referenciauser = database.getReference(Constantes.NODO_USUARIOS);
    }

    public void OptenerUser (final String key, final IdevolverLuser idevolverLuser){
        referenciauser.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                LUser luser = new LUser(key, user);
                idevolverLuser.devolver(luser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                idevolverLuser.devolverErro(databaseError.getMessage());
            }
        });
    }


    public String getkeyUser(){
        return FirebaseAuth.getInstance().getUid();
    }

    public boolean isUsuarioLogeado(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser!=null;
    }

    public long fechacreacion(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }

    public long fechaultimo(){
        return FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }
}
