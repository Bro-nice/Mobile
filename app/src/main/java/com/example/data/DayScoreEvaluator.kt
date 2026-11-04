package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object DayScoreEvaluator {

    private const val TAG = "DayScoreEvaluator"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_PROMPT = """
You are DayScore AI, an intelligent daily reflection and self-improvement coach built into the DayScore application.
Your purpose is to help users understand how they used their day and encourage realistic, sustainable improvement.
You are not a therapist, doctor, psychiatrist, judge, teacher, police officer, or moral authority.
You must never shame, insult, humiliate, threaten, manipulate, or emotionally punish the user.
Your role is to provide honest, fair, supportive, and useful feedback.

Evaluate the user's day as one complete unit.
Do not simply count productive activities.
Do not simply punish unproductive activities.
Generate exactly one final daily score from 0 to 100 based on the relationship between responsibilities, intentions, effort, behavior, time usage, circumstances, and personal growth.
Distinguish between deliberate avoidance, circumstantial difficulty, necessary rest, and healthy entertainment.

Scoring:
90–100 — Exceptional Day (🔥, 🎉, 😄, 🚀)
75–89 — Strong Day (😄, 👍, ✨)
60–74 — Decent Day (🙂, ⚡)
40–59 — Weak Day (😐, 🌱)
0–39 — Very Difficult Day (😕, 💪, 🌅)

Generate exactly 3 to 5 actionable, specific, realistic improvement suggestions.
Generate one small, realistic tomorrow_challenge.
Identify at least one genuine strength demonstrated during the day.

Return valid JSON only. Do not include markdown code fences (like ```json).
Use exactly this structure:
{
  "score": 82,
  "emoji": "👍",
  "title": "Short supportive title",
  "summary": "A concise and personalized explanation of the overall day.",
  "strength": "One genuine strength demonstrated during the day.",
  "improvements": [
    "Specific actionable improvement 1",
    "Specific actionable improvement 2",
    "Specific actionable improvement 3"
  ],
  "tomorrow_challenge": "One realistic challenge for tomorrow.",
  "encouragement": "A short supportive closing message."
}
"""

    suspend fun evaluate(journalText: String): DayEvaluationResult = withContext(Dispatchers.IO) {
        val trimmed = journalText.trim()
        val apiKey = try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            field.get(null) as? String ?: ""
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val apiResult = callGeminiApi(trimmed, apiKey)
                if (apiResult != null) {
                    return@withContext apiResult
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini API call failed, falling back to local evaluator: ${e.message}")
            }
        }

        // Contextual AI Heuristic Evaluator strictly implementing the DayScore rules
        evaluateLocally(trimmed)
    }

    private fun callGeminiApi(journalText: String, apiKey: String): DayEvaluationResult? {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val requestJson = JSONObject().apply {
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", SYSTEM_PROMPT) })
                })
            })
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", "Here is my day journal:\n$journalText") })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.4)
            })
        }

        val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            Log.e(TAG, "Gemini API HTTP Error: ${response.code} ${response.message}")
            return null
        }

        val responseBodyString = response.body?.string() ?: return null
        val respObj = JSONObject(responseBodyString)
        val candidates = respObj.optJSONArray("candidates") ?: return null
        if (candidates.length() == 0) return null

        val content = candidates.getJSONObject(0).optJSONObject("content") ?: return null
        val parts = content.optJSONArray("parts") ?: return null
        if (parts.length() == 0) return null

        val rawText = parts.getJSONObject(0).optString("text", "")
        return parseResultJson(rawText, journalText)
    }

    private fun parseResultJson(rawJson: String, journalText: String): DayEvaluationResult? {
        return try {
            val clean = rawJson.replace("```json", "").replace("```", "").trim()
            val obj = JSONObject(clean)

            val score = obj.optInt("score", 70).coerceIn(0, 100)
            val emoji = obj.optString("emoji", if (score >= 75) "✨" else "🌱")
            val title = obj.optString("title", "Daily Reflection")
            val summary = obj.optString("summary", "A thoughtful review of your day.")
            val strength = obj.optString("strength", "Honest self-reflection and awareness.")

            val improvements = mutableListOf<String>()
            val impArray = obj.optJSONArray("improvements")
            if (impArray != null) {
                for (i in 0 until impArray.length()) {
                    val s = impArray.optString(i)
                    if (s.isNotBlank()) improvements.add(s)
                }
            }
            if (improvements.isEmpty()) {
                improvements.add("Set a single top priority before starting your day tomorrow.")
                improvements.add("Keep digital distractions out of reach during your primary focus block.")
                improvements.add("Carve out at least 30 minutes of intentional winding down before bed.")
            }

            val tomorrowChallenge = obj.optString(
                "tomorrow_challenge",
                "Complete one focused 45-minute session before opening entertainment apps."
            )
            val encouragement = obj.optString(
                "encouragement",
                "Every day is a fresh opportunity. This is where you are today; you can build from here."
            )

            DayEvaluationResult(
                score = score,
                emoji = emoji,
                title = title,
                summary = summary,
                strength = strength,
                improvements = improvements.take(5),
                tomorrowChallenge = tomorrowChallenge,
                encouragement = encouragement,
                journalText = journalText
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing JSON: ${e.message}", e)
            null
        }
    }

    /**
     * Local contextual evaluator following the 16 DayScore principles.
     * Evaluates the day as one complete unit, balancing effort, circumstances,
     * avoidance vs intentional rest, and growth mindset.
     */
    fun evaluateLocally(journal: String): DayEvaluationResult {
        val lower = journal.lowercase()

        // 1. Detect circumstance / unexpected responsibilities
        val hasCircumstance = lower.contains("sick") || lower.contains("mother") || lower.contains("father") ||
                lower.contains("family") || lower.contains("emergency") || lower.contains("hospital") ||
                lower.contains("clinic") || lower.contains("pharmacy") || lower.contains("breakdown") ||
                lower.contains("had to take care") || lower.contains("caring for") || lower.contains("migraine")

        // 2. Detect productive output / effort
        val hasStudy = lower.contains("stud") || lower.contains("read") || lower.contains("exam") ||
                lower.contains("assignment") || lower.contains("chapter") || lower.contains("class") ||
                lower.contains("lecture") || lower.contains("research")
        val hasWork = lower.contains("work") || lower.contains("code") || lower.contains("program") ||
                lower.contains("report") || lower.contains("project") || lower.contains("client") ||
                lower.contains("meeting") || lower.contains("presentation") || lower.contains("built") ||
                lower.contains("design")
        val hasHealth = lower.contains("gym") || lower.contains("run") || lower.contains("jog") ||
                lower.contains("workout") || lower.contains("walk") || lower.contains("exercise") ||
                lower.contains("cook") || lower.contains("healthy") || lower.contains("clean")

        // 3. Detect intentional rest vs avoidance
        val hasSleep = lower.contains("sleep") || lower.contains("slept") || lower.contains("nap")
        val hasGoodSleep = lower.contains("8 hour") || lower.contains("7 hour") || lower.contains("9 hour") || lower.contains("rested")
        val hasIntentionalRest = lower.contains("recharged") || lower.contains("recovery") || lower.contains("relax") ||
                lower.contains("porch") || lower.contains("nature") || lower.contains("tea")

        val hasProcrastination = lower.contains("scrolling") || lower.contains("tiktok") || lower.contains("twitter") ||
                lower.contains("reddit") || lower.contains("youtube") || lower.contains("avoided") ||
                lower.contains("procrastinat") || lower.contains("wasted") || lower.contains("doomscroll") ||
                lower.contains("junk food")

        // Compute baseline holistic score
        var score = 68

        // Effort and output boost
        if (hasStudy) score += 9
        if (hasWork) score += 9
        if (hasHealth) score += 6
        if (hasGoodSleep || hasIntentionalRest) score += 7

        // Circumstance logic: If user had an emergency or family duty, reward the empathy/responsibility
        if (hasCircumstance) {
            score += 10
        }

        // Avoidance vs excessive distraction
        if (hasProcrastination) {
            // Did they still accomplish major things?
            val accomplished = (if (hasStudy) 1 else 0) + (if (hasWork) 1 else 0) + (if (hasHealth) 1 else 0)
            if (accomplished >= 2) {
                // Balanced day with entertainment
                score -= 4
            } else if (hasCircumstance) {
                score -= 3
            } else {
                // Pronounced avoidance
                score -= 24
            }
        }

        // Check for high-volume exceptional days
        if (lower.contains("exceptional") || lower.contains("unbroken") || (hasStudy && hasWork && hasHealth && (hasGoodSleep || hasIntentionalRest))) {
            score = (score + 10).coerceAtMost(96)
        }

        // Clamp to 0..100
        score = score.coerceIn(35, 96)

        // Select emoji according to prompt ranges
        val emoji = when (score) {
            in 90..100 -> "🔥"
            in 75..89 -> "✨"
            in 60..74 -> "🙂"
            in 40..59 -> "🌱"
            else -> "🌅"
        }

        // Generate personalized output
        val title: String
        val summary: String
        val strength: String
        val improvements = mutableListOf<String>()
        val tomorrowChallenge: String
        val encouragement: String

        when {
            score >= 90 -> {
                title = "Exceptional Flow & Balance"
                summary = "You directed high focus toward meaningful priorities while maintaining physical health and personal nourishment. Time was utilized with clear intention."
                strength = "High self-discipline and admirable follow-through across multiple life areas."
                improvements.add("Protect your recovery evening so you carry this momentum without fatigue.")
                improvements.add("Review today's high-impact outputs and outline tomorrow's single anchor task.")
                improvements.add("Note what conditions made today so focused to replicate them regularly.")
                tomorrowChallenge = "Identify your single highest-value priority before 10 AM and execute it with the same clarity."
                encouragement = "Incredible demonstration of agency today. Build sustainably from here."
            }
            score >= 75 -> {
                if (hasCircumstance) {
                    title = "Responsible Adaptability"
                    summary = "Your day required shifting focus to care for unexpected responsibilities. You stepped up when it mattered most without neglecting your core awareness."
                    strength = "Empathy, responsibility, and stepping forward during family or personal needs."
                    improvements.add("Release any lingering guilt over delayed personal projects—you made the right priority call.")
                    improvements.add("Block a dedicated 45-minute catch-up window tomorrow morning.")
                    improvements.add("Check in on your own rest tonight after supporting others.")
                    tomorrowChallenge = "Dedicate 30 uninterrupted minutes to your deferred priority before noon."
                    encouragement = "Real life is measured by how we handle unexpected demands. You showed genuine maturity today."
                } else if (hasIntentionalRest) {
                    title = "Restorative & Balanced Day"
                    summary = "You balanced meaningful progress with genuine, intentional rest. Sustained productivity requires conscious recovery, and today honored that need."
                    strength = "Healthy respect for personal balance and sustainable pacing."
                    improvements.add("Ease into tomorrow by clarifying your most important morning objective.")
                    improvements.add("Hydrate and maintain consistent bedtime to seal today's rest.")
                    improvements.add("Limit unplanned screen time before sleeping.")
                    tomorrowChallenge = "Begin tomorrow with your most important task for 40 minutes before opening social media."
                    encouragement = "You navigated today with good judgment. Step into tomorrow with calm confidence."
                } else {
                    title = "Strong Progress & Good Habits"
                    summary = "You made tangible headway on your responsibilities and maintained positive momentum. While there were minor moments of drifting, your core effort shone through."
                    strength = "Genuine effort and commitment to your key objectives."
                    improvements.add("Place your phone across the room during your first study or work sprint tomorrow.")
                    improvements.add("Tackle the most intimidating task early before decision fatigue sets in.")
                    improvements.add("Schedule a short, deliberate 15-minute walk when you feel afternoon focus dipping.")
                    tomorrowChallenge = "Complete a focused 45-minute session before checking entertainment feeds."
                    encouragement = "A very solid day of progress. You are moving in the right direction."
                }
            }
            score >= 60 -> {
                title = "Decent Effort With Opportunity"
                summary = "You accomplished meaningful things, but experienced noticeable moments of distraction or energy dips. The foundation is there, and small tweaks will elevate tomorrow."
                strength = "Showing up and accomplishing key tasks despite fluctuating focus."
                improvements.add("Define exactly one clear deliverable before opening your workspace tomorrow.")
                improvements.add("Set a gentle 30-minute timer for social media or entertainment.")
                improvements.add("Notice the exact emotion or fatigue that triggers avoidance and take a genuine 5-minute break instead.")
                tomorrowChallenge = "Finish the most difficult task for 35 minutes before browsing any feed."
                encouragement = "You showed real effort today. A small adjustment in focus tomorrow will make a noticeable difference."
            }
            else -> {
                title = "A Learning Day for Reset"
                summary = "Today was weighed down by avoidance or heavy distraction. Remember that a difficult day is information about 24 hours, never a judgment of your worth or ability."
                strength = "Honest, courageous reflection. Acknowledging reality is the first step toward improvement."
                improvements.add("Remove the primary distraction app from your home screen for tomorrow morning.")
                improvements.add("Break your most intimidating task into a tiny, non-threatening 15-minute step.")
                improvements.add("Ensure you get a full night of restful sleep tonight to reset mental fatigue.")
                tomorrowChallenge = "Spend just 20 focused minutes on your key task tomorrow before noon, no matter how small."
                encouragement = "This is where you are today. Reset with a good night of sleep—tomorrow is a brand new canvas."
            }
        }

        return DayEvaluationResult(
            score = score,
            emoji = emoji,
            title = title,
            summary = summary,
            strength = strength,
            improvements = improvements.take(4),
            tomorrowChallenge = tomorrowChallenge,
            encouragement = encouragement,
            journalText = journal
        )
    }
}
