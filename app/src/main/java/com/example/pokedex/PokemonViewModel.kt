package com.example.pokedex

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.api.data.Damage
import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.api.repository.PokemonRepositoryData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PokemonViewModel(
    private val pokemonRepositoryData: PokemonRepositoryData
) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>>
        get() = _pokemonList

    private val _pokemonDetail = MutableLiveData<Pokemon>()
    val pokemonDetail: LiveData<Pokemon>
        get() = _pokemonDetail

    private val _pokemonListLoading = MutableLiveData<Boolean>()
    val pokemonListLoading: LiveData<Boolean>
        get() = _pokemonListLoading

    private val _pokemonDetailLoading = MutableLiveData<Boolean>()
    val pokemonDetailLoading: LiveData<Boolean>
        get() = _pokemonDetailLoading

    private val _pokemonTextError = MutableLiveData<Int>()
    val pokemonTextError: LiveData<Int>
        get() = _pokemonTextError

    fun getPokemonList(limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _pokemonListLoading.postValue(true)
            try {
                _pokemonList.postValue(pokemonRepositoryData.pokemonList(limit)?.results)
            } catch (e: SocketTimeoutException) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_timeout_error)
            } catch (e: UnknownHostException) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_unknownhost_error)
            } catch (e: HttpException) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_http_error)
            } catch (e: Exception) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_general_error)
            }
            _pokemonListLoading.postValue(false)
        }
    }

    fun getPokemonDetail(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _pokemonDetailLoading.postValue(true)
            try {
                val pokemon = pokemonRepositoryData.pokemonDetail(id)
                pokemon?.let { pokemon_obj ->

                    pokemonRepositoryData.pokemonEncounters(id)?.let { api_location_list ->
                        pokemon.apply { encounters = api_location_list }
                    }

                    pokemonRepositoryData.pokemonCharacteristic(id)?.let { api_characteristic ->
                        pokemon_obj.apply { characteristic = api_characteristic }
                    }

                    pokemonRepositoryData.pokemonSpecies(id)?.let { api_specie ->
                        pokemon_obj.apply { specie = api_specie }
                        val parse = Uri.parse(api_specie.evolution_chain.url)
                        parse.pathSegments.getOrNull(3)?.let { specie_id ->
                            pokemonRepositoryData.pokemonEvolution(specie_id.toInt())
                                ?.let { api_evolution ->
                                    pokemon_obj.apply { evolution = api_evolution }
                                }
                        }
                    }

                    val damage_list: MutableList<Damage> = mutableListOf()
                    pokemon_obj.types?.forEach { type_list ->
                        pokemonRepositoryData.pokemonDamageRelations(type_list.type.name)?.let {
                            it.type = type_list.type
                            damage_list.add(it)
                        }
                    }
                    pokemon_obj.damage = damage_list

                }
                _pokemonDetail.postValue(pokemon)
            } catch (e: SocketTimeoutException) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_timeout_error)
            } catch (e: UnknownHostException) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_unknownhost_error)
            } catch (e: HttpException) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_http_error)
            } catch (e: Exception) {
                _pokemonList.postValue(listOf())
                _pokemonTextError.postValue(R.string.is_general_error)
            }
            _pokemonDetailLoading.postValue(false)
        }
    }

}
