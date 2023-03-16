package com.example.transweather.features.common.app_module

import android.content.Context
import com.example.transweather.BuildConfig
import com.example.transweather.R
import com.example.transweather.features.common.services.OpenWeatherService
import com.google.gson.Gson
import com.pluto.plugins.network.PlutoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideOpenWeatherService(retrofit: Retrofit): OpenWeatherService =
        retrofit.create(OpenWeatherService::class.java)

    @Provides   /* provide gson converter */
    fun provideConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }


    @Named("appIdInterceptor")
    @Provides
    fun provideAppIdInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url
        //Since this is a assessment I am putting api key however in a real project api
        //key would depend on security level of examples of hiding api would be in local.properties
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("appid", "42cca71b4d7f0b36d9c9eb118f849943").build()
        val request = original.newBuilder().url(url).build()
        chain.proceed(request)
    }

    @Named("LoggingInterceptor")
    @Provides
    fun httpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor { message ->
        if (BuildConfig.DEBUG) Timber.tag("OkHttp").d(message)
    }.setLevel(HttpLoggingInterceptor.Level.BODY)


    @Singleton
    @Provides
    fun provideOkhttpClient(
        @Named("appIdInterceptor") appIdInterceptor: Interceptor,
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient().newBuilder().run {
        addInterceptor(appIdInterceptor)
        addInterceptor(loggingInterceptor)
        addInterceptor(PlutoInterceptor())
        connectTimeout(2, TimeUnit.MINUTES)
        readTimeout(2, TimeUnit.MINUTES)
        build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder().baseUrl(context.getString(R.string.base_url))
        .addConverterFactory(converterFactory).client(okHttpClient).build()


}