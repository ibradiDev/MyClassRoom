package ibradi.classroom.utils

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ibradi.classroom.models.User

class UserViewModel : ViewModel() {


    fun registerUserWithFirebase(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()

        // Créer un compte utilisateur avec Firebase Auth
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Récupérer l'ID utilisateur (UID) après l'inscription
                    val firebaseUser = task.result?.user
                    val uid = firebaseUser?.uid ?: return@addOnCompleteListener
                    Log.d("registerUserWithFirebase", user.toString())
                    // Si une image de profil est sélectionnée, uploader dans Firebase Storage
                    if (user.profileImage.isNotEmpty()) {
                        val storageRef = storage.reference.child("profileImages/$uid.jpg")
                        val imageUri = Uri.parse(user.profileImage)

                        // Uploader l'image de profil
                        storageRef.putFile(imageUri).addOnSuccessListener {
                            // Récupérer l'URL de téléchargement
                            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                // Enregistrer les infos utilisateur dans Firestore avec l'URL de l'image
                                saveUserToFirestore(
                                    user, uid, downloadUri.toString(), db, onSuccess, onError
                                )
                            }.addOnFailureListener { e ->
                                onError("Erreur lors du téléchargement de l'image : ${e.message}")
                            }
                        }.addOnFailureListener { e ->
                            onError("Erreur lors du téléchargement de l'image : ${e.message}")
                        }
                    } else {
                        try {
                            saveUserToFirestore(user, uid, "", db, onSuccess, onError)
                        } catch (e: Exception) {
                            onError(
                                e.message
                                    ?: "Erreur lors de l'enregistrement des données utilisateur"
                            )
                        }
                    }
                } else {
                    onError(
                        task.exception?.message ?: "Erreur lors de la création de l'utilisateur"
                    )
                }
            }
    }

    private fun saveUserToFirestore(
        user: User,
        uid: String,
        profileImageUrl: String,
        db: FirebaseFirestore,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userMap = mapOf(
            "uid" to uid,
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "email" to user.email,
            "profile" to user.profile.toString(),
            "studyField" to user.studyField,
            "grade" to user.grade,
            "immat" to user.immat,
            "profileImage" to profileImageUrl,
        )

        db.collection("users").document(uid).set(userMap).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onError(e.message ?: "Erreur lors de l'enregistrement des données utilisateur")
        }
    }

}
