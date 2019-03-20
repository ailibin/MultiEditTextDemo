package com.aiitec.multiedittextdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        test_multiInputEditText.setOnInputCompleteLister { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }

        test_multiInputEditText.setOnKeyBackPressLister {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        test_multiInputEditText.onKeyDownInView(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }
}
