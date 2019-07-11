package firebase.app.pruebas2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import firebase.app.pruebas2.Actividades.FotoActivity;
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
        }else {
            View v = LayoutInflater.from(c).inflate(R.layout.card_view_receptor, parent, false);
            return new HolderMensaje(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderMensaje holder, int position) {

        final LMensaje lmensaje = listmensaje.get(position);
        LUser luser = lmensaje.getLuser();

        if(luser != null){
            holder.getNombre().setText(luser.getUsuario().getNombre());
        }
        holder.getMensaje().setText(lmensaje.getMessaje().getMensaje());
        holder.getHora().setText(lmensaje.fechamensaje());

        lmensaje.getMessaje().isConFoto();

       if(lmensaje.getMessaje().isConFoto()){

            holder.getImage().setVisibility(View.VISIBLE);
           Glide.with(c).asBitmap().load(lmensaje.getMessaje().getUrlFoto()).into(new SimpleTarget<Bitmap>() {
               @Override
               public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                   String fichero = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Chat/Images/";
                   saveImage(resource, lmensaje.getMessaje().getNameFoto(), fichero);

                   Glide.with(c).load(Environment.getExternalStorageDirectory().getAbsolutePath() + lmensaje.getMessaje().getUbicacion()).into(holder.getImage());
               }
           });



        }else {
            holder.getImage().setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return listmensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(listmensaje.get(position).getLuser() != null){
            if(listmensaje.get(position).getLuser().getKey().equals(UserDAO.getInstancia().getkeyUser())) {
                return 1;
            }else

                return 0;
        }else
            return 1;
    }

    public void saveImage(Bitmap bitmap, String fileName, String fichero){

        File file = new File(fichero, fileName);
        if(!file.exists()){
            Log.d("pathImage", file.toString());
            FileOutputStream fos = null;
            try{
                //file.mkdirs();
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                Log.d("pathImage", file.toString());
            }catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
        //Toast.makeText(FotoActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
    }
}
