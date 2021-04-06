package com.example.cbr_manager.UI.clientListFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cbr_manager.Database.Client;
import com.example.cbr_manager.Database.ClientManager;
import com.example.cbr_manager.GoogleMaps.MyItem;
import com.example.cbr_manager.R;
import com.example.cbr_manager.UI.ClientInfoActivity;
import com.example.cbr_manager.UI.ClientListActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    private ClientListActivity clientListActivity;
    private ClientManager clientManager;
    private ClusterManager<MyItem> clusterManager;
    private Map<String, Long> mMarkerMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.clientListActivity = (ClientListActivity) getActivity();
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() != null) {
            SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map));

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(clientListActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(clientListActivity, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(clientListActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        setUpClusterer();
    }

    private void setUpClusterer() {
        clusterManager = new ClusterManager<>(clientListActivity, mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        addItems();
    }

    private void addItems() {
        LatLng northernUganda = new LatLng(2.8780, 32.7181);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(northernUganda).zoom(6).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        clientManager = ClientManager.getInstance(clientListActivity);

        // clustering
        for (Client client: clientManager.getClients()) {
            MyItem offsetItem = new MyItem(client.getLatitude(), client.getLongitude(), client.getFirstName(), client.getLastName());
            clusterManager.addItem(offsetItem);
        }

        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>() {
            @Override
            public void onClusterItemInfoWindowClick(MyItem myItem) {
                LatLng positionLatLng = myItem.getPosition();
                long position = clientManager.getClientIndexByLatLng(positionLatLng);
                Intent intent = ClientInfoActivity.makeIntent(clientListActivity, (int) position, position);
                startActivity(intent);
            }
        });


    }


}