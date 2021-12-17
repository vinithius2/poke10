package com.example.pokedex

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.databinding.PokemonViewholderBinding
import com.squareup.picasso.Picasso

class PokemonViewHolder(val binding: PokemonViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        position: Int,
        pokemon: Pokemon,
        onCallBackClickDetail: ((url: String) -> Unit)?,
        clickPokeball: (position: Int, pokemon: Pokemon) -> Unit
    ) {
        binding.titlePokemon.text = pokemon.name.lowercase().replaceFirstChar(Char::uppercase)
        binding.layoutData.setOnClickListener {
            pokemon.url?.let { url -> onCallBackClickDetail?.invoke(url) }
        }
        binding.layoutPokeball.setOnClickListener {

        }
        setImage(pokemon.name)
        getStatusImagePokeball(pokemon.name)
        clickPokeball(position, pokemon)
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

    /**
     * Muda a imagem da pokebola do item de acordo com o status de favoritos.
     */
    private fun getStatusImagePokeball(name: String) {
        val is_favorite = getIsFavorite(name)
        if (is_favorite) {
            binding.imagePokeball.background =
                ContextCompat.getDrawable(binding.root.context, R.drawable.pokeball_01)
        } else {
            binding.imagePokeball.background =
                ContextCompat.getDrawable(binding.root.context, R.drawable.pokeball_03_gray)
        }
    }

    /**
     * Pega se o pokemon é favorito ou não, caso nulo, retorna falso.
     */
    private fun getIsFavorite(name: String): Boolean {
        val sharedPref = binding.root.context.getSharedPreferences(
            PokemonAdapter.FAVORITES,
            Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean(name, false)
    }

}