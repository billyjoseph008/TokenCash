package com.brainstormideas.tokencash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.brainstormideas.tokencash.utils.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var profile_fab: FloatingActionButton
    private lateinit var notification_fab: FloatingActionButton
    private lateinit var pay_fab: FloatingActionButton
    private lateinit var scan_fab: FloatingActionButton

    private lateinit var credits_tv: TextView
    private lateinit var see_all_tv: TextView

    var sessionManager: SessionManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide()

        initComponents()

    }

    fun initComponents() {

        sessionManager = SessionManager()
        sessionManager!!.checkLogin()

        profile_fab = findViewById(R.id.profile_fab)
        notification_fab =  findViewById(R.id.notification_fab)
        pay_fab = findViewById(R.id.pay_fab)
        scan_fab = findViewById(R.id.scan_fab)
        credits_tv = findViewById(R.id.credits_tv)
        see_all_tv = findViewById(R.id.see_all_tv)

        profile_fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                goToProfile()
            }
        })
        notification_fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                goToNotifications()
            }
        })
        pay_fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                goToPayScan()
            }
        })
        scan_fab.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                goToNormalScan()
            }
        })
        see_all_tv.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                goToAllWallet()
            }
        })

    }

    fun goToProfile() {
        val intent = Intent(this, Profile::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun goToNotifications() {
        val intent = Intent(this, Notifications::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun goToPayScan() {
        val intent = Intent(this, ScanView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun goToNormalScan(){
        val intent = Intent(this, ScanView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun goToAllWallet(){
        val intent = Intent(this, WalletHistory::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}