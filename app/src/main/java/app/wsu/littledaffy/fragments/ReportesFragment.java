package app.wsu.littledaffy.fragments;

import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import app.wsu.littledaffy.R;
import app.wsu.littledaffy.Utility.NetworkChangeListener;
import app.wsu.littledaffy.adapter.OrganizacionAdapter;
import app.wsu.littledaffy.model.DireccionDto;
import app.wsu.littledaffy.model.OrganizacionDto;
import app.wsu.littledaffy.model.RegisterHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportesFragment extends Fragment {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_reportes, container, false);


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
