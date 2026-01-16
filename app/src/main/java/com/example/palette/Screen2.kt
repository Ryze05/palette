package com.example.palette

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette

@Composable
fun Screen2(
    modifier: Modifier,
    imageRes: Int,
    swatches: List<Pair<String, Palette.Swatch?>>
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        }

        items(swatches) { (name, swatch) ->
            if (swatch != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(swatch.rgb))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        color = Color(swatch.bodyTextColor),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}