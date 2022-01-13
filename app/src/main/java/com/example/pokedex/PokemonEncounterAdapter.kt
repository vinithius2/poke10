package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ShapeDefaultViewholderBinding

class PokemonEncounterAdapter(
    val pokemon_encounter_list: List<String>,
    val dark: Palette.Swatch?,
    val dominant: Palette.Swatch?,
) : RecyclerView.Adapter<PokemonEncounterViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: ShapeDefaultViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonEncounterViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shape_default_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonEncounterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonEncounterViewHolder, position: Int) {
        holder.bind(
            view,
            pokemon_encounter_list[position],
            dominant,
            dark
        )
    }

    override fun getItemCount() = pokemon_encounter_list.size

}