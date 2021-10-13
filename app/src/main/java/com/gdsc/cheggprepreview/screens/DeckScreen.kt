package com.gdsc.cheggprepreview.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gdsc.cheggprepreview.CardItem
import com.gdsc.cheggprepreview.models.Card
import com.gdsc.cheggprepreview.ui.theme.CheggPrepReviewTheme
import com.gdsc.cheggprepreview.ui.theme.DeepOrange

@Composable
fun DeckScreen(navController: NavController, title: String, cardsNum: Int) {
    Scaffold(topBar = {
        TopAppBar(
            elevation = 0.dp,
            backgroundColor = Color.White,
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "navigation back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* 공유하기 버튼 눌렀을 때 */ }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "share")
                }
                IconButton(onClick = { /* 더보기 버튼 눌렀을 때 */}) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
                }
            }
        )
    }, bottomBar = {
        Column(modifier = Modifier.background(color = Color.White)) {
            Divider(Modifier.height(2.dp), color = Color.LightGray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier
                    .clip(shape = CircleShape)
                    .clickable { /* 카드 연습하기 텍스트를 눌렀을 때 */ }
                    .background(color = DeepOrange)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Practice all cards",
                        color = Color.White,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = cardsNum.toString() + if (cardsNum > 1) " Cards" else " Card",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            repeat(cardsNum) {
                CardItem(card = Card("Title", "description"))
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}