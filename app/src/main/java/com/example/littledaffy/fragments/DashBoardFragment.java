package com.example.littledaffy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.littledaffy.R;
import com.example.littledaffy.adapter.CategoriesAdapter;
import com.example.littledaffy.adapter.ListaInicialAdapter;
import com.example.littledaffy.model.CategoriasDto;
import com.example.littledaffy.model.MascotaDto;
import com.example.littledaffy.model.RegisterHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardFragment extends Fragment {



    ArrayList<CategoriasDto> categoriesAdapterArrayList;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView rv_categorias;
    RecyclerView.LayoutManager categories_layoutManager;


    RecyclerView rv_mascotas;
    Query userquery;
    DatabaseReference database,userdatbase;
    ListaInicialAdapter listaInicialAdapter;
    ArrayList<MascotaDto> mascotaDtoArrayList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth mAuth;
    ConstraintLayout progress_bar;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView nombre;
    CircleImageView perfilfoto;
    String idu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
        nombre = root.findViewById(R.id.nombreingreso);
        perfilfoto = root.findViewById(R.id.profile_user);





        progress_bar = (ConstraintLayout) root.findViewById(R.id.progress_bar);

        //CATEGORIAS MASCOTAS
        rv_categorias = (RecyclerView) root.findViewById(R.id.rv_categorias);
        rv_categorias.setHasFixedSize(true);
        categories_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_categorias.setLayoutManager(categories_layoutManager);

        categoriesAdapterArrayList = new ArrayList<>();
        CategoriasDto categoria1 = new CategoriasDto(0,"Gatos", getResources().getDrawable(R.drawable.cate));
        CategoriasDto categoria2 = new CategoriasDto(1,"Perros", getResources().getDrawable(R.drawable.cate2));
        CategoriasDto categoria3 = new CategoriasDto(2,"Conejos", getResources().getDrawable(R.drawable.cate));
        CategoriasDto categoria4 = new CategoriasDto(3,"Aves", getResources().getDrawable(R.drawable.cate));
        CategoriasDto categoria5 = new CategoriasDto(4,"Hamsters", getResources().getDrawable(R.drawable.cate));
        CategoriasDto categoria6 = new CategoriasDto(5,"Otros", getResources().getDrawable(R.drawable.cate));
        categoriesAdapterArrayList.add(categoria1);
        categoriesAdapterArrayList.add(categoria2);
        categoriesAdapterArrayList.add(categoria3);
        categoriesAdapterArrayList.add(categoria4);
        categoriesAdapterArrayList.add(categoria5);
        categoriesAdapterArrayList.add(categoria6 );

        categoriesAdapter = new CategoriesAdapter(categoriesAdapterArrayList);
        rv_categorias.setAdapter(categoriesAdapter);


        //LISTA PRINCIPAL
        rv_mascotas = (RecyclerView) root.findViewById(R.id.rv_mascotas);
        rv_mascotas.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_mascotas.setLayoutManager(layoutManager);
        //ACCIONES PARA LA LISTA
        database = FirebaseDatabase.getInstance().getReference("mascotas");
        mascotaDtoArrayList = new ArrayList<>();
        listaInicialAdapter = new ListaInicialAdapter(getContext(), mascotaDtoArrayList);
        rv_mascotas.setAdapter(listaInicialAdapter);


        //ACTUALIZAR LISTA
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList();
            }
        });

        updateMascotasList();
        obtenernombres();

        return root;

    }

    private void updateList(){
        updateMascotasList();
        swipeRefreshLayout.setRefreshing(false);
    }


    private void updateMascotasList(){
        progress_bar.setVisibility(View.VISIBLE);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mascotaDtoArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MascotaDto mascotaDto = dataSnapshot.getValue(MascotaDto.class);

                    if (mascotaDto.getVerificacion() == 1 && mascotaDto.getEstado().equals("1")) {
                        progress_bar.setVisibility(View.GONE);
                        mascotaDtoArrayList.add(mascotaDto);

                    }
                }

                listaInicialAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void obtenernombres(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        idu = user.getUid();
        userquery = FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("id").equalTo(idu);
        userquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    RegisterHelper registerHelper = new RegisterHelper();
                    nombre.setText(registerHelper.getNombres());
                    Picasso.get().load(registerHelper.getFoto()).placeholder(R.drawable.a).into(perfilfoto, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("PICASSO ERROR", "onError: " + e);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
