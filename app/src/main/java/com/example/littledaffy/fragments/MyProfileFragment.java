package com.example.littledaffy.fragments;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.littledaffy.R;
import com.example.littledaffy.Utility.NetworkChangeListener;
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

public class MyProfileFragment extends Fragment {
    TextView nombre,correo,telefono,direccion;
    TextView genero;
    CircleImageView perfil;
    String id;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_my_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        nombre = root.findViewById(R.id.nombreperfil);
        correo = root.findViewById(R.id.correoperfil);
        telefono = root.findViewById(R.id.telefonoperfil);
        genero = root.findViewById(R.id.generoperfil);
        direccion = root.findViewById(R.id.direccionperfil);
        perfil = root.findViewById(R.id.fotoperfil);
        FirebaseUser urs = mAuth.getCurrentUser();
        id = urs.getUid();

        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterHelper registerHelper = snapshot.getValue(RegisterHelper.class);
                String name = registerHelper.getNombres();
                nombre.setText(name+" "+registerHelper.getApellidos());
                correo.setText(registerHelper.getCorreo());
                telefono.setText(registerHelper.getTelefono());
                direccion.setText(registerHelper.getDireccion());
                genero.setText(registerHelper.getSexo());
                Picasso.get().load(registerHelper.getFoto()).placeholder(R.drawable.a).into(perfil, new Callback() {
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

        return root;
    }
    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeListener,filter);

        super.onStart();
    }

    @Override
    public void onStop() {
        requireActivity().unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
