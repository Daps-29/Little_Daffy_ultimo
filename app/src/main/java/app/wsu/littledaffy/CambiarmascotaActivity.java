package app.wsu.littledaffy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import app.wsu.littledaffy.databinding.ActivityMaps2Binding;
import app.wsu.littledaffy.model.OrganizacionDto;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.wsu.littledaffy.Utility.NetworkChangeListener;
import app.wsu.littledaffy.model.MascotaDto;
import de.hdodenhof.circleimageview.CircleImageView;

import org.jetbrains.annotations.NotNull;

public class CambiarmascotaActivity extends AppCompatActivity implements OnMapReadyCallback {
    CircleImageView imag;
    TextView nombre, descripcion;
    Spinner estado;
    Button guardar;
    String idmascota, nombremascota, descripcionmascota;
    DatabaseReference mascotainfo;
    ProgressDialog progressDialog;

    // Configuracion para el mapa
    private GoogleMap mMap;
    LinearLayout containerMapa;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private DatabaseReference datosCollar;
    private ArrayList<Marker> marke = new ArrayList<>();
    private ArrayList<Marker> realmarkes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        containerMapa = (LinearLayout) findViewById(R.id.containerMapa);

        setContentView(R.layout.activity_cambiarmascota);
        imag = findViewById(R.id.fotoestadp);
        nombre = findViewById(R.id.nombreestado);
        descripcion = findViewById(R.id.descripcionestado);
        estado = findViewById(R.id.Cambiar);
        guardar = findViewById(R.id.guardarestado);

        String[] Estado = {"Adoptado", "Encontrado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_custom, Estado);
        estado.setAdapter(adapter);

        //OBTENER DATOS DE LA ANTERIOR ACTIVITY
        Intent intent = getIntent();
        nombremascota = intent.getStringExtra("user");
        descripcionmascota = intent.getStringExtra("user");
        idmascota = intent.getStringExtra("id_mascota");
        mascotainfo = FirebaseDatabase.getInstance().getReference("mascotas").child(idmascota);

        mascotainfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MascotaDto mascotaDto = snapshot.getValue(MascotaDto.class);
                nombre.setText(mascotaDto.getNombre());
                descripcion.setText(mascotaDto.getDescripcion());
                Picasso.get().load(mascotaDto.getFoto1()).placeholder(R.drawable.a).into(imag, new Callback() {
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

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MascotaDto mascotaDto = new MascotaDto();
                String estadomascota = estado.getSelectedItem().toString();
                DatabaseReference base = FirebaseDatabase.getInstance().getReference("mascotas");
                Map<String, Object> actualizar = new HashMap<>();
                actualizar.put("estado", estadomascota);
                progressDialog = new ProgressDialog(CambiarmascotaActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progresdialog);
                startActivity(new Intent(CambiarmascotaActivity.this, MainActivity.class));
                Toast.makeText(CambiarmascotaActivity.this, "Cambiaste el estado de la mascota", Toast.LENGTH_SHORT).show();

                base.child(idmascota).updateChildren(actualizar);
            }
        });

        // Datos para GOOGLE MAPS
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
//            actualizarUbicacion();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void actualizarUbicacion() {
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFished) {
                Log.e("segundos", "" + millisUntilFished / 1000);
            }

            public void onFinish() {
                ubicacionColar();
            }
        }.start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        FirebaseDatabase.getInstance().getReference("collares").orderByChild("mascota").equalTo(idmascota)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "Mascota localizada", Toast.LENGTH_LONG).show();
                            ubicacionColar();
                        } else {
                            Toast.makeText(getApplicationContext(), "La mascota no cuenta con un collar registrado", Toast.LENGTH_LONG).show();
                            if (containerMapa != null) {
                                containerMapa.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Error al buscar la ubicación de la mascota", Toast.LENGTH_LONG).show();
                        if (containerMapa != null) {
                            containerMapa.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void ubicacionColar(){
        datosCollar = FirebaseDatabase.getInstance().getReference("collar");
        datosCollar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (Marker marker:realmarkes){
                    marker.remove();
                }
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Maps maps = dataSnapshot.getValue(Maps.class);
                    Double latitud = maps.getF_latitude();
                    Double longitud = maps.getF_longitude();
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng sydney = new LatLng(latitud,longitud);
                    markerOptions.position(new LatLng(latitud,longitud));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    marke.add(mMap.addMarker(markerOptions));
                }
                realmarkes.clear();
                realmarkes.addAll(marke);
                actualizarUbicacion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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