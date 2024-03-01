package com.example.retrofitapp.api
import com.example.retrofitapp.model.Character
import com.example.retrofitapp.room.CharacterApplication

class Repository {
    private val apiInterface = APIInterface.create()
    private val daoInterfase = CharacterApplication.database.characterDao()

    suspend fun getAllCharacters() = apiInterface.getCharacters()
    suspend fun getCharacter(charId: Int) = apiInterface.getCharacter(charId)
    suspend fun saveAsFavorite(character:Character) = daoInterfase.addCharacter(character)
    suspend fun deleteFavorite(character:Character) = daoInterfase.deleteCharacter(character)
    suspend fun isFavorite(character:Character) = daoInterfase.getCharacterById(character.id).isNotEmpty()
    suspend fun getFavorites() = daoInterfase.getAllCharacters()
}