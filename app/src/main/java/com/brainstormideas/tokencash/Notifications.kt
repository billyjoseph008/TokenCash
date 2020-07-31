package com.brainstormideas.tokencash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class Notifications : AppCompatActivity() {

    private lateinit var back_notificaciones_ibtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        getSupportActionBar()?.hide()
        initComponents()
    }

    fun initComponents() {

        back_notificaciones_ibtn = findViewById(R.id.back_notificaciones_ibtn)

        back_notificaciones_ibtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                goToMain()
            }

        })

    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
