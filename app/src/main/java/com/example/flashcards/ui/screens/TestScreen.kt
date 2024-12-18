package com.example.flashcards.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.DbSingleton
import com.example.flashcards.data.room.models.Card
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.ui.components.CardContent
import com.example.flashcards.ui.components.CardFace
import com.example.flashcards.ui.components.FlashCard
import com.example.flashcards.ui.viewmodels.CardViewModel
import com.example.flashcards.ui.viewmodels.CardViewModelFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration


@Composable
fun TestScreen(
    onNavigateBack: () -> Unit,
    viewModel: CardViewModel = viewModel(factory = CardViewModelFactory(DbSingleton.repository))
) {
    val allWords by viewModel.words.collectAsState()
    val allTopics by viewModel.topics.collectAsState()
    var wordsToReview by rememberSaveable { mutableStateOf(emptyList<Card>()) }
    var currentWordIndex by rememberSaveable { mutableStateOf(0) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var repeatWords by rememberSaveable { mutableStateOf(emptyList<Card>()) }

    var selectedTopics by rememberSaveable { mutableStateOf(emptyList<Long>()) }
    var topicsDialogVisible by rememberSaveable { mutableStateOf(true) }

    if (topicsDialogVisible) {
        TopicSelectionDialog(
            availableTopics = allTopics,
            onTopicsSelected = { topics ->
                selectedTopics = topics
                wordsToReview = allWords.filter { it.topicId in topics }.shuffled()
                currentWordIndex = 0
                finished = wordsToReview.isEmpty()
                topicsDialogVisible = false
            },
            onDismiss = { topicsDialogVisible = false }
        )
    }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val cardWidth = if (isPortrait) 300.dp else 200.dp
    val cardHeight = if (isPortrait) 400.dp else 200.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (finished && wordsToReview.isEmpty() && repeatWords.isEmpty()) {
            Text("Нет карточек для изучения. Пожалуйста, выберите темы.", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { topicsDialogVisible = true }) {
                Text("Выбрать темы")
            }
        } else if (finished && repeatWords.isEmpty()) {
            Text("Поздравляем, вы выучили все карточки!", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNavigateBack) {
                Text("Вернуться")
            }
        } else {
            if (finished && repeatWords.isNotEmpty()) {
                wordsToReview = repeatWords.shuffled()
                currentWordIndex = 0
                repeatWords = emptyList()
                finished = false
            }

            val currentWord = wordsToReview.getOrNull(currentWordIndex)
            currentWord?.let { card ->
                var cardFace by remember { mutableStateOf(CardFace.Front) }

                if (isPortrait) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FlashCard(
                            cardFace = cardFace,
                            onClick = { face -> cardFace = face.next },
                            front = { CardContent(card.content) },
                            back = { CardContent(card.description) },
                            modifier = Modifier
                                .width(cardWidth)
                                .height(cardHeight)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            IconButton(onClick = {
                                currentWordIndex++
                                cardFace = CardFace.Front
                                if (currentWordIndex >= wordsToReview.size) {
                                    finished = true
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Знаю")
                            }

                            IconButton(onClick = {
                                repeatWords = repeatWords + card
                                currentWordIndex++
                                cardFace = CardFace.Front
                                if (currentWordIndex >= wordsToReview.size) {
                                    finished = true
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Не знаю")
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FlashCard(
                            cardFace = cardFace,
                            onClick = { face -> cardFace = face.next },
                            front = { CardContent(card.content) },
                            back = { CardContent(card.description) },
                            modifier = Modifier
                                .width(cardWidth)
                                .height(cardHeight)
                        )

                        Spacer(modifier = Modifier.width(32.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = {
                                currentWordIndex++
                                cardFace = CardFace.Front
                                if (currentWordIndex >= wordsToReview.size) {
                                    finished = true
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Знаю")
                            }

                            IconButton(onClick = {
                                repeatWords = repeatWords + card
                                currentWordIndex++
                                cardFace = CardFace.Front
                                if (currentWordIndex >= wordsToReview.size) {
                                    finished = true
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Не знаю")
                            }
                        }
                    }
                }
            } ?: run {
                finished = true
            }
        }
    }
}

@Composable
fun TopicSelectionDialog(
    availableTopics: List<Topic>,
    onTopicsSelected: (List<Long>) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTopics by remember { mutableStateOf(emptySet<Long>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите темы") },
        text = {
            Column {
                availableTopics.forEach { topic ->
                    val isSelected = selectedTopics.contains(topic.id)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                selectedTopics = if (isSelected) {
                                    selectedTopics - topic.id
                                } else {
                                    selectedTopics + topic.id
                                }
                            }
                        )
                        Text(topic.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onTopicsSelected(selectedTopics.toList())
                    onDismiss()
                }
            ) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}