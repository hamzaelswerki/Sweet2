package com.apps.mysweet.veiw;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsSelectPlaceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;

    MarkerOptions userLocationMarker;
    private LocationCallback locationCallback;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final int REQUEST_LOCATION_SETTINGS = 101;
    private LocationRequest locationRequest;
    Marker MylocationMarker;
    Button buttonSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_select_place);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        createButtonSelect();

        createLocationRequest();

    }

    private void createButtonSelect() {
        buttonSelect = findViewById(R.id.btn_select);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ModalSheetAddTittle modalSheetAddTittle = new ModalSheetAddTittle();
                modalSheetAddTittle.show(getSupportFragmentManager(), modalSheetAddTittle.getTag());

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        createLocationCallback(googleMap);
        readLocation();
        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {


                userLocationMarker = new MarkerOptions().position(latLng).title("My Location")
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_group_13005));

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(userLocationMarker);
            }
        });


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    protected void createLocationRequest() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // دالة طلب الموقع الحديث بواسطة ال gps
    private void createLocationCallback(final GoogleMap mMaps) {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    if (MylocationMarker != null) {

                        MylocationMarker.remove();
                    }


                    userLocationMarker = new MarkerOptions()
                            .position(new LatLng(location.getLatitude()
                                    , location.getLongitude())).title("My Location")
                            .icon(bitmapDescriptorFromVector(getApplicationContext()
                                    , R.drawable.ic_group_13005));
                    MylocationMarker = mMaps.addMarker(userLocationMarker);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude()
                                    , location.getLongitude()), 14), 3000, null);

                }
            }
        };


    }
    // دالة فحص البيرمشن واذا لم يكن معطي يطلب


    @Override
    public void onResume() {
        super.onResume();
        readLocation();

    }

    // دالة قراءة الموقع ويستحسن استدعاءها في onresume
    public void readLocation() {
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            checkLocationSettings();
        }
    }

    // دالة فحص تشغيل الgps وطلب تشغيله اذا كان مغلق
    private void checkLocationSettings() {
        final LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(getApplicationContext());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequest);
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (locationSettingsResponse.getLocationSettingsStates().isLocationUsable()) {
                    Toast.makeText(getApplicationContext(), "available success", Toast.LENGTH_SHORT).show();
                    requestLocationRead(mMap);
                } else {
                    Toast.makeText(getApplicationContext(), "Location Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                try {
                    Log.d("ttt", "failuer");

                    resolvableApiException.startResolutionForResult(MapsSelectPlaceActivity.this, REQUEST_LOCATION_SETTINGS);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            } else {
            }
        }
    }

    // دالة طلب قراءة الموقع
    private void requestLocationRead(final GoogleMap mMap) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {

            fusedLocationProviderClient
                    .requestLocationUpdates(locationRequest,
                            locationCallback, Looper.getMainLooper());

        }
    }

    // عند طلب البيرمشن ماذا يعود من المستخدم هل موافقة ام لا
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_LOCATION_SETTINGS && resultCode == RESULT_OK) {
                requestLocationRead(mMap);
            }
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
