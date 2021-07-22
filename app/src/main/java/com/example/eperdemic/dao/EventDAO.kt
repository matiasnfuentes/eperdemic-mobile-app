package com.example.eperdemic.dao

interface EventDAO {

    fun feedPatogeno(tipo: String)
    fun feedUbicacion(tipo: String)
    fun feedVector(tipo: String)

}