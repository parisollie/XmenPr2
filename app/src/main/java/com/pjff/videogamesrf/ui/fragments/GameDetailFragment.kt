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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pjff.videogamesrf.R
import java.text.DecimalFormat

private const val GAME_ID = "game_id"


class GameDetailFragment : Fragment(), OnMapReadyCallback{

    private var gameId: String? = null
    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: GameRepository

    //Para los mapas
    private var lati: Double = 0.0
    private var longi: Double = 0.0
    private val decimalsFormat = DecimalFormat("###,###,###.00")
    private lateinit var map: GoogleMap

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
                                //Video
                                vvVideo.setVideoURI(Uri.parse(response.body()?.vidDesc))
                                val mc = MediaController(requireContext())
                                mc.setAnchorView(vvVideo)
                                vvVideo.setMediaController(mc)
                                vvVideo.start()

                               //Mapa
                                lati = response.body()?.log_Lat!!
                                longi= response.body()?.log_Long!!

                                val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                                mapFragment.getMapAsync(this@GameDetailFragment)
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    private fun createMarker(){
        val coordinates = LatLng(lati, longi)
        val marker = MarkerOptions()
            .position(coordinates)
            .title("Location: (${decimalsFormat.format(lati)}, ${decimalsFormat.format(longi)})")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.wolf))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )
    }
}