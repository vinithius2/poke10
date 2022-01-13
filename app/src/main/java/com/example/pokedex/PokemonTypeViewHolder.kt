package com.example.pokedex

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Type
import com.example.pokedex.databinding.TypeViewholderBinding

class PokemonTypeViewHolder(val binding: TypeViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(type: Type) {
        binding.textType.text = type.type.name
        val imgUri: Uri = Uri.parse("android.resource://com.example.pokedex/drawable/${type.type.name}")
        binding.imageType.setImageURI(imgUri)
    }

}
