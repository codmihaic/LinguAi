package com.example.linguai.data.history

import com.example.linguai.model.TranslationHistoryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

data class HistoryItem(
    val createdAt: Long = System.currentTimeMillis(),
    val inputText: String = "",
    val translatedText: String = "",
    val mode: String = "",        // "text" | "voice" | "image"
    val sourceLang: String? = null,
    val targetLang: String = ""
)

class HistoryRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun uidOrThrow(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    suspend fun add(item: TranslationHistoryItem) {
        val uid = uidOrThrow()
        val doc = db.collection("users")
            .document(uid)
            .collection("history")
            .document()

        doc.set(
            mapOf(
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "mode" to item.mode,
                "inputText" to item.inputText,
                "translatedText" to item.translatedText,
                "sourceLang" to item.sourceLang,
                "targetLang" to item.targetLang
            )
        ).await()
    }

    suspend fun listLatest(limit: Long = 50): List<TranslationHistoryItem> {
        val uid = uidOrThrow()
        val snap = db.collection("users")
            .document(uid)
            .collection("history")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()

        return snap.documents.map { d ->
            TranslationHistoryItem(
                id = d.id,
                createdAt = d.getTimestamp("createdAt"),
                mode = d.getString("mode") ?: "text",
                inputText = d.getString("inputText") ?: "",
                translatedText = d.getString("translatedText") ?: "",
                sourceLang = d.getString("sourceLang") ?: "auto",
                targetLang = d.getString("targetLang") ?: "ro",
            )
        }
    }

    suspend fun delete(id: String) {
        val uid = uidOrThrow()
        db.collection("users")
            .document(uid)
            .collection("history")
            .document(id)
            .delete()
            .await()
    }
}
