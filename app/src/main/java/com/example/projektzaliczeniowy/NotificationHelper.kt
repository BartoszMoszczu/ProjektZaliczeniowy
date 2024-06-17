package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

// Adnotacja wskazująca, że ta klasa wymaga API na poziomie co najmniej Oreo (Build.VERSION_CODES.O)
@RequiresApi(Build.VERSION_CODES.O)
// Definiujemy klasę NotificationHelper
/**
 * Klasa pomocnicza do tworzenia i wysyłania powiadomień o przypomnieniach dotyczących ciśnienia krwi.
 *
 * @property context Kontekst aplikacji, używany do uzyskiwania dostępu do usług systemowych.
 */
class NotificationHelper(private val context: Context) {

    // Stałe wartości dla ID kanału powiadomień i nazwy kanału
    private val CHANNEL_ID = "blood_pressure_reminder_channel"
    private val CHANNEL_NAME = "Blood Pressure Reminder"

    // Inicjalizator, który wywołuje metodę tworzenia kanału powiadomień
    init {
        createNotificationChannel()
    }

    // Metoda do tworzenia kanału powiadomień
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Tworzymy kanał powiadomień z określonym ID, nazwą i poziomem ważności
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            // Ustawiamy opis kanału
            description = "Kanał do przypomnień o ciśnieniu krwi"
        }
        // Uzyskujemy dostęp do menedżera powiadomień
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Rejestrujemy kanał powiadomień
        manager.createNotificationChannel(channel)
    }

    // Metoda do wysyłania powiadomień
    fun sendNotification(title: String, message: String) {
        // Tworzymy powiadomienie za pomocą NotificationCompat.Builder
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.powiadomienie)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        // Uzyskujemy dostęp do menedżera powiadomień
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Wysyłamy powiadomienie z określonym ID (1)
        manager.notify(1, notification)
        Log.d("NotificationHelper", "Notification sent.")
        }
    }

