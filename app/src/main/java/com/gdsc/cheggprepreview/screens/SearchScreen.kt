package com.gdsc.cheggprepreview.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Search
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
import com.gdsc.cheggprepreview.ui.theme.DeepOrange

enum class SearchState {
    ButtonScreen,
    QueryScreen,
    ResultScreen
}

@Composable
fun SearchScreen(navController: NavHostController) {
    val (screenState, setScreenState) = remember {
        mutableStateOf(SearchState.ButtonScreen)
    }

    val (queryString, setQueryString) = remember {
        mutableStateOf("")
    }

    // screenState에 따라 다른 화면 보여주기
    when (screenState) {
        SearchState.ButtonScreen -> {
            SearchButtonScreen {
                // 검색창이 비어 있지 않으면 ResultScreen
                if(queryString.isNotBlank()){
                    setScreenState(SearchState.ResultScreen)
                }else{ // 검색창이 비어있으면 QueryScreen
                    setScreenState(SearchState.QueryScreen)
                }
            }
        }

        SearchState.QueryScreen -> {
            SearchQueryScreen(
                queryString = queryString,
                setQueryString = setQueryString,
                toButtonScreen = { setScreenState(SearchState.ButtonScreen) },
                toResultScreen = { setScreenState(SearchState.ResultScreen) }
            )
        }

        SearchState.ResultScreen -> {
            SearchResultScreen(
                queryString = queryString,
                setQueryString = setQueryString,
                toButtonScreen = { setScreenState(SearchState.ButtonScreen) },
                onSearchKey = { /* 검색 결과 업데이트 */ }
            )
        }
    }

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
            FindFlashCards(onClick = { /* 텍스트 입력 창이 나오도록 */ })
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

            // SubjectItem 7번 보여주기
            repeat(7) {
                SubjectItem()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SearchButtonScreen(function: () -> Unit) {
    TODO("Not yet implemented")
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
        ){
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
fun SearchResultScreen(
    queryString: String,
    setQueryString: (String) -> Unit,
    toButtonScreen: () -> Unit,
    onSearchKey: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.25f),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "$queryString 검색 결과",
            style = MaterialTheme.typography.body1,
            fontSize = 20.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold
        )
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

