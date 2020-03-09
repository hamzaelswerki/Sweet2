package com.apps.mysweet.veiw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.apps.mysweet.model.Category;
import com.apps.mysweet.model.Constants;
import com.apps.mysweet.model.Order;
import com.apps.mysweet.viewmodel.OrderViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity implements HomeFragment.onFragmentListener {
    BottomNavigationView navigationView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final int REQUEST_LOCATION_SETTINGS = 101;
    private LocationRequest locationRequest;
    static OrderViewModel orderViewModel;
    BasketFragment basketFragment;
     HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        navigationView = findViewById(R.id.bottom_navigation);
        basketFragment = new BasketFragment();
        homeFragment =new HomeFragment();

        receivedIntentFromModalSheetAddTitle();
        createNavigationView();

        hideStaustBar();

        createLocationRequest();
        createLocationCallback();


    }
    private void receivedIntentFromModalSheetAddTitle() {
        if (getIntent().getBooleanExtra("IsComesFromAddTitle", false)) {

         /*   if (getIntent().getStringExtra("Address") != null) {
                Bundle b = new Bundle();
                b.putString("Address", getIntent().getStringExtra("Address"));
                basketFragment.setArguments(b);
                Toast.makeText(getApplicationContext(), "true ", Toast.LENGTH_SHORT).show();
            }
*/

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, new BasketFragment()).commit();
            navigationView.setSelectedItemId(R.id.action_basket);
        }
    }

    private void createNavigationView() {
        navigationView.performClick();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_home,new HomeFragment() ).commit();
                        return true;
                    case R.id.action_basket:

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_home, new BasketFragment()).commit();
                        return true;

                }
                return false;
            }
        });
        navigationView.setSelectedItemId(R.id.action_home);

    }

    @Override
    public void categorySelected(Category category) {

        Intent intent = new Intent(getApplicationContext(), ListProductsActivity.class);
        intent.putExtra(Constants.INTENT_CATEGEORY_NAME, category.getCategoryName());
        intent.putExtra(Constants.INTENT_CATEGEORY_COLOR, category.getBackgroundColor());

        startActivity(intent);
    }

    @Override
    public void imageProfileClicked() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }


    private void hideStaustBar() {
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        if (getActionBar() != null) {
            ;

            getActionBar().hide();
        }
    }


    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // دالة طلب الموقع الحديث بواسطة ال gps
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };
    }
    // دالة فحص البيرمشن واذا لم يكن معطي يطلب


    @Override
    public void onResume() {
        super.onResume();
        readLocation();
 receivedIntentFromModalSheetAddTitle();
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
            //   progressDialog.show();
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
                    //showProgressDialog();
                    requestLocationRead();
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
                    //    if (progressDialog.isShowing()){
                    //       progressDialog.dismiss();
                    // }
                    resolvableApiException.startResolutionForResult(HomeActivity.this, REQUEST_LOCATION_SETTINGS);
                } catch (Exception ex) {
                    //   if (progressDialog.isShowing()){ progressDialog.dismiss(); }
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  progressDialog.show();
                checkLocationSettings();
            } else {
            }
        }
    }

    // دالة طلب قراءة الموقع
    private void requestLocationRead() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            //   progressDialog.show();

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
                requestLocationRead();
            }
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
