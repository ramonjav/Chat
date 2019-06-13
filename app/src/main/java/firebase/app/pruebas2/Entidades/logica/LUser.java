package firebase.app.pruebas2.Entidades.logica;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import firebase.app.pruebas2.Entidades.firebase.logica.User;
import firebase.app.pruebas2.persistencia.UserDAO;

public class LUser {

    private User usuario;
    private String key;


    public LUser(String key, User usuario) {
        this.usuario = usuario;
        this.key = key;
    }


    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getDateCreated(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(UserDAO.getIntacia().fechacreacion());
        return sdf.format(date);

    }

    public String getDateLastSingin(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(UserDAO.getIntacia().fechaultimo());
        return sdf.format(date);

    }
}
