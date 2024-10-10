package ibradi.classroom.components.student

import MenuItemCard
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.MarkUnreadChatAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import ibradi.classroom.ui.theme.MyClassRoomTheme
import kotlinx.coroutines.launch

@Preview
@Composable
fun StudentHomeScreen() {
    val context = LocalContext.current

    MyClassRoomTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val coroutineScope = rememberCoroutineScope()
            val auth = FirebaseAuth.getInstance()

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Espace étudiant",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "M2 SIGL",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.height(45.dp))

                // First row of options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    MenuItemCard(
                        title = "MESSAGES",
                        icon = Icons.Default.MarkUnreadChatAlt,
                        modifier = Modifier.clickable { /* TODO: Action to go to messages */ }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    MenuItemCard(
                        title = "ESPACE EVALUATIONS",
                        icon = Icons.Default.School,
                        modifier = Modifier.clickable { /* TODO: Action to go to evaluations */ }
                    )
                }

                Spacer(Modifier.height(28.dp))

                // Second row of options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    MenuItemCard(
                        title = "RESSOURCES",
                        icon = Icons.Default.FolderSpecial,
                        modifier = Modifier.clickable { /* TODO: Action to go to resources */ }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    MenuItemCard(
                        title = "EMPLOI DU TEMPS",
                        icon = Icons.Default.Event,
                        modifier = Modifier.clickable { /* TODO: Action to go to schedule */ }
                    )
                }

                Spacer(Modifier.height(48.dp))

                // Logout Button
                Row(
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                try {
                                    auth.signOut()
                                } catch (err: Exception) {
                                    Toast
                                        .makeText(context, err.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Déconnexion",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
