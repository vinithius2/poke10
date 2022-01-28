package com.vinithius.poke10

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinithius.poke10.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
    private var favorites_filter = false
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setConfigActionBar()
        observerPokemonLoading()
        observerPokemonList()
        observerPokemonTextError()
        with(viewModel) {
            getPokemonList(LIMIT)
        }
    }

    /**
     * Notifies the adapter every time it comes back from the details screen, as the status may change
     * from favorites.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        if (::pokemonAdapter.isInitialized) {
            pokemonAdapter.notifyDataSetChanged()
        }
        super.onResume()
    }

    /**
     * Config actionbar for add logo ico, enabled app title and show menu.
     */
    private fun setConfigActionBar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.pokemon_logo_small)
        supportActionBar?.hide()
    }

    /**
     * When request start, loading is visible, however, when finish, loading is invisible.
     */
    private fun observerPokemonLoading() {
        viewModel.pokemonListLoading.observe(this, { loading ->
            if (loading) {
                binding.loadingListPokemon.visibility = View.VISIBLE
            } else {
                binding.loadingListPokemon.visibility = View.GONE
            }
        })
    }

    /**
     * Insert pokemons in adapter and add actions in filters callbacks.
     */
    private fun observerPokemonList() {
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerViewPokemon.layoutManager = layoutManager
        viewModel.pokemonList.observe(this, { pokemonList ->
            if (pokemonList.isNotEmpty()) {
                binding.recyclerViewPokemon.visibility = View.VISIBLE
                supportActionBar?.show()
                pokemonAdapter = PokemonAdapter(pokemonList).apply {
                    with(binding) {
                        onCallBackDataSetFilterRemove = { size, position ->
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, size)
                            if (size == 0) {
                                layoutSemItensNaBusca.visibility = View.VISIBLE
                                msgFavoriteIsEmpty(msgEmptyTitle, msgEmptySubtitle, semItensNaBusca)
                            } else {
                                layoutSemItensNaBusca.visibility = View.INVISIBLE
                            }
                        }
                        onCallBackDataSetFilterSize = { size, is_favorite ->
                            notifyDataSetChanged()
                            if (size > 0) {
                                layoutSemItensNaBusca.visibility = View.INVISIBLE
                            } else {
                                layoutSemItensNaBusca.visibility = View.VISIBLE
                                if (is_favorite) {
                                    msgFavoriteIsEmpty(
                                        msgEmptyTitle,
                                        msgEmptySubtitle,
                                        semItensNaBusca
                                    )
                                } else {
                                    msgFilterIsEmpty(
                                        msgEmptyTitle,
                                        msgEmptySubtitle,
                                        semItensNaBusca
                                    )
                                }
                            }
                        }
                        onCallBackClickDetail = { url ->
                            val bundle = Bundle()
                            bundle.putString("url_detail", url)
                            val intent =
                                Intent(this@MainActivity, PokemonDetailActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                }
                binding.recyclerViewPokemon.adapter = pokemonAdapter
            }
        })
    }

    /**
     * If is error, main layout is hidden and show layout error.
     */
    private fun observerPokemonTextError() {
        viewModel.pokemonTextError.observe(this, { id_text_error ->
            id_text_error?.let {
                supportActionBar?.hide()
                binding.layoutError.root.visibility = View.VISIBLE
                binding.layoutError.textError.text = getString(it)
                binding.recyclerViewPokemon.visibility = View.GONE
                binding.loadingListPokemon.visibility = View.GONE
                binding.layoutError.btnReloading.setOnClickListener {
                    viewModel.getPokemonList(LIMIT)
                    binding.layoutError.root.visibility = View.GONE
                }
            }
        })
    }

    /**
     * Change title, subtitle and background image to notify you when there are no favorites.
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
     * Change the title, subtitle and the screen background image to notify you when there are no pokemons filtered.
     */
    private fun msgFilterIsEmpty(
        title: TextView,
        subtitle: TextView,
        image_itens_empty: ImageView
    ) {
        title.text = getString(R.string.there_are_no_items_in_the_search_title)
        subtitle.text = getString(R.string.there_are_no_items_in_the_search_subtitle)
        image_itens_empty.setImageResource(R.drawable.pokeball_03_gray)
        image_itens_empty.layoutParams.height = 120
        image_itens_empty.layoutParams.width = 100
    }

    /**
     * Add action search in item menu
     */
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

    /**
     * Add ico favorite in actionbar.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                favorites_filter = !favorites_filter
                val is_favorite =
                    ContextCompat.getDrawable(this,
                        R.drawable.ic_baseline_is_favorite_24
                    )
                val is_not_favorite =
                    ContextCompat.getDrawable(this,
                        R.drawable.ic_baseline_is_not_favorite_24
                    )
                item.icon = if (favorites_filter) is_favorite else is_not_favorite
                pokemonAdapter.getFavorites(favorites_filter)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val LIMIT: Int = 1118
    }

}
