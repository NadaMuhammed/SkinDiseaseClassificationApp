package com.example.gp.list

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "disease_table")
data class Disease(
    @PrimaryKey
    val id: Int,
    var image: Int,
    var diseaseName: String,
    var diseaseDescription: String
): Parcelable