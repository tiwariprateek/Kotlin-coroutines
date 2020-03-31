package com.example.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        button2.setOnClickListener {
            button2.setText("Clicked")
            fakeApiRequest2()

            //fakeApiRequest1()
        }
    }
    private fun fakeApiRequest2(){
        CoroutineScope(IO).launch {
            val executionTime= measureTimeMillis {
                val result1 : Deferred<String> = async {
                    println("Debug : Launching job1 on ${Thread.currentThread().name}")
                    getResult1FromApi()
                }
                val result2 : Deferred<String> = async {
                    println("Debug : Launching job1 on ${Thread.currentThread().name}")
                    getResult2FromApi()
                }
                setTextOnMainThread("GOT ${result1.await()}")
                setTextOnMainThread("GOT ${result2.await()}")
            }
            println("Debug : Total time of execution is $executionTime")
        }
    }
    private fun fakeApiRequest1(){
        val startTime=System.currentTimeMillis()
        val parentJob=CoroutineScope(IO).launch {
            val job1= launch {
                //time1 is stuck inside coroutine
                val time1 = measureTimeMillis {
                    println("Debug : Launching job1 in ${Thread.currentThread().name} ")
                    val result=getResult1FromApi()
                    setTextOnMainThread(result)
                }
                println("Debug : Time of execution for job1 is $time1")
            }
            val job2= launch {
                val time2 = measureTimeMillis {
                    println("Debug : Launching job2 in ${Thread.currentThread().name} ")
                    val result=getResult2FromApi()
                    setTextOnMainThread(result)
                }
                println("Debug : Time of execution for job2 is $time2")
            }
            }
        parentJob.invokeOnCompletion {
            println("Debug : Total time for both the job completion is ${System.currentTimeMillis()-startTime}")
        }
        }
    private fun setNewText(text:String){
        val newText=textView2.text.toString()+ "\n$text"
        textView2.setText(newText)
    }
    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setNewText(input)
        }
    }
    private suspend fun getResult1FromApi():String{
        delay(1000)
        return "RESULT #1"
    }
    private suspend fun getResult2FromApi():String{
        delay(1700)
        return "RESULT #2"
    }
}
