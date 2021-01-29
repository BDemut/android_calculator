package com.example.projekcik_1_kalkulator.scientificFragment

import androidx.lifecycle.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projekcik_1_kalkulator.Calculator
import com.example.projekcik_1_kalkulator.basicFragment.BasicCalculator
import com.example.projekcik_1_kalkulator.basicFragment.BasicFragmentViewModel


class ScientificFragmentViewModel : ViewModel() {

    private var calculator = ScientificCalculator()

    private var solutionOnScreen = false
    private var errorOnScreen = false
    private val _displayedString = MutableLiveData<String>()
    val displayedString: LiveData<String>
        get() = _displayedString
    private val _displayedExpressionString = MutableLiveData<String>()
    val displayedExpressionString: LiveData<String>
        get() = _displayedExpressionString

    init {
        _displayedString.value = ""
        _displayedExpressionString.value = ""
    }

    private fun refreshUI() {
        _displayedString.value = calculator.inputBuffer
        _displayedExpressionString.value = calculator.expressionBuffer
    }

    fun clear() {
        errorOnScreen = false
        solutionOnScreen = false
        calculator.clear()

        refreshUI()
    }

    fun back() {
        if (errorOnScreen)
            clear()
        else
            calculator.back()

        refreshUI()
    }

    fun chooseOperator(operator: Char) {
        if (errorOnScreen) {
            clear()
            errorOnScreen = false
        }
        if (solutionOnScreen) {
            calculator.clearExpressionBuffer()
            solutionOnScreen = false
        }

        calculator.chooseOperator(operator)

        refreshUI()
    }

    fun choosefunction(function: String) {
        if (errorOnScreen) {
            clear()
            errorOnScreen = false
        }
        if (solutionOnScreen) {
            calculator.clearExpressionBuffer()
            solutionOnScreen = false
        }

        calculator.choosefunction(function)

        refreshUI()
    }

    //initiate counting the expression
    fun countAll() {
        try {
            if (!errorOnScreen)
                calculator.countAll()

            refreshUI()
            solutionOnScreen = true
        } catch (e : Calculator.Error) {
            _displayedString.value = e.message
            _displayedExpressionString.value = ""
            errorOnScreen = true
        }
    }

    fun addSymbol(symbol: Char) {
        if (errorOnScreen) {
            clear()
        }
        if (solutionOnScreen || (calculator.inputBuffer == "0" && symbol != '.')) {
            calculator.clearInputBuffer()
            solutionOnScreen = false
        }

        calculator.add(symbol)

        refreshUI()
    }
}