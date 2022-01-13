package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Abilities
import com.example.pokedex.databinding.ShapeDefaultViewholderBinding


class PokemonAbilitiesAdapter(
    val pokemon_abilities_list: List<Abilities>,
    val dark: Palette.Swatch?,
    val dominant: Palette.Swatch?,
) : RecyclerView.Adapter<PokemonAbilitiesViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: ShapeDefaultViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAbilitiesViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shape_default_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonAbilitiesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonAbilitiesViewHolder, position: Int) {
        holder.bind(
            view,
            pokemon_abilities_list[position],
            dominant,
            dark
        )
    }

    override fun getItemCount() = pokemon_abilities_list.size

}