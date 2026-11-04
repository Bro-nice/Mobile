package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.DayEvaluationResult

object DayScoreIntents {

    const val DAYSCORE_URL = "https://dayscore.ai.studio"

    fun openWebsite(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DAYSCORE_URL))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening $DAYSCORE_URL", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareResult(context: Context, result: DayEvaluationResult) {
        val shareText = """
🌟 DayScore AI Daily Reflection
Score: ${result.score}/100 ${result.emoji}
"${result.title}"

Strength: ${result.strength}

🎯 Tomorrow's Micro-Challenge:
${result.tomorrowChallenge}

Reflect on your day without shame or guilt at:
$DAYSCORE_URL
""".trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share DayScore Reflection")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun shareApp(context: Context) {
        val shareText = """
Discover DayScore AI — daily reflection and self-improvement coaching that never shames, respects rest, and gives you a single realistic challenge for tomorrow.

Check it out at: $DAYSCORE_URL
""".trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Invite friends to DayScore")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
