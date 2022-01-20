package com.example.pokedex

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.api.data.Damage
import com.example.pokedex.api.data.DamageRelations
import com.example.pokedex.api.data.Default
import com.example.pokedex.databinding.DamageViewholderBinding
import com.example.pokedex.extension.capitalize
import com.example.pokedex.extension.collapse
import com.example.pokedex.extension.getCollapseAndExpand
import com.example.pokedex.extension.setDrawableIco

class PokemonDamageViewHolder(val binding: DamageViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var action_expand_card: Boolean = false

    fun bind(context: Context, damage: Damage) {
        with(binding) {
            constraintlayoutCustom.collapse()
            setIco(context, damage, imageType)
            setTitle(damage, titleDamage)
            cardviewCustom.setOnClickListener {
                action_expand_card =
                    constraintlayoutCustom.getCollapseAndExpand(
                        action_expand_card,
                        binding.imageviewArrowCustom
                    )
            }
        }
        setAdapters(damage.damage_relations)
    }

    /**
     * Add drawable damage left ico in ImageView.
     */
    private fun setIco(context: Context, damage: Damage, imageType: ImageView) {
        damage.type.name.setDrawableIco(context, imageType)
    }

    /**
     * Add text damage.
     */
    private fun setTitle(damage: Damage, textView: TextView) {
        textView.text =
            binding.root.context.getString(R.string.title_damage, damage.type.name.capitalize())
    }

    /**
     * Set all damage adapters.
     */
    private fun setAdapters(damage: DamageRelations?) {
        damage?.let {
            with(binding) {
                setAdapterDefault(
                    damage.effective_damage_from,
                    titleEffectiveDamageFrom,
                    recyclerEffectiveDamageFrom,
                    icoEffectiveDamageFrom
                )
                setAdapterDefault(
                    damage.effective_damage_to,
                    titleEffectiveDamageTo,
                    recyclerEffectiveDamageTo,
                    icoEffectiveDamageTo
                )
                setAdapterDefault(
                    damage.ineffective_damage_from,
                    titleIneffectiveDamageFrom,
                    recyclerIneffectiveDamageFrom,
                    icoIneffectiveDamageFrom
                )
                setAdapterDefault(
                    damage.ineffective_damage_to,
                    titleIneffectiveDamageTo,
                    recyclerIneffectiveDamageTo,
                    icoIneffectiveDamageTo
                )
                setAdapterDefault(
                    damage.no_damage_from,
                    titleNoDamageFrom,
                    recyclerNoDamageFrom,
                    icoNoDamageFrom
                )
                setAdapterDefault(
                    damage.no_damage_to,
                    titleNoDamageTo,
                    recyclerNoDamageTo,
                    icoNoDamageTo
                )
            }
        }
    }

    /**
     * Adapter default for all damage.
     */
    private fun setAdapterDefault(
        data_list: List<Default>,
        view_title: TextView,
        recycler: RecyclerView,
        ico: ImageView
    ) {
        if (data_list.isNullOrEmpty()) {
            view_title.visibility = View.GONE
            recycler.visibility = View.GONE
            ico.visibility = View.GONE
        } else {
            val layoutManager = GridLayoutManager(binding.root.context, COUNT_ITENS)
            recycler.layoutManager = layoutManager
            recycler.adapter = PokemonTypeAdapter(data_list)
        }
    }

    companion object {
        const val COUNT_ITENS = 4
        const val URI_BASE = "android.resource://com.example.pokedex/drawable/"
    }

}
