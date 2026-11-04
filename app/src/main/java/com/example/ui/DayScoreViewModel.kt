package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DayEvaluationResult
import com.example.data.DayScoreDataDefaults
import com.example.data.DayScoreEvaluator
import com.example.data.DayScoreRepository
import com.example.data.SampleScenario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val label: String, val testTag: String) {
    SHOWCASE("Overview", "tab_showcase"),
    EVALUATE("AI Coach", "tab_evaluate"),
    HISTORY("Reflections", "tab_history"),
    DIMENSIONS("8 Criteria", "tab_dimensions")
}

class DayScoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DayScoreRepository(application)

    val history: StateFlow<List<DayEvaluationResult>> = repository.history

    private val _selectedTab = MutableStateFlow(AppTab.SHOWCASE)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _inputText = MutableStateFlow(DayScoreDataDefaults.sampleScenarios[0].prompt)
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentResult = MutableStateFlow<DayEvaluationResult?>(null)
    val currentResult: StateFlow<DayEvaluationResult?> = _currentResult.asStateFlow()

    private val _isCurrentSaved = MutableStateFlow(false)
    val isCurrentSaved: StateFlow<Boolean> = _isCurrentSaved.asStateFlow()

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun onInputTextChanged(text: String) {
        _inputText.value = text
    }

    fun loadScenario(scenario: SampleScenario) {
        _inputText.value = scenario.prompt
        _selectedTab.value = AppTab.EVALUATE
    }

    fun evaluateCurrentDay() {
        val text = _inputText.value.trim()
        if (text.isBlank() || _isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = DayScoreEvaluator.evaluate(text)
                _currentResult.value = result
                _isCurrentSaved.value = false
                _selectedTab.value = AppTab.EVALUATE
            } catch (e: Exception) {
                // Fallback to local
                val localResult = DayScoreEvaluator.evaluateLocally(text)
                _currentResult.value = localResult
                _isCurrentSaved.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveCurrentEvaluation() {
        val result = _currentResult.value ?: return
        repository.saveEvaluation(result)
        _isCurrentSaved.value = true
    }

    fun deleteEvaluation(id: Long) {
        repository.deleteEvaluation(id)
    }

    fun selectDayFromHistory(day: DayEvaluationResult) {
        _currentResult.value = day
        _isCurrentSaved.value = true
        _selectedTab.value = AppTab.EVALUATE
    }

    fun resetEvaluation() {
        _currentResult.value = null
        _isCurrentSaved.value = false
        _inputText.value = ""
    }
}
