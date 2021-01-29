package com.example.projekcik_1_kalkulator.basicFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projekcik_1_kalkulator.Calculator
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

open class BasicFragmentViewModel: ViewModel() {

    private var calculator = BasicCalculator()

    private var solutionOnScreen = false
    private var errorOnScreen = false
    private val _displayedString = MutableLiveData<String>()
    val displayedString : LiveData<String>
        get() = _displayedString

    init {
        _displayedString.value = ""
    }

    open fun clear() {
        solutionOnScreen = false
        errorOnScreen = false
        _displayedString.value = ""
        calculator.clear()
    }

    open fun back() {
        if (errorOnScreen) {
           clear()
        }
        else
            calculator.back()
        _displayedString.value = calculator.inputBuffer
    }

    open fun addSymbol(symbol : Char){
        if (errorOnScreen) {
            clear()
        }
        if (solutionOnScreen || (calculator.inputBuffer == "0" && symbol != '.')) {
            calculator.clearInputBuffer()
            solutionOnScreen = false
        }
        calculator.add(symbol)
        _displayedString.value = calculator.inputBuffer
    }

    open fun chooseOperator(operator : Char) {
        if (errorOnScreen) {
            clear()
        }
        try {
            _displayedString.value = calculator.chooseOperator(operator)
            solutionOnScreen = true
        } catch (e : Calculator.Error) {
            _displayedString.value = e.message
            errorOnScreen = true
        }
    }

    fun count(){
        if (!errorOnScreen) {
            try {
                calculator.count()
                _displayedString.value = calculator.inputBuffer
                solutionOnScreen = true
            } catch (e : Calculator.Error) {
                _displayedString.value = e.message
                errorOnScreen = true
            }
        }
    }

}