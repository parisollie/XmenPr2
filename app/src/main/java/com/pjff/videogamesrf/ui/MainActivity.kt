package com.pjff.videogamesrf.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pjff.videogamesrf.R
import com.pjff.videogamesrf.data.GameRepository
import com.pjff.videogamesrf.data.remote.RetrofitHelper
import com.pjff.videogamesrf.data.remote.model.GameDto
import com.pjff.videogamesrf.databinding.ActivityMainBinding
import com.pjff.videogamesrf.ui.fragments.GamesListFragment
import com.pjff.videogamesrf.util.Constants
import com.pjff.videogamesrf.util.XmenSound
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


import com.google.android.gms.maps.GoogleMap
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions




class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var binding: ActivityMainBinding

    //Para Google Maps
    private lateinit var map: GoogleMap

    //Para los permisos,con que pidamos la lozalizacion fina ,la otra se activa
    private var fineLocationPermissionGranted = false

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //Se concedió el permiso
            actionPermissionGranted()
        }else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso para poder ubicar la posición del usuario en el mapa")
                    .setPositiveButton("Entendido"){ _, _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de ubicación se ha negado permanentemente",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }



    private var user: FirebaseUser? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MANDAMOS AL ID DEL MAPA
      /* val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)*/



        //Para la musica
        startService(Intent(this, XmenSound::class.java))

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GamesListFragment())
                .commit()

        }

        //Para la musica
        binding.btnStopSound.setOnClickListener {
            stopService(Intent(this, XmenSound::class.java))
        }
    }

    //MAPAS

    //Cuando el usuario ya tiene permiso
    private fun actionPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Manejar el permiso
            //Pero en este caso ya no es necesario
            return
        }
        //El usuario ya dijo que tienes permiso
        map.isMyLocationEnabled = true


        //Cuando el usuario cambio de posicion

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //AQUI SE MANDA LA UBICAICON
        locationManager.requestLocationUpdates(
            //verme refrescando la pocisio del usuario cada 2 seg
            LocationManager.GPS_PROVIDER,
            2000,
            10f,
            this
        )
    }

    private fun updateOrRequestPermissions() {
        //Revisando el permiso
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        fineLocationPermissionGranted = hasFineLocationPermission

        if (!fineLocationPermissionGranted) {
            //Pedimos el permiso
            permissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            //Tenemos los permisos
            actionPermissionGranted()
        }

    }

    //NUESTRA INTERFAZ
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //Mandamos a llamar las coordenadas
        createMarker()
        //Posicion actual de nuestros usuarios
        updateOrRequestPermissions()
        //dejar marcadores donde quiero ,le doy un click y dejo el marcador donde quiero
        map.setOnMapLongClickListener { position ->
            val marker = MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.school))
            map.addMarker(marker)
        }
    }

    //Creamos un marcador en nuestro map y ponemos la latitud y longitud de nuestro punto
    private fun createMarker(){
        val coordinates = LatLng(19.322326, -99.184592)
        val marker = MarkerOptions()
            .position(coordinates)
            .title("DGTIC-UNAM")
            .snippet("Cursos y diplomados en TIC")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.school))

        map.addMarker(marker)

        //es para que nos ponga una animacion en la ubicacin
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )

    }

    //Es ciclo de vida del usuario ,cuando el usuario nos otorga el permiso
    override fun onRestart() {
        super.onRestart()
        if(!::map.isInitialized) return
        if(!fineLocationPermissionGranted)
            updateOrRequestPermissions()
    }

    //Cuando el usuario se mueve y cambia su localizacion

    override fun onLocationChanged(location: Location) {
        map.clear()
        val coordinates = LatLng(location.latitude, location.longitude)
        val marker = MarkerOptions()
            .position(coordinates)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery))

        map.addMarker(marker)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f))
    }

}