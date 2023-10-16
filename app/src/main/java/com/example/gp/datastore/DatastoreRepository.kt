package com.example.gp.datastore
//
//import android.content.Context
//import android.util.Log
//import androidx.datastore.DataStore
//import androidx.datastore.preferences.*
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.map
//import java.io.IOException
//
//const val PREFERENCE_NAME = "my_preference"
//class DatastoreRepository(context: Context) {
//    private object PreferencesKey{
//        val access_token = preferencesKey<String?>("access_token")
//    }
//    private val dataStore : DataStore<Preferences> = context.createDataStore(PREFERENCE_NAME)
//
//    val readFromDatastore : Flow<String?> = dataStore.data.catch { exception->
//        if (exception is IOException){
//            Log.d("Datastore", exception.message.toString())
//            emit(emptyPreferences())
//        }else{
//            throw exception
//        }
//    }.map { preference->
//        val myToken = preference[PreferencesKey.access_token] ?: "none"
//        myToken
//    }
//
//    suspend fun addToDatastore(accessToken: String?){
//        dataStore.edit { preference->
//            preference[PreferencesKey.access_token] = accessToken
//        }
//    }
//
//
//}