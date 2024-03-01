package com.example.retrofitapp.view


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.retrofitapp.model.Character
import com.example.retrofitapp.model.Data
import com.example.retrofitapp.ui.theme.LightGolden
import com.example.retrofitapp.ui.theme.LightPurple
import com.example.retrofitapp.viewmodel.APIViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavsScreen(navController: NavController, avm: APIViewModel) {
    val showLoading: Boolean by avm.loading.observeAsState(true)
    val favoriteCharacters: MutableList<Character> by avm.favorites.observeAsState(Data())
    avm.getFavorites()

    if (showLoading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp), color = MaterialTheme.colorScheme.secondary
        )
    } else {
        LazyColumn {
            items(favoriteCharacters) {
                FavItem(
                    character = it, avm = avm, navController = navController
                )
            }
        }
    }
}

@Composable
fun FavItem(avm: APIViewModel, character: Character, navController: NavController) {
    val imageSize = 120
    val borderColor = LightGolden
    val backgroundColor = LightPurple
    CharacterItem(borderColor, backgroundColor, avm, character, navController, imageSize)
}
