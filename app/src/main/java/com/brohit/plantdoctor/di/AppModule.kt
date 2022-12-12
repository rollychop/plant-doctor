package com.brohit.plantdoctor.di

import com.brohit.plantdoctor.common.Configs
import com.brohit.plantdoctor.common.Constants
import com.brohit.plantdoctor.data.remote.PlantDoctorApi
import com.brohit.plantdoctor.data.repository.PlantDetectionRepositoryImpl
import com.brohit.plantdoctor.domain.repository.PlantDetectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePaprikaApi(): PlantDoctorApi {
        return Retrofit.Builder()
            .baseUrl(
                Configs.mutableURL.ifEmpty { Constants.BASE_URL }
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlantDoctorApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(api: PlantDoctorApi): PlantDetectionRepository {
        return PlantDetectionRepositoryImpl(api)
    }
}