package com.gdsc.cheggprepreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gdsc.cheggprepreview.ui.theme.CheggPrepReviewTheme
import com.gdsc.cheggprepreview.ui.theme.DeepOrange
import androidx.compose.material.TextFieldDefaults
import com.gdsc.cheggprepreview.models.DECK_ADDED
import com.gdsc.cheggprepreview.models.DECK_CREATED
import com.gdsc.cheggprepreview.models.Deck
import com.gdsc.cheggprepreview.ui.theme.SampleDataSet


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheggPrepReviewTheme {
                HomeScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CheggPrepReviewTheme {
        Column {
//            DeckInSubject()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            StudyGuide()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            DeckItem()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            MyDeckItem()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            MakeMyDeck()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            SubjectItem()
//            Spacer(modifier = Modifier.height(8.dp))
//
//            CardItem()
//            Spacer(modifier = Modifier.height(8.dp))

//            FindFlashCards()
//
//            DeckTitleTextField()
//
              HomeScreen()

//            Row(){
//                FilterText(text = "A", selected = true){
//
//                }
//                FilterText(text = "B", selected = false){
//
//                }
//                FilterText(text = "C", selected = false){
//
//                }
//            }

            // 이 부분 코드 다시 짜기!!!!
            //CardItemField()
        }
    }
}

@Composable
fun FindFlashCards() {
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
            // vertical padding은 top/bottom에, horizontal padding은 start/end에 적용됨.
            .padding(vertical = 16.dp, horizontal = 8.dp)
            // 클릭해서 원하는 플래시카드 찾을 수 있도록
            .clickable {

            },
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
fun DeckTitleTextField() {
    var text by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.h4,
            placeholder = {
                Text(
                    text = " Untitled deck",
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
}

@Composable
fun HomeScreen() {
    var (selectedFilterIndex, setFilterIndex) = remember {
        mutableStateOf(0)
    }

    Scaffold( // Material 디자인을 쉽게 구현할 수 있게 해줌.
        topBar = {
            Column(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        bottom = 4.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                Text(
                    text = "CheggPrep",
                    style = MaterialTheme.typography.h5,
                    color = DeepOrange,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                FilterSection(selectedFilterIndex, setFilterIndex)
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {

//            // 1. 리스트의 각 아이템을 순회하는 forEach (현재 아이템은 it)
//            SampleDataSet.deckSample.forEach {
//                item {
//                    // 기존에 Spacer로 줬던 여백을 Modifier.padding으로 전달!
//                    DeckItem(deck = it, modifier = Modifier.padding(bottom = 8.dp))
//                }
//            }

            // 2. selectedFilterIndex로 필터링 구현하기!
            when(selectedFilterIndex){
                0-> // SampleDataSet의 모든 리스트 보여주기
                    SampleDataSet.deckSample.forEach{
                        item{
                            DeckItem(deck = it, modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                1-> // deck의 bookmarked가 true인 아이템들만 보여주기
                    SampleDataSet.deckSample.filter { it.bookmarked }.forEach{
                        item{
                            DeckItem(deck = it, modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                2-> // deck의 deckType이 DECK_CREATED인 아이템들만 보여주기
                    SampleDataSet.deckSample.filter { it.deckType == DECK_CREATED }.forEach{
                        item{
                            DeckItem(deck = it, modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
            }
        }
    }
}

@Composable
fun FilterSection(selectedFilterIndex: Int, setIndex: (Int) -> Unit) {
    Row {
        FilterText("All", selectedFilterIndex == 0) { setIndex(0) }
        Spacer(modifier = Modifier.width(8.dp))
        FilterText("Bookmarks", selectedFilterIndex == 1) { setIndex(1) }
        Spacer(modifier = Modifier.width(8.dp))
        FilterText("Created", selectedFilterIndex == 2) { setIndex(2) }
    }
}

@Composable
fun FilterText(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(enabled = !selected, onClick = onClick)
            // 선택하면 텍스트 배경을 회색으로, 아니면 투명색으로
            .background(color = if (selected) Color.LightGray else Color.Transparent)
            .padding(horizontal = 20.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun CardItemField() {
    val (frontText, setFrontText) = remember {
        mutableStateOf("")
    }

    val (backText, setBackText) = remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .border(2.dp, Color.LightGray)
    ) {
        //  ConstraintLayout은 다른 요소 또는 레이아웃들과의 관계를 통해서 배치가 이루어지므로
        //  요소의 가로, 세로에 제약 조건을 하나 이상 설정해줘야 한다.
        ConstraintLayout {
            val (front, back, delete, divider) = createRefs()

            TextField(
                value = frontText,
                onValueChange = setFrontText,
                modifier = Modifier
                    .constrainAs(front) {
                        // 자식의 top을 parent의 top에 붙인다.
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

            Divider(
                modifier = Modifier
                    .constrainAs(divider) {
                        top.linkTo(front.bottom)
                    }
                    .fillMaxWidth()
                    .height(2.dp),
                color = Color.LightGray
            )

            TextField(
                value = backText,
                onValueChange = setBackText,
                modifier = Modifier
                    .constrainAs(back) {
                        // 자식의 top을 parent의 bottom에 붙인다.
                        top.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    Text(
                        text = "Front",
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
                onClick = {

                },
                modifier = Modifier
                    .constrainAs(delete) {
                        bottom.linkTo(parent.bottom, 10.dp)
                        start.linkTo(parent.start, 8.dp)
                    }
            ) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
            }
        }
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

// DeckItem 하나만으로 모든 Deck를 표현할 수 있다!
@Composable
fun DeckItem(deck: Deck, modifier: Modifier) {
    Column( // vertical (수직)
        modifier = modifier
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
                text = deck.cardList.size.toString() + if(deck.cardList.size > 1) " Cards" else "Card",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
//            Icon( // bulid.gradle (Module:app)에 라이브러리 추가하기
//                imageVector = Icons.Default.Bookmark,
//                contentDescription = "bookmark",
//                tint = Color.Gray
//            )
            when(deck.deckType){
                DECK_CREATED -> { // 내가 만든 DECK이고,
                    if(deck.shared){ // 공유한 것이라면
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "shared",
                            tint = Color.Gray
                        )
                    }else{ // 공유하지 않은 것이라면
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = "not shared",
                            tint = Color.Gray
                        )
                    }
                }

                DECK_ADDED -> { // 다른 사람이 만든 DECK이고,
                    if(deck.bookmarked){ // 북마크를 추가한 경우라면
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
fun MakeMyDeck() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .border(
            width = 2.dp,
            color = Color.LightGray
        )
        .clickable {

        }
        .padding(20.dp)
    ) {
        Text(
            text = "Make your own cards",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "It's easy to create your own flashcard deck for free.",
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.NoteAdd,
                contentDescription = "add note",
                tint = Color.Blue
            )
            Text(
                text = "Get started",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
        }
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
fun CardItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.LightGray)
    ) {
        Text(
            text = "Operating Systems",
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
            text = "A request to execute an OS service-layer function",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray
        )
    }
}
