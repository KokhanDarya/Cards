package com.example.flashcards.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.flashcards.data.room.models.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Upsert
    suspend fun upsert(card: Card)

    @Query("SELECT * FROM cards")
    fun listAll(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE topic_id = :topicId")
    fun listByTopicId(topicId: Long): Flow<List<Card>>
}