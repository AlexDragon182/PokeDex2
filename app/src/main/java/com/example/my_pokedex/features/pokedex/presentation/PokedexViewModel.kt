package com.example.my_pokedex.features.pokedex.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_pokedex.features.pokedex.domain.PokedexRepository
import com.example.my_pokedex.features.pokedex.domain.PokemonResultModel
import com.example.my_pokedex.utils.Error
import com.example.my_pokedex.utils.Loading
import com.example.my_pokedex.utils.Resource
import com.example.my_pokedex.utils.Success
import com.example.my_pokedex.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val repository: PokedexRepository,
) : ViewModel() {

    protected val _response = Channel<ViewState<Any>>(Channel.BUFFERED)
    val response = _response.receiveAsFlow()
    private var searchJob: Job? = null

    val searchQuery: String = ""
    var pokemonList: MutableList<PokemonResultModel> = mutableListOf()
    var noMoreItems : Boolean = false
    var offset : Int = 0
    var limit : Int = 40


    init {
        getPokemon()
    }


    fun getPokemon() {
        viewModelScope.launch {

            _response.send(Loading())

            val result = repository.getPokemon(limit, offset)
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        if (it.results.isEmpty()) {
                           noMoreItems = true
                        } else {
                            pokemonList.addAll(it.results)
                            offset += 40
                            _response.send(Success(data = it))
                        }
                    }
                }

                is Resource.Error -> {
                    _response.send(Error())
                }

                is Resource.Loading -> {
                    //NO-OP//
                }
            }
        }
    }
}