package com.example.pokedex

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Abilities


class PokemonAbilitiesAdapter(
    val pokemon_abilities_list: List<Abilities>,
    val dark: Palette.Swatch?,
    val dominant: Palette.Swatch?,
) : RecyclerView.Adapter<PokemonAbilitiesViewHolder>() {

    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonAbilitiesViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habilities_viewholder, parent, false)
        return PokemonAbilitiesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonAbilitiesViewHolder, position: Int) {
        val name = pokemon_abilities_list[position].ability.name.lowercase().replaceFirstChar(Char::uppercase)
        val hidden = pokemon_abilities_list[position].is_hidden
        holder.text_abilitie.text = name
        setColorBackground(holder.layout_abilities, dominant)
        if (hidden) {
            setColorBackground(holder.layout_abilities_hidden, dark)
            holder.layout_abilities_hidden.visibility = View.VISIBLE
        }
    }

    private fun setColorBackground(view: View, color: Palette.Swatch?) {
        val background = view.background
        if (background is GradientDrawable) {
            color?.let {
                background.setColor(it.rgb)
            }
        }
    }

    override fun getItemCount() = pokemon_abilities_list.size

}