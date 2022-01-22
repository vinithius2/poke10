package com.vinithius.poke10

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.databinding.ShapeDefaultViewholderBinding

class PokemonCustomAdapter(
    private val item_list: List<String>,
    private val dark: Palette.Swatch?,
    private val dominant: Palette.Swatch?,
    private val drawable: Drawable?,
    private val title_item_right: String?,
    private val hidden_list: List<Boolean>?,
) : RecyclerView.Adapter<PokemonCustomViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: ShapeDefaultViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCustomViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shape_default_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonCustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonCustomViewHolder, position: Int) {
        val hidden: Boolean? = if (hidden_list.isNullOrEmpty()) null else hidden_list[position]
        holder.bind(
            item_list[position],
            dominant,
            dark,
            drawable,
            title_item_right,
            hidden
        )
    }

    override fun getItemCount() = item_list.size

}
