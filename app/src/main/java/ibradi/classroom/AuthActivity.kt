package ibradi.classroom

import LoginScreen
import SignUpScreen
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ibradi.classroom.models.User
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            var isLoading by remember { mutableStateOf(true) }

            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            var firebaseUser by remember { mutableStateOf(auth.currentUser) }

            val db: FirebaseFirestore = FirebaseFirestore.getInstance()
            var currentUser by remember { mutableStateOf<User?>(null) }
            var encodedUser by remember { mutableStateOf("") }

            // Auth state listener
            DisposableEffect(Unit) {
                val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                    firebaseUser = firebaseAuth.currentUser
                }
                auth.addAuthStateListener(authStateListener)
                onDispose {
                    auth.removeAuthStateListener(authStateListener)
                }
            }

            // Fetch user info in a coroutine
            LaunchedEffect(firebaseUser) {
                if (firebaseUser != null) {
                    val docRef = db.collection("users").document(firebaseUser!!.uid)
                    try {
                        val doc = docRef.get().await()
                        currentUser = if (doc.exists()) doc.toObject(User::class.java) else null
                        if (currentUser != null) {
                            encodedUser =
                                URLEncoder.encode(Json.encodeToString(currentUser), "UTF-8")

                            val intent = Intent(this@AuthActivity, MainActivity::class.java)
                            intent.putExtra("encoded-user", encodedUser)
                            startActivity(intent)
                            finish()
                        }
                    } catch (e: Exception) {
                        isLoading = false
                        Log.e(
                            "FirestoreError",
                            "Erreur lors de la récupération de l'utilisateur: ${e.message}"
                        )
                    }
                }
                isLoading = false
            }

//            MyClassRoomTheme {
                if (isLoading) {
                    LoadingScreen() // Écran de chargement
                } else {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("signup") { SignUpScreen(navController) }
                    }
                }
            }
//        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Change si nécessaire
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator() // Indicateur de chargement
    }
}
