package com.example.myapplication.game

import android.R.attr.fontWeight
import android.R.attr.text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun GameScreen() {
    var gameStarted by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        var playerX by remember { mutableStateOf(0f) }
        var playerY by remember { mutableStateOf(0f) }
        //Estado de los Enemy
        /*    val enemies = listOf(
            Enemy( 150f, 150f),
            Enemy( 300f, 150f),
            Enemy( 450f, 150f),
        )*/
        //val enemies = createEnemyFormation()
        var enemies by remember { mutableStateOf(createEnemyFormation()) }
        var enemyDirection by remember { mutableStateOf(1f) }

        //Estado de la puntuación
        var score by remember { mutableStateOf(0) }

        //Estado de la vida
        var lives by remember { mutableStateOf(3) }

        //Estado del disparo
        var fireRequested by remember { mutableStateOf(false) }
        var bullets by remember { mutableStateOf(listOf<Bullet>()) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(16L)
                bullets = bullets.map { bullet ->
                    bullet.copy(y = bullet.y - 8f)
                }
                    .filter { bullet -> bullet.y > 0f }
            }
        }


        //Estado del juego
        var playerInitialized by remember { mutableStateOf(false) }
        var screenWidth by remember { mutableStateOf(0f) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(30L)
                val enemyWidth = 60f
                val margin = enemyWidth/2
                val speed = 3f
                val leftEdge = enemies.minOfOrNull { it.x - margin } ?: 0f
                val rightEdge = enemies.maxOfOrNull { it.x + margin } ?: 0f
                if (
                    (enemyDirection > 0f && rightEdge + speed >= screenWidth)
                    || (enemyDirection < 0f && leftEdge - speed <= 0f)
                ){
                    enemyDirection *= -1f
                }
                enemies = enemies.map { enemy ->
                    enemy.copy(x = enemy.x +(speed * enemyDirection))
                }
            }
        }
        Canvas(
            modifier = Modifier.fillMaxSize()
                .onSizeChanged { size ->
                    screenWidth = size.width.toFloat()
                    if (!playerInitialized) {
                        playerX = size.width / 2f
                        playerY = size.height - 120f
                        playerInitialized = true
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val shipWidth = 80f
                            playerX = (playerX + dragAmount.x)
                                .coerceIn(
                                    shipWidth / 2, screenWidth - shipWidth / 2
                                )
                        }
                    )
                }) {
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
                lineTo(left, bottom)
                lineTo(playerX, bottom - 15f)
                lineTo(right, bottom)
                close()
            }
            drawPath(shipPath, color = Color.White)
            //Dibujar enemigos
            enemies.forEach { enemy ->
                val enemyWidth = 60f
                val enemyHeight = 40f
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(
                        enemy.x - enemyWidth / 2,
                        enemy.y - enemyHeight / 2
                    ),

                    size = Size(enemyWidth, enemyHeight)
                )
            }
            //Dibujar disparos
            bullets.forEach { bullet ->
                drawRect(
                    color = Color.Yellow,
                    topLeft = Offset(
                        bullet.x - 4f,
                        bullet.y - 15f),
                    size = Size(8f, 20f))
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly){
                Text(
                    text = "SCORE: %04d".format(score),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "LIVES: " + "♥️".repeat(lives),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "SPACE SHOOTER",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = {
                gameStarted = true
                score += 100
                if (lives > 0) {
                    lives -= 1
                }
            }) {
                Text(text = "START")
            }
            if (gameStarted) {
                Text(
                    text = "GAME STARTED",
                    color = Color.White
                )
            }
            Button(onClick =  {
               // fireRequested = true
                bullets = bullets + Bullet(
                    x = playerX,
                    y = playerY - 40f)
            }){
                Text(text = "FIRE")
            }
        }
    }
}
    fun createEnemyFormation(): List<Enemy> {
        return listOf(
            Enemy(150f, 150f),
            Enemy(300f, 150f),
            Enemy(450f, 150f),
        )
    }
