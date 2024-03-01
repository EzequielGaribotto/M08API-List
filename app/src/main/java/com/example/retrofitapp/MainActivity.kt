package com.example.retrofitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.retrofitapp.navigation.BottomNavigationScreens
import com.example.retrofitapp.navigation.Routes
import com.example.retrofitapp.ui.theme.DarkerGreen
import com.example.retrofitapp.ui.theme.IntermediateGreen
import com.example.retrofitapp.ui.theme.LightGreen
import com.example.retrofitapp.ui.theme.RetrofitAppTheme
import com.example.retrofitapp.view.DetailScreen
import com.example.retrofitapp.view.FavsScreen
import com.example.retrofitapp.view.ListScreen
import com.example.retrofitapp.viewmodel.APIViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiViewModel by viewModels<APIViewModel>()
        val bottomNavigationItems = listOf(
            BottomNavigationScreens.ListScreen,
            BottomNavigationScreens.FavsScreen,
        )
        setContent {
            RetrofitAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    Scaffold(topBar = {
                        if (currentRoute != null) {
                            MyTopAppBar(apiViewModel, currentRoute)
                        }
                    }, bottomBar = {
                        if (currentRoute != null) {
                            MyBottomBar(
                                navigationController, bottomNavigationItems, currentRoute
                            )
                        }
                    }) { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            NavHost(
                                navController = navigationController,
                                startDestination = Routes.ListScreen.route
                            ) {
                                composable(Routes.ListScreen.route) {
                                    ListScreen(
                                        navigationController, apiViewModel
                                    )
                                }
                                composable(Routes.FavsScreen.route) {
                                    FavsScreen(
                                        navigationController, apiViewModel
                                    )
                                }
                                composable(Routes.DetailScreen.route) {
                                    DetailScreen(
                                        apiViewModel,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(avm: APIViewModel, currentRoute: String) {
    if (currentRoute == Routes.ListScreen.route) {
        val showSearch by avm.showSearchBar.observeAsState(false)
        TopAppBar(
            title = { Text(text = "") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = LightGreen,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.Black,
                ),
            actions = {
            if (!showSearch) {
                EnableSearchButton(avm)
            } else {
                MySearchBar(avm, currentRoute)
            }
        })
    }
}

@Composable
private fun EnableSearchButton(avm: APIViewModel) {
    IconButton(onClick = { avm.switchSearchBar() }) {
        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(avm: APIViewModel, currentRoute: String) {
    val searchText by avm.searchText.observeAsState("")
    if (currentRoute == Routes.ListScreen.route) {
        SearchBar(
            query = searchText,
            onQueryChange = { avm.onSearchTextChange(it) },
            onSearch = { avm.onSearchTextChange(it) },
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .clip(CircleShape)
                .background(Color.Transparent),
            active = true,
            placeholder = { Text("Buscar") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    Modifier.clickable { avm.switchSearchBar() })
            },
            colors = SearchBarDefaults.colors(
                containerColor = Color.Transparent,
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear query",
                    Modifier.clickable { avm.onSearchTextChange("") })
            },
            onActiveChange = { },

            ) {
        }
    }
}


@Composable
fun MyBottomBar(
    navController: NavController,
    bottomNavigationItems: List<BottomNavigationScreens>,
    currentRoute: String
) {
    BottomNavigation(backgroundColor = LightGreen, contentColor = Color.White) {
        bottomNavigationItems.forEach { item ->
            BottomNavigationItem(icon = { Icon(item.icon, contentDescription = item.label) },
                selected = currentRoute == item.route,
                enabled = currentRoute != item.route,
                label = { Text(item.label) },
                selectedContentColor = DarkerGreen,
                unselectedContentColor = IntermediateGreen,
                alwaysShowLabel = false,
                onClick = { navController.navigate(item.route) })
        }
    }
}