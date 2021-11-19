package com.example.pokedex

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonDetailActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
    private lateinit var image_pokemon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)
        intent.extras?.let { bundle ->
            val url = bundle.getString("url_detail").toString()
            val parse = Uri.parse(url)
            parse.pathSegments.getOrNull(3)?.let {
                viewModel.getPokemonDetail(it.toInt())
            }
        }
        image_pokemon = findViewById(R.id.image_pokemon)
        observerPokemonLoading()
        observerPokemon()
    }

    private fun observerPokemonLoading() {
        viewModel.pokemonListLoading.observe(this, { loading ->
            val loading_detail_pokemon = findViewById<ProgressBar>(R.id.loading_detail_pokemon)
            if (loading) {
                loading_detail_pokemon.visibility = View.VISIBLE
            } else {
                loading_detail_pokemon.visibility = View.INVISIBLE
            }
        })
    }

    private fun observerPokemon() {
        viewModel.pokemonDetail.observe(this, { pokemon ->
            getActionBar()?.let { it.title = pokemon.name }
            val url_image = "https://img.pokemondb.net/artwork/${pokemon.name.lowercase()}.jpg"
            Picasso.get()
                .load(url_image)
                .error(R.drawable.ic_error_image)
                .into(image_pokemon)
        })
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.getItemId()) {
//            android.R.id.home -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}