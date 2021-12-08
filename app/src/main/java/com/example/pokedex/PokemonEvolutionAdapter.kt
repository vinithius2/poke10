package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PokemonEvolutionAdapter(
    val pokemon_evolution: MutableList<Pair<String, String>>
) : RecyclerView.Adapter<PokemonEvolutionViewHolder>() {

    var onCallBackClickDetail: ((url: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonEvolutionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.evolution_viewholder, parent, false)
        return PokemonEvolutionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonEvolutionViewHolder, position: Int) {
        val (name, url) = pokemon_evolution[position]
        holder.text_name_pokemon.text = name
        setImage(name, holder)
        if (pokemon_evolution.size == position + 1) {
            holder.arrow_evolution.visibility = View.INVISIBLE
        }
        holder.card_evolutions.setOnClickListener {
            pokemon_evolution[position]
            onCallBackClickDetail?.invoke(url)
        }
    }

    override fun getItemCount() = pokemon_evolution.size

    /**
     * Adiciona a imagem do pokemon da fonte "img.pokemondb" em cada item da lista.
     */
    private fun setImage(name: String, holder: PokemonEvolutionViewHolder) {
        val url_image = "https://img.pokemondb.net/artwork/${name.lowercase()}.jpg"
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(holder.image_pokemon)
    }

}