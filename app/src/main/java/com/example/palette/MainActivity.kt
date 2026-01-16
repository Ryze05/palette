package com.example.palette

import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.palette.ui.theme.PaletteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaletteTheme {

                val navController = rememberNavController()
                var texto by remember { mutableStateOf("StaggeredGrid") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            onChangeText = { texto = it },
                            //onMenuClick = { navController.navigate("menu") }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("main")
                            },
                            containerColor = Color(0xFF6AB7FF),
                            contentColor = Color.White,
                            elevation = FloatingActionButtonDefaults.elevation(8.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Arrow back",
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main"
                    ) {
                        composable("main") {
                            MainScreen(
                                modifier = Modifier.padding(innerPadding),
                                texto,
                                navController
                            )
                        }
                        composable(
                            "screen2/{title}/{imageRes}",
                            arguments = listOf(
                                navArgument("imageRes") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val title = backStackEntry.arguments?.getString("title") ?: ""
                            val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: 0

                            /*Screen2(
                                modifier = Modifier.padding(innerPadding),
                                title,
                                imageRes
                            )*/
                        }
                    }
                }
            }
        }
    }
}

data class ImageItem(
    val title: String,
    @DrawableRes val drawable: Int
)

val images = listOf<ImageItem>(
    ImageItem("Morella", R.drawable.image),
    ImageItem("Playa Algarve", R.drawable.image1),
    ImageItem("Maldivas", R.drawable.image2),
    ImageItem("Machu Picchu", R.drawable.image3),
    ImageItem("Gran Muralla China", R.drawable.image4),
    ImageItem("Alhambra", R.drawable.image5),
    ImageItem("Atenas", R.drawable.image6),
    ImageItem("Monumento", R.drawable.image7),
    ImageItem("Hawai", R.drawable.image8)
)

@Composable
fun MainScreen(modifier: Modifier = Modifier, texto: String, navController: NavController) {

    val gridState = rememberLazyStaggeredGridState()

    Box(
        modifier.fillMaxSize()
    ) {

        if (texto == "StaggeredGrid") {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(images) {
                    Box(
                        modifier = Modifier.clickable {
                            navController.navigate("screen2/${it.title}/${it.drawable}")
                        }
                    ) {
                        Image(
                            painter = painterResource(it.drawable),
                            contentDescription = null,
                            //modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.4f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = it.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }

                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(images) {
                    Box {
                        Image(
                            painter = painterResource(it.drawable),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.4f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = it.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(onChangeText: (String) -> Unit/*, onMenuClick: () -> Unit*/) {

    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Palette") },
        navigationIcon = {
            IconButton(onClick = { /*onMenuClick()*/ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menú")
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Buscar")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("LazyColumn") },
                    onClick = {
                        expanded = false
                        onChangeText("LazyColumn")
                    }
                )
                DropdownMenuItem(
                    text = { Text("StaggeredGrid") },
                    onClick = {
                        expanded = false
                        onChangeText("StaggeredGrid")
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF005CB2),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}