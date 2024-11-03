package ibradi.classroom.models

import kotlinx.serialization.Serializable

enum class Profile { STUDENT, TEACHER }
enum class TITLE { Dr, Prof, AssocProf, Associate, Assistant }

@Serializable
data class User(
    var uid: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var profileImage: String = "",
    var password: String = "",
    var profile: Profile = Profile.STUDENT,
    var title: String = "",
    var studyField: String = "",
    var grade: String = "",
    var immat: String = "",
)
