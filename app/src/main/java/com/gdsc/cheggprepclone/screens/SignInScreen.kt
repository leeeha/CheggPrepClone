package com.gdsc.cheggprepclone.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gdsc.cheggprepclone.R
import com.gdsc.cheggprepclone.utils.AuthResultContract
import com.gdsc.cheggprepclone.viewmodel.CheggViewModel
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SignInScreen(viewModel: CheggViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var text by remember {
        mutableStateOf<String?>(null)
    }
    val user by remember(viewModel) {
        viewModel.user
    }.collectAsState()
    val signInRequestCode = 1
    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { task ->
            try {
                val account = task?.getResult(ApiException::class.java)
                if (account == null) {
                    text = "Google Sign In Failed"
                } else {
                    coroutineScope.launch {
                        viewModel.triggerAuth(account.idToken!!) // 사용자를 구분하는 token
                    }
                }
            } catch (e: ApiException) {
                text = "Google SignIn Failed"
            }
        }
    SignInView(errorText = text, onClick = {
        text = null
        authResultLauncher.launch(signInRequestCode)
        // authResultLauncer launch : SignIn Intent 실행 및 결과 처리 (AuthResultContract)
    })
    user?.let {
        viewModel.toMainScreen() // Flow를 통해 user가 전달되면 MainScreen으로 이동
    }
}

@ExperimentalMaterialApi
@Composable
fun SignInView(errorText: String?, onClick: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GoogleSignInButtonUi(
            text = "Sign Up With Google",
            loadingText = "Signing In....",
            onClicked = onClick
            // authResultLauncer launch : SignIn Intent 실행 및 결과 처리 (AuthResultContract)
        )
        errorText?.let { // errorText가 전달된 경우 이를 표시
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = it)
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun GoogleSignInButtonUi(
    text: String = "",
    loadingText: String = "",
    onClicked: () -> Unit
) {
    var clicked by remember {
        mutableStateOf(false)
    }

    Surface(
        onClick = { clicked = !clicked },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        color = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_icon),
                contentDescription = "Google Sign Button",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))

            // 클릭 시 loadingText 보여줌
            Text(text = if (clicked) loadingText else text)

            if (clicked) { // 클릭 시 실행
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colors.primary
                )
                onClicked()
                // authResultLauncer launch : SignIn Intent 실행 및 결과 처리 (AuthResultContract)
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun GoogleButtonPreview() {
    GoogleSignInButtonUi(
        text = "Sign Up With Google",
        loadingText = "Signing In ....",
        onClicked = {}
    )
}