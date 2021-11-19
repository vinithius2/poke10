package com.example.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Pokemon
import com.squareup.picasso.Picasso

class PokemonAdapter(
    dataSet: List<Pokemon>
): RecyclerView.Adapter<PokemonViewHolder>(), Filterable {

    private var dataSetAll: MutableList<Pokemon> = dataSet.toMutableList()
    private var dataSetFilter: MutableList<Pokemon> = dataSet.toMutableList()
    var onCallBackDataSetFilterSize: ((size: Int) -> Unit)? = null
    var onCallBackClickDetail: ((url: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_viewholder, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val name = dataSetFilter[position].name
        // capitalize deprecated =/
        holder.textView.text = name.lowercase().replaceFirstChar(Char::uppercase)
        val url_image = "https://img.pokemondb.net/artwork/${name.lowercase()}.jpg"
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(holder.image)
        holder.textView.setOnClickListener {
            dataSetFilter[position].url?.let { url -> onCallBackClickDetail?.invoke(url) }
        }
        holder.image.setOnClickListener {
            dataSetFilter[position].url?.let { url -> onCallBackClickDetail?.invoke(url) }
        }
    }

    override fun getItemCount() = dataSetFilter.size

    override fun getFilter(): Filter {
        return filterVal
    }

    private val filterVal = object : Filter() {
        // Run on background thread
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Pokemon>()
            if (p0.toString().lowercase().isEmpty()) {
                filteredList.addAll(dataSetAll)
            } else {
                dataSetAll.forEach { pokemon ->
                    if (pokemon.name.lowercase().contains(p0.toString().lowercase())) {
                        filteredList.add(pokemon)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        // Runs on a ui thread
        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            dataSetFilter.clear()
            p1?.values?.let { values ->
                dataSetFilter.addAll(values as Collection<Pokemon>)
                onCallBackDataSetFilterSize?.invoke(dataSetFilter.size)
            }
        }
    }

}