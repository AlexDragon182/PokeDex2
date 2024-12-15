package com.example.my_pokedex.features.pokedex.data

import com.example.my_pokedex.database.PokemonDao
import com.example.my_pokedex.features.PokemonDetails.data.toPokemonDetailsModel
import com.example.my_pokedex.features.PokemonDetails.domain.PokemonDetailsModel
import com.example.my_pokedex.features.pokedex.domain.PokedexRepository
import com.example.my_pokedex.features.pokedex.domain.PokemonModel
import com.example.my_pokedex.networking.ApiPokedex
import com.example.my_pokedex.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


class PokedexRepositoryImpl @Inject constructor(
    private val remoteDataSource: ApiPokedex,
    private val localDataSource: PokemonDao,
) : PokedexRepository {

    override suspend fun getPokemon(limit: Int,offset: Int): Resource<PokemonModel> {
        return withContext(Dispatchers.IO) {
            try {
                var response = remoteDataSource.getPokemon(limit, offset)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var model = response.body()!!.toPokemonModel()
                        Resource.Success(model)
                    } else {
                        Resource.Success(PokemonModel())
                    }
                } else {
                    Resource.Error()
                }
            } catch (e: HttpException) {
                Resource.Error()
            } catch (e: IOException) {
                Resource.Error()
            } catch (e: SocketTimeoutException) {
                Resource.Error()
            }
        }
    }

    override suspend fun getPokemonByName(name: String): Resource<PokemonDetailsModel> {
        return withContext(Dispatchers.IO) {
            try {
                var response = remoteDataSource.getPokemonByName(name)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var model = response.body()!!.toPokemonDetailsModel()
                        Resource.Success(model)
                    } else {
                        Resource.Success(PokemonDetailsModel())
                    }
                } else {
                    Resource.Error()
                }
            } catch (e: HttpException) {
                Resource.Error()
            } catch (e: IOException) {
                Resource.Error()
            } catch (e: SocketTimeoutException) {
                Resource.Error()
            }
        }
    }

}