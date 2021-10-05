package com.example.pokedex

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView.findViewById(R.id.title_pokemon)

}