import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import ibradi.classroom.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Preview
@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val coroutineScope = rememberCoroutineScope()

    // Création des focusRequesters pour les champs de texte
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current // Pour cacher le clavier à la fin


    // Obtenez le contexte de l'application
    val context = LocalContext.current

    // Configurez les options de connexion Google
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build()

    // Créez un client GoogleSignInClient
    val googleSignInClient: GoogleSignInClient =
        GoogleSignIn.getClient(context, googleSignInOptions)

    // Un lanceur d'activité pour gérer la connexion Google
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                coroutineScope.launch {
                    try {
                        auth.signInWithCredential(credential).await()
                        // Authentification réussie
                    } catch (e: Exception) {
                        errorMessage = e.message
                    }
                }
            }
        } catch (e: ApiException) {
            errorMessage = when (e.statusCode) {
                GoogleSignInStatusCodes.SIGN_IN_FAILED -> "Échec de la connexion."
                GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Connexion annulée."
                GoogleSignInStatusCodes.NETWORK_ERROR -> "Erreur réseau."
                else -> "Erreur lors de la connexion : ${e.localizedMessage}"
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Connexion", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Logo Google
        Image(
            painter = painterResource(id = R.drawable.google_logo), // Utilisez une icône Google ici
            contentDescription = "Logo Google",
            modifier = Modifier
                .size(48.dp) // Ajuste la taille selon tes besoins
                .clickable {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
        )


        Spacer(modifier = Modifier.height(16.dp))


        // Champ Email
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username or Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(2.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = Color.Gray,
            ),
            shape = RoundedCornerShape(20.dp),
            textStyle = TextStyle(
                fontSize = 16.sp, fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next // Passe au champ suivant
            ),
            keyboardActions = KeyboardActions(onNext = {
                passwordFocusRequester.requestFocus() // Demande le focus sur le champ mot de passe
            })
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Champ Mot de passe
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(2.dp)
                .focusRequester(passwordFocusRequester), // Associer le focusRequester ici
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = Color.Gray,
            ),
            shape = RoundedCornerShape(20.dp),
            textStyle = TextStyle(
                fontSize = 16.sp, fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // Indiquer que c'est la fin de la saisie
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus() // Cacher le clavier après soumission
            }),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                loading = true
                coroutineScope.launch {
                    try {
                        val result = auth.signInWithEmailAndPassword(username, password).await()
                        loading = false
                    } catch (e: Exception) {
                        loading = false
                        errorMessage = e.message
                    }
                }
            }, modifier = Modifier.fillMaxWidth(), enabled = !loading
        ) {
            Text("Soumettre")
        }

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "Erreur inconnue",
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

    }
}
