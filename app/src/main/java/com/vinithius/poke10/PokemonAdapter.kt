package com.vinithius.poke10

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.api.data.Pokemon
import com.vinithius.poke10.databinding.PokemonViewholderBinding


class PokemonAdapter(
    dataSet: List<Pokemon>
) : RecyclerView.Adapter<PokemonViewHolder>(), Filterable {

    private var dataSetAll: MutableList<Pokemon> = dataSet.toMutableList()
    private var dataSetFilter: MutableList<Pokemon> = dataSet.toMutableList()
    private var dataSetFavorites: MutableList<Pokemon> = dataSet.toMutableList()

    var onCallBackDataSetFilterSize: ((size: Int, is_favorite: Boolean) -> Unit)? = null
    var onCallBackDataSetFilterRemove: ((size: Int, position: Int) -> Unit)? = null
    var onCallBackClickDetail: ((url: String) -> Unit)? = null

    private var char_sequence: CharSequence = ""
    private var favorites_filter = false
    private lateinit var view: View
    private lateinit var binding: PokemonViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pokemon_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(
            dataSetFilter[position],
            onCallBackClickDetail,
            ::callBackRemoveFavorite,
            favorites_filter,
            position,
        )
    }

    /**
     * Callback remove item.
     */
    private fun callBackRemoveFavorite(position: Int) {
        dataSetFilter.removeAt(position)
        onCallBackDataSetFilterRemove?.invoke(dataSetFilter.size, position)
    }

    override fun getItemCount() = dataSetFilter.size

    /**
     * Returns the favorites list.
     */
    fun getFavorites(filter: Boolean) {
        val sharedPreferences = view.context.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        val favorites = sharedPreferences.all.filter { it.value == true }.map { it.key }
        val filteredList = mutableListOf<Pokemon>()
        if (filter) {
            favorites_filter = filter
            dataSetAll.forEach { pokemon ->
                if (pokemon.name.lowercase() in favorites) {
                    filteredList.add(pokemon)
                }
            }
            dataSetFilter.clear()
            dataSetFavorites.clear()
            dataSetFilter.addAll(filteredList)
            dataSetFavorites.addAll(filteredList)
            getFilter().filter(char_sequence)
        } else {
            favorites_filter = filter
            dataSetFilter.clear()
            dataSetFavorites.clear()
            dataSetFilter.addAll(dataSetAll)
            getFilter().filter(char_sequence)
        }
        onCallBackDataSetFilterSize?.invoke(dataSetFilter.size, favorites_filter)
    }

    /**
     * Adds the value in CharSequence to a global variable.
     */
    fun setCharSequence(char: CharSequence?) {
        char?.let {
            char_sequence = it
        }
    }

    override fun getFilter(): Filter {
        return filterVal
    }

    /**
     * Filters the list of pokemons according to the search text.
     */
    private fun filterSearchAndIsFavorite(
        char_sequence: CharSequence?,
        dataSet: MutableList<Pokemon>,
        filteredList: MutableList<Pokemon>
    ): MutableList<Pokemon> {
        if (char_sequence.toString().lowercase().isEmpty()) {
            filteredList.addAll(dataSet)
        } else {
            dataSet.forEach { pokemon ->
                if (pokemon.name.lowercase().contains(char_sequence.toString().lowercase())) {
                    filteredList.add(pokemon)
                }
            }
        }
        return filteredList
    }

    /**
     * Filter() instance that does the search logic.
     */
    private val filterVal = object : Filter() {
        // Run on background thread
        override fun performFiltering(char_sequence: CharSequence?): FilterResults {
            var filteredList = mutableListOf<Pokemon>()

            filteredList = if (favorites_filter) {
                if (dataSetFilter.isEmpty()) {
                    filterSearchAndIsFavorite(char_sequence, dataSetFavorites, filteredList)
                } else {
                    filterSearchAndIsFavorite(char_sequence, dataSetFavorites, filteredList)
                }
            } else {
                filterSearchAndIsFavorite(char_sequence, dataSetAll, filteredList)
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        // Runs on a ui thread
        override fun publishResults(char_sequence: CharSequence?, filter_results: FilterResults?) {
            dataSetFilter.clear()
            filter_results?.values?.let { values ->
                dataSetFilter.addAll(values as Collection<Pokemon>)
                onCallBackDataSetFilterSize?.invoke(dataSetFilter.size, favorites_filter)
            }
        }
    }

    companion object {
        const val FAVORITES = "FAVORITES"
    }

}
