package com.example.pokedex

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Pokemon

class PokemonAdapter(
    private val dataSet: List<Pokemon>
): RecyclerView.Adapter<PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_viewholder, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.textView.text = dataSet[position].name.capitalize()
        holder.textView.setOnClickListener {
            Log.i("Click", dataSet[position].url)
        }
    }

    override fun getItemCount() = dataSet.size

}