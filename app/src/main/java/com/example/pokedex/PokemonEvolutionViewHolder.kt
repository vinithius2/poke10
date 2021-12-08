package com.example.pokedex

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class PokemonEvolutionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val card_evolutions: CardView = itemView.findViewById(R.id.card_evolutions)
    val image_pokemon: ImageView = itemView.findViewById(R.id.image_pokemon)
    val arrow_evolution: ImageView = itemView.findViewById(R.id.arrow_evolution)
    val text_name_pokemon: TextView = itemView.findViewById(R.id.text_name_pokemon)
}
