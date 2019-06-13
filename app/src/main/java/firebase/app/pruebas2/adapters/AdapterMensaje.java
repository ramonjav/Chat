package firebase.app.pruebas2.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import firebase.app.pruebas2.Actividades.MensajeActivity;
import firebase.app.pruebas2.Entidades.logica.LMensaje;
import firebase.app.pruebas2.Entidades.logica.LUser;
import firebase.app.pruebas2.R;
import firebase.app.pruebas2.holders.HolderMensaje;
import firebase.app.pruebas2.persistencia.UserDAO;

public class AdapterMensaje extends RecyclerView.Adapter<HolderMensaje> {

    private List<LMensaje> listmensaje = new ArrayList<>();
    private Context c;

    public AdapterMensaje(Context c) {
        this.c = c;
    }

    public int addmensaje(LMensaje lmensaje){
        listmensaje.add(lmensaje);
        int position = listmensaje.size()-1;
        notifyItemInserted(listmensaje.size());
        return position;
    }


    public void actualizarmensaje(int position, LMensaje lMensaje){
        listmensaje.set(position, lMensaje);
        notifyItemChanged(position);

    }

    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View v = LayoutInflater.from(c).inflate(R.layout.card_view_emisor, parent, false);
            return new HolderMensaje(v);
        }else{
            View v = LayoutInflater.from(c).inflate(R.layout.card_view_receptor, parent, false);
            return new HolderMensaje(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {

        LMensaje lmensaje = listmensaje.get(position);
        LUser luser = lmensaje.getLuser();
        if(luser != null){
            holder.getNombre().setText(luser.getUsuario().getNombre());
        }
        holder.getMensaje().setText(lmensaje.getMessaje().getMensaje());
        holder.getHora().setText(lmensaje.fechamensaje());

    }

    @Override
    public int getItemCount() {
        return listmensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(listmensaje.get(position).getLuser() != null){
            if(listmensaje.get(position).getLuser().getKey().equals(UserDAO.getIntacia().getkeyUser())) {
                return 1;
            }else
                return 0;
        }else
            return 0;
    }
}
