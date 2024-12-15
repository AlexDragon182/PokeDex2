package com.example.my_pokedex.features.pokedex.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.my_pokedex.R
import com.example.my_pokedex.databinding.FragmentPokedexScreenBinding
import com.example.my_pokedex.features.pokedex.domain.PokemonModel
import com.example.my_pokedex.networking.ApiPokedex
import com.example.my_pokedex.utils.Error
import com.example.my_pokedex.utils.Loading
import com.example.my_pokedex.utils.Success
import com.example.my_pokedex.utils.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokedexScreen : Fragment() {

    private var _binding: FragmentPokedexScreenBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var pokedexAdapter: PokedexAdapter
    private val viewModel: PokedexViewModel by viewModels()

    private var previousLastVisibleItem = 0
    private var previousLastVisibleItemCache = previousLastVisibleItem
    private var lastVisibleItem = 0
    private var isLoading: Boolean = true
    private var currentItem = 0
    private var totalItemCount = 0
    private var currentLastItemId = "bulbasaur"
    private var previousLastItemId = ""
    private var isFirstTimeLoading = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokedexScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPokemon.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPokemon.adapter = pokedexAdapter
        registerObservers()
        addScrollListener()
    }

    private fun addScrollListener() {
            binding.rvPokemon.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        binding.rvPokemon.layoutManager?.let {
                            currentItem = it.childCount
                            totalItemCount = it.itemCount
                            lastVisibleItem = (it as LinearLayoutManager).findFirstVisibleItemPosition()
                            if (!isLoading && (currentItem + lastVisibleItem == totalItemCount) && currentLastItemId != previousLastItemId) {
                                isLoading = true
                                previousLastVisibleItemCache = previousLastVisibleItem
                                if (viewModel.pokemonList.size > 0) {
                                    previousLastVisibleItem = viewModel.pokemonList.size - 1
                                    previousLastItemId =
                                        viewModel.pokemonList.get(viewModel.pokemonList.size - 1).name
                                }
                                viewModel.getPokemon()
                            }
                        }
                    }
                }
            })
        }

    private fun registerObservers() {
        lifecycleScope.launch {
            viewModel.response.collect {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(viewState: ViewState<Any>){
        when (viewState) {
            is Success -> {
                isLoading = false
                binding.loading.visibility = View.GONE
                if (viewState.data is PokemonModel) {
                    val response = viewState.data.results
                    pokedexAdapter.submitList(viewModel.pokemonList)
                    if (isFirstTimeLoading){
                        binding.rvPokemon.scheduleLayoutAnimation()
                        isFirstTimeLoading = false
                    }
                }
            }

            is Error -> {
                isLoading = false
                binding.loading.visibility = View.GONE
            }

            is Loading -> {
                isLoading = true
                binding.loading.visibility = View.VISIBLE
            }
        }
    }

}