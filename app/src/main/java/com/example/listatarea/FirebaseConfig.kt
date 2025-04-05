package com.example.listatarea

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseConfig {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    fun init(context: Context) {
        FirebaseApp.initializeApp(context)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun getDb(): FirebaseFirestore {
        return db
    }
}