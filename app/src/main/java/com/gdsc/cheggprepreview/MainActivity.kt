package com.gdsc.cheggprepreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
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
import com.gdsc.cheggprepreview.screens.CreateScreen
import com.gdsc.cheggprepreview.screens.HomeScreen
import com.gdsc.cheggprepreview.screens.MoreScreen
import com.gdsc.cheggprepreview.screens.SearchScreen
import com.gdsc.cheggprepreview.ui.theme.SampleDataSet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateScreen()
        }
            /*
//            CheggPrepReviewTheme {
//                DeckTitleTextField()
//            }

            // 박스의 크기 상태를 기억하도록
            var sizeState by remember { mutableStateOf(200.dp)}

            // 연속적으로 값이 변하도록
            val size by animateDpAsState(targetValue = sizeState,
              // tween, spring, keyframes 중에 골라서 사용하기
//            tween(
//                durationMillis = 3000,
//                delayMillis = 300,
//                easing = LinearOutSlowInEasing
//            )
            )

            // 유튜브 11강 참고하기
//            val color by infiniteTransition.animateFloat(
//                initialValue = Color.Red,
//                targetValue = Color.Green,
//                animationSpec = infiniteRepeatable(
//                    tween(durationMillis = 2000),
//                    repeatMode = RepeatMode.Reverse
//                )
//            )

            Box(modifier = Modifier
                .size(size) // dp 변화에 따라 부드럽게 크기가 변하도록
                .background(Color.Red),
                contentAlignment = Alignment.Center){
                // 버튼을 클릭하면 50dp 확대
                Button(onClick = { sizeState += 50.dp}){
                    Text("Increase Size")
                }
            }
        }
             */

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
            //DeckTitleTextField()
//
              //HomeScreen()

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

//            CardItemField()
        }
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
            .fillMaxWidth()
            .border(2.dp, Color.LightGray)
    ) {
        //  ConstraintLayout은 다른 요소 또는 레이아웃들과의 관계를 통해서 배치가 이루어지므로
        //  요소의 가로 및 세로에 제약 조건을 하나 이상 설정해줘야 한다.
        ConstraintLayout {
            val (front, back, delete, divider) = createRefs()

            TextField(
                value = frontText,
                onValueChange = setFrontText,
                modifier = Modifier
                    .constrainAs(front) {
                        // 본인의 top을 parent의 top에 붙인다.
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
                        // 본인의 top을 front의 bottom에 붙인다.
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
                        // 본인의 top을 divider의 bottom에 붙인다.
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

