package com.example.projekcik_1_kalkulator.scientificFragment

import com.example.projekcik_1_kalkulator.Calculator

class ScientificCalculator : Calculator() {

    private var brackets = 0
    private var _expressionBuffer = ""
    val expressionBuffer : String
        get() = _expressionBuffer

    override fun clear() {
        brackets = 0
        _expressionBuffer = ""
        _inputBuffer = ""
    }
    fun clearExpressionBuffer() {
        _expressionBuffer = ""
    }

    override fun back(){
        if (_inputBuffer != "") {
            if (_inputBuffer.last()==')')
                brackets += 1
            else if (_inputBuffer.last()=='(')
                brackets -= 1
            _inputBuffer = _inputBuffer.dropLast(1)
        }
        else if (_expressionBuffer != ""){
            if (_expressionBuffer.last()==')')
                brackets += 1
            else if (_expressionBuffer.last()=='(')
                brackets -= 1
            _expressionBuffer = _expressionBuffer.dropLast(1)
        }
    }

    override fun add(symbol: Char) {
        if (symbol == ')') {
            if (brackets != 0 && _inputBuffer!="" && _inputBuffer.last() != '(') {
                brackets -= 1
            }
            else
                return
        }
        else if (symbol == '(') {
            if (_inputBuffer == "" || _inputBuffer.last() == '(')
                brackets += 1
            else
                return
        }

        super.add(symbol)
    }

    fun chooseOperator(operator: Char) {
        if ((_inputBuffer.contains("[0-9]".toRegex()))
            || (_expressionBuffer.isNotEmpty() && !"+-*/^".contains(_expressionBuffer[_expressionBuffer.length-1]))) {
                _expressionBuffer = _expressionBuffer + _inputBuffer + operator
                _inputBuffer = ""
        }
        else if (operator == '-') {
            if (_inputBuffer.isEmpty()){
                add('(')
                add('-')
            }
            else if (_inputBuffer == "(")
                add('-')
        }
    }

    fun choosefunction(function: String) {
        if (_inputBuffer.isNotEmpty() && _inputBuffer.first()=='(')
            _inputBuffer = function + _inputBuffer
        else {
            _inputBuffer = function + "(" + _inputBuffer
            brackets += 1
        }
    }

    //initiate counting the expression
    fun countAll() {
        if (_inputBuffer.isNotEmpty()) {

            //close all brackets
            while (brackets > 0)
                add(')')

            //replace '-' with '_' for negative numbers so to not confuse them with the '-' operator
            _expressionBuffer += _inputBuffer
            val regex = "\\(-[0-9]*".toRegex()
            val matchResults = regex.findAll(_expressionBuffer)
            for (match in matchResults) {
                val replacement = match.value.replace('-', '_')
                _expressionBuffer =
                    _expressionBuffer.replace(match.value, replacement)
            }

            //evaluate expressions in brackets
            while (_expressionBuffer.contains(')')) {
                val regex = "\\([^()]*\\)".toRegex()
                val matchResult = regex.find(_expressionBuffer)
                val solution =
                    chainCount(matchResult!!.value.substring(1, matchResult.value.length - 1))
                _expressionBuffer =
                    _expressionBuffer.replace(matchResult.value, solution)
            }

            //finally evaluate the expression after there are no brackets left
            var solution = chainCount(_expressionBuffer)
            if (solution[0] == '_') {
                solution = "(-" + solution.substring(1, solution.length) + ")"
            }

            _inputBuffer = solution
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
            val numStr = formatNumber(simpleCount(num.toDouble(),0.0,'s'))
            result = result.replace(match.value, numStr)
        }

        //log
        regex = "log_?[0-9.]*".toRegex()
        matchResults = regex.findAll(result)
        for (match in matchResults) {
            val num = match.value.substring(3,match.value.length).replace('_','-')
            val numStr = formatNumber(simpleCount(num.toDouble(),0.0,'l'))
            result = result.replace(match.value, numStr)
        }

        result = useOperators(result,"^")
        result = useOperators(result,"*/")
        result = useOperators(result,"+-")
        return result
    }
    private fun useOperators(equation:String, operators:String): String{
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

                var solution = formatNumber(
                    simpleCount(numL.toDouble(),numR.toDouble(),result[i])!!
                ).replace('-','_')

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
}