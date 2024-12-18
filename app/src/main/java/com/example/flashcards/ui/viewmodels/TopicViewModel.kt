package com.example.flashcards.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.DbSingleton
import com.example.flashcards.data.room.models.Card
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.ui.repository.FlashCardsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class TopicState(
    val topics: List<Topic> = emptyList(),
    val cards: List<Card> = emptyList(),
    val currentCardInd: Int = 0,
    val currentCard: Card? = null,
)

class TopicViewModel(
    private val repository: FlashCardsRepository = DbSingleton.repository
) : ViewModel() {

    var state = mutableStateOf(TopicState())
        private set

    init {
        getTopics()
    }

    private fun getTopics() {
        viewModelScope.launch {
            repository.getAllTopics().collectLatest { topics ->
                state.value = state.value.copy(topics = topics)
            }
        }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch {
            repository.deleteTopic(topic)
            getTopics()
        }
    }

    fun onTopicSelect(topicInd: Int) {
        if (topicInd >= state.value.topics.size || topicInd < 0) return
        getCardsByTopicId(state.value.topics[topicInd].id)
    }

    private fun getCardsByTopicId(topicId: Long) {
        viewModelScope.launch {
            repository.getCardsByTopicId(topicId).collectLatest { cards ->
                val shuffled = cards.shuffled(java.util.Random(System.currentTimeMillis()))
                state.value = state.value.copy(
                    cards = shuffled,
                    currentCardInd = 0,
                    currentCard = shuffled.getOrNull(0)
                )
            }
        }
    }

    fun nextCard() {
        if (state.value.currentCardInd + 1 < state.value.cards.size) {
            val newIndex = state.value.currentCardInd + 1
            state.value = state.value.copy(
                currentCardInd = newIndex,
                currentCard = state.value.cards[newIndex]
            )
        }
    }

    fun previousCard() {
        if (state.value.currentCardInd - 1 >= 0) {
            val newIndex = state.value.currentCardInd - 1
            state.value = state.value.copy(
                currentCardInd = newIndex,
                currentCard = state.value.cards[newIndex]
            )
        }
    }
}