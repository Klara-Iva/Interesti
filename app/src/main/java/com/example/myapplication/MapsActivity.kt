package com.example.myapplication

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.myapplication.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val db = Firebase.firestore


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Welcome to VTC!!")
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val viewBounds= LatLngBounds(LatLng(45.829170, 17.381278),LatLng(45.836548, 17.388014))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewBounds.center,16f))

        mMap.setOnMarkerClickListener {
         val intent = Intent(this, InfoActivity::class.java)
         intent.putExtra("id",it.tag.toString() )
         startActivity(intent)
         true
        }

        val locations= mutableListOf<MyMarker>()
        val markers= mutableListOf<Marker?>()

        val docRef = db.collection("places")
        docRef.get()
            .addOnSuccessListener { documents ->
                for( document in documents.documents) {
                    var cordinates = LatLng(document?.data!!["lati"] as Double,
                        document?.data!!["long"] as Double)
                    locations.add(MyMarker(document?.id!!,cordinates))
                }

                for (location in locations){
                    val myMarker = mMap.addMarker(MarkerOptions().position(location.cordinates))
                    myMarker!!.tag=location.id
                    markers.add(myMarker)
                }
            }


        val findButton=findViewById<ImageButton>(R.id.button1)
        findButton.setOnClickListener(){
            val intent = Intent(this, MainView::class.java)
            startActivity(intent)
            true


        }


    }

    data class MyMarker(
     var id: String,
     var cordinates: LatLng)

}
