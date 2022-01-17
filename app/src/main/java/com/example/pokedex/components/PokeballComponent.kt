package com.example.pokedex.components

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.pokedex.PokemonAdapter
import com.example.pokedex.R
import com.example.pokedex.databinding.PokeballComponentBinding

class PokeballComponent(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var pokemon_name: String = ""
        set(value) {
            field = value
            getStatusImagePokeball(value)
        }

    private val binding = PokeballComponentBinding.inflate(LayoutInflater.from(context), this, true)

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

    /**
     * Adiciona os valores booleanos de favoritar.
     */
    private fun setPreferences(name: String, value: Boolean) {
        val sharedPref =
            context.getSharedPreferences(PokemonAdapter.FAVORITES, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(name, value)
            commit()
        }
    }

    /**
     * Faz a animação da Pokebola abrindo ou fechando.
     */
    private fun setAnimation(id_click: Int) {
        binding.imagePokeball.background =
            ContextCompat.getDrawable(context, id_click)
        val frameAnimation: AnimationDrawable =
            binding.imagePokeball.background as AnimationDrawable
        frameAnimation.start()
    }

    /**
     * Callback de click na pokebola para adicionar como favorito ou não.
     */
    fun clickPokeball(): Boolean {
        val is_favorite = getIsFavorite(pokemon_name)
        setPreferences(pokemon_name, !is_favorite)
        val draw =
            if (is_favorite) R.drawable.animation_click_off else R.drawable.animation_click_on
        setAnimation(draw)
        return is_favorite
    }

    fun setData(
        name: String
    ) {
        pokemon_name = name
    }

}
