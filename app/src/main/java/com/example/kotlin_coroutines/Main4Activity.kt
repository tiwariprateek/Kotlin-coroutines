package com.example.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main4.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Main4Activity : AppCompatActivity() {

    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        main()
        button3.setOnClickListener {
            textView3.setText((count++).toString())
        }
    }
    fun main(){
        CoroutineScope(Main).launch {
            println("Debug : current thread ${Thread.currentThread().name} ")
            delay(3000)
            //This could freeze the UI as this makes the main thread to sleep
            //Thread.sleep(3000)
        }
    }
}
