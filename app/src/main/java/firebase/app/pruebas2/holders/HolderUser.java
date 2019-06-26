package firebase.app.pruebas2.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import firebase.app.pruebas2.R;

public class HolderUser extends RecyclerView.ViewHolder {

    private CircleImageView circleImageView;
    private TextView name, correo;

    private LinearLayout linearLayout;

    public HolderUser(@NonNull View itemView) {
        super(itemView);

        circleImageView = itemView.findViewById(R.id.profile_image);
        name = itemView.findViewById(R.id.txt_name);
        correo = itemView.findViewById(R.id.txt_correo);

        linearLayout = itemView.findViewById(R.id.id_Layout_Prin);
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getCorreo() {
        return correo;
    }

    public void setCorreo(TextView correo) {
        this.correo = correo;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}
