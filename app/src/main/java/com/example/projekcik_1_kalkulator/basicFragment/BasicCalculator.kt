package com.example.projekcik_1_kalkulator.basicFragment

import com.example.projekcik_1_kalkulator.Calculator
import java.lang.Exception

class BasicCalculator : Calculator() {
    private var firstNumber = 0.0
    private var operator = '0'

    override fun clear() {
        firstNumber = 0.0
        operator = '0'
        _inputBuffer = ""
    }

    //returns string to display (not always inputbuffer)
    fun chooseOperator(op : Char) : String {
        //"classic" usage (for example: 2 + 2 =)
        if (firstNumber == 0.0) {
            setOperator(op)
            clearInputBuffer()
            return inputBuffer
        }
        //chaining operations (for example: 2 + 2 + 2 + 2 =)
        else
        {
            count()
            setOperator(op)
            var str = inputBuffer
            clearInputBuffer()
            return str
        }
    }

    //extracts the first operand from the EditText and sets the operator
    private fun setOperator(operator : Char){
        if (_inputBuffer != "" && _inputBuffer != ".") {
            firstNumber = _inputBuffer.toDouble()
            if ("+-/*".contains(operator))
                this.operator = operator
            else
                throw IllegalArgumentException("Bad char in chooseOperator()")
        }
    }

    //do one simple count to get the solution
    fun count() {
        if (_inputBuffer != "" && operator != '0') {
            var solution = formatNumber( simpleCount(firstNumber, _inputBuffer.toDouble(), operator))
            clear()
            _inputBuffer = solution
        }
    }
}