package com.example.myapplication.game

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged

@Composable
fun GameScreen(){
    var gameStarted by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
     var playerX by remember { mutableStateOf(0f) }
     var playerY by remember { mutableStateOf(0f) }
     var playerInitialized by remember { mutableStateOf(false) }
        Canvas(modifier = Modifier.fillMaxSize()
            .onSizeChanged{ size ->
                if (!playerInitialized){
                    playerX = size.width / 2f
                    playerY = size.height - 120f
                    playerInitialized = true
                }
            }
            .pointerInput(Unit){detectDragGestures(
                onDrag = { change, dragAmount ->
                    change.consume()
                    // val dragX = dragAmount.x
                    playerX += dragAmount.x
                }
            )}){
            drawRect(color = Color.Black)
            val shipWidth = 80f
            val shipHeight = 80f
            //val playerX = size.width / 2
            //val playerY = size.height - 120f
            val left = playerX - shipWidth / 2
            val top = playerY - shipHeight / 2
            val right = playerX + shipWidth / 2
            val bottom = playerY + shipHeight / 2
            val shipPath = Path().apply {
                moveTo(playerX, top)
                lineTo( left, bottom)
                lineTo(playerX, bottom -15f)
                lineTo(right, bottom)
                close()
            }
            drawPath(shipPath, color = Color.White)
        }
        Column(modifier = Modifier.fillMaxSize()
            .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = "SPACE SHOOTER",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { gameStarted = true }) {
                Text(text = "START")
            }
            if(gameStarted){
                Text(text = "GAME STARTED",
                    color = Color.White)
            }
        }
    }
}