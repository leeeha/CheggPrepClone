package com.gdsc.cheggprepreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gdsc.cheggprepreview.ui.theme.CheggPrepReviewTheme
import com.gdsc.cheggprepreview.ui.theme.DeepOrange
import androidx.compose.material.TextFieldDefaults
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gdsc.cheggprepreview.models.Card
import com.gdsc.cheggprepreview.navigation.BottomNavigationBar
import com.gdsc.cheggprepreview.navigation.Screen
import com.gdsc.cheggprepreview.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheggPrepReviewTheme {
                val navController = rememberNavController()

                val (bottomBarShown, showBottomBar) = remember {
                    mutableStateOf(true)
                }

                Scaffold(
                    bottomBar = {
                        if (bottomBarShown) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) {
                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            showBottomBar(true)
                            HomeScreen(navController)
                        }

                        composable(Screen.Search.route) {
                            showBottomBar(true)
                            SearchScreen(navController)
                        }

                        composable(Screen.Create.route) {
                            showBottomBar(false)
                            CreateScreen(navController)
                        }

                        composable(Screen.More.route) {
                            showBottomBar(true)
                            MoreScreen(navController)
                        }

                        composable(Screen.Deck.route + "/{deckTitle}/{cardsNum}") { backStackEntry ->
                            val deckTitle =
                                backStackEntry.arguments?.getString("deckTitle") ?: "no title"
                            val cardsNum =
                                backStackEntry.arguments?.getString("cardsNum")?.toInt() ?: 0
                            showBottomBar(false)
                            DeckScreen(
                                navController = navController,
                                title = deckTitle,
                                cardsNum = cardsNum
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CheggPrepReviewTheme {
    }
}


@Composable
fun DeckInSubject() {
    Column( // vertical (수직으로 배치)
        modifier = Modifier // https://foso.github.io/Jetpack-Compose-Playground/general/modifier/
            .fillMaxWidth() // match_parent
            .border( // 테두리 두께와 색상
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable {

            }
            .padding(16.dp) // 안쪽 여백
    ) {
        Text(
            text = "recursion",
            style = MaterialTheme.typography.h5, // 폰트 크기
            fontWeight = FontWeight.Bold // 폰트 두께
        )
        Spacer(modifier = Modifier.height(4.dp)) // 바깥 여백 (margin)
        Text(
            text = "8 Cards",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Composable
fun StudyGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable {

            }
            .padding(16.dp)
    ) {
        Text(
            text = "c-plus-plus",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "12 Decks · 207 Cards",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Composable
fun MyDeckItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable {

            }
            .padding(16.dp)
    ) {
        Text(
            text = "recursion",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "11 Cards",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = "VisibilityOff",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun CardItem(card: Card) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.LightGray)
    ) {
        Text(
            text = card.front,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.ExtraBold
        )
        // 구분선 만들기
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = Color.LightGray
        )
        Text(
            text = card.back,
            modifier = Modifier.padding(16.dp),
            color = Color.Gray
        )
    }
}

