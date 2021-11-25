package com.example.pokedex

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val layout: View = itemView.findViewById(R.id.viewholder_layout)
    val textView: TextView = itemView.findViewById(R.id.title_pokemon)
    val image: ImageView = itemView.findViewById(R.id.image_pokemon)

}