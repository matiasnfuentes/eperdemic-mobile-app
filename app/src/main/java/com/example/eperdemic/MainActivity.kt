package com.example.eperdemic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eperdemic.dao.FirebaseEventDAO
import com.example.eperdemic.services.FeedService
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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

        val eventDAO = FirebaseEventDAO(db, eventAdapter)
        val feedService = FeedService(eventDAO)

        Firebase.messaging.subscribeToTopic("Pandemia")

        db.collection("Mutacion")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Error: ", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val events = mutableListOf<Event>()
                for (dc in value!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> events.add(Event(dc.document.data["mensaje"].toString(),
                                                                      dc.document.data["momento"].toString()))
                    }
                }

                eventAdapter.showEvents(events)
            }

        db.collection("Contagio")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Error: ", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val events = mutableListOf<Event>()
                for (dc in value!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> events.add(Event(dc.document.data["mensaje"].toString(),
                            dc.document.data["momento"].toString()))
                    }
                }

                eventAdapter.showEvents(events)
            }

        db.collection("Arribo")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Error: ", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val events = mutableListOf<Event>()
                for (dc in value!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> events.add(Event(dc.document.data["mensaje"].toString(),
                            dc.document.data["momento"].toString()))
                    }
                }

                eventAdapter.showEvents(events)
            }

        btnFeedPatogeno.setOnClickListener {
            val tipo = etInput.text.toString()
            if(tipo.isNotEmpty()){
                feedService.feedPatogeno(tipo)
                etInput.text.clear()
            }
        }

        btnFeedUbicacion.setOnClickListener {
            val ubicacion = etInput.text.toString()
            if(ubicacion.isNotEmpty()){
                feedService.feedUbicacion(ubicacion)
                etInput.text.clear()
            }
        }

        btnFeedVector.setOnClickListener {
            val id = etInput.text.toString()
            if(id.isNotEmpty()){
                feedService.feedVector(id)
                etInput.text.clear()
            }
        }


        btnClear.setOnClickListener { eventAdapter.clearEvents() }

    }

}