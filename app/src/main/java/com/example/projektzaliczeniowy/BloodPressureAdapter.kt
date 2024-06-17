package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Definiujemy adapter BloodPressureAdapter, który dziedziczy po RecyclerView.Adapter
/**
 * Adapter do wyświetlania pomiarów ciśnienia krwi w RecyclerView.
 *
 * @property data Lista pomiarów ciśnienia krwi do wyświetlenia.
 */
class BloodPressureAdapter(private val data: List<BloodPressureMeasurement>) :
    RecyclerView.Adapter<BloodPressureAdapter.ViewHolder>() {

    // Definiujemy ViewHolder, który przechowuje widoki dla pojedynczego elementu listy
    /**
     * ViewHolder przechowujący widoki dla pojedynczego elementu listy pomiarów ciśnienia krwi.
     *
     * @property systolicTextView TextView do wyświetlania wartości ciśnienia skurczowego.
     * @property diastolicTextView TextView do wyświetlania wartości ciśnienia rozkurczowego.
     * @property partOfDayTextView TextView do wyświetlania części dnia pomiaru.
     * @property dateTextView TextView do wyświetlania daty pomiaru.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val systolicTextView: TextView = view.findViewById(R.id.systolicTextView)
        val diastolicTextView: TextView = view.findViewById(R.id.diastolicTextView)
        val partOfDayTextView: TextView = view.findViewById(R.id.partOfDayTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    }

    // Metoda tworząca nowe widoki (wywoływana przez layout managera)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Wywołujemy widok dla pojedynczego elementu listy
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.blood_pressure_item, parent, false)
        return ViewHolder(view)
    }

    // Metoda wiążąca dane z widokami (wywoływana przez layout managera)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pobieramy element danych z określonej pozycji
        val item = data[position]
        // Ustawiamy wartości dla TextView z danych elementu
        holder.systolicTextView.text = "Skurczowe: ${item.systolic}"
        holder.diastolicTextView.text = "Rozkurczowe: ${item.diastolic}"
        holder.partOfDayTextView.text = "Czas: ${item.partOfDay}"
        holder.dateTextView.text = "Data: ${item.date}"
    }

    // Metoda zwracająca liczbę elementów w liście
    override fun getItemCount(): Int = data.size
}

