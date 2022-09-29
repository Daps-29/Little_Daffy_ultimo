package app.wsu.littledaffy.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.wsu.littledaffy.Collar;
import app.wsu.littledaffy.MascotaNuevaActivity;
import app.wsu.littledaffy.R;
import app.wsu.littledaffy.Utility.NetworkChangeListener;
import app.wsu.littledaffy.adapter.MascotasAdapter;
import app.wsu.littledaffy.model.CollarDto;
import app.wsu.littledaffy.model.MascotaDto;

public class MisMascotasFragment extends Fragment {
    RecyclerView cardMasco;
    TextView sin;
    DatabaseReference database,collare;
    MascotasAdapter mascotasAdapter;
    FloatingActionButton btn,collar;
    ArrayList<MascotaDto> list;
    ArrayList<CollarDto> collarlist;
    private FirebaseAuth mFirebaseAuth;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_mascotas, container, false);
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        cardMasco = root.findViewById(R.id.recicle);
        sin = root.findViewById(R.id.sin);
        btn = (FloatingActionButton)root.findViewById(R.id.nuevamascota);
        collar = (FloatingActionButton)root.findViewById(R.id.collar);
        btn = (FloatingActionButton) root.findViewById(R.id.nuevamascota);
        collar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MisMascotasFragment.this.getContext(), Collar.class);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MisMascotasFragment.this.getContext(), MascotaNuevaActivity.class));
            }
        });
        database = FirebaseDatabase.getInstance().getReference("mascotas");
        collare = FirebaseDatabase.getInstance().getReference("collares");
        cardMasco.setHasFixedSize(true);
        cardMasco.setLayoutManager(new LinearLayoutManager(this.getContext()));

        list = new ArrayList<>();
        collarlist = new ArrayList<>();
        mascotasAdapter = new MascotasAdapter(this.getContext(),list);
        cardMasco.setAdapter(mascotasAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MascotaDto mascotaDto = dataSnapshot.getValue(MascotaDto.class);

                    String usermascota = mascotaDto.getUser();
                    FirebaseUser usr = mFirebaseAuth.getCurrentUser();
                    String idu = usr.getUid();
                    if (usermascota.equals(idu) && mascotaDto.getVerificacion() == 1 && mascotaDto.getEstado().equals("1")) {
                        list.add(mascotaDto);
                        sin.setVisibility(View.INVISIBLE);
                    }

                }

                mascotasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        collare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    CollarDto collarDto = dataSnapshot.getValue(CollarDto.class);

                    String usercollar = collarDto.getUser();
                    FirebaseUser usr = mFirebaseAuth.getCurrentUser();
                    String idu = usr.getUid();
                    if (usercollar.equals(idu)) {

                        collar.setVisibility(View.VISIBLE);
                    }

                }
                mascotasAdapter.notifyDataSetChanged();
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
        collar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        requireActivity().unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
