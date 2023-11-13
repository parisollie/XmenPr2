package com.pjff.videogamesrf.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.pjff.videogamesrf.application.VideoGamesRFApp
import com.pjff.videogamesrf.data.GameRepository
import com.pjff.videogamesrf.data.remote.model.GameDetailDto
import com.pjff.videogamesrf.databinding.FragmentGameDetailBinding
import com.pjff.videogamesrf.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.MediaController

import androidx.appcompat.app.AppCompatActivity

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
import com.pjff.videogamesrf.R

private const val GAME_ID = "game_id"


class GameDetailFragment : Fragment() {

    private var gameId: String? = null
    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: GameRepository
    private lateinit var map: GoogleMap
    //Mapas
    //private lateinit var binding: ActivityMainBinding

    //Para Google Maps
   /* private lateinit var map: GoogleMap

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
    }*/











    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            gameId = args.getString(GAME_ID)

            Log.d(Constants.LOGTAG, "Id recibido: $gameId")

            repository = (requireActivity().application as VideoGamesRFApp).repository

            lifecycleScope.launch {

                gameId?.let { id ->

                    val call: Call<GameDetailDto> = repository.getGameDetailApiary(id)

                    call.enqueue(object: Callback<GameDetailDto>{
                        override fun onResponse(
                            call: Call<GameDetailDto>,
                            response: Response<GameDetailDto>
                        ) {


                            binding.apply {
                                pbLoading.visibility = View.GONE

                                tvTitle.text = response.body()?.name

                                tvLongDesc.text = response.body()?.longDesc

                                tvLevel.text = response.body()?.levelX

                                tvFirstTime.text = response.body()?.first_Time

                                tvPower.text = response.body()?.power

                                Glide.with(requireContext())
                                    .load(response.body()?.image)
                                    .into(ivImage)

                                vvVideo.setVideoURI(Uri.parse(response.body()?.vidDesc))

                                val mc = MediaController(requireContext())
                                mc.setAnchorView(vvVideo)
                                vvVideo.setMediaController(mc)
                                vvVideo.start()

                            }

                        }

                        override fun onFailure(call: Call<GameDetailDto>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE

                            Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()
                        }

                    })
                }

            }

        }
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(gameId: String) =
            GameDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(GAME_ID, gameId)
                }
            }
    }










}