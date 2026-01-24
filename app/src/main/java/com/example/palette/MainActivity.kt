package com.example.palette

import android.R.attr.type
import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
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
import androidx.palette.graphics.Palette
import com.example.palette.ui.theme.PaletteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaletteTheme {

                val navController = rememberNavController()
                val context = LocalContext.current
                var texto by remember { mutableStateOf("StaggeredGrid") }

                var appBarColor by remember { mutableStateOf(Color(0xFF005CB2)) }
                var statusBarColor by remember { mutableStateOf(Color(0xFF005CB2)) }
                var onAppBarColor by remember { mutableStateOf(Color.White) }
                var currentSwatches by remember { mutableStateOf<List<Pair<String, Palette.Swatch?>>>(emptyList()) }

                val animatedAppBarColor by animateColorAsState(targetValue = appBarColor, label = "colorAnim")

                val view = LocalView.current
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor = statusBarColor.toArgb()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MyTopAppBar(
                            animatedAppBarColor,
                            onAppBarColor,
                            onChangeText = { texto = it }
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
                        startDestination = "main",
                        enterTransition = { fadeIn(animationSpec = tween(900)) + slideInHorizontally() },
                        exitTransition = { fadeOut(animationSpec = tween(900)) + slideOutHorizontally() }
                    ) {
                        composable("main") {
                            LaunchedEffect(Unit) {
                                appBarColor = Color(0xFF005CB2)
                                statusBarColor = Color(0xFF005CB2)
                                onAppBarColor = Color.White
                            }
                            MainScreen(
                                modifier = Modifier.padding(innerPadding),
                                texto,
                                navController
                            )
                        }
                        composable(
                            "screen2/{imageRes}",
                            arguments = listOf(
                                navArgument("imageRes") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: 0

                            LaunchedEffect(imageRes) {
                                val bitmap = BitmapFactory.decodeResource(context.resources, imageRes)
                                Palette.from(bitmap).generate { palette ->
                                    palette?.let {
                                        val vibrant = it.vibrantSwatch
                                        appBarColor = vibrant?.rgb?.let { Color(it) } ?: Color(0xFF005CB2)
                                        onAppBarColor = vibrant?.titleTextColor?.let { Color(it) } ?: Color.White

                                        statusBarColor = it.darkVibrantSwatch?.rgb?.let { Color(it) }
                                            ?: it.vibrantSwatch?.rgb?.let { Color(it) }
                                                    ?: Color(0xFF004080)
                                        currentSwatches = listOf(
                                            "Light Vibrant" to it.lightVibrantSwatch,
                                            "Dark Vibrant" to it.darkVibrantSwatch,
                                            "Light Muted" to it.lightMutedSwatch,
                                            "Muted" to it.mutedSwatch,
                                            "Dark Muted" to it.darkMutedSwatch
                                        )
                                    }
                                }
                            }

                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) { visible = true }

                            AnimatedVisibility(
                                visible = visible,
                                enter = slideInVertically() + fadeIn()
                            ) {
                                Screen2(
                                    modifier = Modifier.padding(innerPadding),
                                    imageRes = imageRes,
                                    swatches = currentSwatches
                                )
                            }
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
                            navController.navigate("screen2/${it.drawable}")
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
                    Box(
                        modifier = Modifier.clickable {
                            navController.navigate("screen2/${it.drawable}")
                        }
                    ) {
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
fun MyTopAppBar(
    containerColor: Color,
    titleContentColor: Color,
    onChangeText: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Palette") },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Menu, contentDescription = "Menú")
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Buscar")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("LazyColumn") }, onClick = { expanded = false; onChangeText("LazyColumn") })
                DropdownMenuItem(text = { Text("StaggeredGrid") }, onClick = { expanded = false; onChangeText("StaggeredGrid") })
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = titleContentColor,
            navigationIconContentColor = titleContentColor,
            actionIconContentColor = titleContentColor
        )
    )
}