package com.gdsc.cheggprepclone.utils

import android.content.Context
import com.gdsc.cheggprepclone.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

/*
    - GoogleSignInClient: 구글 로그인 API와 상호작용하는 클라이언트
    - google-services.json 파일에서 oauth_client의 client_id 가져오기

    여기서 oauth란, 다양한 플랫폼 환경에서 권한을 부여하기 위한 산업 표준 프로토콜!
    간단하게 말하면, 인증(Authentication)과 권한(Authorization)을 획득하는 것
    - 인증은 시스템 접근을 확인하는 것 (로그인) -> 인증만 하는 것은 openID
    - 권한은 행위의 권리를 검증하는 것
    https://showerbugs.github.io/2017-11-16/OAuth-%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%BC%EA%B9%8C
 */
fun getGoogleSignInClient(context: Context) : GoogleSignInClient {
    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(context.getString(R.string.web_client_id)) // client_id 전달
        .build()

    return GoogleSignIn.getClient(context, signInOptions)
}