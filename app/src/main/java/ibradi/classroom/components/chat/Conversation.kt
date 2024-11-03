package ibradi.classroom.components.chat

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ibradi.classroom.models.User
import ibradi.classroom.utils.ChatViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Conversation(user: User?, viewModel: ChatViewModel) {
    val messageList by viewModel.messageList.collectAsState()
    var newMessage by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.startListeningToMessages(user)
    }

    LaunchedEffect(messageList) {
        if (messageList.isNotEmpty()) {
            scrollState.animateScrollToItem(messageList.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
            .imePadding()
    ) {
        Text(
            text = "Messagerie",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp),
            state = scrollState
        ) {
            items(messageList) { message ->
                MessageCard(message)
            }
        }
        InputArea(
            newMessage = newMessage,
            onMessageChange = { newMessage = it },
            onSend = {
                viewModel.sendMessage(user, newMessage)
                newMessage = ""
            }
        )
    }



    if (viewModel.errorMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputArea(newMessage: String, onMessageChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = newMessage,
            onValueChange = onMessageChange,
            placeholder = { Text("Type a message...") },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
//                containerColor = Color(0xFFE0E0E0), // Couleur de fond
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        SendButton(onSend = onSend)
    }
}

@Composable
fun SendButton(onSend: () -> Unit) {
    Button(
        onClick = onSend,
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF1C82C6)), // Couleur du bouton
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1C82C6), // Couleur de fond du bouton
            contentColor = Color.White // Couleur du texte du bouton
        ),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send",
            modifier = Modifier.size(24.dp)
        )
    }
}
