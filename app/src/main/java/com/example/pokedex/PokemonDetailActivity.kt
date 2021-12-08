package com.example.pokedex

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.db.williamchart.view.HorizontalBarChartView
import com.example.pokedex.api.data.Chain
import com.example.pokedex.api.data.EvolutionChain
import com.example.pokedex.api.data.Pokemon
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonDetailActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
    private lateinit var pokemonTypeAdapter: PokemonTypeAdapter
    private lateinit var pokemonAbilitiesAdapter: PokemonAbilitiesAdapter
    private lateinit var pokemonEvolutionAdapter: PokemonEvolutionAdapter
    private lateinit var recyclerViewType: RecyclerView
    private lateinit var recyclerViewAbilitie: RecyclerView
    private lateinit var recyclerViewEvolution: RecyclerView
    private lateinit var image_pokemon: ImageView
    private lateinit var image_sprite: ImageView
    private lateinit var image_mythical: ImageView
    private lateinit var image_legendary: ImageView
    private lateinit var text_weight: TextView
    private lateinit var text_height: TextView
    private lateinit var text_base_value: TextView
    private lateinit var text_description: TextView
    private lateinit var text_generation_value: TextView
    private lateinit var loading_detail_pokemon: ProgressBar
    private lateinit var scroll_detail_pokemon: ScrollView
    private lateinit var optionsMenu: Menu
    private var light: Palette.Swatch? = null
    private var dominant: Palette.Swatch? = null
    private var dark: Palette.Swatch? = null
    private var pokemon_name = ""
    private var favorite = false
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
        scroll_detail_pokemon = findViewById(R.id.scroll_detail_pokemon)
        loading_detail_pokemon = findViewById(R.id.loading_detail_pokemon)
        image_pokemon = findViewById(R.id.image_pokemon)
        image_sprite = findViewById(R.id.image_sprite)
        image_mythical = findViewById(R.id.image_mythical)
        image_legendary = findViewById(R.id.image_legendary)
        text_height = findViewById(R.id.text_height)
        text_weight = findViewById(R.id.text_weight)
        text_base_value = findViewById(R.id.text_base_value)
        text_description = findViewById(R.id.text_description)
        text_generation_value = findViewById(R.id.text_generation_value)
        observerPokemonLoading()
        observerPokemon()
    }

    private fun changeColorToolBar(color: Int) {
        val window: Window = this.getWindow()
        window.statusBarColor = color
    }

    private fun observerPokemonLoading() {
        viewModel.pokemonDetailLoading.observe(this, { loading ->
            if (loading) {
                scroll_detail_pokemon.visibility = View.INVISIBLE
                loading_detail_pokemon.visibility = View.VISIBLE
            } else {
                scroll_detail_pokemon.visibility = View.VISIBLE
                loading_detail_pokemon.visibility = View.INVISIBLE
            }
        })
    }

    private fun observerPokemon() {
        viewModel.pokemonDetail.observe(this, { pokemon ->
            supportActionBar?.title = pokemon.name.lowercase().replaceFirstChar(Char::uppercase)
            pokemon_name = pokemon.name
            getPreferences(pokemon_name)
            getFavoriteIconStatus()
            val url_image = "https://img.pokemondb.net/artwork/${pokemon.name.lowercase()}.jpg"
            getDominantColor(url_image)
            getPokemonImage(pokemon, url_image)
            getTexts(pokemon)
            getStats(pokemon)
            getTypes(pokemon)
            getAbilities(pokemon)
            getEvolutions(pokemon)
            getIconsMythicalAndLegendary(pokemon)
        })
    }

    private fun getTexts(pokemon: Pokemon) {
        text_height.text = pokemon.height.toString()
        text_weight.text = pokemon.weight.toString()
        text_base_value.text = pokemon.base_experience.toString()
        pokemon.characteristic?.let {
            val description = it.descriptions.filter { it.language.name == "en" }.map { it.description }
            if (description.isNotEmpty()) {
                text_description.text = description.first()
            }
        }
        text_generation_value.text = pokemon.specie.generation.name.split("-").last().uppercase()

        //TODO: Adequar esse de baixo
        pokemon.specie.shape.name
        pokemon.specie.capture_rate
        pokemon.specie.habitat?.name
        pokemon.specie.is_baby
        pokemon.encounters.map { it.location_area.name.replace("-", " ").lowercase().replaceFirstChar(Char::uppercase) }
        pokemon.specie.egg_groups.map { it.name.lowercase().replaceFirstChar(Char::uppercase) }

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

    private fun getTypes(pokemon: Pokemon) {
        recyclerViewType = findViewById(R.id.recycler_view_pokemon_type)
        val layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerViewType.layoutManager = layoutManager
        pokemon.types?.let {
            pokemonTypeAdapter = PokemonTypeAdapter(it)
            recyclerViewType.adapter = pokemonTypeAdapter
        }
    }

    private fun getAbilities(pokemon: Pokemon) {
        recyclerViewAbilitie = findViewById(R.id.recycler_view_pokemon_abilities)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewAbilitie.layoutManager = layoutManager
        pokemon.abilities?.let {
            pokemonAbilitiesAdapter = PokemonAbilitiesAdapter(it, dark, dominant)
            recyclerViewAbilitie.adapter = pokemonAbilitiesAdapter
        }
    }

    private fun getEvolutions(pokemon: Pokemon) {
        recyclerViewEvolution = findViewById(R.id.recycler_view_pokemon_evolutions)
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewEvolution.layoutManager = layoutManager
        pokemon.evolution.let {
            val list_evolutions = getListEvolutions(it)
            pokemonEvolutionAdapter = PokemonEvolutionAdapter(list_evolutions)
            recyclerViewEvolution.adapter = pokemonEvolutionAdapter.apply {
                onCallBackClickDetail = { url ->
                    val bundle = Bundle()
                    bundle.putString("url_detail", url)
                    val intent = Intent(this@PokemonDetailActivity, PokemonDetailActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getIconsMythicalAndLegendary(pokemon: Pokemon) {
        image_mythical.visibility = if (pokemon.specie.is_mythical) View.VISIBLE else View.INVISIBLE
        image_legendary.visibility = if (pokemon.specie.is_legendary) View.VISIBLE else View.INVISIBLE
    }

    private fun getListEvolutions(evolution: EvolutionChain): MutableList<Pair<String, String>> {
        var list_evolutions = mutableListOf<Pair<String, String>>()
        evolution.chain.species?.let {
            list_evolutions.add(Pair(it.name, it.url))
            list_evolutions = getEvolvesTo(evolution.chain, list_evolutions)
        }
        return list_evolutions
    }

    private fun getEvolvesTo(
        chain: Chain,
        list_evolutions: MutableList<Pair<String, String>>
    ): MutableList<Pair<String, String>> {
        if (chain.evolves_to?.size != 0) {
            chain.evolves_to?.forEach { evolve ->
                evolve.species?.let { specie ->
                    list_evolutions.add(Pair(specie.name, specie.url))
                    evolve.evolves_to?.forEach { evolves_to ->
                        getEvolvesTo(evolves_to, list_evolutions)
                    }
                }
            }
        } else {
            chain.species?.let { specie -> list_evolutions.add(Pair(specie.name, specie.url)) }
        }
        return list_evolutions
    }

    private fun getPokemonImage(pokemon: Pokemon, url_image: String) {
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(image_pokemon)
        Picasso.get()
            .load(pokemon.sprites.front_default)
            .into(image_sprite)
    }

    private fun getDominantColor(url: String) {
        Picasso.get().load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    palette = Palette.from(it).generate()
                    palette?.let {
                        light = it.lightVibrantSwatch ?: it.lightMutedSwatch
                        dominant = it.dominantSwatch ?: light
                        dark = it.darkVibrantSwatch ?: it.darkMutedSwatch
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

    /**
     * Adiciona os valores booleanos de favoritar.
     */
    private fun setPreferences(name: String, value: Boolean) {
        val sharedPref = this.getSharedPreferences(PokemonAdapter.FAVORITES, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(name, value)
            commit()
        }
    }

    private fun getPreferences(name: String) {
        val sharedPreferences = this.getSharedPreferences(PokemonAdapter.FAVORITES, Context.MODE_PRIVATE)
        favorite = sharedPreferences.getBoolean(name, false)
    }

    private fun getFavoriteIconStatus() {
        val item = optionsMenu.findItem(R.id.action_favorite)
        val is_favorite =
            ContextCompat.getDrawable(this, R.drawable.ic_baseline_is_favorite_24)
        val is_not_favorite =
            ContextCompat.getDrawable(this, R.drawable.ic_baseline_is_not_favorite_24)
        item.icon = if (favorite) is_favorite else is_not_favorite
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menu?.let {
            optionsMenu = it
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_favorite -> {
                favorite = !favorite
                setPreferences(pokemon_name, favorite)
                getFavoriteIconStatus()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
