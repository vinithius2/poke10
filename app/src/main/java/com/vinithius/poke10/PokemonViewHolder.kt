package com.vinithius.poke10

import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.api.data.Pokemon
import com.vinithius.poke10.databinding.PokemonViewholderBinding
import com.vinithius.poke10.extension.capitalize
import com.vinithius.poke10.extension.setPokemonImage


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
        setImage(pokemon)
    }

    /**
     * Adiciona a imagem do pokemon da fonte "img.pokemondb" em cada item da lista.
     */
    private fun setImage(pokemon: Pokemon) {
        binding.imagePokemon.setPokemonImage(pokemon) {}
    }
}
