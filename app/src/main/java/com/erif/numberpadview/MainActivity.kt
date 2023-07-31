package com.erif.numberpadview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erif.library.NumberPadListener
import com.erif.library.NumberPadView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberPad: NumberPadView = findViewById(R.id.act_main_numberPadView)
        numberPad.addListener(object : NumberPadListener{
            override fun onClickNumber(number: String) {
                toast(number)
            }

            override fun onClickLeftPane() {
                toast("Left Pane")
            }

            override fun onClickBackSpace() {
                toast("Backspace")
            }

        })

    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}