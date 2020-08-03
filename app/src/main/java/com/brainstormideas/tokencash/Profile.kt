package com.brainstormideas.tokencash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brainstormideas.tokencash.utils.SessionManager

class Profile : AppCompatActivity() {

    private lateinit var back_ibtn: ImageButton
    private lateinit var pass_chance_btn: Button
    private lateinit var politics_btn: Button
    private lateinit var terms_btn: Button
    private lateinit var contact_btn: Button
    private lateinit var logout_btn: Button

    var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        getSupportActionBar()?.hide()

        initComponents()
    }

    fun initComponents(){

        sessionManager = SessionManager()
        sessionManager!!.SessionManager(applicationContext)

        back_ibtn = findViewById(R.id.back_ibtn)
        pass_chance_btn = findViewById(R.id.pass_chance_btn)
        politics_btn = findViewById(R.id.politics_btn)
        terms_btn = findViewById(R.id.terms_btn)
        contact_btn = findViewById(R.id.contact_btn)
        logout_btn = findViewById(R.id.logout_btn)

        back_ibtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                goToMain()
            }
        })
        pass_chance_btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
            }
        })
        politics_btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                goToPolitics()
            }
        })
        terms_btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                goToTerms()
            }
        })
        contact_btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                goToContact()
            }
        })
        logout_btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                logout()
            }
        })

    }

    fun goToMain(){
       intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun goToPassChange(){

    }
    fun goToPolitics(){
        val url="https://tokencash.mx/terminosycondiciones.php";
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    fun goToTerms(){
        val url="https://tokencash.mx/avisoprivacidad.php";
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    fun goToContact(){
        val url="https://tokencash.mx/contacto.php";
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    fun logout(){
        Toast.makeText(applicationContext, "Ha cerrado sesion exitosamente.", Toast.LENGTH_LONG).show()
        sessionManager?.logoutUser()
        goToMain()
    }
}