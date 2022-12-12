package com.brohit.plantdoctor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BaseUrlDataStore(
    private val context: Context
) {
    // to make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by
        preferencesDataStore("urls")
        val USER_EMAIL_KEY = stringPreferencesKey("base_url")
    }

    // to get the email
    val getBaseUrl: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL_KEY] ?: ""
        }

    // to save the email
    suspend fun saveBaseUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = url
        }
    }
}