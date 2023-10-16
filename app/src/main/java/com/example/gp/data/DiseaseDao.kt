package com.example.gp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gp.list.Disease
import retrofit2.http.GET

@Dao
interface DiseaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDiseases(list: ArrayList<Disease>)

    @Query("SELECT * FROM disease_table")
    fun getAllDiseases(): LiveData<List<Disease>>

    @Query("SELECT * FROM disease_table WHERE diseaseName LIKE :searchQuery")
    fun searchDisease(searchQuery: String): LiveData<List<Disease>>
}