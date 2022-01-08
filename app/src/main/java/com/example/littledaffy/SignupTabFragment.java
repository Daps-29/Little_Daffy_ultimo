package com.example.littledaffy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupTabFragment extends Fragment {
    private EditText nombres;
    private EditText apellidos;
    private EditText correo;
    private EditText contraseña;
    private Button registrar;



    //Variable de datos a registrar

    private String name = "";
    private String ape = "";
    private String email = "";
    private String pass = "";
    private String tipo = "";
    String id;

    FirebaseAuth mAuth;
    DatabaseReference mDatabse;
    @Nullable


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabse = FirebaseDatabase.getInstance().getReference();


        nombres = root.findViewById(R.id.nombre);
        apellidos = root.findViewById(R.id.apellido);
        correo =  root.findViewById(R.id.correo);
        contraseña =  root.findViewById(R.id.contra);
        registrar = root.findViewById(R.id.registrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nombres.getText().toString();
                ape = apellidos.getText().toString();
                email = correo.getText().toString();
                pass = contraseña.getText().toString();

                if (!name.isEmpty() && !ape.isEmpty() && !email.isEmpty() && !pass.isEmpty()){
                    if (pass.length()>=6){
                        registrarU();
                    }



                }


            }
        });
        return root;

    }
    private void registrarU(){
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String name1 = nombres.getText().toString();
                    String ape1 = apellidos.getText().toString();
                    String email1 = correo.getText().toString();
                    String pass1 = contraseña.getText().toString();
                    id = mAuth.getCurrentUser().getUid();
                    int tipou = 1;
                    String idu= id;
                    String direc = "";
                    String foto ="";
                    String telefono="";
                    String sexo ="";
                    RegisterHelper registerHelper = new RegisterHelper(name1,ape1,email1,pass1,idu,tipou,direc,foto,telefono,sexo);



                    mDatabse.child("usuarios").child(id).setValue(registerHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(SignupTabFragment.this.getContext(),OrganizacionActivity.class));
                               
                            }
                            else {
                                Toast.makeText(SignupTabFragment.this.getContext(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}