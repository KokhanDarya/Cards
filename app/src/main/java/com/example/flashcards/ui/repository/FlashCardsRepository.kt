package com.example.flashcards.ui.repository

import com.example.flashcards.data.room.dao.CardDao
import com.example.flashcards.data.room.dao.TopicDao
import com.example.flashcards.data.room.models.Card
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.data.room.models.TopicWithCards
import kotlinx.coroutines.flow.Flow

class FlashCardsRepository(
    private val topicDao: TopicDao,
    private val cardDao: CardDao
) {
    fun getAllTopics(): Flow<List<Topic>> {
        return topicDao.list()
    }

    fun getAllCards(): Flow<List<Card>> {
        return cardDao.listAll()
    }

    fun getCardsByTopicId(topicId: Long): Flow<List<Card>> {
        return cardDao.listByTopicId(topicId)
    }

    suspend fun insertCard(card: Card) {
        cardDao.upsert(card)
    }

    suspend fun insertWithCards(topicWithCards: TopicWithCards) {
        topicDao.insertWithCards(topicWithCards)
    }

    suspend fun deleteTopic(topic: Topic) {
        topicDao.delete(topic)
    }
}