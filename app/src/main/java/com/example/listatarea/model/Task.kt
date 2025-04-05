package com.example.listatarea.model

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "pending",
    val priority: Int = 0,
    val category: String = "Other",
    val uid: String = ""
)