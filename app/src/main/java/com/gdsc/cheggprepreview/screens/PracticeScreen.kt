package com.gdsc.cheggprepreview.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gdsc.cheggprepreview.ui.theme.DeepOrange
import com.gdsc.cheggprepreview.ui.theme.SampleDataSet
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun PracticeScreen() {
    // 샘플 데이터에서 첫번째 deck의 카드 리스트를 가져옴.
    val sampleData = SampleDataSet.myDeckSample[0].cardList

    // Pager의 상태 (페이지 수, 현재 페이지 등)
    val pagerState = rememberPagerState()

    // 현재 카드 수 기억하기
    val (count, setCount) = remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = true) { // 시작 효과 : 0 -> 1
        setCount(1f)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        setCount((pagerState.currentPage + 1).toFloat())
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1}/${sampleData.size}",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "close screen"
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Outlined.Shuffle,
                        contentDescription = "shuffle cards"
                    )
                }
            },
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
        )
    }) {
        Column {
            Box(modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)) {
                // 현재 페이지 위치를 알려주는 프로그래스바
                ProgressBar(count = count, totCount = sampleData.size)
            }
            Box {
                // 페이지를 가로로 넘기는 데 사용되는 HorizontalPager
                HorizontalPager(
                    count = sampleData.size,
                    // 선언한 pagerState 사용 (선언하지 않으면 내부에서 자동으로 사용)
                    state = pagerState,
                    // 양쪽에 이전, 다음 카드를 보여줌.
                    contentPadding = PaddingValues(32.dp)
                ) { page ->
                    FlipCard(
                        back = {
                            CardBack(text = sampleData[page].back)
                        },
                        front = {
                            CardFront(text = sampleData[page].front)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    color: Color = DeepOrange,
    animDuration: Int = 300,
    animDelay: Int = 0,
    count: Float,
    totCount: Int,
) {
    val curPercentage by animateFloatAsState(
        targetValue = count / totCount,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay,
            easing = LinearOutSlowInEasing
        )
    )
    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(CircleShape),
        progress = curPercentage,
        color = color,
        backgroundColor = Color.LightGray
    )
}

// FlipCard 구현
// https://stackoverflow.com/questions/68044576/how-to-make-flipcard-animation-in-jetpack-compose

// CardFace라는 enum 클래스를 생성하여 angle에 따라 Front와 Back 구별
enum class CardFace(val angle: Float) {
    // 현재 Front면 다음은 Back
    Front(angle = 0f) {
        override val next: CardFace
            get() = Back
    },

    // 현재 Back이면 다음은 Front
    Back(angle = 180f) { // 180도씩 전환
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

enum class RotationAxis {
    AxisX,
    AxisY,
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    Column(Modifier.padding(16.dp)) {
        CardFront(text = "Iterations")
        CardBack(
            text = "Repeated steps in an SDLC process;" +
                    " for example, in the UP, each iteration, consisting of specific activities"
        )
    }
}

@Composable
fun CardFront(text: String) { // 카드 앞면
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 2.dp, color = Color.LightGray)
            .padding(16.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.h3)
    }
}

@Composable
fun CardBack(text: String) { // 카드 뒷면
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 2.dp, color = Color.LightGray)
            .verticalScroll(state = scrollState)
            .padding(16.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.h4)
    }
}

@ExperimentalMaterialApi
@Composable
fun FlipCard(
    modifier: Modifier = Modifier,
    axis: RotationAxis = RotationAxis.AxisY,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
) {
    // 앞면, 뒷면 상태
    var cardFace by remember {
        mutableStateOf(CardFace.Front)
    }

    // cardFace의 angle을 tween 애니메이션에 적용
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )

    Box(modifier = Modifier.padding(8.dp)) {
        Card(
            // 클릭하면 카드 뒤집기
            onClick = { cardFace = cardFace.next },
            // 변화하는 rotation 값을 rotationX 또는 rotationY로 사용
            modifier = modifier
                .graphicsLayer {
                    if (axis == RotationAxis.AxisX) {
                        rotationX = rotation.value
                    } else {
                        rotationY = rotation.value
                    }
                    cameraDistance = 12f * density
                },
        ) {
            // 애니메이션이 적용된 cardFace의 angle이 90도 이하일 때는
            // 카드의 앞면 보여주기
            if (rotation.value <= 90f) {
                Box(Modifier.fillMaxSize()) {
                    front()
                }
            } else { // 90도보다 클 때는 180도 전환해서 뒷면 보여주기
                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            if (axis == RotationAxis.AxisX) {
                                rotationX = 180f
                            } else {
                                rotationY = 180f
                            }
                        },
                ) {
                    back()
                }
            }
        }
    }
}