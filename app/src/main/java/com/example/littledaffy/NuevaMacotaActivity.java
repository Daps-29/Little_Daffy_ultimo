package com.example.littledaffy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.littledaffy.Utility.NetworkChangeListener;
import com.example.littledaffy.model.MascotaDto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NuevaMacotaActivity extends AppCompatActivity {
    private EditText nombre,descripcion,ubicacion,edad,raza,vacuna,fecha;
    private Spinner estado,categoria,tiempo,sexo;
    private TextView mostrarfecha;
    private Button btnagregar;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private int e;
    Dialog dialog;
    //fotos
    ImageView image1,image2,image3;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Uri uri;
    Uri uri1;
    Uri uri2;
    String id_foto;
    ProgressDialog progressDialog;
    private DatePickerDialog datePickerDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_macota);


        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("mascotas");
        dialog = new Dialog(this);
        //fotos
        image1 = findViewById(R.id.imagen1);
        image2 = findViewById(R.id.imagen2);
        image3 = findViewById(R.id.imagen3);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,3);
            }
        });






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






        String[]Estado={"Adpción","Desaparecido"};
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
                if (uri != null && uri1 != null && uri2 != null) {
                    progressDialog = new ProgressDialog(NuevaMacotaActivity.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progresdialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                    uploadimagetofirebase(uri,uri1,uri2);

                }else{
                    Toast.makeText(NuevaMacotaActivity.this, "Selecciona las imaganes", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    public void uploadimagetofirebase(Uri uri,Uri uri1,Uri uri2){
        StorageReference file = storageReference.child(System.currentTimeMillis()+""+getFileExtension(uri)+""+getFileExtension(uri1)+""+getFileExtension(uri2));
        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri uri) {

                        String publicacion = estado.getSelectedItem().toString();
                        String cate = categoria.getSelectedItem().toString();
                        String tiem = tiempo.getSelectedItem().toString();
                        String se = sexo.getSelectedItem().toString();
                        String nom = nombre.getText().toString();
                        String des = descripcion.getText().toString();
                        String ubi = ubicacion.getText().toString();
                        String fec = fecha.getText().toString();
                        String ed = edad.getText().toString();
                        e = Integer.parseInt(edad.getText().toString());
                        String ra = raza.getText().toString();
                        String vacu = vacuna.getText().toString();
                        String foto1 = "";
                        String foto2 = "";
                        String foto3 = "";
                        int edaa=1;
                        String estadoeli = "1";
                        int verificacion = 0;
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        id_foto = user.getUid();
                        String idu = user.getUid();
                        if (!TextUtils.isEmpty(nom) && !TextUtils.isEmpty(des) &&!TextUtils.isEmpty(ubi) &&!TextUtils.isEmpty(fec)  &&!TextUtils.isEmpty(ra) &&!TextUtils.isEmpty(vacu)
                                && !TextUtils.isEmpty(publicacion) &&!TextUtils.isEmpty(cate) &&!TextUtils.isEmpty(tiem) &&!TextUtils.isEmpty(se)){
                            String mascotaid = databaseReference.push().getKey();
                            MascotaDto mascotaDto = new MascotaDto(mascotaid,e,estadoeli,publicacion,tiem,verificacion,cate,fec,ra,se,uri.toString(),uri.toString(),uri.toString(),ubi,vacu,idu,nom,des);
                            databaseReference.child("").child(mascotaid).setValue(mascotaDto);

                            opendialog();
                            Toast.makeText(NuevaMacotaActivity.this, "Guardado con exito", Toast.LENGTH_SHORT).show();



                        }else{
                            Toast.makeText(NuevaMacotaActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NuevaMacotaActivity.this, "Error de subida", Toast.LENGTH_SHORT).show();

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    uri = data.getData();
                    image1.setImageURI(uri);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final String imageName = id_foto + "-1";
//                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
//                            if (SDK_INT > 8)
//                            {
//                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                                        .permitAll().build();
//                                StrictMode.setThreadPolicy(policy);
//                                utils.uploadFile(data.getData().getPath(), imageName + ".jpeg","http://aynichef.com/UploadToPlatos.php" , getApplicationContext());
//                            }
//                        }
//                    });
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    uri1 = data.getData();
                    image2.setImageURI(uri1);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final String imageName = id_plato + "-2";
//                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
//                            if (SDK_INT > 8)
//                            {
//                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                                        .permitAll().build();
//                                StrictMode.setThreadPolicy(policy);
//                                utils.uploadFile(data.getData().getPath(), imageName + ".jpeg","http://aynichef.com/UploadToPlatos.php" , getApplicationContext());
//                            }
//                        }
//                    });
                }
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    uri2 = data.getData();
                    image3.setImageURI(uri2);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final String imageName = id_plato + "-3";
//                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
//                            if (SDK_INT > 8)
//                            {
//                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                                        .permitAll().build();
//                                StrictMode.setThreadPolicy(policy);
//                                utils.uploadFile(data.getData().getPath(), imageName + ".jpeg","http://aynichef.com/UploadToPlatos.php" , getApplicationContext());
//                            }
//                        }
//                    });
                }
                break;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void opendialog(){
        progressDialog = new ProgressDialog(NuevaMacotaActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progresdialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView btnclose = dialog.findViewById(R.id.closebtn);
        Button aceptar = dialog.findViewById(R.id.btnaceptar);

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NuevaMacotaActivity.this, MainActivity.class));
            }
        });
        dialog.show();

    }
    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }


}