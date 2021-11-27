package com.example.pokedex

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val layout_data: View = itemView.findViewById(R.id.layout_data)
    val layout_pokeball: View = itemView.findViewById(R.id.layout_pokeball)
    val textView: TextView = itemView.findViewById(R.id.title_pokemon)
    val image_pokemon: ImageView = itemView.findViewById(R.id.image_pokemon)
    val image_pokeball: ImageView = itemView.findViewById(R.id.image_pokeball)

}