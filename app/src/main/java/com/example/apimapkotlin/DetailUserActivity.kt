package com.example.apimapkotlin

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailUserActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var b_detailuser_back: Button
    lateinit var iv_detailuser_image: ImageView
    lateinit var tv_detailuser_name: TextView
    lateinit var tv_detailuser_email: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailpage)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        iv_detailuser_image = findViewById(R.id.iv_detailuser_image)
        tv_detailuser_name = findViewById(R.id.tv_detailuser_name)
        tv_detailuser_email = findViewById(R.id.tv_detailuser_email)

        Glide.with(this).load(GlobalConstants.UserData.picture).into(iv_detailuser_image)
        tv_detailuser_name.setText(GlobalConstants.UserData.name)
        tv_detailuser_email.setText(GlobalConstants.UserData.email)

        b_detailuser_back = findViewById(R.id.b_detailuser_back)
        b_detailuser_back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        })


        if (!isNetworkConnected()) {
            Toast.makeText(this, "No Internet Communication", Toast.LENGTH_LONG).show()
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMinZoomPreference(4.0f)
        //    mMap.setMaxZoomPreference(20.0f);


        // Add a marker in Sydney and move the camera
        val location = LatLng(GlobalConstants.UserData.latitude, GlobalConstants.UserData.longitude)

        mMap.addMarker(MarkerOptions().position(location).title("Marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
    }
}
