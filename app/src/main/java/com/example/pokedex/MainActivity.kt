package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
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
        val divider = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(divider)
        val layout_sem_itens_na_busca = findViewById<LinearLayout>(R.id.layout_sem_itens_na_busca)
        viewModel.pokemonList.observe(this, { pokemonList ->
            pokemonAdapter = PokemonAdapter(pokemonList).apply {
                onCallBackDataSetFilterSize = { size ->
                    notifyDataSetChanged()
                    if(size > 0) {
                        layout_sem_itens_na_busca.visibility = View.INVISIBLE
                    } else {
                        layout_sem_itens_na_busca.visibility = View.VISIBLE
                    }
                }
                onCallBackClickDetail = { url ->
                    val bundle = Bundle()
                    bundle.putString("url_detail", url)
                    val intent = Intent(this@MainActivity, PokemonDetailActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
            recyclerView.adapter = pokemonAdapter
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        item?.let {
            val searchView: SearchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    pokemonAdapter.getFilter().filter(p0)
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }
}