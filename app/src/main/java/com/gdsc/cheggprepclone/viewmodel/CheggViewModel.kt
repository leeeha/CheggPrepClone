package com.gdsc.cheggprepclone.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.gdsc.cheggprepclone.models.Deck
import com.gdsc.cheggprepclone.models.User
import com.gdsc.cheggprepclone.screens.SearchState
import com.gdsc.cheggprepclone.ui.theme.SampleDataSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

/*
    https://yoon-dailylife.tistory.com/72
    최근 코틀린 코루틴 라이브러리 (1.4.1)에서 Stable API로 배포된 StateFlow, SharedFlow는
    observable mutable state를 관리할 때 사용하며, LivaData 보다 더 좋은 성능을 갖는다.
 */
class CheggViewModel: ViewModel() {
    // SignIn
    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> = _user

    fun signIn(email: String, displayName: String){
        _user.value = User(email, displayName)
    }

    // SignOut
    fun signOut() {
        _user.value = null
    }

    // MainActivity에서 firebaseAuth 호출하기
    private val _firebaseAuth = MutableStateFlow(false)
    val firebaseAuth = _firebaseAuth

    private val _token = MutableStateFlow("")
    val token = _token

    fun triggerAuth(idToken: String) { // firebaseAuthWithGoogle을 호출하기 위함
        _token.value = idToken
        _firebaseAuth.value = true
    }

    fun completeAuth() { // firebaseAuthWithGoogle 실행 후
        _firebaseAuth.value = false
    }

    // 내가 생성하거나 북마크한 deck 목록
    var myDeckList = mutableStateListOf<Deck>()
        private set
        // 뷰모델에 저장된 값을 얻는 건 (getter) 자유롭지만, 값을 수정하는 건 (setter) 제한적이라는 뜻

    // 모든 사용자가 생성한 deck 목록
    var totalDeckList = mutableStateListOf<Deck>()
        private set

    // 사용자가 입력한 검색어
    var queryString = mutableStateOf("")
        private set

    // queryString 값 변경
    fun setQueryString(query: String){
        queryString.value = query
    }

    // SearchScreen에서의 화면 전환에 사용되는 변수
    var searchScreenState = mutableStateOf(SearchState.ButtonScreen)
        private set

    // ButtonScreen으로 이동
    fun toButtonScreen(){
        searchScreenState.value = SearchState.ButtonScreen
    }

    // QueryScreen으로 이동
    fun toQueryScreen(){
        searchScreenState.value = SearchState.QueryScreen
    }

    // ResultScreen으로 이동
    fun toResultScreen(){
        searchScreenState.value = SearchState.ResultScreen
    }

    // totalDeckList에서 queryString을 포함하고 있는 deck 목록들을 찾아 반환한다.
    fun getQueryResult() = totalDeckList.filter {  deck ->
            deck.deckTitle.lowercase(Locale.getDefault())
                .contains(queryString.value.lowercase())
    }.toMutableStateList()

    // createScreenState에 따라 TitleScreen이나 CardScreen을 보여준다.
    var createScreenState = mutableStateOf(CreateState.TitleScreen)
        private set

    fun toCardScreen() {
        createScreenState.value = CreateState.CardScreen
    }

    // MoreScreen
    var moreScreenState = mutableStateOf(MoreState.MainScreen)
        private set

    fun toLogInScreen() {
        moreScreenState.value = MoreState.LogInScreen
    }

    fun toMainScreen() {
        moreScreenState.value = MoreState.MainScreen
    }

    // 샘플 데이터 사용해서 deck 목록 초기화 (나중에 파이어베이스 사용할 예정)
    init {
        myDeckList = SampleDataSet.myDeckSample.toMutableStateList()
        totalDeckList = SampleDataSet.totalDeckSample.toMutableStateList()
    }


}