package com.example.littledaffy;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.littledaffy.model.DireccionDto;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.littledaffy.databinding.ActivityUbicacionBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Http;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UbicacionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityUbicacionBinding binding;
    private FusedLocationProviderClient ubicacion;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String id;
    Button btnguardarubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUbicacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();

        btnguardarubicacion = (Button) findViewById(R.id.btnguardarubicacion);

        databaseReference = FirebaseDatabase.getInstance().getReference("direcciones");


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        ubicacion = LocationServices.getFusedLocationProviderClient(UbicacionActivity.this);
        ubicacion.getLastLocation().addOnSuccessListener(UbicacionActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    Double latitud = location.getLatitude();
                    Double longitud = location.getLongitude();

                    // Add a marker in Sydney and move the camera
                    LatLng sydney = new LatLng(latitud, longitud);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Ubicación actual"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//                    Geocoder gcd = new Geocoder(this, Locale.getDefault());
//                    List<Address> addresses = gcd.getFromLocation(currentLatitude, currentLongitude,100);
//                    if (addresses.size() > 0 && addresses != null) {
//                        StringBuilder result = new StringBuilder();
//                        myaddress.setText(addresses.get(0).getFeatureName()+"-"+addresses.get(0).getLocality()+"-"+addresses.get(0).getAdminArea()+"-"+addresses.get(0).getCountryName());
//                    }

                    if (latitud != 0.0 && longitud != 0.0) {
                        try {
                            Geocoder geocoder = new Geocoder(UbicacionActivity.this, Locale.getDefault());
                            List<Address> list = geocoder.getFromLocation(
                                    latitud, longitud, 1);
                            if (!list.isEmpty()) {
                                String nombrecalle = list.get(0).getFeatureName();
                                String ciudad = list.get(0).getLocality();
                                String Estado = list.get(0).getAdminArea();
                                String country = list.get(0).getCountryName();

                                Toast.makeText(UbicacionActivity.this, "Direc: " + Estado, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    btnguardarubicacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String idDireccion = databaseReference.push().getKey();
                            String calle = "";
                            String referencia = "";
                            String iduser = id;
                            String direccionLiteral = "";
                            DireccionDto direccionDto = new DireccionDto(idDireccion, calle, referencia, iduser, latitud.toString(), longitud.toString(), direccionLiteral);
                            databaseReference.child("").child(idDireccion).setValue(direccionDto);
                            Toast.makeText(UbicacionActivity.this, "Direccion agregada correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UbicacionActivity.this, MainActivity.class));

                            DatabaseReference base = FirebaseDatabase.getInstance().getReference("usuarios");
                            FirebaseUser urs = mAuth.getCurrentUser();
                            id = urs.getUid();
                            Map<String,Object> update = new HashMap<>();
                            update.put("direccion",idDireccion);
                            base.child(id).updateChildren(update);
                        }
                    });


                }
            }
        });




    }




}