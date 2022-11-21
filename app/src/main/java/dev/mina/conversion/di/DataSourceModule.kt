package dev.mina.conversion.di


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mina.conversion.BuildConfig
import dev.mina.conversion.data.FixerAPI
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
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
    @Named("CacheInterceptor")
    fun providesCacheInterceptor(@ApplicationContext context: Context) = Interceptor { chain ->
        var request = chain.request()
        request =
            if (isNetworkAvailable(context))
                request.newBuilder()
                    .header("Cache-Control",
                        "public, max-age=" + TimeUnit.DAYS.toSeconds(1))
                    .build()
            else
                request.newBuilder()
                    .header("Cache-Control",
                        "public, only-if-cached, max-stale=" + TimeUnit.DAYS.toSeconds(2))
                    .build()
        chain.proceed(request)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    @Singleton
    @Provides
    @Named("Cache")
    fun provideCache(
        @ApplicationContext context: Context,
    ): Cache = Cache(context.cacheDir, (5 * 1024 * 1024).toLong())

    @Singleton
    @Provides
    @Named("Client")
    fun providesOkHttpClient(
        @Named("Cache") cache: Cache,
        @Named("LoggingInterceptor") loggingInterceptor: HttpLoggingInterceptor,
        @Named("KeyInterceptor") keyInterceptor: Interceptor,
        @Named("CacheInterceptor") cacheInterceptor: Interceptor,
        ) = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(keyInterceptor)
        .addInterceptor(cacheInterceptor)
        .cache(cache)
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
    PREVIOUS(-2)
}
