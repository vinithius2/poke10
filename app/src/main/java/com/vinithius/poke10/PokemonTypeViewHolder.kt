package com.vinithius.poke10

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.api.data.Default
import com.vinithius.poke10.databinding.TypeViewholderBinding
import com.vinithius.poke10.extension.setDrawableIco

class PokemonTypeViewHolder(val binding: TypeViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, type: Default) {
        binding.textType.text = type.name
        type.name.setDrawableIco(context, binding.imageType)
    }

}
