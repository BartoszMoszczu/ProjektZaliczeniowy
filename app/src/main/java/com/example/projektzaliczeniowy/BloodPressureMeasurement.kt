package com.example.projektzaliczeniowy

// Importujemy wymaganą klasę Parcelable z bibliotek Androida
import android.os.Parcel
import android.os.Parcelable

// Definiujemy klasę danych BloodPressureMeasurement, która implementuje interfejs Parcelable
/**
 * Klasa reprezentująca pomiar ciśnienia krwi, która może być przesyłana pomiędzy komponentami Androida.
 * Służy ona zmniejszeniu rozmiarów danych, co ułatwia odwoływanie się do nich w innych fragmentach aplikacji.
 * @property systolic Ciśnienie skurczowe.
 * @property diastolic Ciśnienie rozkurczowe.
 * @property partOfDay Część dnia, w której dokonano pomiaru.
 * @property date Data pomiaru.
 */
data class BloodPressureMeasurement(
    val systolic: Float = 0f,
    val diastolic: Float = 0f,
    val partOfDay: String? = "",
    val date: String? = ""
) : Parcelable {
    // Konstruktor do tworzenia obiektu z Parcel
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString()
    )

    // Opis zawartości dla Parcelable (nieużywane, zwraca 0)
    override fun describeContents(): Int {
        return 0
    }

    // Metoda do zapisywania danych do Parcel
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(systolic)
        dest.writeFloat(diastolic)
        dest.writeString(partOfDay)
        dest.writeString(date)
    }

    // Obiekt towarzyszący CREATOR do tworzenia instancji Parcelable
    companion object CREATOR : Parcelable.Creator<BloodPressureMeasurement> {
        // Tworzy obiekt z Parcel
        override fun createFromParcel(parcel: Parcel): BloodPressureMeasurement {
            return BloodPressureMeasurement(parcel)
        }

        // Tworzy nową tablicę obiektów Parcelable
        override fun newArray(size: Int): Array<BloodPressureMeasurement?> {
            return arrayOfNulls(size)
        }
    }
}

