package com.example.track;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);



        OkHttpClient client = new OkHttpClient();
        String url = "http://52.70.101.244:8000/verMensajeConfirmacion/";

        final Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("ACA ERROR:");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String myRespose  = response.body().string();
                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapsActivity.this, myRespose, Toast.LENGTH_LONG).show(); //Correcto
                            System.out.println("RESPUESTA:");
                            System.out.println(myRespose);
                        }
                    });
                }
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
        mMap.setMyLocationEnabled(true);


        final LatLng punto1 = new LatLng(-33.024706, -71.561580);
        //LatLng punto2 = new LatLng(-33.0232266,-71.562934);
        //LatLng punto3 = new LatLng(-33.0250436,-71.5680409);
        //LatLng punto4 = new LatLng(-33.0264178,-71.5556597);
        //LatLng punto5 = new LatLng(-33.0226581,-71.5589947);


        mMap.addMarker(new MarkerOptions().position(punto1));
        //mMap.addMarker(new MarkerOptions().position(punto2));
        //mMap.addMarker(new MarkerOptions().position(punto3));
        //mMap.addMarker(new MarkerOptions().position(punto4));
        //mMap.addMarker(new MarkerOptions().position(punto5));



        //   Add a marker in Sydney and move the camera

       locationListener = new LocationListener() {
           @Override
           public void onLocationChanged(Location location) {

               try {
                   latLng = new LatLng(location.getLatitude(), location.getLongitude());
                   //mMap.addMarker(new MarkerOptions().position(latLng).title("Aqui es donde estoy viviendo en mi cuarentena"));
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng ));
                   //distanciaEntre2Puntos(latLng, punto1);

                   Toast.makeText(MapsActivity.this,  "ubicacion cambio", Toast.LENGTH_LONG).show(); //Correcto
               }
               catch (SecurityException e){

                   e.printStackTrace();
               }
           }

           @Override
           public void onStatusChanged(String provider, int status, Bundle extras) {

           }

           @Override
           public void onProviderEnabled(String provider) {

           }

           @Override
           public void onProviderDisabled(String provider) {

           }
       };

       locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

       try {
           locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, MIN_TIME,MIN_DIST,locationListener);
       }
       catch (SecurityException e){
           e.printStackTrace();
       }
    }


    public double distanciaEntre2Puntos(LatLng miUbicacion, LatLng punto){

        double distancia = Math.sqrt( Math.pow(punto.latitude - miUbicacion.latitude, 2)  +  Math.pow(punto.latitude - miUbicacion.longitude, 2)  );
        System.out.println("CTMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
        System.out.println(distancia);

        return(distancia);
    }
}

