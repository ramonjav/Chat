package firebase.app.pruebas2.Entidades.firebase.logica;

import com.google.firebase.database.ServerValue;

public class Mensaje {

    private String mensaje;
    private String keyEmisor;
    private Object createdTimestamp;

    public Mensaje() {
        createdTimestamp = ServerValue.TIMESTAMP;
    }

    public Mensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getKeyEmisor() {
        return keyEmisor;
    }

    public void setKeyEmisor(String keyEmisor) {
        this.keyEmisor = keyEmisor;
    }

    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }
}
