package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class DayScoreRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("dayscore_prefs", Context.MODE_PRIVATE)

    private val _history = MutableStateFlow<List<DayEvaluationResult>>(emptyList())
    val history: StateFlow<List<DayEvaluationResult>> = _history.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        val raw = prefs.getString(KEY_HISTORY, null)
        if (raw.isNullOrBlank()) {
            // Seed with one welcoming starter example reflection so new users immediately see the experience
            val initial = listOf(
                DayEvaluationResult(
                    id = 1L,
                    score = 82,
                    emoji = "✨",
                    title = "Balanced Output & Family Care",
                    summary = "You balanced serious academic focus with physical fitness and warm family time. Evening relaxation was intentional and well-earned.",
                    strength = "Demonstrated consistent follow-through and protected restorative sleep.",
                    improvements = listOf(
                        "Put phone in another room during the first morning study block.",
                        "Prepare tomorrow's study materials before going to sleep.",
                        "Take a 10-minute stretch break midway through your study sprint."
                    ),
                    tomorrowChallenge = "Complete a 45-minute focused study sprint before lunchtime.",
                    encouragement = "Strong momentum built today. Step into tomorrow with calm consistency.",
                    timestamp = System.currentTimeMillis() - 86400000L,
                    journalText = "Studied 4 hours for chemistry exam tomorrow. Went for a 30-minute jog, helped make dinner with my family, slept 8 hours last night, and relaxed with 2 hours of YouTube in the evening."
                )
            )
            _history.value = initial
            saveHistoryToPrefs(initial)
            return
        }

        try {
            val array = JSONArray(raw)
            val list = mutableListOf<DayEvaluationResult>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val improvements = mutableListOf<String>()
                val impArr = obj.optJSONArray("improvements")
                if (impArr != null) {
                    for (j in 0 until impArr.length()) {
                        improvements.add(impArr.optString(j))
                    }
                }
                list.add(
                    DayEvaluationResult(
                        id = obj.optLong("id", System.currentTimeMillis()),
                        score = obj.optInt("score", 75),
                        emoji = obj.optString("emoji", "✨"),
                        title = obj.optString("title", "Daily Reflection"),
                        summary = obj.optString("summary", ""),
                        strength = obj.optString("strength", ""),
                        improvements = improvements,
                        tomorrowChallenge = obj.optString("tomorrowChallenge", ""),
                        encouragement = obj.optString("encouragement", ""),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        journalText = obj.optString("journalText", "")
                    )
                )
            }
            _history.value = list
        } catch (e: Exception) {
            _history.value = emptyList()
        }
    }

    fun saveEvaluation(result: DayEvaluationResult) {
        val updated = listOf(result) + _history.value.filter { it.id != result.id }
        _history.value = updated
        saveHistoryToPrefs(updated)
    }

    fun deleteEvaluation(id: Long) {
        val updated = _history.value.filter { it.id != id }
        _history.value = updated
        saveHistoryToPrefs(updated)
    }

    private fun saveHistoryToPrefs(list: List<DayEvaluationResult>) {
        try {
            val array = JSONArray()
            for (item in list) {
                val obj = JSONObject().apply {
                    put("id", item.id)
                    put("score", item.score)
                    put("emoji", item.emoji)
                    put("title", item.title)
                    put("summary", item.summary)
                    put("strength", item.strength)
                    put("improvements", JSONArray(item.improvements))
                    put("tomorrowChallenge", item.tomorrowChallenge)
                    put("encouragement", item.encouragement)
                    put("timestamp", item.timestamp)
                    put("journalText", item.journalText)
                }
                array.put(obj)
            }
            prefs.edit().putString(KEY_HISTORY, array.toString()).apply()
        } catch (e: Exception) {
            // Log if needed
        }
    }

    companion object {
        private const val KEY_HISTORY = "dayscore_evaluations_history_v1"
    }
}
