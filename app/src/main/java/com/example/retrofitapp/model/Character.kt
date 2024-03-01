package com.example.retrofitapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CharacterEntity")
data class Character(
    @PrimaryKey
    val id: Int,
    val biography: Biography,
    val powerstats: Powerstats,
    val images: Images,
    val name: String,
)