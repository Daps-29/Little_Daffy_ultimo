package com.example.littledaffy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.littledaffy.model.MascotaDto;
import com.example.littledaffy.model.RegisterHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MascotaDetalleActivity extends AppCompatActivity {
    CircleImageView imag;
    TextView nombreuser,edad,categoria,estado,nombremascotainfo,descipcion;
    ImageView foto1,foto2;
    String mascotaid,iduser,nombremascota;
    DatabaseReference mascotainfo, infouser;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_detalle);
        mAuth = FirebaseAuth.getInstance();
        edad = findViewById(R.id.edadinfo);
        categoria = findViewById(R.id.categoriainfo);
        estado = findViewById(R.id.estadoinfo);
        nombremascotainfo = findViewById(R.id.nombremascotainfo);
        descipcion = findViewById(R.id.descripcioninfo);
        foto1 = findViewById(R.id.fotodetalle);
        foto2 = findViewById(R.id.fotodetalle2);
        imag = findViewById(R.id.imagendetalle);
        nombreuser = findViewById(R.id.nombredetalleuser);


        //OBTENER DATOS DE LA ANTERIOR ACTIVITY
        Intent intent = getIntent();
        iduser = intent.getStringExtra("user");
        mascotaid = intent.getStringExtra("id_mascota");
//        nombremascota = intent.getStringExtra("nombre");
        mascotainfo = FirebaseDatabase.getInstance().getReference("mascotas").child(mascotaid);
        infouser = FirebaseDatabase.getInstance().getReference("usuarios").child(iduser);
        FirebaseUser user = mAuth.getCurrentUser();

        //RECUPERAMOS INFORMACION PARA LLENAR EL ACTIVITY
        infouser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegisterHelper registerHelper = dataSnapshot.getValue(RegisterHelper.class);

                nombreuser.setText(registerHelper.getNombres());
                Picasso.get().load(registerHelper.getFoto()).placeholder(R.drawable.a).into(imag, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("PICASSO ERROR", "onError: " + e);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mascotainfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MascotaDto mascotaDto = snapshot.getValue(MascotaDto.class);
                nombremascotainfo.setText(mascotaDto.getNombre());
                edad.setText(mascotaDto.getEdad() + " " + mascotaDto.getTiempo());
                categoria.setText(mascotaDto.getCategorias());
                descipcion.setText(mascotaDto.getDescripcion());

                Picasso.get().load(mascotaDto.getFoto1()).placeholder(R.drawable.a).into(foto1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("PICASSO ERROR", "onError: " + e);
                    }
                });
                Picasso.get().load(mascotaDto.getFoto2()).placeholder(R.drawable.a).into(foto2, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("PICASSO ERROR", "onError: " + e);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}