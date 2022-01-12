package com.example.littledaffy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.littledaffy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends Fragment {
    TextView nombre,correo,telefono,genero,direccion;
    CircleImageView perfil;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_my_profile, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        nombre = root.findViewById(R.id.nombreperfil);
        correo = root.findViewById(R.id.correoperfil);
        telefono = root.findViewById(R.id.telefonoperfil);
        genero = root.findViewById(R.id.generoperfil);
        direccion = root.findViewById(R.id.direccionperfil);
        perfil = root.findViewById(R.id.fotoperfil);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        nombre.setText(user.getDisplayName());
        correo.setText(user.getEmail());

        Glide.with(this).load(user.getPhotoUrl()).into(perfil);

        return root;
    }

}
