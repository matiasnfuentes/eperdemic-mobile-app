package com.example.eperdemic.services

import android.util.Log
import com.example.eperdemic.Event
import com.example.eperdemic.EventAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FeedService(private val db : FirebaseFirestore,
                  private val eventAdapter: EventAdapter) {

    fun feedPatogeno(tipo: String){

        val currentEvents = mutableListOf<Event>()

        db.collection("Mutacion")
            .whereEqualTo("especie.patogeno.tipo", tipo)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { currentEvents.add(getEvent(it)) }
                getContagios(currentEvents, listOf("Pandemia","PrimerContagioEnUbicacion"), "especie.patogeno.tipo",  tipo)
            }
    }


    fun feedUbicacion(ubicacion: String){

        val currentEvents = mutableListOf<Event>()

        db.collection("Arribo")
            .whereEqualTo("ubicacion.nombre", ubicacion)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { currentEvents.add(getEvent(it)) }
                getContagios(currentEvents, listOf("Contagio"),"ubicacion.nombre", ubicacion)
            }
    }

    fun feedVector(vectorId: String){

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

    private fun getContagios(currentEvents:MutableList<Event>, subtipos: List<String>, field : String, tipo: String){
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

    private fun getEvent(document: QueryDocumentSnapshot) : Event {
        val text = document.data["mensaje"].toString()
        val momento = document.data["momento"].toString()
        return Event(text, momento)
    }

}