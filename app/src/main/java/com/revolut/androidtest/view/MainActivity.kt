package com.revolut.androidtest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.revolut.androidtest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_main.setNavigationIcon(R.drawable.ic_back);
    }
}
