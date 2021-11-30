package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
    private var favorites_filter = false
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var recyclerView: RecyclerView
    private val LIMIT: Int = 1118

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observerPokemonLoading()
        observerPokemonList()
        with(viewModel) {
            getPokemonList(LIMIT)
        }
    }

    /**
     * Notifica o adapter todas vez que voltar da tela de detalhes, pois pode haver mudanças no status
     * de favoritos.
     */
    override fun onResume() {
        if (::pokemonAdapter.isInitialized) {
            pokemonAdapter.notifyDataSetChanged()
        }
        super.onResume()
    }

    private fun observerPokemonLoading() {
        viewModel.pokemonListLoading.observe(this, { loading ->
            val loading_list_pokemon = findViewById<ProgressBar>(R.id.loading_list_pokemon)
            if (loading) {
                loading_list_pokemon.visibility = View.VISIBLE
            } else {
                loading_list_pokemon.visibility = View.INVISIBLE
            }
        })
    }

    private fun observerPokemonList() {
        recyclerView = findViewById(R.id.recycler_view_pokemon)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager

        val layout_sem_itens_na_busca = findViewById<LinearLayout>(R.id.layout_sem_itens_na_busca)
        val msg_empty_title = findViewById<TextView>(R.id.msg_empty_title)
        val msg_empty_subtitle = findViewById<TextView>(R.id.msg_empty_subtitle)
        val image_itens_empty = findViewById<ImageView>(R.id.sem_itens_na_busca)

        viewModel.pokemonList.observe(this, { pokemonList ->
            pokemonAdapter = PokemonAdapter(pokemonList).apply {
                onCallBackDataSetFilterRemove = { size, position ->
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, size)
                    if (size == 0) {
                        layout_sem_itens_na_busca.visibility = View.VISIBLE
                        msgFavoriteIsEmpty(msg_empty_title, msg_empty_subtitle, image_itens_empty)
                    } else {
                        layout_sem_itens_na_busca.visibility = View.INVISIBLE
                    }
                }
                onCallBackDataSetFilterSize = { size ->
                    notifyDataSetChanged()
                    if (size > 0) {
                        layout_sem_itens_na_busca.visibility = View.INVISIBLE
                    } else {
                        layout_sem_itens_na_busca.visibility = View.VISIBLE
                        if (favorites_filter) {
                            msgFavoriteIsEmpty(
                                msg_empty_title,
                                msg_empty_subtitle,
                                image_itens_empty
                            )
                        } else {
                            msgFilterIsEmpty(msg_empty_title, msg_empty_subtitle, image_itens_empty)
                        }
                    }
                }
                onCallBackClickDetail = { url, favorite ->
                    val bundle = Bundle()
                    bundle.putString("url_detail", url)
                    bundle.putBoolean("favorite", favorite)
                    val intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
            recyclerView.adapter = pokemonAdapter
        })
    }

    /**
     * Altera no título, subtítulo e a imagem do fundo da tela pata notificar quando não há favoritos
     */
    private fun msgFavoriteIsEmpty(
        title: TextView,
        subtitle: TextView,
        image_itens_empty: ImageView
    ) {
        title.text = getString(R.string.favorite_pokemon_is_empty_title)
        subtitle.text = getString(R.string.favorite_pokemon_is_empty_subtitle)
        image_itens_empty.setImageResource(R.drawable.pokeball_03_gray)
        image_itens_empty.layoutParams.height = 120
        image_itens_empty.layoutParams.width = 100
    }

    /**
     * Altera no título, subtítulo e a imagem do fundo da tela pata notificar quando não há pokemons filtrados
     */
    private fun msgFilterIsEmpty(
        title: TextView,
        subtitle: TextView,
        image_itens_empty: ImageView
    ) {
        title.text = getString(R.string.there_are_no_items_in_the_search_title)
        subtitle.text = getString(R.string.there_are_no_items_in_the_search_subtitle)
        image_itens_empty.setImageResource(R.drawable.ic_baseline_assignment_late_24)
        image_itens_empty.layoutParams.height = 120
        image_itens_empty.layoutParams.width = 100
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        item?.let {
            val searchView: SearchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(text_search: String?): Boolean {
                    pokemonAdapter.setCharSequence(text_search)
                    pokemonAdapter.getFilter().filter(text_search)
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                favorites_filter = !favorites_filter
                val is_favorite =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_is_favorite_24)
                val is_not_favorite =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_is_not_favorite_24)
                item.icon = if (favorites_filter) is_favorite else is_not_favorite
                pokemonAdapter.getFavorites(favorites_filter)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
