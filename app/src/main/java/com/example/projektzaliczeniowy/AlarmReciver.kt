package com.example.projektzaliczeniowy
/*
Klasa AlarmReciver służaca do implementacji powiadomień o pomiarze ciśnienia.
 */

// Importujemy wymagane klasy z bibliotek Androida
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

// Definiujemy klasę AlarmReceiver, która dziedziczy po BroadcastReceiver
/**
 * Odbiornik alarmów, który otrzymuje powiadomienia o alarmach i wysyła powiadomienia do użytkownika.
 */
class AlarmReceiver : BroadcastReceiver() {

    // Nadpisujemy metodę onReceive, która jest wywoływana, gdy odbiornik otrzyma powiadomienie o alarmie
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm received. Sending notification.")
        // Tworzymy instancję NotificationHelper
        val notificationHelper = NotificationHelper(context)
        // Wysyłamy powiadomienie do użytkownika
        notificationHelper.sendNotification(
            "Pomiary ciśnienia krwi",
            "Czas na wykonanie pomiaru ciśnienia krwi."
        )
    }
}

