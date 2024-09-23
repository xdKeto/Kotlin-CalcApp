package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private var canAddOperation = false
    private var canAddDecimal = true

    fun allClearAction(view: View) {
        val inputText = findViewById<TextView>(R.id.workingText)
        val resultText = findViewById<TextView>(R.id.resultText)
        inputText.text = ""
        resultText.text = ""
    }
    fun backSpaceAction(view: View) {
        val inputText = findViewById<TextView>(R.id.workingText)
        val length = inputText.length()
        if (length > 0) {
            inputText.text = inputText.text.subSequence(0, length - 1)
        }

    }

    fun equalAction(view: View) {
        val resultText = findViewById<TextView>(R.id.resultText)

        resultText.text = calculateResult()
    }

    private fun calculateResult(): String {
        val inputText = findViewById<TextView>(R.id.workingText)
        val resultText = findViewById<TextView>(R.id.resultText)

        val operations = digitOperations()
        if(operations.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(operations)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(timesDivision: MutableList<Any>): Float {
        var result = timesDivision[0] as Float

        for (i in timesDivision.indices){
            if(timesDivision[i] is Char && i != timesDivision.lastIndex){
                val operator = timesDivision[i]
                val nextDigit = timesDivision[i+1] as Float

                if(operator == '+'){
                    result += nextDigit
                }
                if(operator == '–'){
                    result -= nextDigit
                }
            }
        }

        return result
    }

    private fun timesDivisionCalculate(operations: MutableList<Any>): MutableList<Any> {
        var list = operations
        while (list.contains('×') || list.contains('÷')){
            list = calculateTimesDivs(list)
        }

        return list
    }

    private fun calculateTimesDivs(list: MutableList<Any>): MutableList<Any> {

        val newList = mutableListOf<Any>()
        var restartindex = list.size

        for(i in list.indices){
            if(list[i] is Char && i != list.lastIndex && i < restartindex){
                val operator = list[i]
                val prevDigit = list[i-1] as Float
                val nextDigit = list[i+1] as Float

                when(operator){
                    '×' ->{
                        newList.add(prevDigit * nextDigit)
                        restartindex = i + 1
                    }
                    '÷' ->{
                        newList.add(prevDigit / nextDigit)
                        restartindex = i + 1
                    }
                    else ->{
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if( i > restartindex){
                newList.add(list[i])
            }
        }


        return newList
    }

    fun operatorAction(view: View) {
        val inputText = findViewById<TextView>(R.id.workingText)

        if(view is Button && canAddOperation){
            inputText.append(view.text)
            canAddOperation = false
            canAddDecimal = true

        }
    }

    fun numberAction(view: View) {
        val inputText = findViewById<TextView>(R.id.workingText)

        if(view is Button){
            if(view.text == "."){
                if(canAddDecimal){
                    inputText.append(view.text)
                }
                canAddDecimal = false
            } else{
                inputText.append(view.text)
            }
            canAddOperation = true
        }
    }

    fun digitOperations():MutableList<Any>{
        val list = mutableListOf<Any>()
        var current = ""
        val inputText = findViewById<TextView>(R.id.workingText)


        for (char in inputText.text){
            if(char.isDigit() || char == '.'){
                current += char
            }else{
                list.add(current.toFloat())
                current = ""
                list.add(char)
            }
        }

        if(current != ""){
            list.add(current.toFloat())
        }


        return list
    }


}