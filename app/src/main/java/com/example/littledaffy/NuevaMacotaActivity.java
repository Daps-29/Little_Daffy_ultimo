package com.example.littledaffy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.littledaffy.model.Mascota;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NuevaMacotaActivity extends AppCompatActivity {
    private EditText nombre,descripcion,ubicacion,fecha,edad,raza,vacuna;
    private Spinner estado,categoria,tiempo,sexo;
    private Button btnagregar;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_macota);

        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("mascotas");
        nombre = findViewById(R.id.nombremasco);
        descripcion = findViewById(R.id.descripcionmascota);
        ubicacion = findViewById(R.id.ubicacionmascota);
        fecha = findViewById(R.id.fecha);
        edad = findViewById(R.id.edadmascota);
        raza = findViewById(R.id.raza);
        vacuna = findViewById(R.id.vacuna);
        estado = findViewById(R.id.etsadospinner);
        categoria = findViewById(R.id.categoriaspinner);
        tiempo = findViewById(R.id.tiempo);
        sexo = findViewById(R.id.sexomascota);
        btnagregar = findViewById(R.id.guardar);

        String[]Estado={"Adpción","Perdida"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Estado);
        estado.setAdapter(adapter);

        String[]Categorias={"Perro","Gato","Ave","Roedor"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Categorias);
        categoria.setAdapter(adapter1);

        String[]Tiempo={"Semanas","Meses","Años"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Tiempo);
        tiempo.setAdapter(adapter2);
        String[]Sexo={"Macho","Hembra"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Sexo);
        sexo.setAdapter(adapter3);

        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarMascota();
            }
        });

    }
    public void registrarMascota(){
        String publicacion = estado.getSelectedItem().toString();
        String cate = categoria.getSelectedItem().toString();
        String tiem = tiempo.getSelectedItem().toString();
        String se = sexo.getSelectedItem().toString();
        String nom = nombre.getText().toString();
        String des = descripcion.getText().toString();
        String ubi = ubicacion.getText().toString();
        String fec = fecha.getText().toString();
        String ed = edad.getText().toString();
        String ra = raza.getText().toString();
        String vacu = vacuna.getText().toString();
        String foto1 = "";
        String foto2 = "";
        String foto3 = "";
        String estadoeli = "1";
        String verificacion = "0";
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String idu = user.getUid();
        if (!TextUtils.isEmpty(nom) && !TextUtils.isEmpty(des) &&!TextUtils.isEmpty(ubi) &&!TextUtils.isEmpty(fec) &&!TextUtils.isEmpty(ed) &&!TextUtils.isEmpty(ra) &&!TextUtils.isEmpty(vacu)
        && !TextUtils.isEmpty(publicacion) &&!TextUtils.isEmpty(cate) &&!TextUtils.isEmpty(tiem) &&!TextUtils.isEmpty(se)){
            String mascotaid = databaseReference.push().getKey();
            Mascota mascota = new Mascota(mascotaid,nom,des,ubi,fec,publicacion,cate,ed,tiem,se,ra,vacu,foto1,foto2,foto3,estadoeli,idu,verificacion);
            databaseReference.child("").child(mascotaid).setValue(mascota);
            Toast.makeText(this, "Agregado Exitosamente", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }


    }
}