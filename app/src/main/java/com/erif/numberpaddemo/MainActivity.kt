package com.erif.numberpaddemo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)
        val btn: Button = findViewById(R.id.btnExample)

        val frg = FrgPasswordInput()

        btn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.password_show,
                R.anim.password_exit,
                R.anim.password_show,
                R.anim.password_exit
            )
            transaction.replace(R.id.act_main_passwordContainer, frg)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

}