package com.example.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val RESULT_1="Result #1"
    val RESULT_2="Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener{
            CoroutineScope(IO).launch {
                fakeApiResult()
            }
        }
    }
    private fun setNewText(input:String){
        val text=textView.text.toString() + "\n$input"
        textView.setText(text)
    }
    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setNewText(input)
        }
    }
    private suspend fun fakeApiResult(){
        val result1=getResult1FromApi()
        setTextOnMainThread(result1)

        val result2=getResult2FromApi()
        setTextOnMainThread(result2)


    }
    private suspend fun getResult2FromApi():String{
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }
    private suspend fun getResult1FromApi():String{
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    fun logThread(methodName:String){
        Log.d("Debug","${methodName} is ${Thread.currentThread().name}")
    }
}
