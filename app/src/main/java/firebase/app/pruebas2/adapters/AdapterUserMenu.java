package firebase.app.pruebas2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import firebase.app.pruebas2.Actividades.MensajeActivity;
import firebase.app.pruebas2.Entidades.logica.LUser;
import firebase.app.pruebas2.R;
import firebase.app.pruebas2.Utilidades.Constantes;

public class AdapterUserMenu extends ArrayAdapter<LUser> {

    private ArrayList<LUser> users;

    private Context context;

    public AdapterUserMenu(Context context, ArrayList<LUser> users) {
        super(context, R.layout.activity_menu, users);

        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_aceeso, parent, false);
        }
        final LUser lUser =  users.get(position);

        LinearLayout linearLayout= convertView.findViewById(R.id.id_Layout_Acceso);

        TextView name = convertView.findViewById(R.id.txt_name);
        TextView correo = convertView.findViewById(R.id.txt_correo);

        name.setText(lUser.getUsuario().getNombre());
        correo.setText(lUser.getUsuario().getEmal());

       linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(context, MensajeActivity.class);
               intent.putExtra(Constantes.KEY, lUser.getKey());
               intent.putExtra(Constantes.TOKEN, lUser.getUsuario().getToken());
               context.startActivity(intent);
               //Toast.makeText(context, lUser.getUsuario().getToken(), Toast.LENGTH_SHORT).show();
           }
       });

        return convertView;
    }
}
