package com.example.reclaim;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FindGymActivity extends AppCompatActivity implements LocationListener {

    private TextView mLocationText;   // displays current gps location details
    private TextView locality;         // shows the address found
    private LocationManager locationManager;    // interacts with location services
    private long minTime = 500;              // minimum time between updates of location
    private float minDistance = 1;           // minimum distance in metres between updates
    private static final int MY_PERMISSION_GPS = 1;   // identifies permission request
    private ListView locationsListView;
    private ArrayAdapter<String> locationsAdapter;
    private ArrayList<String> locationsList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_gym_activity);
        mLocationText = (TextView) findViewById(R.id.location);   // initialises the text view using its id from xml
        locality = (TextView) findViewById(R.id.locality);// initialises locality with a text view from xml
        locationsListView = (ListView) findViewById(R.id.locations_list);
        locationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationsList);
        locationsListView.setAdapter(locationsAdapter);
        setUpLocation();
        Toast.makeText(FindGymActivity.this, "Values Updated: Time = " + minTime + ", Distance = " + minDistance, Toast.LENGTH_SHORT).show();


    }

    private void setUpLocation()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(FindGymActivity.this, // checks if gps permission is given
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FindGymActivity.this, //requests permission from user for gps location
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_GPS);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this); // requests updates on the users location
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String latestLocation = "";

        Log.d("GPSLOCATION", "LOCATION CHANGED!"); // logs message when location is changed

        if (location != null) {
            latestLocation = String.format(
                    "Current Location: Latitude %1$s Longitude : %2$s",
                    Math.round(location.getLatitude()), Math.round(location.getLongitude()));
            Log.d("GPSLOCATION", "Location formatted for TextView!");
        }
        mLocationText.setText("GPS Location" + "\n" + latestLocation);  // updates the TextView with new location

        fetchNearbyGyms(location.getLatitude(), location.getLongitude());

        try {
            Geocoder geo = new Geocoder(FindGymActivity.this.getApplicationContext(), Locale.getDefault());   // Geocoder for translating longitude and latitude into human-readable address
            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Creates list of addresses from current location

            if (addresses.isEmpty()) {
                locality.setText("Waiting for Location");
            } else {
                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    locality.setText(address); // Updates the locality TextView with the new address
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void fetchNearbyGyms(double latitude, double longitude) {
        // Placeholder for fetching nearby gyms
        locationsList.clear();
        locationsList.add("Gym 1 at Location XYZ");
        locationsList.add("Gym 2 at Location ABC");
        locationsAdapter.notifyDataSetChanged();
    }

    @Override  // displays toast is user doesnt grant permission
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();  // resumes tracking
        setUpLocation();    // sets location
        Log.i("Lab9", "restarted location updates");  // restarts location updates

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        setUpLocation(); // Set up location updates again
        Log.i("Lab9", "Just restarted location checking as activity came back to the foreground");
    }



    /*@Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);  // removes updated location
        Log.i("Lab9", "stopped location updates"); // stops updating location

    }*/
    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) {
            locationManager.removeUpdates(this); // Stop receiving location updates
            Log.i("Lab9", "Just dropped location checking when activity gone into the background");
        }
    }



}