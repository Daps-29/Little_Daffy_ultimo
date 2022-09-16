package app.wsu.littledaffy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.wsu.littledaffy.databinding.ActivityMaps2Binding;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private DatabaseReference mdatabase;
    private ArrayList<Marker> marke = new ArrayList<>();
    private ArrayList<Marker> realmarkes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mdatabase = FirebaseDatabase.getInstance().getReference("collar");
        countDownTimer();
    }

    private void countDownTimer() {
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFished) {
                Log.e("segundos", "" + millisUntilFished / 1000);

            }

            public void onFinish() {
                //Toast.makeText(MapsActivity2.this, "Ubicacion Actualizada", Toast.LENGTH_SHORT).show();
                onMapReady(mMap);
            }
        }.start();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            //&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                countDownTimer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}