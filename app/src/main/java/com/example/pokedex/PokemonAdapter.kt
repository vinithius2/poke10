package com.example.pokedex

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Pokemon
import com.squareup.picasso.Picasso


class PokemonAdapter(
    dataSet: List<Pokemon>
) : RecyclerView.Adapter<PokemonViewHolder>(), Filterable {

    private var dataSetAll: MutableList<Pokemon> = dataSet.toMutableList()
    private var dataSetFilter: MutableList<Pokemon> = dataSet.toMutableList()
    private var dataSetFavorites: MutableList<Pokemon> = dataSet.toMutableList()

    var onCallBackDataSetFilterSize: ((size: Int) -> Unit)? = null
    var onCallBackDataSetFilterRemove: ((size: Int, position: Int) -> Unit)? = null
    var onCallBackClickDetail: ((url: String, favorites_filter: Boolean) -> Unit)? = null

    private var char_sequence: CharSequence = ""
    private var favorites_filter = false
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_viewholder, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val name = dataSetFilter[position].name
        holder.textView.text = name.lowercase().replaceFirstChar(Char::uppercase)
        setImage(name, holder)
        detail(dataSetFilter[position].url, name, holder)
        getStatusImagePokeball(name, holder)
        clickPokeball(name, position, dataSetFilter[position], holder)
    }

    /**
     * Pega se o pokemon é favorito ou não, caso nulo, retorna falso.
     */
    private fun getIsFavorite(name: String): Boolean {
        val sharedPref = view.context.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(name, false)
    }

    /**
     * Adiciona os valores booleanos de favoritar.
     */
    private fun setPreferences(name: String, value: Boolean) {
        val sharedPref = view.context.getSharedPreferences(FAVORITES, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(name, value)
            commit()
        }
    }

    /**
     * Adiciona a imagem do pokemon da fonte "img.pokemondb" em cada item da lista.
     */
    private fun setImage(name: String, holder: PokemonViewHolder) {
        val url_image = "https://img.pokemondb.net/artwork/${name.lowercase()}.jpg"
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(holder.image_pokemon)
    }

    /**
     * Chamada para a tela de detalhes do Pokemon.
     */
    private fun detail(url: String?, name: String, holder: PokemonViewHolder) {
        holder.layout_data.setOnClickListener {
            val favorite = getIsFavorite(name)
            url?.let { url -> onCallBackClickDetail?.invoke(url, favorite) }
        }
    }

    /**
     * Clica no pokebola para adicionar como favorito ou não.
     */
    private fun clickPokeball(
        name: String,
        position: Int,
        pokemon: Pokemon,
        holder: PokemonViewHolder
    ) {
        holder.layout_pokeball.setOnClickListener {
            val is_favorite = getIsFavorite(name)
            if (is_favorite) {
                setPreferences(name, false)
                setAnimation(R.drawable.animation_click_off, holder)
                if (favorites_filter) {
                    dataSetFilter.remove(pokemon)
                    onCallBackDataSetFilterRemove?.invoke(dataSetFilter.size, position)
                }
            } else {
                setPreferences(name, true)
                setAnimation(R.drawable.animation_click_on, holder)
            }
        }
    }

    /**
     * Faz a animação da Pokebola abrindo ou fechando.
     */
    private fun setAnimation(id_click: Int, holder: PokemonViewHolder) {
        holder.image_pokeball.background =
            ContextCompat.getDrawable(view.context, id_click)
        val frameAnimation: AnimationDrawable =
            holder.image_pokeball.background as AnimationDrawable
        frameAnimation.start()
    }

    /**
     * Muda a imagem da pokebola do item de acordo com o status de favoritos.
     */
    private fun getStatusImagePokeball(name: String, holder: PokemonViewHolder) {
        val is_favorite = getIsFavorite(name)
        if (is_favorite) {
            holder.image_pokeball.background =
                ContextCompat.getDrawable(view.context, R.drawable.pokeball_01)
        } else {
            holder.image_pokeball.background =
                ContextCompat.getDrawable(view.context, R.drawable.pokeball_03_gray)
        }
    }

    override fun getItemCount() = dataSetFilter.size

    /**
     * Retorna a lista de favoritos
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
        onCallBackDataSetFilterSize?.invoke(dataSetFilter.size)
    }

    /**
     * Adiciona o valor em CharSequence em uma variável global.
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
     * Filtra a lista de pokemons de acordo com o texto na busca.
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
     * Instância de Filter() que faz a lógica de busca.
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
                onCallBackDataSetFilterSize?.invoke(dataSetFilter.size)
            }
        }
    }

    companion object {
        const val FAVORITES = "FAVORITES"
    }

}
