package com.orpheu.pokeplexus.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.orpheu.pokeplexus.BuildConfig
import com.orpheu.pokeplexus.database.AppDatabase
import com.orpheu.pokeplexus.data.PokemonRepository
import com.orpheu.pokeplexus.network.PokeService
import com.orpheu.pokeplexus.network.interceptor.CacheInterceptor
import com.orpheu.pokeplexus.network.interceptor.OfflineCacheInterceptor
import com.orpheu.pokeplexus.ui.pokemon.PokemonViewModel
import com.orpheu.pokeplexus.ui.pokemon_details.PokemonDetailsViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


val retrofitModule = module {

    fun provideCache(context: Context): Cache {
        val cacheSize = 30 * 1024 * 1024 // 30 MiB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    fun provideOkHttpClient(context: Context, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .cache(cache)
            .addNetworkInterceptor(CacheInterceptor(2))
            .addInterceptor(OfflineCacheInterceptor(context, 5))
            .addNetworkInterceptor(
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("OkHttp", message)
                    }
                }).apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()
    }


    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.POKE_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    single { provideCache(androidContext()) }
    single { provideOkHttpClient(androidContext(), get()) }
    single { provideRetrofit(get()) }
}

val apiModule = module {
    fun providePokeService(retrofit: Retrofit): PokeService {
        return retrofit.create(PokeService::class.java)
    }
    single { providePokeService(get()) }
}

val databaseModule = module {

    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "favorite_pokemon"
        ).build()
    }

    single { provideAppDatabase(androidContext()) }

}


val viewModelModule = module {
    viewModel { PokemonViewModel(get()) }
    viewModel { (pokemonId: Int) -> PokemonDetailsViewModel(pokemonId, get()) }
}

val repositoryModule = module {
    factory { PokemonRepository(get(), get<AppDatabase>().pokemonDao()) }
}
