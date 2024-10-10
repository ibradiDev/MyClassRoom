package ibradi.classroom

import SignUpScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import ibradi.classroom.ui.theme.MyClassRoomTheme

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()

        setContent {
            MyClassRoomTheme {
                // Observing the authentication state
                var user by remember { mutableStateOf(auth.currentUser) }

                // This is a simple side-effect to listen for auth state changes
                DisposableEffect(Unit) {
                    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                        user = firebaseAuth.currentUser
                    }
                    auth.addAuthStateListener(authStateListener)
                    // Clean up the listener on disposal
                    onDispose {
                        auth.removeAuthStateListener(authStateListener)
                    }
                }

                // Display screen based on user state
//                if (user != null) {
//                    TeacherHomeScreen()
//                StudentHomeScreen()
//                ChatScreen()
//                } else {
                SignUpScreen(onSignUp = {})
//                }
            }
        }
    }
}
