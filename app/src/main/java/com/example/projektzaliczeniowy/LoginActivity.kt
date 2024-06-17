package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida i Firebase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

// Definiujemy klasę LoginActivity, która dziedziczy po AppCompatActivity
/**
 * Aktywność logowania, umożliwiająca użytkownikom logowanie się za pomocą Firebase Authentication.
 *
 * @property auth Instancja FirebaseAuth do zarządzania uwierzytelnianiem użytkowników.
 * @property db Instancja FirebaseFirestore do interakcji z bazą danych.
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Metoda onCreate jest wywoływana podczas tworzenia aktywności
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicjalizujemy instancje FirebaseAuth i FirebaseFirestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Inicjalizujemy widoki EditText dla e-maila i hasła oraz przyciski
        val emailEditText: EditText = findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val loginButton: Button = findViewById(R.id.buttonLogin)
        val backToMainButton: Button = findViewById(R.id.buttonBackToMain)

        // Ustawiamy akcję kliknięcia dla przycisku logowania
        loginButton.setOnClickListener {
            // Pobieramy tekst z pól e-maila i hasła
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Sprawdzamy, czy pola e-maila i hasła nie są puste
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            if (currentUser != null) {
                                val userId = currentUser.uid
                                val userEmail = currentUser.email ?: ""

                                // Tworzymy nowy dokument w Firestore
                                val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
                                val currentDateTime = sdf.format(Date())

                                db.collection("users").document(userId).get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            // Użytkownik istnieje, aktualizujemy tylko czas logowania
                                            db.collection("users").document(userId)
                                                .update("loginTime", currentDateTime)
                                                .addOnSuccessListener {
                                                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                                                    Toast.makeText(this, "Czas logowania zaktualizowany pomyślnie", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w(TAG, "Błąd podczas aktualizacji dokumentu", e)
                                                    Toast.makeText(this, "Błąd podczas aktualizacji czasu logowania: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            // Użytkownik nie istnieje, tworzymy nowy dokument
                                            val userMap = hashMapOf(
                                                "email" to userEmail,
                                                "password" to password,
                                                "loginTime" to currentDateTime
                                            )
                                            db.collection("users").document(userId).set(userMap)
                                                .addOnSuccessListener {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!")
                                                    Toast.makeText(this, "Logowanie i dane zapisane pomyślnie", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.w(TAG, "Błąd podczas zapisywania dokumentu", e)
                                                    Toast.makeText(this, "Błąd podczas zapisywania danych użytkownika: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Błąd podczas pobierania dokumentu", e)
                                        Toast.makeText(this, "Błąd podczas sprawdzania danych użytkownika: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }

                                val intent = Intent(this, FirestoreActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            Toast.makeText(this, "Logowanie udane", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Logowanie nieudane: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }

        // Ustawiamy akcję kliknięcia dla przycisku powrotu do MainActivity
        backToMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    // Obiekt towarzyszący zawierający stałe wartości
    companion object {
        private const val TAG = "LoginActivity"
    }
}

