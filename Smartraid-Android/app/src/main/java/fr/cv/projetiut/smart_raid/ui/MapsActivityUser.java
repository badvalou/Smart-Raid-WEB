package fr.cv.projetiut.smart_raid.ui;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.cv.projetiut.smart_raid.R;
import fr.cv.projetiut.smart_raid.data.Position;
import fr.cv.projetiut.smart_raid.user.User;

public class MapsActivityUser extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private User user = User.getUserInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_user);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addMarkers();
        moveCamera();
     
    }

    private void addMarkers() {
        for (Position position : user.getPositionList()) {
            LatLng latLng = new LatLng(position.getLatitude(), position.getLongitude());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yy");
            String dateFormatString = simpleDateFormat.format(new Date(position.getDate()));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(dateFormatString);
            mMap.addMarker(markerOptions);

        }
    }

    private void moveCamera() {
        Position userPosition = user.getPositionList().get(user.getPositionList().size() - 1);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(userPosition.getLatitude(), userPosition.getLongitude())));
    }

}
