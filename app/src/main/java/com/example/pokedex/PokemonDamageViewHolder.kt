package com.example.pokedex

import android.net.Uri
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

class PokemonDamageViewHolder(val binding: DamageViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var action_expand_card: Boolean = false

    fun bind(damage: Damage) {
        with(binding) {
            constraintlayoutCustom.collapse()
            setIco(damage, imageType)
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

    private fun setIco(damage: Damage, imageType: ImageView) {
        val imgUri: Uri = Uri.parse("${URI_BASE}${damage.type.name}")
        imageType.setImageURI(imgUri)
    }

    private fun setTitle(damage: Damage, textView: TextView) {
        textView.text =
            binding.root.context.getString(R.string.title_damage, damage.type.name.capitalize())
    }

    private fun setAdapters(damage: DamageRelations?) {
        damage?.let {
            with(binding) {
                setAdapterDefault(
                    damage.effective_damage_from,
                    titleEffectiveDamageFrom,
                    recyclerEffectiveDamageFrom
                )
                setAdapterDefault(
                    damage.effective_damage_to,
                    titleEffectiveDamageTo,
                    recyclerEffectiveDamageTo
                )
                setAdapterDefault(
                    damage.ineffective_damage_from,
                    titleIneffectiveDamageFrom,
                    recyclerIneffectiveDamageFrom
                )
                setAdapterDefault(
                    damage.ineffective_damage_to,
                    titleIneffectiveDamageTo,
                    recyclerIneffectiveDamageTo
                )
                setAdapterDefault(
                    damage.no_damage_from,
                    titleNoDamageFrom,
                    recyclerNoDamageFrom
                )
                setAdapterDefault(
                    damage.no_damage_to,
                    titleNoDamageTo,
                    recyclerNoDamageTo
                )
            }
        }
    }

    private fun setAdapterDefault(
        data_list: List<Default>,
        view_title: TextView,
        recycler: RecyclerView
    ) {
        if (data_list.isNullOrEmpty()) {
            view_title.visibility = View.GONE
            recycler.visibility = View.GONE
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