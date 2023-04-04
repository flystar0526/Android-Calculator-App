package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var tvInput : TextView? = null
    private var lastNum : Boolean = false
    private var lastDot : Boolean = false
    private var subtract : Boolean = true
    private var stop : Boolean = false
    private var count : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput)
    }

    fun onDigit(view: View){
        if (!stop){
            tvInput?.append((view as Button).text)
            lastNum = true
            count ++
        }
        if (count >= 12){
            stop = true
        }
    }

    fun onClear(view: View){
        tvInput?.text = ""
        lastDot = false
        lastNum = false
        subtract = true
        stop = false
        count = 0
    }

    fun onDecimalPoint(view: View){
        if (!stop){
            if (lastNum && !lastDot){
                tvInput?.append((view as Button).text)
                lastDot = true
                count ++
            }
            if (count >= 12){
                stop = true
            }
        }
    }

    fun onOperator(view: View){
        if (!stop){
            tvInput?.text?.let {
                if ((lastNum || subtract) && !isOperatorAdded(it.toString())){
                    tvInput?.append((view as Button).text)
                    subtract = false
                    lastDot = false
                    count ++
                }
                if (count >= 12){
                    stop = true
                }
            }
        }
    }

    fun onEqual(view: View){
        if (!stop){
            if (lastNum){
                var tvValue = tvInput?.text.toString()
                var prefix = ""
                try {
                    if (tvValue.startsWith("-")){
                        prefix = "-"
                        tvValue = tvValue.substring(1)
                    }
                    if (tvValue.contains("-")){
                        val splitValue = tvValue.split("-")
                        var first = splitValue[0]
                        var second = splitValue[1]
                        tvInput?.text = removeZeroAfterDot(((prefix + first).toDouble() - second.toDouble()).toString())
                    }else if (tvValue.contains("+")){
                        val splitValue = tvValue.split("+")
                        var first = splitValue[0]
                        var second = splitValue[1]
                        tvInput?.text = removeZeroAfterDot(((prefix + first).toDouble() + second.toDouble()).toString())
                    }else if (tvValue.contains("*")){
                        val splitValue = tvValue.split("*")
                        var first = splitValue[0]
                        var second = splitValue[1]
                        var result = removeZeroAfterDot(((prefix + first).toDouble() * second.toDouble()).toString())
                        count = result.length
                        tvInput?.text = result
                        if (count >= 12){
                            stop = true
                        }
                    }else if (tvValue.contains("/")){
                        val splitValue = tvValue.split("/")
                        var first = splitValue[0]
                        var second = splitValue[1]
                        if (second == "0"){
                            tvInput?.text = "Infinity"
                            stop = true
                        }
                        var result = removeZeroAfterDot(((prefix + first).toDouble() / second.toDouble()).toString())
                        count = result.length
                        if (count >= 12){
                            stop = true
                            result = result.substring(0, 12)
                        }
                        tvInput?.text = result
                    }
                }catch (e: ArithmeticException){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun removeZeroAfterDot(result: String) : String{
        var value = result
        if (result.endsWith(".0")){
            value = result.substring(0, result.length - 2)
        }
        return value
    }

    private fun isOperatorAdded(value: String) : Boolean{
        if (value.startsWith("-")){
            return false
        }else{
            return value.contains("/") || value.contains("*") || value.contains("+") || value.contains("-")
        }
    }
}