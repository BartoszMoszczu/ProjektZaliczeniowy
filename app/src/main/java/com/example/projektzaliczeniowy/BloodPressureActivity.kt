package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida, Firebase oraz MPAndroidChart
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

// Definiujemy klasę BloodPressureActivity, która dziedziczy po AppCompatActivity
/**
 * Aktywność do zarządzania i wyświetlania pomiarów ciśnienia krwi.
 *
 * @property systolicEditText Pole do wprowadzania ciśnienia skurczowego.
 * @property diastolicEditText Pole do wprowadzania ciśnienia rozkurczowego.
 * @property timeRadioGroup Grupa przycisków radiowych do wyboru pory dnia.
 * @property morningRadioButton Przycisk radiowy do wyboru poranka.
 * @property eveningRadioButton Przycisk radiowy do wyboru wieczoru.
 * @property addButton Przycisk do dodawania nowego pomiaru.
 * @property backToFirestoreButton Przycisk do powrotu do aktywności Firestore.
 * @property toTableButton Przycisk do przejścia do tabeli pomiarów.
 * @property lineChart Wykres do wyświetlania danych pomiarów.
 * @property firestore Instancja FirebaseFirestore do interakcji z bazą danych.
 * @property bloodPressureData Lista przechowująca dane pomiarów ciśnienia krwi.
 */
class BloodPressureActivity : AppCompatActivity() {
    private lateinit var systolicEditText: EditText
    private lateinit var diastolicEditText: EditText
    private lateinit var timeRadioGroup: RadioGroup
    private lateinit var morningRadioButton: RadioButton
    private lateinit var eveningRadioButton: RadioButton
    private lateinit var addButton: Button
    private lateinit var backToFirestoreButton: Button
    private lateinit var toTableButton: Button
    private lateinit var lineChart: LineChart
    private lateinit var firestore: FirebaseFirestore

    private val bloodPressureData = mutableListOf<BloodPressureMeasurement>()

    // Metoda onCreate jest wywoływana podczas tworzenia aktywności
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sprawdzamy, czy użytkownik jest zalogowany
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_blood_pressure)

        // Inicjalizujemy FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()

        // Inicjalizujemy przyciski i widoki
        systolicEditText = findViewById(R.id.systolicEditText)
        diastolicEditText = findViewById(R.id.diastolicEditText)
        timeRadioGroup = findViewById(R.id.timeRadioGroup)
        morningRadioButton = findViewById(R.id.morningRadioButton)
        eveningRadioButton = findViewById(R.id.eveningRadioButton)
        addButton = findViewById(R.id.addButton)
        backToFirestoreButton = findViewById(R.id.backToFirestoreButton)
        toTableButton = findViewById(R.id.toTableButton)
        lineChart = findViewById(R.id.lineChart)

        // Ustawiamy akcje kliknięcia dla przycisków
        addButton.setOnClickListener {
            addBloodPressureMeasurement()
        }

        backToFirestoreButton.setOnClickListener {
            val intent = Intent(this, FirestoreActivity::class.java)
            startActivity(intent)
            finish()
        }

        toTableButton.setOnClickListener {
            val intent = Intent(this, TableActivity::class.java)
            intent.putParcelableArrayListExtra("bloodPressureData", ArrayList(bloodPressureData))
            startActivity(intent)
        }
        // Inicjacja wykresu oraz wczytanie danych z bazy firestore
        setupChart()
        loadBloodPressureMeasurementsFromFirestore()
    }

    // Metoda do konfiguracji wykresu
    private fun setupChart() {
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
    }

    // Metoda do dodawania nowego pomiaru ciśnienia krwi
    private fun addBloodPressureMeasurement() {
        val systolic = systolicEditText.text.toString().toFloatOrNull()
        val diastolic = diastolicEditText.text.toString().toFloatOrNull()
        val selectedTimeId = timeRadioGroup.checkedRadioButtonId

        if (systolic == null || diastolic == null || selectedTimeId == -1) {
            Toast.makeText(this, "Proszę wypełnić wszystkie pola prawidłowymi danymi", Toast.LENGTH_SHORT).show()
            return
        }

        val partOfDay = if (selectedTimeId == R.id.morningRadioButton) "morning" else "evening"
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val measurement = BloodPressureMeasurement(systolic, diastolic, partOfDay, date)
        bloodPressureData.add(measurement)
        updateChart()
        saveBloodPressureMeasurementToFirestore(measurement)
    }

    // Metoda do aktualizacji wykresu
    private fun updateChart() {
        val entriesSystolic = ArrayList<Entry>()
        val entriesDiastolic = ArrayList<Entry>()

        for ((index, measurement) in bloodPressureData.withIndex()) {
            entriesSystolic.add(Entry(index.toFloat(), measurement.systolic))
            entriesDiastolic.add(Entry(index.toFloat(), measurement.diastolic))
        }

        val dataSetSystolic = LineDataSet(entriesSystolic, "Skurczowe").apply {
            color = Color.BLUE
        }

        val dataSetDiastolic = LineDataSet(entriesDiastolic, "Rozkurczowe").apply {
            color = Color.RED
            enableDashedLine(10f, 5f, 0f)  // Ustawiamy przerywaną linię
        }

        val lineData = LineData(dataSetSystolic, dataSetDiastolic)
        lineChart.data = lineData
        lineChart.invalidate()
    }

    // Metoda do zapisywania pomiaru ciśnienia krwi do Firestore
    private fun saveBloodPressureMeasurementToFirestore(measurement: BloodPressureMeasurement) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val collection = "bloodPressureMeasurements"
            val document = firestore.collection("users").document(userId).collection(collection).document()

            document.set(measurement)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pomiar zapisany", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Nie udało się zapisać pomiaru: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Metoda do wczytywania pomiarów ciśnienia krwi z Firestore
    private fun loadBloodPressureMeasurementsFromFirestore() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val collection = "bloodPressureMeasurements"

            firestore.collection("users").document(userId).collection(collection).get()
                .addOnSuccessListener { snapshot ->
                    for (document in snapshot) {
                        val measurement = document.toObject(BloodPressureMeasurement::class.java)
                        bloodPressureData.add(measurement)
                    }
                    updateChart()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Nie udało się wczytać pomiarów: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
