    package com.example.labs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

    class MainBackground : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1 =  findViewById<Button>(R.id.task1)
        val btn2 =  findViewById<Button>(R.id.task2)


        btn1.setOnClickListener{
            val intent = Intent(this,ThreadsActivity::class.java)
            startActivity(intent)
        }
        btn2.setOnClickListener{
            val intent = Intent(this,RequestActivity::class.java)
            startActivity(intent)
        }
    }
}