package ibradi.classroom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import ibradi.classroom.models.Profile
import ibradi.classroom.models.User
import ibradi.classroom.ui.theme.MyClassRoomTheme
import kotlinx.serialization.json.Json
import java.net.URLDecoder

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Retrieve encoded user from intent
        val encodedUser = intent.getStringExtra("encoded-user")
        val user = if (encodedUser != null) {
            Json.decodeFromString<User>(URLDecoder.decode(encodedUser, "UTF-8"))
        } else {
            null
        }

        setContent {
            MyClassRoomTheme {
                // Manage navigation based on user profile
                ManageUserNavigation(user, encodedUser)
            }
        }
    }
}

@Composable
fun ManageUserNavigation(user: User?, encodedUser: String?) {
    if (user == null) {
        NavigateToActivity(AuthActivity::class.java)
    } else {
        when (user.profile) {
            Profile.STUDENT -> NavigateToActivity(StudentActivity::class.java, encodedUser)
            Profile.TEACHER -> NavigateToActivity(TeacherActivity::class.java, encodedUser)
        }
    }
}

@Composable
fun NavigateToActivity(activityClass: Class<*>, encodedUser: String? = null) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val intent = Intent(context, activityClass).apply {
            encodedUser?.let {
                putExtra("encoded-user", it)
            }
        }
        context.startActivity(intent)
        // Finish the current activity
        if (context is ComponentActivity) {
            context.finish()
        }
    }
}
