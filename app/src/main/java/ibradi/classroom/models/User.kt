package ibradi.classroom.models

enum class Profile { STUDENT, TEACHER }

class User {
    constructor(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        profileImage: String,
        profile: Profile,
    ) {
        this.uid = uid
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.profile = profile
        this.profileImage = profileImage
        this.password = password
    }

    constructor(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        profileImage: String,
        profile: Profile,
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.profile = profile
        this.profileImage = profileImage
        this.password = password
    }

    lateinit var uid: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var email: String
    var profile: Profile = Profile.STUDENT
    lateinit var profileImage: String
    lateinit var password: String


}
