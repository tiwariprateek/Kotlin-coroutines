package com.example.kotlin_coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class Main2Activity : AppCompatActivity() {
    val PROGRESS_END=100
    val PROGRESS_START=0
    val JOB_TIME=4000
    lateinit var job: CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        job_button.setOnClickListener {
            if (!::job.isInitialized){initJob()}
            job_progress_bar.startJobOrCancel(job)
        }

    }
    fun ProgressBar.startJobOrCancel(job: Job){
        if(this.progress>0) {
            println("Activty already started")
            resetJob()
        }
        else{
            job_button.setText("Cancel Job")
            CoroutineScope(IO+job).launch {
                println("Coroutine is active $this with the job $job")
                for (i in PROGRESS_START..PROGRESS_END){
                    delay((JOB_TIME/PROGRESS_END).toLong())
                    this@startJobOrCancel.progress=i
                }
                updateTextView("JOB IS COMPLETE")
            }
        }
    }
    fun updateTextView(text:String){
        GlobalScope.launch(Main) {
            job_complete_text.setText(text)
        }

    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted){
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    fun initJob(){
        job_button.setText("Start Job")
        updateTextView("")
        job= Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg=it
                if (msg.isNullOrBlank()){
                    msg="Unknown cancellation error"
                }
                println("$job was cancelled. Reason $msg")
                showToast(msg)
            }
        }
        job_progress_bar.max=PROGRESS_END
        job_progress_bar.progress=PROGRESS_START

    }
    fun showToast(text:String){
        GlobalScope.launch(Main) {
        Toast.makeText(this@Main2Activity,text,Toast.LENGTH_LONG).show()
    }
    }
}
