package com.apps.mysweet.veiw;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.MarkerBranch;
import com.apps.mysweet.model.Branch;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public ArrayList<Branch> branches;
    FirebaseFirestore firestore;
    LocationManager locationManager;
    Location mylocation;
    LocationRequest locationRequest=null;
    LocationCallback locationCallback=null;
    Marker MylocationMarker;
    FusedLocationProviderClient locationProviderClient;
    Button btnChat;
    String nameBranch;
    ImageButton btnBake;
    String idBranch;
    FloatingActionButton userLoctionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        btnChat=findViewById(R.id.btn_chat);
        btnBake=findViewById(R.id.btn_back);
        userLoctionBtn=findViewById(R.id.floatingActionButton_user_location);
        setStatusBarColor("#f8f8f8");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firestore=FirebaseFirestore.getInstance();
        branches =new ArrayList<>();
        getBranches();
        userLocation();
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("branchs").whereEqualTo("name", nameBranch)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                        idBranch=document.getId();

                                        startActivity(new Intent(getApplicationContext()
                                                ,ChatBranch.class).putExtra("branchName",nameBranch)
                                                .putExtra("branchId",idBranch));

                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents.");
                                }
                            }
                        });



            }
        });

        btnBake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userLoctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng my_location = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_location, 15), 3000, null);
            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        LatLng gaza = new LatLng(31.416665,  34.333332);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gaza, 11), 3000, null);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Branch branch= (Branch) marker.getTag();

                nameBranch=branch.getName();
                MarkerBranch markerBranch=new MarkerBranch(getApplicationContext());
                markerBranch.getInfoContents(marker);
                mMap.setInfoWindowAdapter(markerBranch);




                if (btnChat.getVisibility() == View.INVISIBLE) {
                    btnChat.setVisibility(View.VISIBLE);
                } else if (btnChat.getVisibility() == View.VISIBLE) {
                    btnChat.setVisibility(View.INVISIBLE);
                }


                return false;
            }
        });
    }


    public void getBranches() {
        Task<QuerySnapshot> task= firestore.collection("branchs").get();
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                Iterator<QueryDocumentSnapshot> iterator=queryDocumentSnapshots.iterator();
                while (iterator.hasNext()){
                    Branch  branch=  iterator.next().toObject(Branch.class);
                    branches.add(branch);

                }

                for(int i=0;i<branches.size();i++){
                    LatLng latLng=new LatLng(branches.get(i).getLat(),branches.get(i).get_long());
                    MarkerOptions marker = new MarkerOptions().position(latLng).
                            title(branches.get(i).getName()).icon(bitmapDescriptorFromVector
                            (getApplicationContext(), R.drawable.ic_group_13005));
                    Marker marker1= mMap.addMarker(marker);
                    marker1.setTag(branches.get(i));}
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Sorry!somthing wrong", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void userLocation(){

        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);

        Boolean isenable= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!isenable){
            Toast.makeText(getApplicationContext(),"please!tern on GPS",Toast.LENGTH_SHORT);
        }
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        locationRequest=LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationProviderClient.requestLocationUpdates(locationRequest,locationCallback=new LocationCallback(){
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if     (locationResult!=null) {
                    if (MylocationMarker != null) {

                        MylocationMarker.remove();
                    }

                    mylocation = locationResult.getLastLocation();
                    LatLng my_location = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
                    MarkerOptions marker_user = new MarkerOptions().position(my_location)
                            .title("My Location").icon(bitmapDescriptorFromVector(getApplicationContext()
                                    , R.drawable.ic_myself));


                    MylocationMarker = mMap.addMarker(marker_user);
                    MylocationMarker.setTag(100);



                }
                super.onLocationResult(locationResult);
            }
        },null);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void setStatusBarColor(String colorString) {

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(colorString));
    }
}
