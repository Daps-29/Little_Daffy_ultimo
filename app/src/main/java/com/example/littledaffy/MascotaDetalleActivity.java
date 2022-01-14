package com.example.littledaffy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.littledaffy.model.MascotaDto;
import com.example.littledaffy.model.RegisterHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MascotaDetalleActivity extends AppCompatActivity {
    CircleImageView imag;
    TextView nombreuser,edad,categoria,estado,nombremascotainfo,descipcion;
    ImageView foto1,foto2,back;
    String mascotaid,iduser,nombremascota;
    DatabaseReference mascotainfo, infouser;
    FloatingActionButton whatsapp;
    FirebaseAuth mAuth;
    SliderLayout sliderLayout;
    String telf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_mascota_detalle);



        mAuth = FirebaseAuth.getInstance();
        edad = findViewById(R.id.edadinfo);
        categoria = findViewById(R.id.categoriainfo);
        estado = findViewById(R.id.estadoinfo);
        nombremascotainfo = findViewById(R.id.nombremascotainfo);
        descipcion = findViewById(R.id.descripcioninfo);
        whatsapp = findViewById(R.id.telf);

        imag = findViewById(R.id.imagendetalle);
        nombreuser = findViewById(R.id.nombredetalleuser);
        back = findViewById(R.id.back);
        sliderLayout = findViewById(R.id.image_slider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderLayout.setPagerIndicatorVisibility(true);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderLayout.setScrollTimeInSec(5);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MascotaDetalleActivity.this, MainActivity.class));
            }
        });


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
                telf = registerHelper.getTelefono()+"";
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
        //BOTON WHATSAPP
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telefono = telf;
                if (telefono.substring(0, 3).equals("591")) {
                    telefono = telefono.substring(3);
                }

                if (telefono.substring(0, 4).equals("+591")) {
                    telefono = telefono.substring(4);
                }

                String mensaje = "Somos el equipo de little daffy: ";
                String url = "https://wa.me/591" + telefono + "?text=" + mensaje;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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
                estado.setText(mascotaDto.getSexo());


                for (int i = 0; i<3; i++){
                    DefaultSliderView sliderView = new DefaultSliderView(MascotaDetalleActivity.this);
                    switch (i){
                        case 0:
                            sliderView.setImageUrl(mascotaDto.getFoto1());
                            break;
                        case 1:
                            sliderView.setImageUrl(mascotaDto.getFoto2());
                            break;
                        case 2:
                            sliderView.setImageUrl(mascotaDto.getFoto3());
                            break;
                    }
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    sliderLayout.addSliderView(sliderView);
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}