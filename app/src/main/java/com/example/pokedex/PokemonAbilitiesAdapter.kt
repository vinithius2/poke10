package com.example.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Abilities

class PokemonAbilitiesAdapter(
    val pokemon_abilities_list: List<Abilities>
) : RecyclerView.Adapter<PokemonAbilitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAbilitiesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habilities_viewholder, parent, false)
        return PokemonAbilitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonAbilitiesViewHolder, position: Int) {
        val name = pokemon_abilities_list[position].ability.name.lowercase().replaceFirstChar(Char::uppercase)
        val hidden = pokemon_abilities_list[position].is_hidden
        holder.textView.text = name
        if (hidden) {
            holder.image.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
    }

    override fun getItemCount() = pokemon_abilities_list.size

}