package dev.mina.conversion.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mina.conversion.BuildConfig
import dev.mina.conversion.data.FixerAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    @Named("CurrentDayDate")
    fun provideCurrentDayDate(): String = calculateDate(RequiredDay.CURRENT)

    @Singleton
    @Provides
    @Named("PastThreeDaysDate")
    fun provideDateMinusThreeDays(): String = calculateDate(RequiredDay.PREVIOUS)

    private fun calculateDate(requiredDay: RequiredDay): String {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DAY_OF_YEAR, requiredDay.value)
        val format = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
        return format.format(cal.time)
    }


    //region Retrofit
    @Singleton
    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://api.apilayer.com/currency_data/"


    @Singleton
    @Provides
    @Named("LoggingInterceptor")
    fun providesLoggingInterceptor() = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    @Named("KeyInterceptor")
    fun providesKeyInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl
            .newBuilder()
            // PLease add different Key (in case of limit exceeded) on your local.properties file or use mocked repos instead
            .addQueryParameter("apikey", BuildConfig.apiKey)
            .build()
        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    @Named("Client")
    fun providesOkHttpClient(
        @Named("LoggingInterceptor") loggingInterceptor: HttpLoggingInterceptor,
        @Named("KeyInterceptor") keyInterceptor: Interceptor,
    ) = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(keyInterceptor)
        .build()


    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("BaseUrl") baseUrl: String,
        @Named("Client") client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofit: Retrofit): FixerAPI =
        retrofit.create(FixerAPI::class.java)
    //endregion
}

enum class RequiredDay(val value: Int) {
    CURRENT(0),
    PREVIOUS(-3)
}
