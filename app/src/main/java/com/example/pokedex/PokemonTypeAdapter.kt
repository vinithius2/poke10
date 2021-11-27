package com.example.pokedex

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Type

class PokemonTypeAdapter(
    val pokemon_type_list: List<Type>
) : RecyclerView.Adapter<PokemonTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTypeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.type_viewholder, parent, false)
        return PokemonTypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonTypeViewHolder, position: Int) {
        val type = pokemon_type_list[position].type.name
        holder.textView.text = type
        val imgUri: Uri = Uri.parse("android.resource://com.example.pokedex/drawable/$type")
        holder.image.setImageURI(imgUri)
    }

    override fun getItemCount() = pokemon_type_list.size

}