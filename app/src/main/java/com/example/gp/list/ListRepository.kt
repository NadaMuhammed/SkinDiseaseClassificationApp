package com.example.gp.list

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.gp.data.DiseaseDatabase

class ListRepository(private val context: Context) {
    val dao = DiseaseDatabase.getDatabase(context).userDao()
    val readAllData = dao.getAllDiseases()

    suspend fun insertAllDiseases(list: ArrayList<Disease>){
        dao.insertAllDiseases(list)
    }

    fun searchDisease(searchQuery: String): LiveData<List<Disease>>{
        return dao.searchDisease(searchQuery)
    }
}