package com.gdsc.cheggprepreview.models

/*
    홈 화면에서 Bookmarks와 Created로 나누고,
    학습 이력이 있는 것 또한 보여주기 때문에
    자신이 만든 것과 다른 사람이 만든 것을 구분해야 함. (deckType)
 */
data class Deck(
    val deckType: Int,
    val deckTitle: String,
    val shared: Boolean,
    val bookmarked: Boolean,
    val cardList: List<Card> // deck에 포함된 Card의 리스트!
)

// deckType 구분
const val DECK_CREATED = 0
const val DECK_ADDED = 1
