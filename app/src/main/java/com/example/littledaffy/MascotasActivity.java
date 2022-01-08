package com.example.littledaffy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.littledaffy.model.OrganizacionDto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MascotasActivity extends AppCompatActivity {

    private RecyclerView cardMasco;
    MascotaAdapter mascotaAdapter;
    FirebaseAuth mauth;
    DatabaseReference database;
    TextView key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);
        database = FirebaseDatabase.getInstance().getReference().child("mascotas");
        mauth = FirebaseAuth.getInstance();
        FirebaseUser usr = mauth.getCurrentUser();
        cardMasco = (RecyclerView) findViewById(R.id.recicle);
        key = (TextView) findViewById(R.id.key);
        cardMasco.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Masco> options = new FirebaseRecyclerOptions.Builder<Masco>()
                .setQuery(database,Masco.class ).build();


        mascotaAdapter = new MascotaAdapter(options);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Masco mascota = dataSnapshot.getValue(Masco.class);
                    String user = mascota.getUser();
                    String idusario = usr.getUid();
                    if (idusario.equals(user)) {
                        cardMasco.setAdapter(mascotaAdapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mascotaAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mascotaAdapter.stopListening();
    }
}