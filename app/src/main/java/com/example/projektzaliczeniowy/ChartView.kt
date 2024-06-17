package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.util.Log

// Definiujemy klasę ChartView, która dziedziczy po klasie View
/**
 *  Widok do stworzenia wykresu pomiarów ciśnienia krwi.
 *
 * @property data Lista pomiarów ciśnienia krwi do wyświetlenia na wykresie.
 * @property paint Obiekt Paint używany do rysowania linii na wykresie.
 */
class ChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var data: List<BloodPressureMeasurement> = emptyList()
    private val paint = Paint()

    init {
        // Inicjalizujemy obiekt Paint z ustawieniami koloru i grubości linii
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
    }

    // Metoda do ustawiania danych pomiarowych
    /**
     * Ustawia dane pomiarowe i odświeża widok.
     *
     * @param data Lista pomiarów ciśnienia krwi.
     */
    fun setData(data: List<BloodPressureMeasurement>) {
        this.data = data
        Log.d("ChartView", "Dane ustawione: $data")
        invalidate() // Odświeżamy widok, aby narysować nowe dane
    }

    // Nadpisujemy metodę onDraw, aby narysować wykres
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isNotEmpty()) {
            // Obliczamy maksymalne wartości skurczowego i rozkurczowego ciśnienia krwi
            val maxSystolic = data.maxOf { it.systolic }
            val maxDiastolic = data.maxOf { it.diastolic }
            val maxY = maxOf(maxSystolic, maxDiastolic)
            val width = width.toFloat()
            val height = height.toFloat()
            val pointDistance = width / (data.size - 1)

            var lastX = 0f
            var lastSystolicY = height - (data[0].systolic / maxY.toFloat()) * height
            var lastDiastolicY = height - (data[0].diastolic / maxY.toFloat()) * height

            // Rysujemy linie pomiędzy punktami danych
            for (i in 1 until data.size) {
                val currentX = i * pointDistance
                val systolicY = height - (data[i].systolic / maxY.toFloat()) * height
                val diastolicY = height - (data[i].diastolic / maxY.toFloat()) * height

                // Rysujemy linie dla ciśnienia skurczowego
                canvas.drawLine(lastX, lastSystolicY, currentX, systolicY, paint)
                // Rysujemy linie dla ciśnienia rozkurczowego
                canvas.drawLine(lastX, lastDiastolicY, currentX, diastolicY, paint)

                lastX = currentX
                lastSystolicY = systolicY
                lastDiastolicY = diastolicY
            }
        }
    }
}


