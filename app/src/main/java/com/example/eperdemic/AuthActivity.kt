package com.example.eperdemic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Set up
        setup()
    }

    private fun setup(){
        title = "Autenticación"

        val mail = etEmail.text
        val pass = etPassword.text

        btnRegistrar.setOnClickListener {
            if (mail.isNotEmpty() && pass.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(mail.toString(), pass.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome()
                        }else{
                            showAlert()
                        }
                    }
            }
        }

        btnLogIn.setOnClickListener {
            if (mail.isNotEmpty() && pass.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(mail.toString(), pass.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome()
                        }else{
                            showAlert()
                        }
                    }
            }

        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(){
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
}