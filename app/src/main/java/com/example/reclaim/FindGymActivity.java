package com.example.reclaim;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FindGymActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private TextView mLocationText;   // displays current gps location details
    private TextView locality;         // shows the address found
    private LocationManager locationManager;    // interacts with location services
    private long minTime = 500;              // minimum time between updates of location
    private float minDistance = 1;           // minimum distance in metres between updates
    private static final int MY_PERMISSION_GPS = 1;   // identifies permission request
    private ListView locationsListView;
    private ArrayAdapter<String> locationsAdapter;
    private ArrayList<String> locationsList = new ArrayList<>();
    //Location location;
    GoogleMap gMap;
    FrameLayout map;
    Spinner spType;
    Button btFind;
    private String[] placeTypeList;
    private String[] placeNameList;
    float currentLat, currentLng;
    SupportMapFragment mapFragment;
    //REFERENCE:  https://www.youtube.com/watch?v=pjFcJ6EB8Dg&ab_channel=AndroidCoding
    //for all places api and markers code
    //REFERENCE for base googlemaps api fragment :https://www.youtube.com/watch?v=JzxjNNCYt_o&ab_channel=AndroidKnowledge
    FusedLocationProviderClient fusedLocationProviderClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_gym_activity);
        spType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        //placeTypeList = new String[]{"atm", "bank", "hospital"};
        //placeNameList = new String[]{"ATM", "Bank", "Hospital"};
        placeTypeList = new String[]{"gym", "park"};//types for places api search
        placeNameList = new String[]{"Gym", "Park"};//name of places
        spType.setAdapter(new ArrayAdapter<>(FindGymActivity.this, android.R.layout.simple_spinner_dropdown_item, placeNameList));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);//location service setup needed for apo
        if (ActivityCompat.checkSelfPermission(FindGymActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();//get location if given permission
        } else {
            ActivityCompat.requestPermissions(FindGymActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        mLocationText = (TextView) findViewById(R.id.location);   // initialises the text view using its id from xml
        locality = (TextView) findViewById(R.id.locality);// initialises locality with a text view from xml
        //locationsListView = (ListView) findViewById(R.id.locations_list);
        //locationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationsList);
        //locationsListView.setAdapter(locationsAdapter);
        locationsAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.textViewItem, locationsList);
        //locationsListView.setAdapter(locationsAdapter);
        setUpLocation();//called for gps updates
        //Toast.makeText(FindGymActivity.this, "Values Updated: Time = " + minTime + ", Distance = " + minDistance, Toast.LENGTH_SHORT).show();
        map = findViewById(R.id.map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//gets the map fragment
    }
    // Method to get the current location using FusedLocationProviderClient


    private void getCurrentLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    currentLat =Math.round(location.getLatitude());
                    currentLng =Math.round(location.getLongitude());
                    // Update map with current location

                    mapFragment.getMapAsync((new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            gMap= googleMap;
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currentLng),1));
                        }
                    }));
                }
            }
        });

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
        //updates the ui with the lat and long
        String latestLocation = String.format(
                "Current Location: Latitude %1$s, Longitude %2$s",
                location.getLatitude(), location.getLongitude());
        mLocationText.setText("GPS Location\n" + latestLocation);

        if (gMap != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            gMap.clear(); // Clear old markers
            gMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 2)); // Zoom level
        }
        //String latestLocation = "";

        Log.d("GPSLOCATION", "LOCATION CHANGED!"); // logs message when location is changed

        if (location != null) {
            latestLocation = String.format(
                    "Current Location: Latitude %1$s Longitude : %2$s",
                    Math.round(location.getLatitude()), Math.round(location.getLongitude()));
            Log.d("GPSLOCATION", "Location formatted for TextView!");


        }/*else{
            ActivityCompat.requestPermissions(FindGymActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }*/
        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //constructing url for google places api
                int i =spType.getSelectedItemPosition();
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"+"?location="+currentLat+","+currentLng + "&radius=100000"+"&types="+ placeTypeList[i]+"&sensor=true" +"&key=" +getResources().getString(R.string.google_map_key2);
                //String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                //        "?location=" + currentLat + "," + currentLng +
                //        "&radius=5000" + "&types=" + placeTypeList[i] + "&key=" + getResources().getString(R.string.google_map_key);
                //executes async for api call
                new PlaceTask().execute(url);
            }
        });
        mLocationText.setText("GPS Location" + "\n" + latestLocation);  // updates the TextView with new location

        //fetchNearbyGyms(location.getLatitude(), location.getLongitude());
        updateAddress(location);// Update address based on current location



    }
    private void updateAddress(Location location) {
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
    @Override  // displays toast is user doesnt grant permission
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==44){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
        switch (requestCode) {
            case MY_PERMISSION_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        this.gMap =googleMap;
        //LatLng mapIreland =new LatLng(53,-6);
        //this.gMap.addMarker(new MarkerOptions().position(mapIreland).title("marker in ireland"));
        //this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapIreland));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currentLng),10));
    }//Reference Complete

    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String...strings){
            String data =null;
            try {
                // Download data from the URL
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
        /*@Override
        protected void onPostExecute(String s){
            new ParserTask().execute(s);
        }*/
        @Override
        protected void onPostExecute(String s) {
                new ParserTask().execute(s);
        }
        private String downloadUrl(String string) throws IOException{
            URL url= new URL(string);// Convert the string to URL
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream =connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder =new StringBuilder();
            String line ="";
            while ((line = reader.readLine()) !=null){
                builder.append(line);  // Read data line by line and append
            }
            String data = builder.toString();
            reader.close();
            return data;//return the fetched data
        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
            if (gMap != null) {
                gMap.clear();
                for (int i = 0; i < hashMaps.size(); i++) {
                    HashMap<String, String> hashMapList = hashMaps.get(i);
                    double lat = Double.parseDouble(hashMapList.get("lat"));//get latitude
                    double lng = Double.parseDouble(hashMapList.get("lng"));//get longitude
                    String name = hashMapList.get("name");//name of location
                    Log.d("ParserTask", "Marker: " + name + " (" + lat + ", " + lng + ")");
                    LatLng latLng = new LatLng(lat, lng);//gets location
                    MarkerOptions options = new MarkerOptions();
                    options.position(latLng);
                    options.title(name);
                    gMap.addMarker(options);//adds markers to the map
                }
                Log.d("ParserTask", "Markers added to the map");
            } else {
                Log.e("ParserTask", "gMap is null");
            }
        }
    }
}//Reference complete +JSON file



