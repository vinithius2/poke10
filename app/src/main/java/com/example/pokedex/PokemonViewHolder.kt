package com.example.pokedex

import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.databinding.PokemonViewholderBinding
import com.example.pokedex.extension.capitalize
import com.example.pokedex.extension.getIdIntoUrl
import com.squareup.picasso.Callback
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
        setImage(pokemon.name, pokemon.url?.getIdIntoUrl())
    }

    /**
     * Adiciona a imagem do pokemon da fonte "img.pokemondb" em cada item da lista.
     */
    private fun setImage(name: String, id: String?) {
        val first_url_image = "$FIRST_BASE_URL${name.lowercase()}$JPG"
        val second_url_image = "$SECOND_BASE_URL${id.toString()}$PNG"
        Picasso.get()
            .load(first_url_image)
            .into(binding.imagePokemon, object : Callback {
                override fun onSuccess() {
                    // Nothing
                }

                override fun onError(e: Exception?) {
                    Picasso.get()
                        .load(second_url_image)
                        .error(R.drawable.ic_error_image)
                        .into(binding.imagePokemon)
                }
            })
    }

    companion object {
        const val FIRST_BASE_URL = "https://img.pokemondb.net/artwork/"
        const val SECOND_BASE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
        const val PNG = ".png"
        const val JPG = ".jpg"
    }

}
