package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida, Firebase i Kotlin Coroutines
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

// Definiujemy klasę FirestoreActivity, która dziedziczy po AppCompatActivity
/**
 * Aktywność do zarządzania profilem użytkownika za pomocą Firebase Firestore.
 *
 * @property auth Instancja FirebaseAuth do zarządzania uwierzytelnianiem użytkowników.
 * @property firestore Instancja FirebaseFirestore do interakcji z bazą danych.
 * @property firestoreDatabase Instancja FirestoreDatabase do operacji na danych użytkowników.
 */
class FirestoreActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firestoreDatabase: FirestoreDatabase

    private lateinit var buttonDownload: Button
    private lateinit var buttonAdd: Button
    private lateinit var buttonDelete: Button
    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextAge: EditText
    private lateinit var buttonBloodPressure: Button
    private lateinit var recycleViewButton: Button
    private lateinit var logOutButton: Button

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase)

        // Inicjalizujemy FirebaseAuth, FirebaseFirestore i FirestoreDatabase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestoreDatabase = FirestoreDatabase(firestore)

        // Inicjalizujemy przyciski i pola tekstowe
        buttonDownload = findViewById(R.id.buttonUserDownload)
        buttonAdd = findViewById(R.id.buttonUserAdd)
        buttonDelete = findViewById(R.id.buttonUserDelete)
        editTextName = findViewById(R.id.editTextName)
        editTextSurname = findViewById(R.id.editTextSurname)
        editTextAge = findViewById(R.id.editTextAge)
        buttonBloodPressure = findViewById(R.id.buttonBloodPressure)
        recycleViewButton = findViewById(R.id.recycleViewButton)
        logOutButton = findViewById(R.id.logOutButton)

        // Ustawiamy akcje kliknięcia dla przycisków
        buttonDownload.setOnClickListener {
            downloadUserProfile()
        }

        buttonAdd.setOnClickListener {
            addUserProfile()
        }

        buttonDelete.setOnClickListener {
            deleteUserProfile()
        }

        buttonBloodPressure.setOnClickListener {
            val intent = Intent(this, BloodPressureActivity::class.java)
            startActivity(intent)
        }

        recycleViewButton.setOnClickListener {
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }

        logOutButton.setOnClickListener {
            auth.signOut()  // Wylogowanie z Firebase
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Ustawiamy dane logowania początkowego przy logowaniu użytkownika
        setInitialLoginData()
    }

    // Metoda do ustawiania początkowych danych logowania
    private fun setInitialLoginData() {
        val userId = auth.currentUser?.uid ?: return
        val userEmail = auth.currentUser?.email ?: return
        val password = "example_password" // zastąp rzeczywistym hasłem, jeśli dostępne
        val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
        val loginTime = sdf.format(Date())

        val initialData = mapOf(
            "email" to userEmail,
            "password" to password,
            "loginTime" to loginTime
        )

        GlobalScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("users").document(userId).set(initialData, SetOptions.merge()).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FirestoreActivity, "Dane logowania zapisane", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FirestoreActivity, "Nie udało się zapisać danych logowania: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Metoda do zapisywania profilu użytkownika
    private fun saveUserProfile() {
        val firstName = editTextName.text.toString()
        val lastName = editTextSurname.text.toString()
        val ageStr = editTextAge.text.toString()

        if (firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show()
            return
        }

        val age: Int = try {
            ageStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Proszę podać prawidłowy wiek", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid ?: return
        val user = User(firstName, lastName, age)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                firestoreDatabase.updateData(userId, user)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FirestoreActivity, "Profil zapisany pomyślnie", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FirestoreActivity, "Nie udało się zapisać profilu: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Metoda do pobierania profilu użytkownika
    private fun downloadUserProfile() {
        val userId = auth.currentUser?.uid ?: return

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val user = firestoreDatabase.getData(userId)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        editTextName.setText(user.name)
                        editTextSurname.setText(user.surname)
                        editTextAge.setText(user.age.toString())
                    } else {
                        Toast.makeText(this@FirestoreActivity, "Nie znaleziono profilu", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FirestoreActivity, "Nie udało się pobrać profilu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Metoda do dodawania profilu użytkownika
    private fun addUserProfile() {
        saveUserProfile()
    }

    // Metoda do usuwania profilu użytkownika
    private fun deleteUserProfile() {
        val userId = auth.currentUser?.uid ?: return

        GlobalScope.launch(Dispatchers.IO) {
            try {
                firestoreDatabase.deleteData(userId)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FirestoreActivity, "Profil usunięty pomyślnie", Toast.LENGTH_SHORT).show()
                    editTextName.text.clear()
                    editTextSurname.text.clear()
                    editTextAge.text.clear()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FirestoreActivity, "Nie udało się usunąć profilu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
