package com.example.ppb_tugas3_googlemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.normal : mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
            case R.id.terrain: mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);break;
            case R.id.satellite: mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);break;
            case R.id.hibryd: mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);break;
            case R.id.none:mMap.setMapType(GoogleMap.MAP_TYPE_NONE);break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button go = (Button) findViewById(R.id.idGo);
        Button cari = (Button) findViewById(R.id.idCari);
        go.setOnClickListener(op);
        cari.setOnClickListener(op);

    }

    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.idGo:sembunyikanKeyBoard(view);
                        gotoLokasi();break;
                case R.id.idCari:sembunyikanKeyBoard(view);
                        goCari();break;
            }
        }
    };


    private void sembunyikanKeyBoard(View v){
        InputMethodManager a = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void goCari(){

        EditText zoom = (EditText) findViewById(R.id.input_zoom);

        if(zoom.length()==0 ||  zoom.getText().toString().trim().equals(".")){
            zoom.setError("Zoom tidak boleh kosong!");
        }
        else{
            Float dblzoom = Float.parseFloat(zoom.getText().toString());
            if(dblzoom > 19){
                zoom.setError("Masukkan zoom antara 0-19!");
            }
            else{
                EditText tempat = (EditText) findViewById(R.id.input_cari);
                Geocoder g = new Geocoder(getBaseContext());
                List<Address> daftar = null;
                try {
                    daftar = g.getFromLocationName(tempat.getText().toString(), 1);
                    Address alamat = daftar.get(0);

                    String nemuAlamat = alamat.getAddressLine(0);
                    Double lintang = alamat.getLatitude();
                    Double bujur = alamat.getLongitude();

                    Toast.makeText(getBaseContext(), "Ketemu" + nemuAlamat, Toast.LENGTH_LONG).show();

                    Toast.makeText(this,"Move to "+ nemuAlamat +" Lat:" +
                            lintang + " Long:" +bujur,Toast.LENGTH_LONG).show();
                    gotoPeta(lintang,bujur,dblzoom);

                    EditText lat = (EditText) findViewById(R.id.input_lat);
                    EditText lng = (EditText) findViewById(R.id.input_long);
                    lat.setText(lintang.toString());
                    lng.setText(bujur.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void gotoPeta(Double lat, Double lng, float z){
        LatLng Lokasibaru = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().
            position(Lokasibaru).
            title("Marker in  " +lat +":" +lng));
        mMap.moveCamera(CameraUpdateFactory.
            newLatLngZoom(Lokasibaru,z));
    }

    private void gotoLokasi(){

        EditText lat = (EditText) findViewById(R.id.input_lat);
        EditText lng = (EditText) findViewById(R.id.input_long);
        EditText zoom = (EditText) findViewById(R.id.input_zoom);



        if(lng.length()==0 ||  lng.getText().toString().trim().equals(".")){
            lng.setError("Longitude tidak boleh kosong!");
        }
        else if(lat.length()==0 ||  lat.getText().toString().trim().equals(".")){
            lat.setError("Latitude tidak boleh kosong!");
        }
        else if(zoom.length()==0 ||  zoom.getText().toString().trim().equals(".")){
            zoom.setError("Zoom tidak boleh kosong!");
        }
        else{
            Double dbllat = Double.parseDouble(lat.getText().toString());
            Double dbllng = Double.parseDouble(lng.getText().toString());
            Float dblzoom = Float.parseFloat(zoom.getText().toString());
            if(dblzoom > 19){
                zoom.setError("Masukkan zoom antara 0-19!");
            }
            else{

                Toast.makeText(this,"Move to Lat:" +dbllat + " Long:" +dbllng,Toast.LENGTH_LONG).show();
                gotoPeta(dbllat,dbllng,dblzoom);
            }
        }


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

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.28, 112.79);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 1));
    }
}
