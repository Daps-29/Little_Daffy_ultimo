package com.example.littledaffy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littledaffy.R;
import com.example.littledaffy.model.Masco;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.MyviewHolder> {
    Context context;
    ArrayList<Masco> list;
    public MascotasAdapter(Context context, ArrayList<Masco> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(context).inflate(R.layout.card_mascota,parent,false);
      return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Masco masco = list.get(position);
        holder.nombre.setText(masco.getNombre());
        holder.descipcion.setText(masco.getDescripcion());


        holder.delte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, LogrosActivity.class);
                //context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(context, OrganizacionActivity.class);
                //context.startActivity(intent);
            }
        });
        Picasso.get().load(masco.getFoto1()).placeholder(R.drawable.a).into(holder.img, new Callback() {
            @Override public void onSuccess() {

            }
            @Override
            public void onError(Exception e) {
                Log.e("PICASSO ERROR", "onError: "+ e);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        TextView nombre,descipcion;
        ImageView img;
        ImageButton delte;
        FloatingActionButton btn;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombrem);
            descipcion = (TextView) itemView.findViewById(R.id.descripcionm);
            img = (ImageView) itemView.findViewById(R.id.imagen);
            delte = (ImageButton) itemView.findViewById(R.id.delete);
            btn = (FloatingActionButton) itemView.findViewById(R.id.nuevamascota);

        }
    }

}
