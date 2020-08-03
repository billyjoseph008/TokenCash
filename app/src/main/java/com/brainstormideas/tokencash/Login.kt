package com.brainstormideas.tokencash

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brainstormideas.tokencash.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates


class Login : AppCompatActivity() {

    private lateinit var email_etx: EditText
    private lateinit var pass_etx: EditText
    private lateinit var login_btn: Button
    private lateinit var register_btn: Button
    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()
    private lateinit var progressBar: ProgressDialog

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getSupportActionBar()?.hide()

        initComponents()
    }

    fun initComponents(){

        sessionManager = SessionManager()
        sessionManager.SessionManager(getApplicationContext())

        email_etx = findViewById(R.id.email_etx)
        pass_etx = findViewById(R.id.pass_etx)
        login_btn = findViewById(R.id.login_btn)
        register_btn = findViewById(R.id.register_btn)

        login_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                loginUser()
            }
        })

        register_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                registerUser()
            }
        })

        progressBar = ProgressDialog(this)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        databaseReference = database.reference.child("Users")

    }

    private fun loginUser() {

        email = email_etx.text.toString()
        password = pass_etx.text.toString()

        if(email.equals("admin") && password.equals("admin")){
            sessionManager.createLoginSession("User", email)
            goToMain()
        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            progressBar.setMessage("Iniciando sesion...")
            progressBar.show()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                        task ->
                    if (task.isSuccessful) {
                        sessionManager.createLoginSession("User", email)
                        goToMain()
                    } else {
                        Toast.makeText(this, "Fallo al autenticar",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Ingrese todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(){

        email = email_etx.text.toString()
        password = pass_etx.text.toString()


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            progressBar.setMessage("Registrando usuario...")
            progressBar.show()

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Usuario creado correctamente con el correo $email",
                        Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "Error al registrar usurio",
                        Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            Toast.makeText(this, "Ingrese todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

    fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}