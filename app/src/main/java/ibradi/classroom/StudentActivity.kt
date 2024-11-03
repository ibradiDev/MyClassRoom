package ibradi.classroom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import ibradi.classroom.components.chat.ChatScreen
import ibradi.classroom.components.student.StudentHomeScreen
import ibradi.classroom.models.User
import ibradi.classroom.ui.theme.MyClassRoomTheme
import kotlinx.serialization.json.Json
import java.net.URLDecoder

class StudentActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val encodedUser = intent.getStringExtra("encoded-user")
        val user = Json.decodeFromString<User>(URLDecoder.decode(encodedUser, "UTF-8"))

        setContent {
            val navController = rememberNavController()
            var isLoggedIn by remember { mutableStateOf(false) }

            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            var firebaseUser by remember { mutableStateOf(auth.currentUser) }

            DisposableEffect(Unit) {
                val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                    firebaseUser = firebaseAuth.currentUser
                }
                auth.addAuthStateListener(authStateListener)
                onDispose {
                    auth.removeAuthStateListener(authStateListener)
                }
            }

            LaunchedEffect(firebaseUser) {
                isLoggedIn = firebaseUser != null
                if (!isLoggedIn) {
                    val intent = Intent(this@StudentActivity, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            MyClassRoomTheme {
                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "home" else "auth"
                ) {
                    composable("home") { StudentHomeScreen(user, navController) }
                    composable("chats") { ChatScreen(user) }
                    // Ajoutez un écran d'authentification si nécessaire
                    composable("auth") { AuthActivity() }
                }
            }
        }
    }
}
