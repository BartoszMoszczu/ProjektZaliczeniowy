package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida i Firebase
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.util.Log

import java.util.Calendar

// Definiujemy klasę MainActivity, która dziedziczy po AppCompatActivity
/**
 * Główna aktywność aplikacji, która zarządza interfejsem użytkownika i alarmami codziennymi.
 *
 * @property db Instancja FirebaseFirestore do interakcji z bazą danych.
 */
class MainActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore

    // Metoda onCreate jest wywoływana podczas tworzenia aktywności

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Ustawiamy codzienne powiadomienia
        setDailyAlarms()

        // Inicjalizujemy przyciski i ustawiamy ich akcje kliknięcia
        val loginButton: Button = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.button)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Ustawiamy, aby widoki reagowały na operacje aplikacji oraz nie zakrywały elementów interfejsu użytkownika
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val infoButton: Button = findViewById(R.id.infoButton)
        infoButton.setOnClickListener {
            showInfoDialog()
        }
    }

    // Metoda do wyświetlania dialogu z informacjami o aplikacji
    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informacje o aplikacji")
        builder.setMessage("\tNadciśnienie tętnicze jest chorobą, w której ciśnienie skurczowe i rozkurczowe krwi w tętnicach jest wyższe niż prawidłowe wynoszące 139/89 mmHg +" +
                "Charakteryzuje się one tym, że wysokie wartości występują przez dłuższy czas. W celu zdiagnozowania \n" +
                "i monitorowania tej choroby, wykonuje się powtarzalne pomiary ciśnienia tętniczego za pomocą ciśnieniomierzy lub holterów ciśnieniowych. W 2018 r. w Polsce zarejestrowano 9,9 mln osób pełnoletnich chorujących na nadciśnienie, co stanowiło ok. 31,5% populacji kraju. Ponadto szacuje się, że na świecie, w 2015 r., na nadciśnienie choruje 1,13 mld ludzi. Na występowanie nadciśnienia wpływają m. in. otyłość, stres, spożywanie sodu (soli kuchennej) i czynniki genetyczne. Nadciśnienie tętnicze powiązane jest również z występowaniem innych chorób tj. zaburzenia przemian lipidów, cukrzyca insulinoniezależna, choroby niedokrwiennej serca (udar), migotanie przedsionków, otyłość, przewlekła niewydolność nerek i miażdżyca +" +
                "Dodatkowo warto podkreślić, że co roku choroby układu krążenia stanowią największą przyczynę zgonów w społeczeństwie Polski. W latach 1990 – 2013 zgony z ich powodu stanowiły od 45 do 53 % wszystkich zgonów +" +
                "oraz 35% w 2021 roku . W celu uniknięcia chorób krążenia i powikłań z nimi związanych, ważne jest, aby regularnie badać i kontrolować ciśnienie krwi.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    // Metoda do ustawiania codziennych alarmów
    private fun setDailyAlarms() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val morningIntent = Intent(this, AlarmReceiver::class.java)
        val morningPendingIntent = PendingIntent.getBroadcast(this, 0, morningIntent,
             PendingIntent.FLAG_IMMUTABLE)

        val eveningIntent = Intent(this, AlarmReceiver::class.java)
        val eveningPendingIntent = PendingIntent.getBroadcast(this, 1, eveningIntent,
             PendingIntent.FLAG_IMMUTABLE)
        // Ustawienie godziny dla alarmów (np. 8:00 i 20:00)
        val morningTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        val eveningTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
        Log.d("MainActivity", "Setting morning alarm for: $morningTime")
        Log.d("MainActivity", "Setting evening alarm for: $eveningTime")
        // Ustawiamy powtarzające się alarmy na rano i wieczór
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            morningTime,
            AlarmManager.INTERVAL_DAY,
            morningPendingIntent
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            eveningTime,
            AlarmManager.INTERVAL_DAY,
            eveningPendingIntent
        )
    }

    // Obiekt towarzyszący zawierający stałe wartości
    companion object {
        private const val TAG = "MainActivity"
    }
}

