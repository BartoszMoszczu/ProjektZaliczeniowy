package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida i Firebase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

// Definiujemy klasę RecyclerViewActivity, która dziedziczy po AppCompatActivity
/**
 * Aktywność wyświetlająca listę użytkowników z Firestore za pomocą RecyclerView.
 *
 * @property recyclerView Widok RecyclerView do wyświetlania listy użytkowników.
 * @property userList Lista użytkowników pobrana z Firestore.
 * @property backToFirestoreButton Przycisk do powrotu do aktywności FirestoreActivity.
 * @property db Instancja FirebaseFirestore do interakcji z bazą danych.
 */
class RecyclerViewActivity : AppCompatActivity() {

    // Deklarujemy zmienne dla RecyclerView, listy użytkowników, przycisku powrotu i instancji FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var backToFirestoreButton: Button
    private var db = FirebaseFirestore.getInstance()

    // Metoda onCreate jest wywoływana podczas tworzenia aktywności
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_view)

        // Inicjalizujemy RecyclerView i ustawiamy jego układ na liniowy
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicjalizujemy przycisk powrotu
        backToFirestoreButton = findViewById(R.id.buttonBackToFirestore)

        // Inicjalizujemy listę użytkowników
        userList = arrayListOf()

        // Pobieramy kolekcję użytkowników z Firestore
        db.collection("users").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    // Jeśli dane nie są puste, dodajemy użytkowników do listy
                    for (data in it.documents) {
                        val user: User? = data.toObject(User::class.java)
                        if (user != null) {
                            userList.add(user)
                        }
                    }
                    // Ustawiamy adapter dla RecyclerView
                    recyclerView.adapter = MyAdapter(userList)
                }
            }
            .addOnFailureListener {
                // Jeśli wystąpił błąd, wyświetlamy komunikat
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

        // Ustawiamy akcję kliknięcia dla przycisku powrotu do FirestoreActivity
        backToFirestoreButton.setOnClickListener {
            val intent = Intent(this, FirestoreActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
