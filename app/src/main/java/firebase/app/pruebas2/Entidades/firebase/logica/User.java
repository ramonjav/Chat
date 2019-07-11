package firebase.app.pruebas2.Entidades.firebase.logica;

import java.io.Serializable;

public class User implements Serializable {
    private String emal;
    private String nombre;
    private String token;


    public User() {

    }

    public User(String emal, String nombre, String token) {
        this.emal = emal;
        this.nombre = nombre;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
