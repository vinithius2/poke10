package com.example.pokedex

import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Abilities
import com.example.pokedex.databinding.ShapeDefaultViewholderBinding
import com.example.pokedex.extension.setColorBackground

class PokemonAbilitiesViewHolder(val binding: ShapeDefaultViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(view: View, ability: Abilities, dominant: Palette.Swatch?, dark: Palette.Swatch?) {
        val hidden = ability.is_hidden
        binding.textShape.text = ability.ability.name.lowercase().replaceFirstChar(Char::uppercase)
        binding.layoutShape.setColorBackground(dominant)
        if (hidden) {
            binding.textShapeIcoText.text = view.context.getString(R.string.hidden)
            binding.layoutShapeIco.setColorBackground(dark)
            binding.layoutShapeIco.visibility = View.VISIBLE
        }
    }

}