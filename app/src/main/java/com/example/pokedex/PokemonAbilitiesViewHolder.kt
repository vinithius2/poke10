package com.example.pokedex

import android.text.Layout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAbilitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image_status_visibility_abilitie: ImageView = itemView.findViewById(R.id.image_status_visibility_abilitie)
    val text_abilitie: TextView = itemView.findViewById(R.id.text_abilitie)
    val text_abilitie_hidden_text: TextView = itemView.findViewById(R.id.text_abilitie_hidden_text)
    val layout_abilities: View = itemView.findViewById(R.id.layout_abilities)
    val layout_abilities_hidden: View = itemView.findViewById(R.id.layout_abilities_hidden)

}