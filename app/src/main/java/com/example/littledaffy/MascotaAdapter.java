package com.example.littledaffy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MascotaAdapter extends FirebaseRecyclerAdapter<Masco,MascotaAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MascotaAdapter(@NonNull FirebaseRecyclerOptions<Masco> options) {
        super(options);
    }
    FirebaseAuth mauth;
    String  id;
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Masco model) {
        mauth = FirebaseAuth.getInstance();

        FirebaseUser usr = mauth.getCurrentUser();
        id = usr.getUid();
        String nombre = usr.getDisplayName();
        String ao = model.getUser();
            holder.nombre.setText(model.getNombre());
            holder.descripcion.setText(model.getDescripcion());
            holder.user.setText(ao);
        Glide.with(holder.img.getContext()).load(model.getFoto1()).placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop().error(R.drawable.common_google_signin_btn_icon_dark_normal).into(holder.img);
        }
        
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mascota,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView nombre,descripcion,user;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.imagen);
            nombre = (TextView) itemView.findViewById(R.id.nombrem);
            descripcion = (TextView) itemView.findViewById(R.id.descripcionm);
            user = (TextView) itemView.findViewById(R.id.user);

        }
    }
}
