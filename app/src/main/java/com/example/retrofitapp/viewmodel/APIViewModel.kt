package com.example.retrofitapp.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitapp.api.Repository
import com.example.retrofitapp.model.Character
import com.example.retrofitapp.model.Data
import com.example.retrofitapp.ui.theme.DarkGreen
import com.example.retrofitapp.ui.theme.Orange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class APIViewModel : ViewModel() {


    private val repository = Repository()
    private val _loading = MutableLiveData(true)
    val loading = _loading

    // LISTA DE PERSONAJES QUE SE MOSTRARÁN EN PANTALLA
    private val _characters = MutableLiveData<Data>()
    val characters = _characters
    // LISTA COMPLETA DE PERSONAJES
    private val _charactersFromAPI = MutableLiveData<Data>()
    private val charactersFromAPI = _charactersFromAPI

    private val _searchText = MutableLiveData("")
    val searchText = _searchText

    private val _currentCharacter = MutableLiveData<Character>()
    val currentCharacter = _currentCharacter

    private val _isFavorite = MutableLiveData(false)
    val isFavorite = _isFavorite
    private val _favorites = MutableLiveData<MutableList<Character>>()
    val favorites = _favorites

    private val _id = MutableLiveData<Int>()
    val id = _id

    private val _showSearchBar = MutableLiveData(false)
    val showSearchBar = _showSearchBar

    fun getCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getAllCharacters()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _characters.value = response.body()
                    _charactersFromAPI.value = characters.value
                    _loading.value = false
                } else {
                    Log.e(
                        "Error en la petición de datos:", response.message() ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun onSearchTextChange(query: String) {
        // Actualizamos el téxto de la barra de búsqueda
        _searchText.value = query
        if (query.isEmpty()) {
            // Si la query está vacía, se muestra toda la lista de personajes de nuevo
            _characters.value = charactersFromAPI.value
        } else {
            // Si no está vacía, se filtran los personajes
            val filteredCharacters =
                charactersFromAPI.value?.filter { it.name.lowercase().contains(query.lowercase()) }
            _characters.value = Data().apply { addAll(filteredCharacters ?: emptyList()) }
        }
    }

    fun getCurrentCharacter(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCharacter(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _currentCharacter.value = response.body()
                    _loading.value = false
                } else {
                    Log.e(
                        "Error en la petición de datos:", response.message() ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun getFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getFavorites()
            withContext(Dispatchers.Main) {
                _favorites.value = response
                _loading.value = false
            }
        }
    }

    fun isFavorite(character: Character) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.isFavorite(character)
            withContext(Dispatchers.Main) {
                _isFavorite.value = response
            }
        }
    }

    fun saveAsFavorite(character: Character) {
        isFavorite(character)
        if (_isFavorite.value == false) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.saveAsFavorite(character)
                _isFavorite.postValue(true)
            }
        }
    }

    fun deleteFavorite(character: Character) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteFavorite(character)
            _isFavorite.postValue(false)
        }
    }

    fun setId(newId: Int) {
        _id.value = newId
    }

    fun switchSearchBar() {
        _showSearchBar.value = showSearchBar.value != true
    }

    fun colorByPower(power: Int): Color {
        val baseColor = when {
            power < 20 -> Color.Red
            power < 40 -> Orange
            power < 60 -> Color.Yellow
            power < 80 -> Color.Green
            else -> DarkGreen
        }
        return baseColor
    }

    fun createCharacterDataMap(character: Character): Map<String, String> {
        val emptyPattern = Regex("^-$")

        val name = character.biography.fullName.ifEmpty { character.name }
        val birthplace = character.biography.placeOfBirth.replace(emptyPattern, "")
        val alterEgos = character.biography.alterEgos.replace("No alter egos found.", "")
        val aliases = character.biography.aliases.joinToString(", ").replace(emptyPattern, "")
        val alsoKnownAs = "$alterEgos$aliases".replace(";", ",")
        val publisher = character.biography.publisher.replace(emptyPattern, "")

        return mapOf(
            "Title" to name,
            "Birthplace" to birthplace,
            "AKA" to alsoKnownAs,
            "Publisher" to publisher
        )
    }

    fun createCharacterSkillsMap(character: Character): HashMap<String, Int> {
        val poder = character.powerstats.power
        val combate = character.powerstats.combat
        val durabilidad = character.powerstats.durability
        val fuerza = character.powerstats.strength
        val inteligencia = character.powerstats.intelligence
        val velocidad = character.powerstats.speed

        return hashMapOf(
            "Poder" to poder,
            "Combate" to combate,
            "Durabilidad" to durabilidad,
            "Fuerza" to fuerza,
            "Inteligencia" to inteligencia,
            "Velocidad" to velocidad
        )
    }
}