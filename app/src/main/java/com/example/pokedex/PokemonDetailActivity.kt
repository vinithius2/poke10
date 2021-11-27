package com.example.pokedex

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.db.williamchart.view.HorizontalBarChartView
import com.example.pokedex.api.data.Pokemon
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonDetailActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
    private lateinit var pokemonTypeAdapter: PokemonTypeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var image_pokemon: ImageView
    private lateinit var image_sprite: ImageView
    private lateinit var text_weight: TextView
    private lateinit var text_height: TextView
    private var palette: Palette? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        intent.extras?.let { bundle ->
            val url = bundle.getString("url_detail").toString()
            val parse = Uri.parse(url)
            parse.pathSegments.getOrNull(3)?.let {
                viewModel.getPokemonDetail(it.toInt())
            }
        }
        image_pokemon = findViewById(R.id.image_pokemon)
        image_sprite = findViewById(R.id.image_sprite)
        text_height = findViewById(R.id.text_height)
        text_weight = findViewById(R.id.text_weight)
        observerPokemonLoading()
        observerPokemon()
    }

    private fun changeColorToolBar(color: Int) {
        val window: Window = this.getWindow()
        window.statusBarColor = color
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
            supportActionBar?.title = pokemon.name.lowercase().replaceFirstChar(Char::uppercase)
            val url_image = "https://img.pokemondb.net/artwork/${pokemon.name.lowercase()}.jpg"
            getDominantColor(url_image)
            getPokemonImage(pokemon, url_image)
            getStats(pokemon)
            getTexts(pokemon)

            recyclerView = findViewById(R.id.recycler_view_pokemon_type)
            val layoutManager = GridLayoutManager(applicationContext, 2)
            recyclerView.layoutManager = layoutManager
            pokemon.types?.let {
                pokemonTypeAdapter = PokemonTypeAdapter(it)
                recyclerView.adapter = pokemonTypeAdapter
            }
        })
    }

    private fun getTexts(pokemon: Pokemon) {
        text_height.text = pokemon.height.toString()
        text_weight.text = pokemon.weight.toString()
    }

    private fun getStats(pokemon: Pokemon) {
        val myChart: HorizontalBarChartView = findViewById(R.id.myChart)
        val mySet = mutableSetOf<Pair<String, Float>>()
        pokemon.stats?.forEach { stat ->
            mySet.add(
                Pair(
                    "${stat.stat.name.uppercase()} (${stat.base_stat})",
                    stat.base_stat.toFloat()
                )
            )
        }
        palette?.let {
            val color = it.dominantSwatch
            color?.let {
                myChart.barsColor = it.rgb
            }
        }
        myChart.labelsFormatter = { it.toInt().toString() }
        myChart.show(mySet.toList())
    }

    private fun getPokemonImage(pokemon: Pokemon, url_image: String) {
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(image_pokemon)
        Picasso.get()
            .load(pokemon.sprites.front_default)
            .error(R.drawable.ic_error_image)
            .into(image_sprite)
    }

    private fun getDominantColor(url: String) {
        Picasso.get().load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    palette = Palette.from(it).generate()
                    palette?.let {
                        val light = it.lightVibrantSwatch ?: it.lightMutedSwatch
                        val dominant = it.dominantSwatch ?: light
                        val main_layout: View = findViewById(R.id.main_layout)
                        dominant?.let { color ->
                            changeColorToolBar(color.rgb)
                            val hexColorAlpha =
                                java.lang.String.format("#4D%06X", 0xFFFFFF and color.rgb)
                            val hexColor =
                                java.lang.String.format("#D9%06X", 0xFFFFFF and color.rgb)
                            val colorDrawableActionBar = ColorDrawable(Color.parseColor(hexColor))
                            val colorDrawableLayoult =
                                ColorDrawable(Color.parseColor(hexColorAlpha))
                            supportActionBar?.setBackgroundDrawable(colorDrawableActionBar)
                            main_layout.background = colorDrawableLayoult
                        }
                    }
                }
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}