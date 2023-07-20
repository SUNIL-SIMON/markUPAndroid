package com.example.plansheets



import android.content.res.Configuration
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.plansheets.Sheet.SheetBaseView


class MainActivity : AppCompatActivity() {
    val shapeDefaultSize = 200
    var sheetBaseView : SheetBaseView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addButtons()

    }
    fun addButtons()
    {
        val context = applicationContext
        val layout = findViewById<FrameLayout>(R.id.layout1)
        sheetBaseView = SheetBaseView(this,context)

        val view = sheetBaseView?.createSheetBaseView(context)
        layout.addView(view)

        sheetBaseView?.performOrientation(context)

    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val context = applicationContext
        sheetBaseView?.performOrientation(context)

    }
}
