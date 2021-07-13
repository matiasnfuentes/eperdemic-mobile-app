package com.example.eperdemic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eperdemic.services.FeedService
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

        val feedService = FeedService(db, eventAdapter)

        Firebase.messaging.subscribeToTopic("Pandemia")

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
            val vectorId = etInput.text.toString()
            if(vectorId.isNotEmpty()){
                feedService.feedVector(vectorId)
                etInput.text.clear()
            }

        }

        btnClear.setOnClickListener { eventAdapter.clearEvents() }

    }


}