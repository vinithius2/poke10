package com.example.pokedex

import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ShapeDefaultViewholderBinding
import com.example.pokedex.extension.capitalize
import com.example.pokedex.extension.setColorBackground

class PokemonEncounterViewHolder(val binding: ShapeDefaultViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(view: View, encounter: String, dominant: Palette.Swatch?, dark: Palette.Swatch?) {
        binding.textShape.text = encounter.capitalize()
        binding.layoutShape.setColorBackground(dominant)
        binding.layoutShapeIco.setColorBackground(dark)
        binding.imageShapeIcoText.background =
            view.context.getDrawable(R.drawable.ic_baseline_location_on_24)
        binding.layoutShapeIco.visibility = View.VISIBLE
    }

}
