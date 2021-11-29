package com.gdsc.cheggprepclone.screens

import android.util.Log
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.gdsc.cheggprepclone.models.Card
import com.gdsc.cheggprepclone.ui.theme.DeepOrange
import com.gdsc.cheggprepclone.ui.theme.LightOrange
import com.gdsc.cheggprepclone.viewmodel.CheggViewModel
import com.gdsc.cheggprepclone.viewmodel.CreateState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

//enum class CreateState {
//    TitleScreen,
//    CardScreen
//}

@ExperimentalPagerApi
@ExperimentalComposeUiApi
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

    // 생성할 카드 리스트
    val cardList = remember {
        mutableStateListOf(Card("", ""))
    }

    // 뷰 모델의 createScreenState에 따라 컴포저블 호출하기
    when (viewModel.createScreenState.value) {
        CreateState.TitleScreen -> {
            CreateTitleScreen(
                deckTitle = deckTitle,
                setDeckTitle = setDeckTitle,
                visibility = visibility,
                setVisibility = setVisibility,

                // popBackStack -> 이전 화면으로 돌아가기 (Close)
                navigateBack = { navController.popBackStack() },

                // setScreenState -> CardScreen 화면으로 전환하기 (Next)
                //toCardScreen = { setScreenState(CreateState.CardScreen) }
                toCardScreen = viewModel::toCardScreen
            )
        }
        CreateState.CardScreen -> {
            CreateCardScreen(
                cardList = cardList, // SnapshotStateList<Card> 전달
                // 카드의 수정, 생성, 삭제
                setCard = { index, card ->
                    cardList[index] = card // Card field 변경
                },
                addCard = { cardList.add(Card("", "")) }, // 새 Card 추가
                removeCard = { index ->
                    cardList.removeAt(index) // Card 삭제

                    // 삭제된 뒤에 cardList 사이즈가 0인 경우 새 Card 추가
                    if (cardList.size == 0)
                        cardList.add(Card("", ""))
                },
                navigateBack = { navController.popBackStack() },
                onDone = { Log.d("cardList", cardList.joinToString("\n")) }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun CreateCardScreen(
    cardList: List<Card>, // 카드 리스트
    setCard: (index: Int, card: Card) -> Unit, // 카드 수정
    addCard: () -> Unit, // 카드 추가
    removeCard: (index: Int) -> Unit, // 카드 삭제
    navigateBack: () -> Unit,
    onDone: () -> Unit
) {
    // Pager의 상태 기억 (페이지 수, 현재 페이지 등)
    val pagerState = rememberPagerState()

    // 이전 페이지 수를 기억
    var prevPageCount by remember {
        mutableStateOf(pagerState.pageCount)
    }

    // HorizontalPager에서의 스와이프 애니메이션
    LaunchedEffect(key1 = pagerState.pageCount) { // 페이지 수가 변했을 때
        // 페이지가 추가된 경우, 마지막 페이지로 스와이프
        if (prevPageCount < pagerState.pageCount) {
            pagerState.animateScrollToPage(pagerState.pageCount - 1,
                pagerState.currentPageOffset)
        }

        Log.d("pagecount", (pagerState.pageCount - 1).toString())

        // prevPageCount 업데이트
        prevPageCount = pagerState.pageCount
    }

    Scaffold(
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
                            text = "${pagerState.currentPage + 1}/${pagerState.pageCount}", //
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = addCard, // 카드 추가
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
        Column {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // 생성한 카드들을 HorizontalPager를 이용해 가로로 스와이프하기
                HorizontalPager(
                    count = cardList.size, // 현재 생성된 카드 수
                    state = pagerState, // 선언한 pagerState 사용 (선언하지 않으면 내부에서 자동으로 사용)
                    // 양쪽에 이전, 다음 카드를 보여줌
                    contentPadding = PaddingValues(start = 32.dp, end = 32.dp)
                ) { page ->
                    CardItemField(
                        // page에 해당하는 card 전달
                        card = cardList[page],
                        // CardItemField가 전달하는 card로 해당 page에 set
                        setCard = { card ->
                            setCard(page, card)
                        },
                        // page에 해당하는 card 삭제
                        removeCard = { removeCard(page) }
                    )
                }
            }
        }
    }
}

@Composable
fun CardItemField(
    card: Card,
    setCard: (Card) -> Unit,
    removeCard: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.LightGray),
    ) {
        ConstraintLayout {
            val (front, back, delete, divider) = createRefs()

            // remember로 기억한 frontText, setFrontText 사용하기
            TextField(
                value = card.front, // frontText
                onValueChange = {
                    setCard(Card(it, card.back))// cardList 안의 아이템을 변경
                },
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
                    .height(2.dp),
                color = Color.LightGray
            )

            // remember로 기억한 backText, setBackText 사용하기
            TextField(
                value = card.back, // backText
                onValueChange = {
                    setCard(Card(card.front, it)) // cardList 안의 아이템 변경
                },
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

            IconButton(
                onClick = removeCard, // 카드 삭제
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

