package com.gdsc.cheggprepreview.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.gdsc.cheggprepreview.ui.theme.DeepOrange
import com.gdsc.cheggprepreview.ui.theme.LightOrange
import com.gdsc.cheggprepreview.viewmodel.CheggViewModel

enum class CreateState {
    TitleScreen,
    CardScreen
}

@Composable
fun CreateScreen(navController: NavHostController, viewModel: CheggViewModel) {
    // 사용자가 입력한 deckTitle 기억해서 DeckTitleTextField 컴포저블에 전달하기
    val (deckTitle, setDeckTitle) = remember {
        mutableStateOf("")
    }

    // 사용자가 설정한 visibility 기억해서 CreateTitleScreen의 스위치에 적용하기
    val (visibility, setVisibility) = remember {
        mutableStateOf(true)
    }

    // 현재 화면 상태 기억하기 (TitleScreen, CardScreen)
    val (screenState, setScreenState) = remember {
        mutableStateOf(CreateState.TitleScreen)
    }

    // screenState에 따라 다른 화면 보여주기
    when (screenState) {
        CreateState.TitleScreen -> {
            CreateTitleScreen(
                deckTitle = deckTitle,
                setDeckTitle = setDeckTitle,
                visibility = visibility,
                setVisibility = setVisibility,
                // popBackStack -> 이전 화면으로 돌아가기 (Close)
                navigateBack = { navController.popBackStack() },
                // setScreenState -> CardScreen 화면으로 전환하기 (Next)
                toCardScreen = { setScreenState(CreateState.CardScreen) }
            )
        }

        CreateState.CardScreen -> {
            CreateCardScreen(
                navigateBack = { navController.popBackStack() }, // Close
                onDone = { /*TODO*/ } // Done
            )
        }
    }
}

@Composable
fun CreateTitleScreen(
    deckTitle: String,
    setDeckTitle: (String) -> Unit,
    visibility: Boolean,
    setVisibility: (Boolean) -> Unit,
    navigateBack: () -> Unit,
    toCardScreen: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            // TopAppBar에 title, navigationIcon, actions 배치
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Create new deck",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = { // 이전 화면으로 돌아가는 Close 버튼
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "close create screen"
                    )
                }
            },
            actions = { // CardScreen으로 넘어가는 Next 버튼
                TextButton(onClick = toCardScreen, enabled = deckTitle.isNotBlank()) {
                    Text(
                        "Next",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Column: DeckTitleTextField, (Text, Switch), Text
            DeckTitleTextField(deckTitle, setDeckTitle)
            Spacer(modifier = Modifier.height(16.dp))

            // Row: Text, Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Visible to everyone",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = visibility,
                    onCheckedChange = setVisibility,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = DeepOrange,
                        checkedTrackColor = LightOrange
                    )
                )
            }

            Text(text = "Other Students can find, view, and study\nthis deck")
        }
    }
}

@Composable
fun DeckTitleTextField(text: String, setText: (String) -> Unit) {
    TextField(
        // remember로 기억한 deckTitle, setDeckTitle 사용하기
        value = text, onValueChange = setText,
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.h4,
        placeholder = {
            Text(
                text = "Untitled deck",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold,
                color = Color.LightGray
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = DeepOrange,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray
        ),
        maxLines = 2
    )
}

@Preview
@Composable
fun CreateCardScreenPreview() {
    CreateCardScreen({}, {})
}

@Composable
fun CreateCardScreen(
    navigateBack: () -> Unit,
    onDone: () -> Unit
) {
    Scaffold(
        // 1. topBar: @Composable () → Unit,
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = Color.Transparent,
                // TopAppBar에 title, navigationIcon, actions 배치
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "1/10",
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "close create screen"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onDone) {
                        Text(
                            "Done",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
        // 2. floatingActionButton: @Composable () → Unit
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                backgroundColor = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 2.dp,
                        color = DeepOrange,
                        shape = CircleShape
                    )
            ) {
                Icon( // 새로운 카드 추가하는 버튼
                    imageVector = Icons.Default.Add,
                    contentDescription = "add new card",
                    modifier = Modifier.fillMaxSize(.8f)
                )
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CardItemField()
        }
    }
}

@Composable
fun CardItemField() {
    // 사용자가 입력한 Front, Back 텍스트 내용 기억하기
    val (frontText, setFrontText) = remember {
        mutableStateOf("")
    }

    val (backText, setBackText) = remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .padding(8.dp)
            .border(2.dp, Color.LightGray),
    ) {
        ConstraintLayout {
            val (front, back, delete, divider) = createRefs()

            // remember로 기억한 frontText, setFrontText 사용하기
            TextField(
                value = frontText,
                onValueChange = setFrontText,
                modifier = Modifier
                    .constrainAs(front) {
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = MaterialTheme.typography.h6,
                placeholder = {
                    Text(
                        text = "Front",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.LightGray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = DeepOrange,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 2
            )

            Divider( // 구분선
                modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(front.bottom)
                    }
                    .fillMaxWidth()
                    .height(2.dp),
                color = Color.LightGray
            )

            // remember로 기억한 backText, setBackText 사용하기
            TextField(
                value = backText,
                onValueChange = setBackText,
                modifier = Modifier
                    .constrainAs(back) {
                        top.linkTo(divider.bottom)
                    }
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp),
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    Text(
                        text = "Back",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.LightGray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = DeepOrange,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            IconButton( // 카드 삭제 버튼
                onClick = { /*TODO*/ },
                modifier = Modifier.constrainAs(delete) {
                    bottom.linkTo(parent.bottom, 10.dp)
                    start.linkTo(parent.start, 8.dp)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "delete"
                )
            }
        }
    }
}