package com.pjff.videogamesrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pjff.videogamesrf.data.remote.model.GameDto
import com.pjff.videogamesrf.databinding.GameElementBinding

class GamesAdapter(
    private val games: List<GameDto>,
    private val onGameClicked: (GameDto) -> Unit
): RecyclerView.Adapter<GamesAdapter.ViewHolder>() {

    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){

        val ivThumbnail = binding.ivThumbnail

        fun bind(game: GameDto){
            binding.tvTitle.text = game.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]

        holder.bind(game)

        //Con Picasso
        /*Picasso.get()
            .load(game.thumbnail)
            .into(holder.ivThumbnail)*/

        //Con Glide
        Glide.with(holder.itemView.context)
            .load(game.thumbnail)
            .into(holder.ivThumbnail)

        //Procesamiento del clic al elemento
        holder.itemView.setOnClickListener {
            onGameClicked(game)
        }
    }
}