package com.example.littledaffy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littledaffy.R;
import com.example.littledaffy.VerOrganizacionesActivity;
import com.example.littledaffy.model.MascotaDto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LogrosAdapter extends RecyclerView.Adapter<LogrosAdapter.MyViewHolder> {

    private ArrayList<MascotaDto> data;
    LayoutInflater inflater;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, edad, estado_logro;
        ImageView foto;
        Context context;


        public MyViewHolder(@NonNull View v) {
            super(v);
            nombre = (TextView) v.findViewById(R.id.nombreMascota);
            edad = (TextView) v.findViewById(R.id.edad);
            foto = (ImageView) v.findViewById(R.id.organizacionImage);
            estado_logro = (TextView) v.findViewById(R.id.estado_logro);
            context = v.getContext();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LogrosAdapter(Context logrosActivity, ArrayList<MascotaDto> myDataset) {
        data = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public LogrosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_encontrados, parent, false);

        LogrosAdapter.MyViewHolder vh = new LogrosAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull LogrosAdapter.MyViewHolder holder, int position) {
        final MascotaDto currentItem = data.get(position);

        holder.nombre.setText(currentItem.getNombre());
        holder.edad.setText(String.valueOf(currentItem.getEdad()));
        holder.estado_logro.setText(String.valueOf(currentItem.getEstado()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context.getApplicationContext(), VerOrganizacionesActivity.class);
                intent.putExtra("id_mascota", currentItem.getId_mascota());
                intent.putExtra("user", currentItem.getUser());
                holder.context.startActivity(intent);
            }
        });

        Picasso.get().load(currentItem.getFoto1()).placeholder(R.drawable.a).into(holder.foto, new Callback() {
            @Override public void onSuccess() {

            }
            @Override
            public void onError(Exception e) {
                Log.e("PICASSO ERROR", "onError: "+ e);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}
