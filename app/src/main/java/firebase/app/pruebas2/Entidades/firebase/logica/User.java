package firebase.app.pruebas2.Entidades.firebase.logica;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class User {
    private String emal;
    private String nombre;

    public User() {


    }

    public User(String emal, String nombre) {
        this.emal = emal;
        this.nombre = nombre;
    }

    public String getEmal() {
        return emal;
    }

    public void setEmal(String emal) {
        this.emal = emal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
