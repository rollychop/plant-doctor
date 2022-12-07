package com.brohit.plantdoctor.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

data class Message(val id: Long, val msg: String)

/**
 * Class responsible for managing Snackbar messages to show on the screen
 */
object SnackbarManager {

    private val mutableStateFlow: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = mutableStateFlow.asStateFlow()

    fun showMessage(msg: String) {
        mutableStateFlow.update { currentMessages ->
            currentMessages + Message(
                id = UUID.randomUUID().mostSignificantBits,
                msg = msg
            )
        }
    }

    fun setMessageShown(messageId: Long) {
        mutableStateFlow.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}