package com.example.projekcik_1_kalkulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

abstract class Calculator {
    class Error(msg : String) : RuntimeException(msg) {}

    protected var _inputBuffer = ""
    val inputBuffer : String
        get() = _inputBuffer

    abstract fun clear()
    fun clearInputBuffer() {
        _inputBuffer = ""
    }

    open fun back() {
        if (_inputBuffer != "")
            _inputBuffer = _inputBuffer.dropLast(1)
    }

    open fun add(symbol : Char){
        //avoid multiple zeros at the beginning eg. 0008.3
        if (!(_inputBuffer == "0" && symbol == '0'))
        //preventing multiple dots
            if (!_inputBuffer.contains('.') || symbol != '.') {
                _inputBuffer += symbol
            }
    }

    //Takes two arguments and an operator and returns a value - the most basic form of calculation
    protected fun simpleCount(oldNum: Double, newNum : Double, operator: Char): Double {
        return when (operator) {
            '+' -> (oldNum.plus(newNum))
            '-' -> (oldNum.minus(newNum))
            '*' -> (oldNum.times(newNum))
            '/' -> {
                if (newNum!=0.0)
                    (oldNum.div(newNum))
                else {
                    throw Error("ERROR: DIVIDE BY 0")
                }
            }
            '^' -> (oldNum.pow(newNum))
            's' -> {
                if (oldNum>=0.0)
                    sqrt(oldNum)
                else {
                    throw Error("ERROR: NEGATIVE SQRT")
                }
            }
            'l' -> {
                if (oldNum>0.0)
                    log10(oldNum)
                else {
                    throw Error("ERROR: NON-POSITIVE LOG")
                }
            }
            else -> throw IllegalArgumentException("Bad operator in simpleCount()")
        }
    }

    //Formats a number to display
    protected fun formatNumber(number : Double) : String {
        var formattedNum = BigDecimal(number).setScale(10, RoundingMode.HALF_UP).toPlainString()
        var numDrops = 0
        for (i in formattedNum.length-1 downTo 0 step 1)
        {
            if (formattedNum[i]!='0')
                break
            else
                numDrops++
        }
        formattedNum = formattedNum.dropLast(numDrops)
        if (formattedNum[formattedNum.length-1] == '.')
            formattedNum = formattedNum.dropLast(1)

        return formattedNum
    }
}