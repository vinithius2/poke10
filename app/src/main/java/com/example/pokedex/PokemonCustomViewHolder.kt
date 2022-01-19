package com.example.pokedex

import android.graphics.drawable.Drawable
import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.ShapeDefaultViewholderBinding
import com.example.pokedex.extension.capitalize
import com.example.pokedex.extension.setColorBackground

class PokemonCustomViewHolder(val binding: ShapeDefaultViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: String,
        dominant: Palette.Swatch?,
        dark: Palette.Swatch?,
        drawable: Drawable?,
        title_item_right: String?,
        hidden: Boolean?
    ) {
        binding.textShape.text = item.capitalize()
        binding.layoutShape.setColorBackground(dominant)
        binding.layoutShapeIco.setColorBackground(dark)
        drawable?.let {
            title_item_right?.let {
                binding.textShapeIcoText.text = it
                binding.textShapeIcoText.visibility = View.VISIBLE
            }
            binding.imageShapeIcoText.background = it
            if (hidden != null && hidden == false) {
                binding.layoutShapeIco.visibility = View.GONE
            } else {
                binding.layoutShapeIco.visibility = View.VISIBLE
            }
        }
    }
}
