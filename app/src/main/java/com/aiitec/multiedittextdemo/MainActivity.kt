package com.aiitec.multiedittextdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        test_multiInputEditText.setOnInputCompleteLister { result ->

        }

        test_multiInputEditText.setOnKeyBackPressLister {
            finish()
        }
    }
}
