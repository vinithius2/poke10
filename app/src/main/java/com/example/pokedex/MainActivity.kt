package com.example.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()
    private val LIMIT: Int = 1118

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observerPokemonList()
        with(viewModel) {
            getPokemonList(LIMIT)
        }
    }

    private fun observerPokemonList() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_pokemon)
        val layoutManager = LinearLayoutManager(applicationContext)
        val divider = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(divider)
        viewModel.pokemonList.observe(this, Observer { pokemonList ->
            recyclerView.adapter = PokemonAdapter(pokemonList)
        })
    }
}