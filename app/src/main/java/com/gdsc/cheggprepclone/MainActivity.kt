package com.gdsc.cheggprepclone

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gdsc.cheggprepclone.ui.theme.CheggPrepReviewTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gdsc.cheggprepclone.models.Card
import com.gdsc.cheggprepclone.navigation.BottomNavigationBar
import com.gdsc.cheggprepclone.navigation.Screen
import com.gdsc.cheggprepclone.screens.*
import com.gdsc.cheggprepclone.viewmodel.CheggViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    // 'lateinit' allows initializing a not-null property outside of a constructor
    private lateinit var auth: FirebaseAuth

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        setContent {
            CheggPrepReviewTheme {
                //PracticeScreen()
                val navController = rememberNavController()

                val cheggViewModel: CheggViewModel = viewModel()

                // currentUser??? null??? ????????? ????????? ?????? user??? ??????
                auth.currentUser?.let {
                    LaunchedEffect(key1 = true) {
                        cheggViewModel.signIn(it.email!!, it.displayName!!)
                    }
                }

                val firebaseAuth = cheggViewModel.firebaseAuth.collectAsState()

                if (firebaseAuth.value) { // firebaseAuth??? true??? ???
                    firebaseAuthWithGoogle(cheggViewModel.token.value, cheggViewModel::signIn)
                    cheggViewModel.completeAuth() // ????????? ??? false??? ??????
                }

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
                            HomeScreen(navController, cheggViewModel)
                        }

                        composable(Screen.Search.route) {
                            showBottomBar(true)
                            SearchScreen(navController, cheggViewModel)
                        }

                        composable(Screen.Create.route) {
                            showBottomBar(false)
                            CreateScreen(navController, cheggViewModel)
                        }

                        composable(Screen.More.route) {
                            showBottomBar(true)
                            MoreScreen(navController, cheggViewModel)
                        }

                        composable(Screen.Deck.route + "/{deckTitle}/{cardsNum}") { backStackEntry ->
                            val deckTitle = backStackEntry.arguments?.getString("deckTitle") ?: "no title"
                            val cardsNum = backStackEntry.arguments?.getString("cardsNum")?.toInt() ?: 0
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

    /*
        ???????????? ??????????????? ??????????????? GoogleSignInAccount ???????????? id ????????? ????????????
        Firebase ????????? ?????? ????????? ????????????, ??? ????????? Firebase??? ????????????.
     */
    private fun firebaseAuthWithGoogle(
        idToken: String,
        signIn: (String, String) -> Unit
    ) {
        // ????????? ??? ???
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        // Firebase??? ?????? ???????????? ??????
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // ????????? ????????????, ????????? ??? ????????? ????????? UI??? ???????????? ??????.
                    Log.d("Firebase", "signInWithCredential: success")
                    signIn(auth.currentUser!!.email!!, auth.currentUser!!.displayName!!)
                }
                else {
                    // ???????????? ????????????, ??????????????? ?????? ???????????? ????????????.
                    Log.d("Firebase", "signInWithCredential: failure", task.exception)
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
    Column( // vertical (???????????? ??????)
        modifier = Modifier // https://foso.github.io/Jetpack-Compose-Playground/general/modifier/
            .fillMaxWidth() // match_parent
            .border( // ????????? ????????? ??????
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable {

            }
            .padding(16.dp) // ?????? ??????
    ) {
        Text(
            text = "recursion",
            style = MaterialTheme.typography.h5, // ?????? ??????
            fontWeight = FontWeight.Bold // ?????? ??????
        )
        Spacer(modifier = Modifier.height(4.dp)) // ?????? ?????? (margin)
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
            text = "12 Decks ?? 207 Cards",
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
        // ????????? ?????????
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

