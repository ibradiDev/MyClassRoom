package ibradi.classroom.components.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ibradi.classroom.models.Profile
import ibradi.classroom.models.User

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ChatScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Conversation(SampleData.conversationSample)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Conversation(messages: List<Message>) {
    var messageList by remember { mutableStateOf(messages.toMutableList()) }
    var newMessage by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()
    var shouldScroll by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeight = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }

    LaunchedEffect(shouldScroll) {
        if (shouldScroll) {
            scrollState.animateScrollToItem(messageList.size - 1)
            shouldScroll = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
//            .imePadding()
            .imeNestedScroll() // Adjust for keyboard
            .padding(top = 16.dp, bottom = 40.dp) // Adjust for top and navigation bar
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp), state = scrollState
        ) {
            items(messageList) { message ->
                MessageCard(message)
            }
        }


        // Input area for new message
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .heightIn(min = 48.dp, max = 120.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                placeholder = { Text("Type a message...") },
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                ),

                )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (newMessage.isNotBlank()) {
                        messageList.add(
                            Message(
                                User("Moi", "Moi", "Moi", "Moi", "Moi", Profile.STUDENT),
                                newMessage,
                                MsgDirection.OUTGOING
                            )
                        )
                        newMessage = ""
                        shouldScroll = true
                    }
                }, modifier = Modifier.clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MessageCard(msg: Message) {
    val alignment =
        if (msg.direction == MsgDirection.OUTGOING) Arrangement.End else Arrangement.Start
    val backgroundColor =
        if (msg.direction == MsgDirection.OUTGOING) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(), horizontalArrangement = alignment
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = backgroundColor,
            shadowElevation = 1.dp,
            modifier = Modifier
                .widthIn(max = 280.dp) // Limit message width for better design
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = msg.author.firstName,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = msg.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (msg.direction == MsgDirection.OUTGOING) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

enum class MsgDirection {
    INCOMING, OUTGOING
}

data class Message(
    val author: User, val body: String, val direction: MsgDirection = MsgDirection.INCOMING
)

object SampleData {

    val currentUser = User(
        firstName = "Ibradi",
        lastName = "",
        profileImage = "",
        password = "",
        email = "ibradi.dev@gmail.com",
        profile = Profile.STUDENT
    )


    val conversationSample = listOf(
        Message(currentUser, "Test...Test...Test..."),
        Message(currentUser, "List of Android versions:", MsgDirection.OUTGOING),
        Message(
            currentUser, "I think Kotlin is my favorite programming language."
        ),
        Message(
            currentUser,
            "I think Kotlin is my favorite programming language.",
            MsgDirection.OUTGOING
        ),
        Message(
            currentUser,
            "I think Kotlin is my favorite programming language.",
            MsgDirection.INCOMING
        ),
        Message(
            currentUser,
            "I think Kotlin is my favorite programming language.",
            MsgDirection.OUTGOING
        ),
        Message(
            currentUser, "I think Kotlin is my favorite programming language."
        ),
        Message(currentUser, "Merci!", MsgDirection.OUTGOING),
        Message(
            currentUser, "I think Kotlin is my favorite programming language."
        ),
    )
}
