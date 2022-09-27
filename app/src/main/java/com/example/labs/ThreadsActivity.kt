package com.example.labs

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max


class ThreadsActivity : AppCompatActivity() {

    private val btnStart by lazy { findViewById<Button>(R.id.btn_start) }
    private val btnPause by lazy { findViewById<Button>(R.id.btn_pause) }
    private val btnReset by lazy { findViewById<Button>(R.id.btn_reset) }
    private val tvTread1 by lazy { findViewById<TextView>(R.id.tv_tread1) }
    private val tvTread2 by lazy { findViewById<TextView>(R.id.tv_tread2) }
    private val btnIncrease by lazy { findViewById<ImageButton>(R.id.btn_increase) }
    private val btnDecrease by lazy { findViewById<ImageButton>(R.id.btn_decrease) }

    private val tread1 by lazy {
        MyThread(600L) { count ->
            runOnUiThread {
                tvTread1.text = "$count"
            }
        }
    }
    private val tread2 by lazy {
        MyThread(400L) { count ->
            runOnUiThread {
                tvTread2.text = "$count"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.treads_layout)

        tread1.start()
        tread2.start()

        btnStart.setOnClickListener {
            tread1.restart()
            tread2.restart()
        }
        btnPause.setOnClickListener {
            tread1.pause()
            tread2.pause()
        }
        btnReset.setOnClickListener {
            tread1.reset()
            tread2.reset()
        }
        btnIncrease.setOnClickListener {
            tread1.increase()
            tread2.increase()
        }
        btnDecrease.setOnClickListener {
            tread1.decrease()
            tread2.decrease()
        }

    }


    class MyThread(sleepMs: Long, private val action: (Int) -> Unit) : Thread() {

        private val isPause = AtomicBoolean(true)
        private var count = 0
        private val sleepMs = AtomicLong(sleepMs)

        override fun run() {
            try {
                while (!isInterrupted) {
                    sleep(sleepMs.get())
                    if (!isPause.get()) action(count++)
                }
            } catch (e: InterruptedException) {
                Log.e("tread", "${currentThread().name} Interrupted", e)
            }
        }

        fun increase() {
            sleepMs.set(sleepMs.get() + 1000)
        }

        fun decrease() {
            sleepMs.getAndAccumulate(1000) { current, given ->
                max(current - given, 0)
            }
        }

        fun restart() {
            isPause.set(false)
        }

        fun pause() {
            isPause.set(true)
        }

        fun reset() {
            count = 0
            action(count)
        }

    }

    override fun onDestroy() {
        tread1.interrupt()
        tread2.interrupt()

        super.onDestroy()
    }
}

//class Person(val id: Int,val name: String){
//    override fun hashCode(): Int {
//        return (id*13+2729*name.hashCode())
//        //return super.hashCode() //по объекту создает число, чтобы можно оценить разные объекты без сравнения полей// сравнивать в хешмапе
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (other == null) return false
//        if (other !is Person) return false
//        return (id == other.id && name == other.name)//
//    }
//
//    override fun toString(): String {
//        return "$id + $name"
//    } //вывод в лог (строковая репрезентация объекта)
//}
//
////в data уже переопределены методы equals and toString and hashCode
//data class PersonNew(val id: Int,val name: String){
//
//}
