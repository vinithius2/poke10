package com.example.pokedex

import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.databinding.PokemonViewholderBinding
import com.example.pokedex.extension.capitalize
import com.squareup.picasso.Picasso

class PokemonViewHolder(val binding: PokemonViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        pokemon: Pokemon,
        onCallBackClickDetail: ((url: String) -> Unit)?,
        callBackRemoveFavorite: ((position: Int) -> Unit)?,
        favorites_filter: Boolean,
        position: Int
    ) {
        binding.titlePokemon.text = pokemon.name.capitalize()
        binding.layoutData.setOnClickListener {
            pokemon.url?.let { url -> onCallBackClickDetail?.invoke(url) }
        }
        with(binding.imgPokeball) {
            setData(pokemon.name)
            setOnClickListener {
                if (clickPokeball()) {
                    if (favorites_filter) {
                        callBackRemoveFavorite?.invoke(position)
                    }
                }
            }
        }
        setImage(pokemon.name)
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
