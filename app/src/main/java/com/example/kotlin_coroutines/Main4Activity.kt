package com.example.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main4.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random


class Main4Activity : AppCompatActivity() {

    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        //main()
        //executeApirequest()
        someFunc()
        button3.setOnClickListener {
            textView3.setText((count++).toString())
        }
    }
    private fun someFunc(){
        CoroutineScope(Main).launch {
            println("Debug : The Result1 is ${getresult()}")
            println("Debug : The Result2is ${getresult()}")
            println("Debug : The Result3 is ${getresult()}")
            println("Debug : The Result4 is ${getresult()}")
            println("Debug : The Result5 is ${getresult()}")
        }
        CoroutineScope(Main).launch {
            delay(1000)
            runBlocking {
                println("Debug : Blocking Thread : ${Thread.currentThread().name}")
                delay(4000)
                println("Debug : Done Blocking Thread : ${Thread.currentThread().name}")
            }
        }

    }
    private suspend fun getresult():Int{
        delay(1000)
        return Random.nextInt(0,100)
    }
    private fun executeApirequest(){
        CoroutineScope(Main).launch {//Parent Job
            for (i in 1..100_000)
                launch { fakeApiRequest() }//Child Job

        }

    }
    private suspend fun fakeApiRequest(){
        println("Started Network request !")
        delay(3000)
        println("Received result")
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
