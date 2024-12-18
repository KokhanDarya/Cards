package com.example.flashcards.data.room.models;

import androidx.room.Embedded
import androidx.room.Relation

data class TopicWithCards(
    @Embedded val topic: Topic,
    @Relation(
        parentColumn = "id",
        entityColumn = "topic_id"
    )
    val cards: List<Card>,
)