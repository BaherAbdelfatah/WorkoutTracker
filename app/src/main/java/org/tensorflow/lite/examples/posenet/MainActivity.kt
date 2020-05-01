package org.tensorflow.lite.examples.posenet

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tfe_pn_activity_posenet.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // access the items of the list
        val Exercises = resources.getStringArray(R.array.Exercises)
        val button = findViewById<Button>(R.id.startButton)
        button.setOnClickListener {
            val intent = Intent(this,CameraActivity::class.java)
            startActivity(intent)
        }
        // access the spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, Exercises)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@MainActivity,
                        getString(R.string.selected_item) + " " +
                                "" + Exercises[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
}
