package com.vinithius.poke10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinithius.poke10.api.data.Damage
import com.vinithius.poke10.databinding.DamageViewholderBinding

class PokemonDamageAdapter(
    private val pokemon_damage_list: List<Damage>
) : RecyclerView.Adapter<PokemonDamageViewHolder>() {

    private lateinit var view: View
    private lateinit var binding: DamageViewholderBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonDamageViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.damage_viewholder,
            parent,
            false
        )
        view = binding.root
        return PokemonDamageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonDamageViewHolder, position: Int) {
        holder.bind(view.context, pokemon_damage_list[position])
    }

    override fun getItemCount() = pokemon_damage_list.size

}