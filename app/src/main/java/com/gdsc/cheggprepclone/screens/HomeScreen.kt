package com.gdsc.cheggprepclone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gdsc.cheggprepclone.models.DECK_ADDED
import com.gdsc.cheggprepclone.models.DECK_CREATED
import com.gdsc.cheggprepclone.models.Deck
import com.gdsc.cheggprepclone.navigation.Screen
import com.gdsc.cheggprepclone.ui.theme.DeepOrange
import com.gdsc.cheggprepclone.viewmodel.CheggViewModel

@Composable
fun HomeScreen(navController: NavHostController, viewModel: CheggViewModel) {
    var (selectedFilterIndex, setFilterIndex) = remember {
        mutableStateOf(0)
    }

    Scaffold(
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
            when(selectedFilterIndex){
                0-> // SampleDataSet의 모든 리스트 보여주기
                    viewModel.myDeckList.forEach { // 뷰모델에 저장된 myDeckList 가져오기
                        item{
                            DeckItem(deck = it, modifier = Modifier.padding(bottom = 8.dp))
                            {
                                navController.navigate(
                                    Screen.Deck.route + "/${it.deckTitle}/${it.cardList.size}"
                                )
                            }
                        }
                    }
                1-> // deck의 bookmarked가 true인 아이템들만 보여주기
                    viewModel.myDeckList.filter { it.bookmarked }.forEach{
                        item{
                            DeckItem(deck = it, modifier = Modifier.padding(bottom = 8.dp))
                            {
                                navController.navigate(
                                    Screen.Deck.route + "/${it.deckTitle}/${it.cardList.size}"
                                )
                            }
                        }
                    }
                2-> // deck의 deckType이 DECK_CREATED인 아이템들만 보여주기
                    viewModel.myDeckList.filter { it.deckType == DECK_CREATED }.forEach{
                        item{
                            DeckItem(deck = it, modifier = Modifier.padding(bottom = 8.dp))
                            {
                                navController.navigate(
                                    Screen.Deck.route + "/${it.deckTitle}/${it.cardList.size}"
                                )
                            }
                        }
                    }
            }

            item { MakeMyDeck(onClick = { navController.navigate(Screen.Create.route)}) }
        }
    }
}

// DeckItem 하나만으로 모든 Deck를 표현할 수 있다!
@Composable
fun DeckItem(deck: Deck, modifier: Modifier, onClick: () -> Unit) {
    Column( // vertical (수직)
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.LightGray
            )
            .clickable(onClick = onClick)
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
fun MakeMyDeck(onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .border(
            width = 2.dp,
            color = Color.LightGray
        )
        .clickable(onClick = onClick)
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
