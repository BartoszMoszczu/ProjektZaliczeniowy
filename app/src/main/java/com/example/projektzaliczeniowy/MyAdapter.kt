package com.example.projektzaliczeniowy

// Importujemy wymagane klasy z bibliotek Androida
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Definiujemy adapter MyAdapter, który dziedziczy po RecyclerView.Adapter
/**
 * Adapter do wyświetlania listy użytkowników w RecyclerView.
 *
 * @property userList Lista użytkowników do wyświetlenia.
 */
class MyAdapter(private val userList: ArrayList<User>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Definiujemy ViewHolder, który zawiera widoki dla pojedynczego elementu listy
    /**
     * ViewHolder przechowujący widoki dla pojedynczego elementu listy użytkowników.
     *
     * @property tvName TekstView wyświetlający imię użytkownika.
     * @property tvSurname TekstView wyświetlający nazwisko użytkownika.
     * @property tvAge TekstView wyświetlający wiek użytkownika.
     */
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.name)
        val tvSurname: TextView = itemView.findViewById(R.id.surname)
        val tvAge: TextView = itemView.findViewById(R.id.age)
    }

    // Metoda tworząca nowe widoki (wywoływana przez layout managera)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    // Metoda zwracająca liczbę elementów w liście
    override fun getItemCount(): Int {
        return userList.size
    }

    // Metoda wiążąca dane z widokami (wywoływana przez layout managera)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.text = userList[position].name
        holder.tvSurname.text = userList[position].surname
        holder.tvAge.text = userList[position].age.toString()
    }
}

