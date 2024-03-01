package com.example.retrofitapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.retrofitapp.model.Data
import com.example.retrofitapp.viewmodel.APIViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import com.example.retrofitapp.model.Character
import com.example.retrofitapp.ui.theme.DarkBrown
import com.example.retrofitapp.ui.theme.LightBrown

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(navController: NavController, avm: APIViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RecyclerViewHeroes(avm, navController)
    }
}


@Composable
fun RecyclerViewHeroes(avm: APIViewModel, navController: NavController) {
    val showLoading: Boolean by avm.loading.observeAsState(true)
    val characters: Data by avm.characters.observeAsState(Data())
    avm.getCharacters()
    if (showLoading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp), color = MaterialTheme.colorScheme.secondary
        )
    } else {
        LazyColumn {
            items(characters) {
                ListItem(character = it, avm = avm, navController = navController)
            }
        }
    }
}

@Composable
fun ListItem(avm: APIViewModel, character: Character, navController: NavController) {
    val imageSize = 80
    val borderColor = DarkBrown
    val backgroundColor = LightBrown
    CharacterItem(borderColor, backgroundColor, avm, character, navController, imageSize)
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun CharacterItem(
    borderColor: Color,
    backgroundColor: Color,
    avm: APIViewModel,
    character: Character,
    navController: NavController,
    imageSize: Int
) {
    Card(
        border = BorderStroke(2.dp, borderColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Box(modifier = Modifier
            .background(backgroundColor)
            .clickable {
                avm.setId(character.id)
                navController.navigate("DetailScreen")
            }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlideImage(
                    model = character.images.lg,
                    contentDescription = "Character Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(imageSize.dp)
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
