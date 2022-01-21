package com.example.pokedex

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Default
import com.example.pokedex.databinding.TypeViewholderBinding
import com.example.pokedex.extension.setDrawableIco

class PokemonTypeViewHolder(val binding: TypeViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, type: Default) {
        binding.textType.text = type.name
        type.name.setDrawableIco(context, binding.imageType)
    }

}
