package com.brainstormideas.tokencash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.brainstormideas.tokencash.model.Registro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class WalletHistory : AppCompatActivity() {

    private lateinit var wallet_back_ibtn : ImageButton
    private lateinit var money_acumulate_tv: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReferenceRegistros: DatabaseReference
    private lateinit var database: FirebaseDatabase

    var historial: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_history)
        getSupportActionBar()?.hide()

        initComponents()
    }

    fun initComponents() {

        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        databaseReferenceRegistros = database.reference.child("Registros")

        wallet_back_ibtn = findViewById(R.id.wallet_back_btn)
        money_acumulate_tv = findViewById(R.id.money_acumulate_tv)

        wallet_back_ibtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                goToMain()
            }

        })

        val registroListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(registroSnapshot in dataSnapshot.children){
                    historial = historial + registroSnapshot.value + "\n"
                    money_acumulate_tv.setText(historial)
                }



                }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("AVISO", "loadPost:onCancelled", databaseError.toException())
            }

        }

        databaseReferenceRegistros.addValueEventListener(registroListener)

    }

    fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}


