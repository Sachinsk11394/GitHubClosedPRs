package com.sachin.githubclosedprs

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Component(modules = [CoreModule::class])
internal interface CoreComponent {
    fun injectSearchActivity(activity: SearchActivity)
}

@Module
class CoreModule(val context: Context) {

    @Provides
    fun getSearchActivityViewModelFactory(repository: SearchActivityRepository): SearchViewModelFactory {
        return SearchViewModelFactory(repository)
    }

    @Provides
    fun getSearchActivityRepository(webService: SearchWebService): SearchActivityRepository {
        return SearchActivityRepository(webService)
    }

    @Provides
    fun getSearchActivityWebService(retrofit: Retrofit): SearchWebService = retrofit.create(SearchWebService::class.java)

    @Provides
    fun getMoshi(): Moshi {
        val moshi = Moshi.Builder()
        moshi.add(KotlinJsonAdapterFactory())
        return moshi.build()
    }

    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient, moshi: Moshi, baseUrl: BaseUrl): Retrofit {
        return  Retrofit.Builder()
            .baseUrl(baseUrl.value)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun getOkHttp(): OkHttpClient {
        val authenication = object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val token = BuildConfig.TOKEN
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "token $token")
                    .build()
                return chain.proceed(newRequest)
            }
        }
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(authenication)
        val isDebugBuild = true // get this value from build config
        if(isDebugBuild) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    @Provides
    fun getBaseUrl(): BaseUrl {
        return BaseUrl("https://api.github.com/")
    }

    fun hasNetwork(): Boolean {
        var isConnected: Boolean? = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected ?: false
    }
}