package com.example.eperdemic.services

import android.util.Log
import com.example.eperdemic.Event
import com.example.eperdemic.EventAdapter
import com.example.eperdemic.dao.EventDAO
import com.example.eperdemic.dao.FirebaseEventDAO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FeedService(private val eventDAO: EventDAO) {

    fun feedPatogeno(tipo: String){ eventDAO.feedPatogeno(tipo) }

    fun feedUbicacion(ubicacion: String){ eventDAO.feedUbicacion(ubicacion) }

    fun feedVector(vectorId: String){ eventDAO.feedVector(vectorId) }

}