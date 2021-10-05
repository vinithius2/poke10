package com.example.pokedex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PokemonViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observerPokemonList()
    }

    fun observerPokemonList() {
        viewModel.pokemonList.observe(this, Observer {
            Log.i("pokemonList", it.toString())
        })
    }
}