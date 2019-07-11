package firebase.app.pruebas2.Entidades.logica;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

import firebase.app.pruebas2.Entidades.firebase.logica.Mensaje;

public class LMensaje {

    private Mensaje mensaje;
    private String key;
    private LUser luser;

    public LMensaje(String key, Mensaje mensaje) {
        this.mensaje = mensaje;
        this.key = key;
    }

    public Mensaje getMessaje() {
        return mensaje;
    }

    public void setMessaje(Mensaje messaje) {
        this.mensaje = messaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long datelong(){
        return (long)mensaje.getCreatedTimestamp();
    }

    public LUser getLuser() {
        return luser;
    }

    public void setLuser(LUser luser) {
        this.luser = luser;
    }

    public String fechamensaje(){
            Date d = new Date((long)mensaje.getCreatedTimestamp());
            PrettyTime pretty = new PrettyTime(new Date(), Locale.getDefault());
            return pretty.format(d);

    }
}
