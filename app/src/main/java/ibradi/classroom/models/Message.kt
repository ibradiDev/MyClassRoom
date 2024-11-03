package ibradi.classroom.models

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val author: User = User(),
    val body: String = "",
    val timestamp: String = Timestamp.now().toString(),
    var direction: MsgDirection = MsgDirection.INCOMING
)

enum class MsgDirection {
    INCOMING, OUTGOING
}