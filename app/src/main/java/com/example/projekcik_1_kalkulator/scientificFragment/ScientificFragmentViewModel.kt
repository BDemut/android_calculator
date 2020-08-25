package com.example.projekcik_1_kalkulator.scientificFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projekcik_1_kalkulator.basicFragment.BasicFragmentViewModel

class ScientificFragmentViewModel : BasicFragmentViewModel() {

    private var brackets = 0
    private val _expressionString = MutableLiveData<String>()
    val expressionString : LiveData<String>
        get() = _expressionString

    init {
        _expressionString.value = ""
    }

    override fun clear() {
        super.clear()
        brackets = 0
        _expressionString.value = ""
    }

    override fun back(){
        if (errorMessage!="") {
            errorMessage=""
            _newNumberString.value=""
        }
        else if (_newNumberString.value != "") {
            if (_newNumberString.value!!.last()==')')
                brackets += 1
            else if (_newNumberString.value!!.last()=='(')
                brackets -= 1
            _newNumberString.value = _newNumberString.value!!.dropLast(1)
        }
        else if (_expressionString.value != ""){
            if (_expressionString.value!!.last()==')')
                brackets += 1
            else if (_expressionString.value!!.last()=='(')
                brackets -= 1
            _expressionString.value = _expressionString.value!!.dropLast(1)
        }
    }

    override fun chooseOperator(operator: Char) {
        if (errorMessage!="") {
            errorMessage=""
            _newNumberString.value=""
        }
        else if ((_newNumberString.value!!.contains("[0-9]".toRegex()))
            || (_expressionString.value!!.isNotEmpty() && !"+-*/^".contains(_expressionString.value!![_expressionString.value!!.length-1]))) {
                if (solutionOnScreen)
                    _expressionString.value = _newNumberString.value + operator
            else
                _expressionString.value = _expressionString.value + _newNumberString.value + operator
            _newNumberString.value = ""
            solutionOnScreen = false
        }
        else if (operator == '-') {
            if (_newNumberString.value!!.isEmpty()){
                addSymbol('(')
                addSymbol('-')
            }
            else if (_newNumberString.value == "(")
                addSymbol('-')
        }
    }

    fun choosefunction(function: String) {

        if (errorMessage!="") {
            errorMessage=""
            _newNumberString.value=""
        }
        if (_newNumberString.value!!.isNotEmpty() && _newNumberString.value!!.first()=='(')
            _newNumberString.value = function + _newNumberString.value
        else {
            _newNumberString.value = function + "(" + _newNumberString.value
            brackets += 1
        }
        if (solutionOnScreen) {
            _expressionString.value=""
            solutionOnScreen=false
        }
    }

    //initiate counting the expression
    fun countAll() {
        if (_newNumberString.value!!.isNotEmpty() && errorMessage=="") {

            //close all brackets
            while (brackets > 0)
                addSymbol(')')

            //replace '-' with '_' for negative numbers so to not confuse them with the '-' operator
            _expressionString.value = _expressionString.value!! + _newNumberString.value
            val regex = "\\(-[0-9]*".toRegex()
            val matchResults = regex.findAll(_expressionString.value!!)
            for (match in matchResults) {
                val replacement = match.value.replace('-', '_')
                _expressionString.value =
                    _expressionString.value!!.replace(match.value, replacement)
            }

            //evaluate expressions in brackets
            while (_expressionString.value!!.contains(')')) {
                val regex = "\\([^()]*\\)".toRegex()
                val matchResult = regex.find(_expressionString.value!!)
                val solution =
                    chainCount(matchResult!!.value.substring(1, matchResult!!.value.length - 1))
                _expressionString.value =
                    _expressionString.value!!.replace(matchResult.value, solution)
            }

            //finally evaluate the expression after there are no brackets left
            var solution = chainCount(_expressionString.value!!)
            if (solution[0] == '_') {
                solution = "(-" + solution.substring(1, solution.length) + ")"
            }
            if (errorMessage=="") {
                _expressionString.value = solution
                _newNumberString.value = _expressionString.value
                solutionOnScreen = true
            }
            else {
                _expressionString.value = ""
                _newNumberString.value = errorMessage
            }
        }
    }

    //called on a chain of operations (without brackets)
    //eg. 2+4^5-6*7+sqrt2
    private fun chainCount(string: String) : String {
        var i = 0
        var result = string

        //sqrt
        var regex = "sqrt_?[0-9.]*".toRegex()
        var matchResults = regex.findAll(result)
        for (match in matchResults) {
            val num = match.value.substring(4,match.value.length).replace('_','-')
            val numStr = simpleCount(num.toDouble(),0.0,'s')
            result = result.replace(match.value,numStr!!)
        }

        //log
        regex = "log_?[0-9.]*".toRegex()
        matchResults = regex.findAll(result)
        for (match in matchResults) {
            val num = match.value.substring(3,match.value.length).replace('_','-')
            val numStr = simpleCount(num.toDouble(),0.0,'l')
            result = result.replace(match.value,numStr!!)
        }

        result = useOperators(result,"^")
        result = useOperators(result,"*/")
        result = useOperators(result,"+-")
        return result
    }
    fun useOperators(equation:String,operators:String): String{
        var i = 0
        var result = equation
        while (i < result.length)
            if (operators.contains(result[i])){
                var l = i-1
                var r = i+1
                while (l>0 && !"+-/*^".contains(result[l-1]))
                    l -= 1
                while (r<result.length-1 && !"+-/*^".contains(result[r+1]))
                    r += 1

                var numL = result.substring(l,i)
                var numR = result.substring(i+1,r+1)
                numL = numL.replace('_','-')
                numR = numR.replace('_','-')

                var solution = simpleCount(numR.toDouble(),numL.toDouble(),result[i])!!.replace('-','_')

                if (l>0)
                    result = result.substring(0,l) + solution + result.substring(r+1, result.length)
                else
                    result = solution + result.substring(r+1, result.length)
                i = 0
            }
            else
                i += 1
        return result
    }

    override fun addSymbol(symbol: Char) {
        if (solutionOnScreen) {
            _expressionString.value = ""
            _newNumberString.value = ""
        }

        if (symbol == ')') {
            if (brackets != 0 && _newNumberString.value!="" && _newNumberString.value!!.last() != '(') {
                brackets -= 1
            }
            else
                return
        }
        else if (symbol == '(') {
            if (_newNumberString.value == "" || _newNumberString.value!!.last() == '(')
                brackets += 1
            else
                return
        }

        super.addSymbol(symbol)
    }
}