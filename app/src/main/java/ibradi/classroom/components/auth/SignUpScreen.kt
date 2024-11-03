import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import ibradi.classroom.models.Profile
import ibradi.classroom.models.User
import ibradi.classroom.utils.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(NavHostController(LocalContext.current))
}


@Composable
fun SignUpScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var profile by remember { mutableStateOf(Profile.TEACHER) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var immat by remember { mutableStateOf("") }
    // Expression régulière pour les formats de matricules ESATIC
    val matNumPattern = "^[1-2][0-9]-ESATIC[0-9]{4}[A-Z]{2}$".toRegex(RegexOption.IGNORE_CASE)
    var matNumError by remember { mutableStateOf(false) }
//    Champs de selection de niveau et filière
    var isGradeExpanded by remember { mutableStateOf(false) }
    var isStudyFieldExpanded by remember { mutableStateOf(false) }
    var selectedGrade by remember { mutableStateOf("") }
    var selectedStudyField by remember { mutableStateOf("") }
    val grades = listOf("LICENCE 1", "LICENCE 2", "LICENCE 3", "MASTER 1", "MASTER 2")
    val studyFields =
        listOf(
            "MPI",
            "SRIT",
            "SIGL",
            "RTEL",
            "TWIN",
            "DASI",
            "SITW",
            "BIHAR",
            "MBDS",
            "ERIS"
        )

    var loading by remember { mutableStateOf(false) }
    var isSignedUp by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val coroutineScope = rememberCoroutineScope()


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3F51B5), Color(0xFF2196F3)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(699.dp)
                .align(Alignment.Center)
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
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
            OutlinedTextField(
                singleLine = true,
                value = firstName,
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
            OutlinedTextField(
                singleLine = true,
                value = lastName,
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
            OutlinedTextField(
                singleLine = true,
                value = username,
                onValueChange = { username = it },
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
            OutlinedTextField(
                singleLine = true,
                value = password,
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
            OutlinedTextField(
                singleLine = true,
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmer mot de passe") },
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
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D47A1), contentColor = Color.White
                ),
            ) {
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

            // Sélection du profil (Étudiant ou Enseignant)
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.clickable { profile = Profile.STUDENT },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = profile == Profile.STUDENT,
                        onClick = { profile = Profile.STUDENT })
                    Text(text = "Étudiant", modifier = Modifier.padding(start = 4.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    modifier = Modifier.clickable { profile = Profile.TEACHER },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = profile == Profile.TEACHER,
                        onClick = { profile = Profile.TEACHER })
                    Text(text = "Enseignant", modifier = Modifier.padding(start = 4.dp))
                }
            }


            if (profile == Profile.STUDENT) {
                Spacer(modifier = Modifier.height(8.dp))
                // Champ de sélection du niveau d'étude
                OutlinedTextField(
                    value = selectedGrade,
                    singleLine = true,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Niveau") },
                    trailingIcon = {
                        IconButton(onClick = { isGradeExpanded = !isGradeExpanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                )
                DropdownMenu(
                    expanded = isGradeExpanded,
                    onDismissRequest = { isGradeExpanded = false },
                    modifier = Modifier.width(200.dp),
                ) {
                    grades.forEach { grade ->
                        DropdownMenuItem(text = { Text(grade) }, onClick = {
                            selectedGrade = grade
                            isGradeExpanded = false
                        })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Champ de sélection de la filière d'étude
                OutlinedTextField(
                    value = selectedStudyField,
                    singleLine = true,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filière") },
                    trailingIcon = {
                        IconButton(onClick = { isStudyFieldExpanded = !isStudyFieldExpanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                )
                DropdownMenu(
                    expanded = isStudyFieldExpanded,
                    onDismissRequest = { isStudyFieldExpanded = false },
                    modifier = Modifier.width(200.dp),
                ) {
                    studyFields.forEach { studyField ->
                        DropdownMenuItem(text = { Text(studyField) }, onClick = {
                            selectedStudyField = studyField
                            isStudyFieldExpanded = false
                        })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    singleLine = true,
                    value = immat,
                    onValueChange = {
                        val upperInput = it.uppercase()
                        if (upperInput.matches(matNumPattern)) {
                            immat = upperInput
                            matNumError = false
                        } else {
                            matNumError = true
                            immat = upperInput
                        }
                    },
                    label = { Text("Matricule") },
                    leadingIcon = {
                        Icon(Icons.Default.Approval, contentDescription = null)
                    },
                    isError = matNumError,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                if (matNumError) {
                    Text(
                        text = "Matricule invalide, format attendu : 01-ESATIC1010AA",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Bouton inscription
            Button(
                onClick = {
                    loading = true
                    if (password == confirmPassword) {
                        val user = User(
                            firstName = firstName,
                            lastName = lastName,
                            email = username,
                            profile = profile,
                            profileImage = profileImageUri?.toString() ?: "",
                            password = password,
                            studyField = selectedStudyField,
                            grade = selectedGrade,
                            immat = immat
                        )
                        UserViewModel().registerUserWithFirebase(
                            user,
                            onSuccess = {
                                loading = false
                                isSignedUp = true
                                /* val intent = Intent(context, MainActivity::class.java)
                                 context.startActivity(intent)
                                 (context as? Activity)?.finish()*/

                            },
                            onError = { errorMessage ->
                                Log.e("SignUp", "Erreur : $errorMessage")
                            })
                    }
                    if (isSignedUp) {
                        coroutineScope.launch {
                            try {
                                auth.signInWithEmailAndPassword(username, password).await()
                                loading = false
                            } catch (e: Exception) {
                                loading = false
                                errorMessage = e.message
                            }
                        }
                    }
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D47A1), contentColor = Color.White
                ), shape = RoundedCornerShape(16.dp), enabled = !loading
            ) {
                Text("S'inscrire", fontSize = 18.sp)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Ajout du lien pour les utilisateurs existants
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vous avez déjà un compte ?", color = Color.Gray, fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Connectez-vous",
                    color = Color.Blue,
                    modifier = Modifier.clickable { navController.navigate("login") },
                    fontSize = 14.sp
                )
            }
        }
    }
}
