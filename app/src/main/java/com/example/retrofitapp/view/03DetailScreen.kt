package com.example.retrofitapp.view


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.retrofitapp.model.Biography
import com.example.retrofitapp.model.Character
import com.example.retrofitapp.model.Images
import com.example.retrofitapp.model.Powerstats
import com.example.retrofitapp.ui.theme.DarkBrown
import com.example.retrofitapp.ui.theme.DarkGreen
import com.example.retrofitapp.ui.theme.LighterGreen
import com.example.retrofitapp.ui.theme.LighterGreenv2
import com.example.retrofitapp.viewmodel.APIViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(avm: APIViewModel) {
    val defaultCharacter = Character(
        0, Biography("", "", "", "", listOf()),
        Powerstats(0, 0, 0, 0, 0, 0), Images("", "", "", ""), ""
    )

    val character by avm.currentCharacter.observeAsState(defaultCharacter)
    val characterId by avm.id.observeAsState(0)
    avm.getCurrentCharacter(characterId)

    val isFavorite by avm.isFavorite.observeAsState(false)
    avm.isFavorite(character)

    val showLoading: Boolean by avm.loading.observeAsState(true)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FavButton(isFavorite, character, avm)
        if (showLoading) CircularProgressIndicator(Modifier.width(64.dp), MaterialTheme.colorScheme.secondary)
        else CharacterDetail(avm, character)
    }
}

@Composable
fun FavButton(isFavorite: Boolean, character: Character, avm: APIViewModel) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd,
    ) {
        IconButton(
            onClick = {
                avm.run { if (isFavorite) deleteFavorite(character) else saveAsFavorite(character) }
            }, modifier = Modifier
                .padding(end = 8.dp, top = 8.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Filled.run { if (isFavorite) Favorite else FavoriteBorder },
                contentDescription = "Favorite",
                tint = Color.Red,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}


@Composable
fun CharacterDetail(avm: APIViewModel, character: Character) {
    Card(
        border = BorderStroke(4.dp, DarkGreen),
        modifier = Modifier
            .padding(top = 0.dp, bottom = 15.dp, start = 15.dp, end = 15.dp)
            .clip(RoundedCornerShape(15.dp))
    ) {
        Box(
            modifier = Modifier
                .background(LighterGreen)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterImage(character)
                CharacterInfo(character, avm)
                CharacterSkills(character, avm)
            }
        }
    }
}


@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun CharacterImage(character: Character) {
    GlideImage(
        model = character.images.lg,
        contentDescription = "Character Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxHeight(0.45f)
            .fillMaxWidth()
    )
}

@Composable
fun CharacterInfo(character: Character, avm: APIViewModel) {
    Card(
        border = BorderStroke(2.dp, DarkBrown),
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
    ) {
        Box(
            modifier = Modifier
                .background(LighterGreenv2)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val characterDataMap: Map<String, String> = avm.createCharacterDataMap(character)
                characterDataMap.forEach { (name, data) ->
                    if (data.isNotEmpty()) TextData(name, data)
                }
            }
        }
    }
}


@Composable
private fun TextData(name: String, data: String) {
    Text(
        text = "${if (name == "Title") "" else "$name: "}$data",
        style = MaterialTheme.typography.run { if (name == "Title") headlineLarge else bodyLarge },
        textAlign = TextAlign.Center
    )
}

@Composable
fun CharacterSkills(character: Character, avm: APIViewModel) {
    LazyColumn {
        item {
            val characterSkillsMap: HashMap<String, Int> = avm.createCharacterSkillsMap(character)
            characterSkillsMap.forEach { (skillName, skillValue) ->
                CharacterSkill(skillName, skillValue, avm)
            }
        }
    }
}

@Composable
fun CharacterSkill(skillName: String, skillValue: Int, avm: APIViewModel) {
    val color = avm.colorByPower(skillValue)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp)
    ) {
        Text(text = skillName)
        LinearProgressIndicator(
            progress = skillValue.toFloat() / 100,
            color = color,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(15.dp)
        )
    }
}