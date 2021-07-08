package com.example.eperdemic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventAdapter
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eventAdapter = EventAdapter(mutableListOf())
        rvEventList.adapter = eventAdapter
        rvEventList.layoutManager = LinearLayoutManager(this)


        btnFeedPatogeno.setOnClickListener {
            val tipo = etInput.text.toString()
            if(tipo.isNotEmpty()){
                feedPatogeno(tipo)
                etInput.text.clear()
            }
        }

        btnFeedUbicacion.setOnClickListener {
            val ubicacion = etInput.text.toString()
            if(ubicacion.isNotEmpty()){
                feedUbicacion(ubicacion)
                etInput.text.clear()
            }
        }

        btnFeedVector.setOnClickListener {
            val vectorId = etInput.text.toString()
            if(vectorId.isNotEmpty()){
                feedVector(vectorId)
                etInput.text.clear()
            }

        }

        btnClear.setOnClickListener { eventAdapter.clearEvents() }

    }

    private fun feedPatogeno(tipo: String){

        val currentEvents = mutableListOf<Event>()

        db.collection("Mutacion")
           .whereEqualTo("especie.patogeno.tipo", tipo)
           .get()
           .addOnSuccessListener { documents ->
               documents.forEach { currentEvents.add(getEvent(it)) }
               getContagios(currentEvents, listOf("Pandemia","PrimerContagioEnUbicacion"), "especie.patogeno.tipo",  tipo)
           }
    }


    private fun feedUbicacion(ubicacion: String){

        val currentEvents = mutableListOf<Event>()

        db.collection("Arribo")
           .whereEqualTo("ubicacion.nombre", ubicacion)
           .get()
           .addOnSuccessListener { documents ->
                documents.forEach { currentEvents.add(getEvent(it)) }
                getContagios(currentEvents, listOf("Contagio"),"ubicacion.nombre", ubicacion)
           }
    }

    private fun feedVector(vectorId: String){

        val currentEvents = mutableListOf<Event>()

        db.collection("Arribo")
            .whereEqualTo("vector.id", vectorId)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { currentEvents.add(getEvent(it)) }
                getInfecciones(vectorId , currentEvents)
            }
    }

    private fun getInfecciones(vectorId : String, currentEvents:MutableList<Event>){
        db.collection("Contagio")
            .whereEqualTo("subtipo", "Contagio")
            .whereEqualTo("transmisor.id", vectorId)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { currentEvents.add(getEvent(it)) }
                getContagios(currentEvents, listOf("Contagio"),"infectado.id", vectorId)
            }

    }

    fun getContagios(currentEvents:MutableList<Event>, subtipos: List<String>, field : String ,tipo: String){
        db.collection("Contagio")
            .whereEqualTo(field, tipo)
            .whereIn("subtipo", subtipos)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { currentEvents.add(getEvent(it)) }
                showEvents(currentEvents)
            }
    }


    private fun showEvents(currentEvents: MutableList<Event>){
        currentEvents.sortByDescending { it.momento }
        currentEvents.forEach {
            Log.i("eperdemic: ",it.text)
            eventAdapter.addEvent(it)
        }
    }

    private fun getEvent(document: QueryDocumentSnapshot) : Event{
        val text = document.data["mensaje"].toString()
        val momento = document.data["momento"].toString()
        val event = Event(text, momento)
        return event
    }

}