package com.gdsc.cheggprepreview.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gdsc.cheggprepreview.models.DECK_ADDED
import com.gdsc.cheggprepreview.models.DECK_CREATED
import com.gdsc.cheggprepreview.models.Deck
import com.gdsc.cheggprepreview.navigation.Screen
import com.gdsc.cheggprepreview.ui.theme.DeepOrange
import com.gdsc.cheggprepreview.viewmodel.CheggViewModel

enum class SearchState {
    ButtonScreen,
    QueryScreen,
    ResultScreen
}

@Composable
fun SearchScreen(navController: NavHostController, viewModel: CheggViewModel) {
    // 뷰모델에 저장된 screenState에 따라 다른 화면 보여주기
    when (viewModel.searchScreenState.value) {
        SearchState.ButtonScreen -> {
            SearchButtonScreen {
                // 뷰모델에 저장된 검색어가 있으면 ResultScreen으로 이동
                if (viewModel.queryString.value.isNotBlank()) {
                    viewModel.toResultScreen()
                } else { // 없으면 QueryScreen으로 이동
                    viewModel.toQueryScreen()
                }
            }
        }

        SearchState.QueryScreen -> {
            SearchQueryScreen(
                queryString = viewModel.queryString.value,
                // cf. 더블콜론(::)은 함수 및 클래스 참조 등에 사용된다.
                setQueryString = viewModel::setQueryString,
                toButtonScreen = viewModel::toButtonScreen,
                toResultScreen = viewModel::toResultScreen
            )
        }

        SearchState.ResultScreen -> {
            SearchResultScreen(
                queryString = viewModel.queryString.value,
                setQueryString = viewModel::setQueryString,

                // 뷰모델로부터 검색 결과를 받아 DeckScreen에 전달
                getQueryResult = viewModel::getQueryResult,
                toButtonScreen = viewModel::toButtonScreen,
                toDeckScreen = {
                    navController.navigate(
                        Screen.Deck.route + "/${it.deckTitle}/${it.cardList.size}"
                    ) // deck 클릭 시 화면 전환
                }
            )
        }
    }
 }

@Composable
fun SearchResultScreen(
    queryString: String,
    setQueryString: (String) -> Unit,
    getQueryResult: () -> List<Deck>,
    toButtonScreen: () -> Unit,
    toDeckScreen: (Deck) -> Unit
) {
    val (queryResult, setQueryResult) = remember {
        mutableStateOf(getQueryResult())
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                queryString = queryString,
                setQueryString = setQueryString,
                onBackButtonClick = toButtonScreen,
                onSearchKey = {
                    setQueryResult(getQueryResult())
                }
            )
        }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            queryResult.forEach {
                item {
                    DeckInResult(
                        deck = it,
                        modifier = Modifier.padding(bottom = 8.dp),
                        onClick = toDeckScreen
                    )
                }
            }
            item{
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun SearchButtonScreen(onButtonClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = Color.Transparent,
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Find flashcards",
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            FindFlashCards(onClick = onButtonClick)
            Spacer(modifier = Modifier.height(24.dp))
            Divider(
                Modifier
                    .fillMaxWidth(.15f)
                    .height(4.dp),
                color = DeepOrange
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Choose your subject",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Jump into studying with free flashcards that are right for you",
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.height(16.dp))
            repeat(7) {
                SubjectItem()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SearchQueryScreen(
    queryString: String,
    setQueryString: (String) -> Unit,
    toButtonScreen: () -> Unit,
    toResultScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                queryString = queryString,
                setQueryString = setQueryString,
                onBackButtonClick = toButtonScreen,
                onSearchKey = toResultScreen
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.25f),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "what are you learning today?",
                style = MaterialTheme.typography.body1,
                fontSize = 20.sp,
                color = Color.LightGray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SearchTopBar(
    queryString: String,
    setQueryString: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onSearchKey: () -> Unit
) {
    TopAppBar(
        elevation = 0.dp,
        backgroundColor = Color.White,
        // TopAppBar에 navigationIcon, title, actions 배치
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        },
        title = {
            TextField(
                value = queryString,
                onValueChange = setQueryString,
                placeholder = {
                    Text(
                        text = "Find flashcards",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = DeepOrange,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent

                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchKey()
                    }
                ),
            )
        },
        actions = {
            if (queryString.isNotBlank()) {
                IconButton(onClick = { setQueryString("") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "delete")
                }
            }
        }
    )
}

@Composable
fun FindFlashCards(onClick: () -> Unit) {
    Row( // Icon, Text 수평 배치
        modifier = Modifier
            .fillMaxWidth() // match_parent
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            // clip을 설정해주지 않으면, border를 CircleShape으로 하더라도
            // 클릭 영역이 네모난 Row 영역이 됨.
            .clip(shape = CircleShape)
            .clickable(onClick = onClick)
            // vertical padding은 top/bottom에, horizontal padding은 start/end에 적용됨.
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search",
            tint = Color.LightGray
        )
        Text(
            text = "Find flashcards",
            style = MaterialTheme.typography.body1,
            color = Color.LightGray
        )
    }
}

@Composable
fun SubjectItem() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .border(
            shape = RoundedCornerShape(size = 8.dp),
            width = 2.dp,
            color = Color.LightGray
        )
        .clip(shape = RoundedCornerShape(size = 8.dp))
        .clickable {

        }
        .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Computer,
            contentDescription = "computer",
            tint = DeepOrange, // Color.kt 파일에 색상 추가
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = "  Computer Science",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DeckInResult(
    deck: Deck,
    modifier: Modifier,
    onClick: (Deck) -> Unit
) {
    Column( // vertical (수직)
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable(onClick = {
                onClick(deck)
            })
            .padding(16.dp)
    ) {
        Text(
            text = deck.deckTitle,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row( // horizontal (수평)
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
            // 가로 방향으로 요소 간의 간격 띄우기
            // https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/Arrangement
        ) {
            Text(
                text = deck.cardList.size.toString() + if (deck.cardList.size > 1) " Cards" else "Card",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            when (deck.deckType) {
                DECK_CREATED -> { // 내가 만든 DECK이고,
                    if (deck.shared) { // 공유한 것이라면
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "shared",
                            tint = Color.Gray
                        )
                    } else { // 공유하지 않은 것이라면
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "not shared",
                            tint = Color.Gray
                        )
                    }
                }

                DECK_ADDED -> { // 다른 사람이 만든 DECK이고,
                    if (deck.bookmarked) { // 북마크를 추가한 경우라면
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "bookmark",
                            tint = Color.Gray
                        )
                    } // 북마크 추가하지 않은 경우는 아이콘 X
                }
            }
        }
    }
}
