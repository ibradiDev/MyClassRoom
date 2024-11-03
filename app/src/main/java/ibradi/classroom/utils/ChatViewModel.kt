package ibradi.classroom.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import ibradi.classroom.models.Message
import ibradi.classroom.models.MsgDirection
import ibradi.classroom.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ChatViewModel : ViewModel() {
    private val _messageList = MutableStateFlow<List<Message>>(emptyList())
    val messageList: StateFlow<List<Message>> = _messageList

    var errorMessage by mutableStateOf("")

    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    fun startListeningToMessages(user: User?) {
        listenerRegistration?.remove() // Remove existing listener if any
        val messagesCollection =
            db.collection("messages").orderBy("timestamp", Query.Direction.ASCENDING)

        listenerRegistration = messagesCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                errorMessage = "Failed to listen: ${e.localizedMessage}"
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val messages = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Message::class.java)?.apply {
                        direction =
                            if (author.uid == user?.uid) MsgDirection.OUTGOING else MsgDirection.INCOMING
                    }
                }
                _messageList.value = messages
            }
        }
    }

    fun sendMessage(user: User?, messageBody: String) {
        if (user == null || messageBody.isBlank()) return

        val newMessage = Message(
            author = user,
            body = messageBody.trim(),
            timestamp = Timestamp.now().toString(),
            direction = MsgDirection.OUTGOING
        )

        db.collection("messages").add(newMessage)
            .addOnSuccessListener { /* Message sent successfully */ }
            .addOnFailureListener { e ->
                errorMessage = "Message failed to send: ${e.localizedMessage}"
            }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove() // Clean up listener
    }
}
