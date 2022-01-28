package com.vinithius.poke10.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinithius.poke10.PokemonCustomAdapter
import com.vinithius.poke10.R
import com.vinithius.poke10.databinding.PokemonCustomListItemComponentBinding
import com.vinithius.poke10.extension.collapse
import com.vinithius.poke10.extension.getCollapseAndExpand


class PokemonCustomListItemComponent(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    private var dark_card: Palette.Swatch? = null
    private var dominant_card: Palette.Swatch? = null
    private var drawable_ico: Drawable? = null

    private var action_expand_card: Boolean = false
    private var expand_card: Boolean = false
        set(value) {
            field = value
            setVisible(value)
            if (value) {
                binding.recyclerViewPokemonCustom.collapse()
            }
        }

    private var title_item_right: String? = null
    private var title_card: String? = null
        set(value) = setTitle(context, value)

    private var hidden_list_card: List<Boolean>? = null
    private var data_list_card: List<String> = listOf()
        set(value) = setAdapter(value)

    private val binding =
        PokemonCustomListItemComponentBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        attrs?.let { attributeSet ->
            val attribute = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.CardWithRecyclerViewComponent
            )
            drawable_ico =
                attribute.getDrawable(R.styleable.CardWithRecyclerViewComponent_ico_card)
            title_card =
                attribute.getString(R.styleable.CardWithRecyclerViewComponent_title_card)
            title_item_right =
                attribute.getString(R.styleable.CardWithRecyclerViewComponent_title_item_right)
            expand_card =
                attribute.getBoolean(R.styleable.CardWithRecyclerViewComponent_expand_card, false)
        }
    }

    private fun setIco(title: String) {
        getIco(title)?.let {
            binding.icoLeft.setImageResource(it)
        }
    }

    private fun getIco(title: String): Int? {
        return when (title.lowercase()) {
            ABILITIES -> R.drawable.ic_left_abilities
            EGG_GROUPS -> R.drawable.ic_left_egg_groups
            HABITAT -> R.drawable.ic_left_habitat
            ENCOUNTERS -> R.drawable.ic_left_encounters
            else -> null
        }
    }

    private fun setVisible(value: Boolean) {
        if (value) {
            with(binding) {
                imageviewArrowCustom.visibility = View.VISIBLE
                cardviewCustom.setOnClickListener {
                    action_expand_card =
                        recyclerViewPokemonCustom.getCollapseAndExpand(
                            action_expand_card,
                            imageviewArrowCustom
                        )
                }
            }
        }
    }

    private fun setTitle(context: Context, value: String?) {
        value?.let {
            binding.titleCustom.text = it
            setIco(it)
        }
    }

    private fun setAdapter(data_list: List<String>) {
        if (data_list.isNullOrEmpty()) {
            binding.cardviewCustom.visibility = View.GONE
        } else {
            val layoutManager = LinearLayoutManager(context)
            binding.recyclerViewPokemonCustom.layoutManager = layoutManager
            binding.recyclerViewPokemonCustom.adapter =
                PokemonCustomAdapter(
                    data_list,
                    dark_card,
                    dominant_card,
                    drawable_ico,
                    title_item_right,
                    hidden_list_card
                )
        }
    }

    fun setData(
        dark: Palette.Swatch?,
        dominant: Palette.Swatch?,
        data_list: List<String>,
        hidden_list: List<Boolean>? = null
    ) {
        dark_card = dark
        dominant_card = dominant
        hidden_list_card = hidden_list
        data_list_card = data_list // Always last item
    }

    companion object {
        const val ABILITIES = "abilities"
        const val EGG_GROUPS = "egg groups"
        const val HABITAT = "habitat"
        const val ENCOUNTERS = "encounters"
    }

}
