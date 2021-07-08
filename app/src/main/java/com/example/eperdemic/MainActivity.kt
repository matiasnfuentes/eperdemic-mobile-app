package com.example.eperdemic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eventAdapter = EventAdapter(mutableListOf())
        rvEventList.adapter = eventAdapter
        rvEventList.layoutManager = LinearLayoutManager(this)

        btnFeedPatogeno.setOnClickListener {

        }

        btnFeedUbicacion.setOnClickListener {

        }

        btnFeedVector.setOnClickListener {

        }

    }
}