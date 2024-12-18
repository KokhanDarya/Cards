package com.example.flashcards.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.ui.components.CardContent
import com.example.flashcards.ui.components.CardFace
import com.example.flashcards.ui.components.FlashCard
import com.example.flashcards.ui.components.IconButton
import com.example.flashcards.ui.viewmodels.TopicViewModel

@Composable
fun CardScreen(topicId: Int) {
    val topicViewModel = viewModel<TopicViewModel>()
    val configuration = LocalConfiguration.current

    LaunchedEffect(topicId) {
        topicViewModel.onTopicSelect(topicId)
    }

    var cardFace by rememberSaveable { mutableStateOf(CardFace.Front) }
    var currentCardInd by rememberSaveable { mutableStateOf(0) }
    val topicState = topicViewModel.state.value
    val cardCount = topicState.cards.size

    LaunchedEffect(currentCardInd) {
        cardFace = CardFace.Front
    }

    val currentCard = topicState.cards.getOrNull(currentCardInd)
    val cardHeight = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 200.dp else 300.dp

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FlashCard(
                        cardFace = cardFace,
                        onClick = { cardFace = cardFace.next },
                        front = {
                            currentCard?.content?.let { content ->
                                CardContent(content)
                            }
                        },
                        back = {
                            currentCard?.description?.let { description ->
                                CardContent(description)
                            }
                        },
                        modifier = Modifier.size(300.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            icon = Icons.Default.KeyboardArrowLeft,
                            onClick = {
                                if (currentCardInd > 0) {
                                    currentCardInd--
                                }
                            },
                            contentDescription = stringResource(R.string.action_previous_card)
                        )

                        Text(
                            text = stringResource(R.string.card_count_last, currentCardInd + 1, cardCount),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            icon = Icons.Default.KeyboardArrowRight,
                            onClick = {
                                if (currentCardInd < cardCount - 1) {
                                    currentCardInd++
                                }
                            },
                            contentDescription = stringResource(R.string.action_next_card)
                        )
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                icon = Icons.Default.KeyboardArrowLeft,
                onClick = {
                    if (currentCardInd > 0) {
                        currentCardInd--
                    }
                },
                contentDescription = stringResource(R.string.action_previous_card)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FlashCard(
                cardFace = cardFace,
                onClick = { cardFace = cardFace.next },
                front = {
                    currentCard?.content?.let { content ->
                        CardContent(content)
                    }
                },
                back = {
                    currentCard?.description?.let { description ->
                        CardContent(description)
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(cardHeight)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                icon = Icons.Default.KeyboardArrowRight,
                onClick = {
                    if (currentCardInd < cardCount - 1) {
                        currentCardInd++
                    }
                },
                contentDescription = stringResource(R.string.action_next_card)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.card_count_last, currentCardInd + 1, cardCount),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
