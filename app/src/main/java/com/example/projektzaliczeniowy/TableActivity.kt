package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Definiujemy klasę TableActivity, która dziedziczy po AppCompatActivity
/**
 * Aktywność wyświetlająca tabelę z pomiarami ciśnienia krwi.
 *
 * @property recyclerView Widok RecyclerView do wyświetlania danych.
 * @property adapter Adapter do zarządzania danymi w RecyclerView.
 * @property bloodPressureData Lista pomiarów ciśnienia krwi.
 * @property backToBloodPressureButton Przycisk do powrotu do aktywności pomiaru ciśnienia krwi.
 */
class TableActivity : AppCompatActivity() {

    // Deklarujemy zmienne dla RecyclerView, Adaptera, danych i przycisku
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BloodPressureAdapter
    private lateinit var bloodPressureData: ArrayList<BloodPressureMeasurement>
    private lateinit var backToBloodPressureButton: Button

    // Metoda onCreate jest wywoływana podczas tworzenia aktywności
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        // Pobieramy dane z Intentu przekazane z poprzedniej aktywności
        bloodPressureData = intent.getParcelableArrayListExtra("bloodPressureData") ?: arrayListOf()

        // Inicjalizujemy RecyclerView i ustawiamy jego układ na liniowy
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicjalizujemy adapter z danymi i przypisujemy go do RecyclerView
        adapter = BloodPressureAdapter(bloodPressureData)
        recyclerView.adapter = adapter

        // Inicjalizujemy przycisk powrotu i ustawiamy jego akcję kliknięcia
        backToBloodPressureButton = findViewById(R.id.backToBloodPressureButton)
        backToBloodPressureButton.setOnClickListener {
            // Tworzymy Intent do przejścia do BloodPressureActivity
            val intent = Intent(this, BloodPressureActivity::class.java)
            // Uruchamiamy BloodPressureActivity
            startActivity(intent)
            // Kończymy bieżącą aktywność
            finish()
        }
    }
}
