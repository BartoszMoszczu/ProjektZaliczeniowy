package com.example.projektzaliczeniowy

// Importujemy adnotację PropertyName z Firebase Firestore
import com.google.firebase.firestore.PropertyName

// Definicja klasy danych User
/**
 * Klasa danych reprezentująca użytkownika z właściwościami imię, nazwisko i wiek.
 * Właściwości są oznaczone adnotacjami, aby mapować je na odpowiednie pola w Firestore.
 *
 * @property name Imię użytkownika.
 * @property surname Nazwisko użytkownika.
 * @property age Wiek użytkownika.
 */
data class User(
    // Adnotacja wskazująca, że pola w Firestore odpowiadają właściwościom klasy np. 'name' odpowiada 'name'.
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("surname") @set:PropertyName("surname") var surname: String = "",
    @get:PropertyName("age") @set:PropertyName("age") var age: Int = 0
)


