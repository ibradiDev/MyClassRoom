import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import ibradi.classroom.models.Profile
import ibradi.classroom.models.User


@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(onSignUp = {})
}


@Composable
fun SignUpScreen(
    onSignUp: (User) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var profile by remember { mutableStateOf(Profile.STUDENT) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF3B8D99), Color(0xFF6DD5ED))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Créer un compte",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF0D47A1),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Champ prénom
            OutlinedTextField(value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champ nom
            OutlinedTextField(value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champ e-mail
            OutlinedTextField(value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champ mot de passe
            OutlinedTextField(value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible })
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champ confirmation mot de passe
            OutlinedTextField(value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Répéter mot de passe") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            confirmPasswordVisible = !confirmPasswordVisible
                        })
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champ image de profil avec bouton de sélection
            Text("Image de profil", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Sélectionner une image")
            }

            profileImageUri?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

// Sélection du profil
            Text("Sélectionner un profil", fontSize = 16.sp, color = Color.Gray)
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                // Option Étudiant
                Row(
                    modifier = Modifier.clickable {
                            profile = Profile.STUDENT
                        }, // Rendre la ligne entière cliquable
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = profile == Profile.STUDENT, onClick = {
                        profile = Profile.STUDENT
                    } // Met à jour le profil lorsque le bouton est cliqué
                    )
                    Text(
                        text = "Étudiant", modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Option Enseignant
                Row(
                    modifier = Modifier.clickable {
                            profile = Profile.TEACHER
                        }, // Rendre la ligne entière cliquable
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = profile == Profile.TEACHER, onClick = {
                        profile = Profile.TEACHER
                    } // Met à jour le profil lorsque le bouton est cliqué
                    )
                    Text(
                        text = "Enseignant", modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Bouton inscription
            Button(
                onClick = {
                    if (password == confirmPassword) {
                        val user = User(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            profile = profile,
                            profileImage = profileImageUri?.toString() ?: "",
                            password = password
                        )
                        onSignUp(user)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("S'inscrire", fontSize = 18.sp)
            }
        }
    }
}
