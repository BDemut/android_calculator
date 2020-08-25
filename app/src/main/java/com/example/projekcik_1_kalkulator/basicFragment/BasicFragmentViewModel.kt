package com.example.projekcik_1_kalkulator.basicFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

open class BasicFragmentViewModel: ViewModel() {

    private var firstNumber = 0.0
    private var operator = '0'
    protected var solutionOnScreen = false
    protected var errorMessage = ""

    protected val _newNumberString = MutableLiveData<String>()
    val newNumberString : LiveData<String>
        get() = _newNumberString

    init{
        _newNumberString.value = ""
    }

    open fun clear() {
        firstNumber = 0.0
        operator = '0'
        solutionOnScreen = false
        _newNumberString.value = ""
        errorMessage=""
    }

    open fun back() {
        if (errorMessage!="") {
            errorMessage=""
            _newNumberString.value=""
        }
        if (_newNumberString.value != null && _newNumberString.value != "")
            _newNumberString.value = _newNumberString.value!!.dropLast(1)
    }

    open fun addSymbol(symbol : Char){
        if (errorMessage!="") {
            errorMessage=""
            _newNumberString.value=""
        }
        //avoid multiple zeros at the beginning eg. 0008.3
        if (!(_newNumberString.value == "0" && symbol == '0'))
        //symbol replaces the string
            if (solutionOnScreen || (_newNumberString.value == "0" && symbol != '.')) {
                _newNumberString.value = symbol.toString()
                solutionOnScreen = false
            }
            //symbol appends the string
            else
            //preventing multiple dots
                if (!_newNumberString.value!!.contains('.') || symbol != '.') {
                    _newNumberString.value = _newNumberString.value + symbol
                    solutionOnScreen = false
                }
    }

    open fun chooseOperator(operator : Char) {
        if (errorMessage!="") {
            errorMessage=""
            _newNumberString.value=""
        }
        //"classic" usage (for example: 2 + 2 =)
        else if (firstNumber == 0.0) {
            setOperator(operator)
            _newNumberString.value = ""
        }
        //chaining operations (for example: 2 + 2 + 2 + 2 =)
        else
        {
            count()
            if (errorMessage=="")
                setOperator(operator)
        }
    }

    //extracts the first operand from the EditText and sets the operator
    private fun setOperator(operator : Char){
        if (_newNumberString.value != "" &&
            _newNumberString.value != null &&
            _newNumberString.value != ".") {

            firstNumber = _newNumberString.value!!.toDouble()
            if ("+-/*".contains(operator))
                this.operator = operator
            else
                throw Exception("Bad char in chooseOperator()")
        }
    }

    fun count(){
        if (errorMessage=="" && _newNumberString.value != "" &&
            _newNumberString.value != null && operator != '0') {
            _newNumberString.value =
                simpleCount(_newNumberString.value!!.toDouble(), firstNumber, operator)
            firstNumber = 0.0
            this.operator = '0'

            if (errorMessage=="")
                solutionOnScreen = true
            else {
                _newNumberString.value=errorMessage
            }
        }

    }

    //calculates a basic operation
    fun simpleCount(newNum: Double, oldNum: Double, operator: Char):String? {
        var solution = when (operator) {
            '+' -> (oldNum.plus(newNum))
            '-' -> (oldNum.minus(newNum))
            '*' -> (oldNum.times(newNum))
            '/' -> {
                if (newNum!=0.0)
                    (oldNum.div(newNum))
                else {
                    errorMessage="ERROR: DIVIDE BY 0"
                    0.0
                }
            }
            '^' -> (oldNum.pow(newNum))
            's' -> {
                if (newNum>=0.0)
                    sqrt(newNum)
                else {
                    errorMessage="ERROR: NEGATIVE SQRT"
                    0.0
                }
            }
            'l' -> {
                if (newNum>0.0)
                    log10(newNum)
                else {
                    errorMessage="ERROR: NON-POSITIVE LOG"
                    0.0
                }
            }
            else -> throw Exception("Bad operator in simpleCount()")
        }

        var stringSolution = BigDecimal(solution).setScale(10, RoundingMode.HALF_UP).toPlainString()
        var numDrops = 0
        for (i in stringSolution.length-1 downTo 0 step 1)
        {
            if (stringSolution[i]!='0')
                break
            else
                numDrops++
        }
        stringSolution = stringSolution.dropLast(numDrops)
        if (stringSolution[stringSolution.length-1] == '.')
            stringSolution = stringSolution.dropLast(1)

        return stringSolution
    }

}