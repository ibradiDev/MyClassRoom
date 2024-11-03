package ibradi.classroom.components.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ibradi.classroom.models.User
import ibradi.classroom.utils.ChatViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(user: User?) {
    val viewModel: ChatViewModel = viewModel()
    Surface(modifier = Modifier
        .fillMaxWidth()
        .imePadding()) {
        Conversation(user, viewModel)
    }
}
