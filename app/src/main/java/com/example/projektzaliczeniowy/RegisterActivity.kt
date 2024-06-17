package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida i Firebase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.util.Log

// Definiujemy klasę RegisterActivity, która dziedziczy po AppCompatActivity
/**
 * Aktywność rejestracyjna, umożliwiająca użytkownikom tworzenie konta za pomocą Firebase Authentication.
 *
 * @property auth Instancja FirebaseAuth do zarządzania uwierzytelnianiem użytkowników.
 */
class RegisterActivity : AppCompatActivity() {
    // Deklarujemy zmienną dla instancji FirebaseAuth
    private lateinit var auth: FirebaseAuth

    // Metoda onCreate jest wywoływana podczas tworzenia aktywności
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicjalizujemy instancję FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Inicjalizujemy widoki EditText dla e-maila i hasła oraz przyciski
        val emailEditText: EditText = findViewById(R.id.editTextEmail)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val registerButton: Button = findViewById(R.id.buttonRegister)
        val backToMainButton: Button = findViewById(R.id.buttonBackToMain)

        // Ustawiamy akcję kliknięcia dla przycisku rejestracji
        registerButton.setOnClickListener {
            // Pobieramy tekst z pól e-maila i hasła
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Sprawdzamy, czy pola e-maila i hasła nie są puste
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Logujemy próbę rejestracji
                Log.d("RegisterActivity", "Próba rejestracji użytkownika z e-mailem: $email")
                // Próbujemy stworzyć użytkownika za pomocą FirebaseAuth
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Jeśli rejestracja się powiedzie, wyświetlamy komunikat i przechodzimy do MainActivity
                            Log.d("RegisterActivity", "Rejestracja użytkownika zakończona sukcesem")
                            Toast.makeText(this, "Rejestracja zakończona sukcesem", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Jeśli rejestracja się nie powiedzie, logujemy błąd i wyświetlamy komunikat
                            Log.e("RegisterActivity", "Rejestracja nie powiodła się", task.exception)
                            Toast.makeText(this, "Rejestracja nie powiodła się: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Jeśli wystąpił błąd podczas rejestracji, logujemy błąd i wyświetlamy komunikat
                        Log.e("RegisterActivity", "Błąd podczas rejestracji", e)
                        Toast.makeText(this, "Błąd podczas rejestracji: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Jeśli pola e-maila i hasła są puste, wyświetlamy komunikat
                Toast.makeText(this, "Proszę wypełnić wszystkie pola", Toast.LENGTH_SHORT).show()
            }
        }

        // Ustawiamy akcję kliknięcia dla przycisku powrotu do MainActivity
        backToMainButton.setOnClickListener {
            // Tworzymy Intent do przejścia do MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Ustawiamy flagi, aby wyczyścić stos aktywności i uruchomić nową instancję MainActivity
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            // Uruchamiamy MainActivity
            startActivity(intent)
            // Kończymy bieżącą aktywność
            finish()
        }
    }
}

