package com.example.pokedex

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.databinding.EvolutionViewholderBinding
import com.example.pokedex.extension.capitalize
import com.squareup.picasso.Picasso

class PokemonEvolutionViewHolder(val binding: EvolutionViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        position: Int,
        evolution_list: MutableList<Pair<String, String>>,
        onCallBackClickDetail: (url: String, name: String) -> Unit
    ) {
        val (name, url) = evolution_list[position]
        binding.textNamePokemon.text = name.capitalize()
        setImage(name)
        if (evolution_list.size == position + 1) {
            binding.arrowEvolution.visibility = View.INVISIBLE
        }
        binding.cardEvolutions.setOnClickListener {
            onCallBackClickDetail.invoke(url, name)
        }
    }

    /**
     * Adiciona a imagem do pokemon da fonte "img.pokemondb" em cada item da lista.
     */
    private fun setImage(name: String) {
        val url_image = "https://img.pokemondb.net/artwork/${name.lowercase()}.jpg"
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(binding.imagePokemon)
    }
}
