package com.example.pokedex

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.db.williamchart.view.HorizontalBarChartView
import com.example.pokedex.api.data.Chain
import com.example.pokedex.api.data.EvolutionChain
import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.databinding.ActivityPokemonDetailBinding
import com.example.pokedex.extension.*
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonDetailActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
    private var optionsMenu: Menu? = null
    private lateinit var binding: ActivityPokemonDetailBinding
    private var light: Palette.Swatch? = null
    private var dominant: Palette.Swatch? = null
    private var dark: Palette.Swatch? = null
    private var pokemon_name = ""
    private var favorite = false
    private var expand_text_entries = false
    private var palette: Palette? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pokemon_detail)
        binding.textEntries.collapse()
        setConfigActionBar()
        getExtras()
        observerPokemonLoading()
        observerPokemon()
    }

    private fun setConfigActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        supportActionBar?.hide()
    }

    private fun getExtras() {
        intent.extras?.let { bundle ->
            val url = bundle.getString("url_detail").toString()
            val parse = Uri.parse(url)
            parse.pathSegments.getOrNull(3)?.let {
                viewModel.getPokemonDetail(it.toInt())
            }
        }
    }

    private fun changeColorToolBar(color: Int) {
        val window: Window = this.getWindow()
        window.statusBarColor = color
    }

    private fun observerPokemonLoading() {
        viewModel.pokemonDetailLoading.observe(this, { loading ->
            with(binding) {
                if (loading) {
                    scrollDetailPokemon.visibility = View.INVISIBLE
                    loadingDetailPokemon.visibility = View.VISIBLE
                } else {
                    supportActionBar?.show()
                    scrollDetailPokemon.visibility = View.VISIBLE
                    loadingDetailPokemon.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun observerPokemon() {
        viewModel.pokemonDetail.observe(this, { pokemon ->
            supportActionBar?.title = pokemon.name.capitalize()
            pokemon_name = pokemon.name
            getPreferences(pokemon_name)
            getFavoriteIconStatus()
            getDominantColor(pokemon)
            getPokemonImage(pokemon)
            setOutputs(pokemon)
        })
    }

    private fun setOutputs(pokemon: Pokemon) {
        setInfo(pokemon)
        setIsBaby(pokemon)
        setHabitat(pokemon)
        setEncounters(pokemon)
        setEggGroups(pokemon)
        setDamage(pokemon)
        setTextEntries(pokemon)
        setStats(pokemon)
        setTypes(pokemon)
        setAbilities(pokemon)
        setEvolutions(pokemon)
        setIconsMythicalAndLegendary(pokemon)
    }

    private fun setInfo(pokemon: Pokemon) {
        with(binding.includeCardPokemonInfoAndImage) {
            val weight_kl = String.format("%.1f", pokemon.weight?.converterIntToDouble())
            val weight_lbs = String.format("%.1f", pokemon.weight?.convertPounds())
            textWeight.text = getString(R.string.kg_lbs, weight_kl, weight_lbs)
            val height_m = String.format("%.1f", pokemon.height?.converterIntToDouble())
            val height_inc = String.format("%.2f", pokemon.height?.convertInch())
            textHeight.text = getString(R.string.m_inch, height_m, height_inc)
            textCaptureRateValue.text = pokemon.specie?.capture_rate?.toString() ?: "?"
            textShapeValue.text = pokemon.specie?.shape?.name?.capitalize() ?: "?"
            textBaseValue.text = pokemon.base_experience.toString()
            pokemon.characteristic?.let {
                val description =
                    it.descriptions.filter { it.language.name == "en" }.map { it.description }
                if (description.isNotEmpty()) {
                    textDescription.text = description.first()
                }
            }
            textGenerationValue.text =
                pokemon.specie?.generation?.name?.split("-")?.last()?.uppercase() ?: "?"
        }
    }

    private fun setIsBaby(pokemon: Pokemon) {
        pokemon.specie?.let {
            if (it.is_baby) {
                binding.includeCardIsABaby.cardViewIsABaby.visibility = View.VISIBLE
            }
        }
    }

    private fun setHabitat(pokemon: Pokemon) {
        pokemon.specie?.let {
            with(binding) {
                cardHabitat.visibility = View.VISIBLE
                it.habitat?.name?.capitalize()?.let {
                    cardHabitat.setData(dark, dominant, listOf(it))
                }
            }
        }
    }

    private fun setEncounters(pokemon: Pokemon) {
        val encounters = pokemon.encounters.map {
            it.location_area.name.replace("-", " ").capitalize()
        }
        binding.cardEncounters.setData(dark, dominant, encounters)
    }

    private fun setEggGroups(pokemon: Pokemon) {
        pokemon.specie?.let { specie ->
            val eggs = specie.egg_groups.map { it.name.capitalize() }
            binding.cardEggGroups.visibility = View.VISIBLE
            binding.cardEggGroups.setData(dark, dominant, eggs)
        }
    }

    private fun setDamage(pokemon: Pokemon) {
        if (pokemon.damage.isNullOrEmpty()) {
            binding.recyclerViewPokemonDamage.visibility = View.GONE
        } else {
            val layoutManager = LinearLayoutManager(applicationContext)
            binding.recyclerViewPokemonDamage.layoutManager = layoutManager
            binding.recyclerViewPokemonDamage.adapter =
                PokemonDamageAdapter(pokemon.damage)
        }
    }

    private fun setTextEntries(pokemon: Pokemon) {
        pokemon.specie?.let { specie ->
            val text_entries = specie.flavor_text_entries.filter { it.language.name == "en" }
                .groupBy { it.flavor_text }
            var text_output = ""
            for ((text_entrie, version_list) in text_entries) {
                version_list.forEach {
                    text_output += " <b>${it.version.name.capitalize()}</b> |"
                }
                if (text_output.last().toString().equals("|")) {
                    text_output = text_output.dropLast(2)
                }
                text_output += "<p>$text_entrie</p>"
            }
            with(binding) {
                textEntries.text = Html.fromHtml(text_output)
                constraintlayoutTextEntries.visibility = View.VISIBLE
                constraintlayoutTextEntries.setOnClickListener {
                    expand_text_entries =
                        textEntries.getCollapseAndExpand(expand_text_entries, imageviewArrowEntries)
                }
            }
        }
    }

    private fun setStats(pokemon: Pokemon) {
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

    private fun setTypes(pokemon: Pokemon) {
        with(binding.includeCardPokemonInfoAndImage) {
            val layoutManager = GridLayoutManager(applicationContext, 2)
            recyclerViewPokemonType.layoutManager = layoutManager
            pokemon.types?.let { list_type ->
                recyclerViewPokemonType.adapter = PokemonTypeAdapter(list_type.map { it.type })
            }
        }
    }

    private fun setAbilities(pokemon: Pokemon) {
        pokemon.abilities?.let { abilitie ->
            binding.cardAbilities.setData(
                dark,
                dominant,
                abilitie.map { it.ability.name },
                abilitie.map { it.is_hidden })
        }
    }

    private fun setEvolutions(pokemon: Pokemon) {
        with(binding) {
            val layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewPokemonEvolutions.layoutManager = layoutManager
            pokemon.evolution?.let {
                recyclerViewPokemonEvolutions.visibility = View.VISIBLE
                recyclerViewPokemonEvolutions.adapter =
                    PokemonEvolutionAdapter(getListEvolutions(it)).apply {
                        onCallBackClickDetail = { url, name ->
                            if (pokemon_name != name) {
                                val bundle = Bundle()
                                bundle.putString("url_detail", url)
                                val intent =
                                    Intent(
                                        this@PokemonDetailActivity,
                                        PokemonDetailActivity::class.java
                                    )
                                intent.putExtras(bundle)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
            }
        }
    }

    private fun setIconsMythicalAndLegendary(pokemon: Pokemon) {
        pokemon.specie?.let {
            with(binding.includeCardPokemonInfoAndImage) {
                imageMythical.visibility = if (it.is_mythical) View.VISIBLE else View.INVISIBLE
                imageLegendary.visibility = if (it.is_legendary) View.VISIBLE else View.INVISIBLE
            }
        }
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

    private fun getPokemonImage(pokemon: Pokemon) {
        val url_image = "${URL_IMAGE}${pokemon.name.lowercase()}${TYPE_IMAGE}"
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(binding.includeCardPokemonInfoAndImage.imagePokemon)
        Picasso.get()
            .load(pokemon.sprites.front_default)
            .into(binding.includeCardPokemonInfoAndImage.imageSprite)
    }

    private fun getDominantColor(pokemon: Pokemon) {
        val url_image = "${URL_IMAGE}${pokemon.name.lowercase()}${TYPE_IMAGE}"
        Picasso.get().load(url_image).into(object : com.squareup.picasso.Target {
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
        val sharedPreferences =
            this.getSharedPreferences(PokemonAdapter.FAVORITES, Context.MODE_PRIVATE)
        favorite = sharedPreferences.getBoolean(name, false)
    }

    private fun getFavoriteIconStatus() {
        optionsMenu?.let {
            val item = it.findItem(R.id.action_favorite)
            val is_favorite =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_is_favorite_24)
            val is_not_favorite =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_is_not_favorite_24)
            item.icon = if (favorite) is_favorite else is_not_favorite
        }
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

    companion object {
        const val URL_IMAGE = "https://img.pokemondb.net/artwork/"
        const val TYPE_IMAGE = ".jpg"
    }
}
