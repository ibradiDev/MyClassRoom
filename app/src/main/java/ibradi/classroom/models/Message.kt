package ibradi.classroom.models

import com.google.firebase.Timestamp

class Message {
    constructor(id: String, sender: String, body: String) {
        this.id = id
        this.author = sender
        this.body = body
    }

    lateinit var id: String
    lateinit var author: String
    lateinit var body: String
    lateinit var timestamp: Timestamp
}