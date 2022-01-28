package com.vinithius.poke10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.databinding.EvolutionViewholderBinding

class PokemonEvolutionAdapter(
    private val pokemon_evolution: MutableList<Pair<String, String>>
) : RecyclerView.Adapter<PokemonEvolutionViewHolder>() {

    var onCallBackClickDetail: ((url: String, name: String) -> Unit)? = null
    private lateinit var view: View
    private lateinit var binding: EvolutionViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonEvolutionViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.evolution_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonEvolutionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonEvolutionViewHolder, position: Int) {
        holder.bind(position, pokemon_evolution, ::onCallBack)
    }

    /**
     * Action for details screen passing pokemon name.
     */
    private fun onCallBack(url: String, name: String) {
        onCallBackClickDetail?.invoke(url, name)
    }

    override fun getItemCount() = pokemon_evolution.size

}
