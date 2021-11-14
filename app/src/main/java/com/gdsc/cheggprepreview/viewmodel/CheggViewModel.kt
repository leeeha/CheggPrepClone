package com.gdsc.cheggprepreview.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.gdsc.cheggprepreview.models.Deck
import com.gdsc.cheggprepreview.screens.SearchState
import com.gdsc.cheggprepreview.ui.theme.SampleDataSet
import java.util.*

class CheggViewModel: ViewModel() {
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
//    fun getQueryResult() : SnapshotStateList<Deck> {
//        return totalDeckList.filter {
//            it.deckTitle.lowercase(Locale.getDefault())
//              .contains(queryString.value.lowercase())
//        }.toMutableStateList()
//    }

    // it 생략한 경우
//    fun getQueryResult() : SnapshotStateList<Deck> {
//        return totalDeckList.filter { deck ->
//            deck.deckTitle.lowercase(Locale.getDefault())
//                .contains(queryString.value.lowercase())
//        }.toMutableStateList()
//    }

    // 코틀린은 리턴 타입을 유추할 수 있기 때문에 다음과 같이 코드를 간략히 작성할 수 있다.
    // 필터링 결과인 리턴값을 함수명에 대입하는 것처럼 보인다.
    fun getQueryResult() = totalDeckList.filter {  deck ->
            deck.deckTitle.lowercase(Locale.getDefault())
                .contains(queryString.value.lowercase())
    }.toMutableStateList()

    // 샘플 데이터 사용해서 deck 목록 초기화 (나중에 파이어베이스 사용할 예정)
    init {
        myDeckList = SampleDataSet.myDeckSample.toMutableStateList()
        totalDeckList = SampleDataSet.totalDeckSample.toMutableStateList()
    }


}