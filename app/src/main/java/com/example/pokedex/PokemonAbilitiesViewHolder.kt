package com.example.pokedex

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAbilitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image: ImageView = itemView.findViewById(R.id.image_abilitie)
    val textView: TextView = itemView.findViewById(R.id.text_abilitie)

}