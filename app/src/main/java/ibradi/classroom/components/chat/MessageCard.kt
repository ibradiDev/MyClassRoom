package ibradi.classroom.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import ibradi.classroom.R
import ibradi.classroom.models.Message
import ibradi.classroom.models.MsgDirection

@Composable
fun MessageCard(msg: Message) {
    var isExpanded by remember { mutableStateOf(false) }
    val alignment =
        if (msg.direction == MsgDirection.OUTGOING) Arrangement.End else Arrangement.Start
    val backgroundColor =
        if (msg.direction == MsgDirection.OUTGOING) Color(0xFF1C82C6) else Color(0xFFffffff)
    val textColor =
        if (msg.direction == MsgDirection.OUTGOING) Color(0xFFFFFFFF) else Color(0xFF000000)
    val authorNameColor =
        if (msg.direction == MsgDirection.OUTGOING) Color(0xFF55E6EE) else Color(0xFFF44336)
    val profileImage =
        if (msg.author.profileImage.isNotEmpty()) rememberAsyncImagePainter(msg.author.profileImage)
        else painterResource(R.drawable.user)

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(), horizontalArrangement = alignment
    ) {
        msg.author.profileImage.let {
            Image(
                painter = profileImage,
                contentDescription = "profileImage",
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(35.dp)
                    .background(Color.LightGray)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = backgroundColor,
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clickable {
                    isExpanded = !isExpanded
                }
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = msg.author.firstName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontStyle = FontStyle.Italic,
                    color = authorNameColor
                )
                Text(
                    text = if (isExpanded) msg.body else msg.body.take(100) + if (msg.body.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )

                if (msg.body.length > 100) {
                    Text(
                        text = if (isExpanded) "Afficher moins" else "Afficher plus",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.End)
                    )
                }
            }
        }
    }
}
