package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Firebase i Kotlin Coroutines
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Definiujemy klasę FirestoreDatabase, która implementuje interfejs FirestoreInterface
/**
 * Klasa odpowiedzialna za operacje na bazie danych Firestore, implementująca interfejs FirestoreInterface.
 *
 * @property db Instancja FirebaseFirestore do interakcji z bazą danych.
 */
class FirestoreDatabase(private val db: FirebaseFirestore) : FirestoreInterface {
    // Implementacja metody addData do dodawania danych użytkownika do Firestore
    /**
     * Dodaje dane użytkownika do Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     * @param data Dane użytkownika do dodania.
     * @throws Exception Jeśli operacja dodawania się nie powiedzie.
     */
    override suspend fun addData(dataId: String, data: User) {
        try {
            db.collection("users").document(dataId).set(data).await()
        } catch (e: Exception) {
            throw Exception("Nie udało się dodać danych użytkownika: ${e.message}")
        }
    }

    // Implementacja metody getData do pobierania danych użytkownika z Firestore
    /**
     * Pobiera dane użytkownika z Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     * @return Dane użytkownika, jeśli istnieją; null w przeciwnym razie.
     */
    override suspend fun getData(dataId: String): User? {
        val snapshot = db.collection("users")
            .document(dataId)
            .get()
            .await()

        return snapshot.toObject(User::class.java)
    }

    // Implementacja metody updateData do aktualizowania danych użytkownika w Firestore
    /**
     * Aktualizuje dane użytkownika w Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     * @param updatedUser Zaktualizowane dane użytkownika.
     * @throws Exception Jeśli operacja aktualizacji się nie powiedzie.
     */
    override suspend fun updateData(dataId: String, updatedUser: User) {
        try {
            db.collection("users").document(dataId).update(
                mapOf(
                    "name" to updatedUser.name,
                    "surname" to updatedUser.surname,
                    "age" to updatedUser.age
                )
            ).await()
        } catch (e: Exception) {
            throw Exception("Nie udało się zaktualizować danych użytkownika: ${e.message}")
        }
    }

    // Implementacja metody deleteData do usuwania danych użytkownika z Firestore
    /**
     * Usuwa dane użytkownika z Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     * @throws Exception Jeśli operacja usunięcia się nie powiedzie.
     */
    override suspend fun deleteData(dataId: String) {
        try {
            db.collection("users").document(dataId).delete().await()
        } catch (e: Exception) {
            throw Exception("Nie udało się usunąć danych użytkownika: ${e.message}")
        }
    }
}
