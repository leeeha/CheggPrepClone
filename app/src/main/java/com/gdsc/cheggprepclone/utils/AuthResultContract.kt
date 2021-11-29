package com.gdsc.cheggprepclone.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

// GoogleSignInAccount
// Class that holds the basic account information of the signed in Google user.
// 구글 유저에 대한 기본 계정 정보를 갖고 있는 클래스

class AuthResultContract : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
    // 인텐트로 로그인 계정 정보 전달하기
    override fun createIntent(context: Context, input: Int?): Intent =
        getGoogleSignInClient(context).signInIntent.putExtra("input", input)

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        return when(resultCode){
            // 인텐트로부터 로그인 계정 정보 가져오기
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> null
        }
    }
}