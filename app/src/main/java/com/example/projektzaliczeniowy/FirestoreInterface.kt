package com.example.projektzaliczeniowy

// Definiujemy interfejs FirestoreInterface, który będzie używany do operacji na danych w Firestore
/**
 * Interfejs definiujący operacje CRUD (Create, Read, Update, Delete) na danych użytkowników w Firestore.
 */
interface FirestoreInterface {
    /**
     * Dodaje dane użytkownika do Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     * @param data Dane użytkownika do dodania.
     */
    suspend fun addData(dataId: String, data: User)

    /**
     * Pobiera dane użytkownika z Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     * @return Dane użytkownika, jeśli istnieją; null w przeciwnym razie.
     */
    suspend fun getData(dataId: String): User?

    /**
     * Aktualizuje dane użytkownika w Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     * @param updatedUser Zaktualizowane dane użytkownika.
     */
    suspend fun updateData(dataId: String, updatedUser: User)

    /**
     * Usuwa dane użytkownika z Firestore.
     *
     * @param dataId Unikalny identyfikator danych.
     */
    suspend fun deleteData(dataId: String)
}
