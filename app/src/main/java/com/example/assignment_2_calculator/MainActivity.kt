package com.example.assignment_2_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_2_calculator.adapter.ItemAdapter
import com.example.assignment_2_calculator.data.Datasource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var useOperation = false
    private var useDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myDataset = Datasource().loadAppName()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, myDataset)

        recyclerView.setHasFixedSize(true)

    }

    fun numberAction(view: View){

        if(view is Button){

            if(view.text == "."){
                if(useDecimal){
                    workingsTV.append(view.text)
                    useDecimal = false

                }
            }else {
                workingsTV.append(view.text)
                useOperation = true
            }

        }
    }

    fun operationAction(view: View) {
        if(view is Button && useOperation){
            workingsTV.append(view.text)
            useOperation = false
            useDecimal = true
        }
    }

    fun allClearAction(view: View)
    {
        workingsTV.text=""
        resultsTV.text=""
    }

    fun backSpaceAction(view: View) {
        val length = workingsTV.length()
        if(length > 0){
            workingsTV.text = workingsTV.text.subSequence(0, length-1)
        }
    }

    fun equalsAction(view: View) {

        resultsTV.text = calculateResults()

    }

    private fun calculateResults(): String{
        val dOperator = digitsOperators()
        if(dOperator.isEmpty()){
            return ""
        }

        val multiplyDivide = multiplyDivideMath(digitsOperators())

        if(multiplyDivide.isEmpty()){
            return ""
        }

        val result = addSubtractMath(digitsOperators())

        return result.toString()
    }

    private fun addSubtractMath(passedList: MutableList<Any>): Float {

        var result = passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if(operator == "+"){
                    result += nextDigit
                }
                if(operator == "-"){
                    result -= nextDigit
                }

            }
        }

        return result
    }

    private fun multiplyDivideMath(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('*') || list.contains('/')){

            list = calcTimesDiv(list)

        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
            val newList = mutableListOf<Any>()
            var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator){

                    "*" ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }

                    "/" ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else ->{
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }
            }

            if(i > restartIndex){
                newList.add(passedList[i])
            }

        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit =""
        for(character in workingsTV.text){
            if(character.isDigit() || character == '.'){
                currentDigit += character
            }else{
                list.add(currentDigit.toFloat())
                currentDigit=""
                list.add(character)
            }
        }

        if(currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return list

    }
}