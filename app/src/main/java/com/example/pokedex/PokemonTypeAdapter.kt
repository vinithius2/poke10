package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Default
import com.example.pokedex.databinding.TypeViewholderBinding

class PokemonTypeAdapter(
    private val pokemon_type_list: List<Default>
) : RecyclerView.Adapter<PokemonTypeViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: TypeViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTypeViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.type_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonTypeViewHolder, position: Int) {
        holder.bind(pokemon_type_list[position])
    }

    override fun getItemCount() = pokemon_type_list.size

}
