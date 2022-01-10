package com.example.littledaffy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littledaffy.adapter.MascotasAdapter;
import com.example.littledaffy.model.Masco;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MascotasActivity extends AppCompatActivity {

    RecyclerView cardMasco;
    DatabaseReference database;
    MascotasAdapter mascotasAdapter;
    FloatingActionButton btn;
    ArrayList<Masco>list;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);
        mFirebaseAuth = FirebaseAuth.getInstance();
        cardMasco = findViewById(R.id.recicle);
        btn = (FloatingActionButton) findViewById(R.id.nuevamascota);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MascotasActivity.this,NuevaMacotaActivity.class));
            }
        });

        database = FirebaseDatabase.getInstance().getReference("mascotas");
        cardMasco.setHasFixedSize(true);
        cardMasco.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        mascotasAdapter = new MascotasAdapter(this,list);
        cardMasco.setAdapter(mascotasAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Masco masco = dataSnapshot.getValue(Masco.class);
                    String usermascota = masco.getUser();
                    FirebaseUser usr = mFirebaseAuth.getCurrentUser();
                    String idu = usr.getUid();
                    if (usermascota.equals(idu)) {
                        list.add(masco);
                    }
                }

                mascotasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}