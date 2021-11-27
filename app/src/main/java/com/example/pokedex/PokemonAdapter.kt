package com.example.pokedex

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
        detail(dataSetFilter[position].url, holder)
        getStatusPokeball(name, holder)
        clickPokeball(name, holder)
    }

    private fun getIsFavorite(name: String): Boolean {
        val sharedPref = view.context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(name, false)
    }

    private fun setImage(name: String, holder: PokemonViewHolder) {
        val url_image = "https://img.pokemondb.net/artwork/${name.lowercase()}.jpg"
        Picasso.get()
            .load(url_image)
            .error(R.drawable.ic_error_image)
            .into(holder.image_pokemon)
    }

    private fun detail(url: String?, holder: PokemonViewHolder) {
        holder.layout_data.setOnClickListener {
            url?.let { url -> onCallBackClickDetail?.invoke(url) }
        }
    }

    private fun clickPokeball(name: String, holder: PokemonViewHolder) {
        holder.layout_pokeball.setOnClickListener {
            val is_favorite = getIsFavorite(name)
            if (is_favorite) {
                setPreferences(name, false)
                holder.image_pokeball.background = ContextCompat.getDrawable(view.context, R.drawable.animation_click_off)
                val frameAnimation: AnimationDrawable = holder.image_pokeball.background as AnimationDrawable
                frameAnimation.start()
            } else {
                Toast.makeText(view.context, "${name.lowercase().replaceFirstChar(Char::uppercase)} capturado! ", Toast.LENGTH_SHORT).show()
                setPreferences(name, true)
                holder.image_pokeball.background = ContextCompat.getDrawable(view.context, R.drawable.animation_click_on)
                val frameAnimation: AnimationDrawable = holder.image_pokeball.background as AnimationDrawable
                frameAnimation.start()
            }
        }
    }

    private fun getStatusPokeball(name: String, holder: PokemonViewHolder) {
        val is_favorite = getIsFavorite(name)
        if (is_favorite) {
            holder.image_pokeball.background = ContextCompat.getDrawable(view.context, R.drawable.pokeball_01)
        } else {
            holder.image_pokeball.background = ContextCompat.getDrawable(view.context, R.drawable.pokeball_03_gray)
        }
    }

    private fun setPreferences(name: String, value: Boolean) {
        val sharedPref = view.context.getSharedPreferences(name, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(name, value)
            commit()
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