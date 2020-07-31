package com.brainstormideas.tokencash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class WalletHistory : AppCompatActivity() {

    private lateinit var wallet_back_btn : Button
    private lateinit var money_acumulate_tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_history)
        getSupportActionBar()?.hide()

        initComponents()
    }

    fun initComponents() {

        wallet_back_btn = findViewById(R.id.wallet_back_btn)
        money_acumulate_tv = findViewById(R.id.money_acumulate_tv)

        wallet_back_btn.setOnClickListener(object : View.OnClickListener{
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
}