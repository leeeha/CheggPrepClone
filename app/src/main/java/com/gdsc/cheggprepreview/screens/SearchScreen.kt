package com.gdsc.cheggprepreview.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gdsc.cheggprepreview.ui.theme.DeepOrange

@Composable
fun SearchScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = Color.Transparent,
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
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
        ){
            FindFlashCards(onClick = { /* 텍스트 입력 창이 나오게 해야 한다!! */ })
            Spacer(modifier = Modifier.height(24.dp))
            Divider(
                Modifier
                    .fillMaxWidth(.15f)
                    .height(4.dp),
                color = DeepOrange
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Choose your subject",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Jump into studying with free flashcards that are right for you",
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.height(16.dp))
            repeat(7){
                SubjectItem()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
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

