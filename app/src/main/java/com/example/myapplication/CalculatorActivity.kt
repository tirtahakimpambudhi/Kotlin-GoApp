package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var resultTv: TextView
    private lateinit var solutionTv: TextView
    private lateinit var buttonC: MaterialButton
    private lateinit var buttonBrackOpen: MaterialButton
    private lateinit var buttonBrackClose: MaterialButton
    private lateinit var buttonDivide: MaterialButton
    private lateinit var buttonMultiply: MaterialButton
    private lateinit var buttonPlus: MaterialButton
    private lateinit var buttonMinus: MaterialButton
    private lateinit var buttonEquals: MaterialButton
    private lateinit var button0: MaterialButton
    private lateinit var button1: MaterialButton
    private lateinit var button2: MaterialButton
    private lateinit var button3: MaterialButton
    private lateinit var button4: MaterialButton
    private lateinit var button5: MaterialButton
    private lateinit var button6: MaterialButton
    private lateinit var button7: MaterialButton
    private lateinit var button8: MaterialButton
    private lateinit var button9: MaterialButton
    private lateinit var buttonAC: MaterialButton
    private lateinit var buttonDot: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)

        resultTv = findViewById(R.id.result_tv)
        solutionTv = findViewById(R.id.solution_tv)

        // Inisialisasi semua tombol dengan findViewById sebelum digunakan
        buttonC = findViewById(R.id.button_c)
        buttonBrackOpen = findViewById(R.id.button_open_bracket)
        buttonBrackClose = findViewById(R.id.button_close_bracket)
        buttonDivide = findViewById(R.id.button_divide)
        buttonMultiply = findViewById(R.id.button_multiply)
        buttonPlus = findViewById(R.id.button_plus)
        buttonMinus = findViewById(R.id.button_minus)
        buttonEquals = findViewById(R.id.button_equals)
        button0 = findViewById(R.id.button_0)
        button1 = findViewById(R.id.button_1)
        button2 = findViewById(R.id.button_2)
        button3 = findViewById(R.id.button_3)
        button4 = findViewById(R.id.button_4)
        button5 = findViewById(R.id.button_5)
        button6 = findViewById(R.id.button_6)
        button7 = findViewById(R.id.button_7)
        button8 = findViewById(R.id.button_8)
        button9 = findViewById(R.id.button_9)
        buttonAC = findViewById(R.id.button_ac)
        buttonDot = findViewById(R.id.button_dot)

        // Pasang listener untuk semua tombol
        buttonC.setOnClickListener(this)
        buttonBrackOpen.setOnClickListener(this)
        buttonBrackClose.setOnClickListener(this)
        buttonDivide.setOnClickListener(this)
        buttonMultiply.setOnClickListener(this)
        buttonPlus.setOnClickListener(this)
        buttonMinus.setOnClickListener(this)
        buttonEquals.setOnClickListener(this)
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        buttonAC.setOnClickListener(this)
        buttonDot.setOnClickListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun assignId(btn: MaterialButton, id: Int) {
        btn.apply {
            findViewById<MaterialButton>(id).setOnClickListener(this@CalculatorActivity)
        }
    }

    override fun onClick(view: View) {
        val button = view as MaterialButton
        val buttonText = button.text.toString()
        var dataToCalculate = solutionTv.text.toString()

        when (buttonText) {
            "AC" -> {
                solutionTv.text = ""
                resultTv.text = "0"
                return
            }
            "=" -> {
                if (isExpressionValid(dataToCalculate)) {
                    solutionTv.text = resultTv.text
                } else {
                    resultTv.text = "Err"
                }
                return
            }
            "C" -> {
                if (dataToCalculate.isNotEmpty()) {
                    dataToCalculate = dataToCalculate.dropLast(1)
                } else {
                    dataToCalculate = "0"
                }
            }
            else -> {
                dataToCalculate += buttonText
            }
        }

        solutionTv.text = dataToCalculate

        val finalResult = getResult(dataToCalculate)

        if (finalResult != "Err") {
            resultTv.text = finalResult
        } else {
            resultTv.text = "Err"
        }
    }


    private fun isExpressionValid(expression: String): Boolean {
        // Memeriksa apakah ekspresi tidak kosong dan tidak diakhiri dengan operator matematika
        return expression.isNotEmpty() && !expression.endsWith("+") &&
                !expression.endsWith("-") && !expression.endsWith("*") &&
                !expression.endsWith("/") && !expression.endsWith(".")
    }

    private fun getResult(data: String): String {
        if (data.isEmpty()) return "0"

        return try {
            val context = Context.enter()
            context.optimizationLevel = -1
            val scriptable: Scriptable = context.initStandardObjects()
            var finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString()

            // Jika hasil adalah "Undefined", kembalikan "Err"
            if (finalResult == "undefined" || finalResult.isEmpty()) {
                "Err"
            } else {
                if (finalResult.endsWith(".0")) {
                    finalResult = finalResult.replace(".0", "")
                }
                finalResult
            }
        } catch (e: Exception) {
            "Err"
        } finally {
            Context.exit()
        }
    }

}