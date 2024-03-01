package com.example.retrofitapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.retrofitapp.model.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM CharacterEntity")
    suspend fun getAllCharacters():MutableList<Character>

    @Query("SELECT * FROM CharacterEntity where id = :characterId")
    suspend fun getCharacterById(characterId: Int): MutableList<Character>
    @Insert
    suspend fun addCharacter(character: Character)

    @Delete
    suspend fun deleteCharacter(character: Character)
}