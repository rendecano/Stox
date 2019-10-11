package io.rendecano.stox.common.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.rendecano.stox.BuildConfig
import io.rendecano.stox.common.data.local.AppDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppBindingModule {

    @Provides
    @Singleton
    internal fun provideGson() = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    internal fun provideHttpClient(): OkHttpClient {

        val timeout = 3600.toLong()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.NONE

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        val builder = OkHttpClient.Builder()
        builder.readTimeout(timeout, TimeUnit.SECONDS)
        builder.writeTimeout(timeout, TimeUnit.SECONDS)
        builder.connectTimeout(timeout, TimeUnit.SECONDS)
        builder.addInterceptor(logging)
        return builder.build()

    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BuildConfig.BASE_API_URL)
                    .client(okHttpClient)
                    .build()

    @Provides
    @Singleton
    internal fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    internal fun provideDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "io.rendecano.stox.db")
                    .fallbackToDestructiveMigration()
                    .build()
}