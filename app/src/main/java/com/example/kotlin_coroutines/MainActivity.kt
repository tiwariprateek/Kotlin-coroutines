package com.example.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {
    val RESULT_1 = "Result #1"
    val RESULT_2 = "Result #2"
    val jobTimeout = 1900L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            setNewText("Click!!")
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private suspend fun fakeApiRequest() {
        withContext(IO) {
            val job = withTimeoutOrNull(jobTimeout) {
                val result1 = getResult1FromApi()
                //setNewText(result1)
                setTextOnMainThread(result1)
                val result2 = getResult2FromApi()
                //setNewText(result2)
                setTextOnMainThread(result2)
            }
            if (job == null) {
                //Toast only works when coroutine are executed on Main thread
                //Toast.makeText(this@MainActivity,"TimeOut",Toast.LENGTH_LONG).show()
                Log.d("Debug", "Too long to respond")
            }
        }
    }

    private fun setNewText(input: String) {
        val text = textView.text.toString() + "\n$input"
        textView.setText(text)
    }
    private suspend fun setTextOnMainThread(input: String){
        withContext(Main){
            setNewText(input)
        }
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    fun logThread(methodName: String) {
        Log.d("Debug", "${methodName} is ${Thread.currentThread().name}")
    }
}

