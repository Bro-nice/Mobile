package com.example.data

data class DayEvaluationResult(
    val id: Long = System.currentTimeMillis(),
    val score: Int,
    val emoji: String,
    val title: String,
    val summary: String,
    val strength: String,
    val improvements: List<String>,
    val tomorrowChallenge: String,
    val encouragement: String,
    val timestamp: Long = System.currentTimeMillis(),
    val journalText: String = ""
)

data class EvaluationDimension(
    val name: String,
    val subtitle: String,
    val question: String,
    val description: String,
    val keySignal: String
)

data class SampleScenario(
    val id: String,
    val title: String,
    val badge: String,
    val prompt: String,
    val scoreEstimate: String
)

data class TrackerComparison(
    val title: String,
    val traditionalWay: String,
    val dayScoreWay: String,
    val iconName: String
)

object DayScoreDataDefaults {
    val dimensions = listOf(
        EvaluationDimension(
            name = "Productivity",
            subtitle = "Meaningful Progress",
            question = "Did you make meaningful progress toward important responsibilities or goals?",
            description = "DayScore doesn't just count tasks crossed off. It looks at whether your work addressed real priorities instead of busywork.",
            keySignal = "Priorities addressed over quantity of checkboxes"
        ),
        EvaluationDimension(
            name = "Consistency",
            subtitle = "Following Through",
            question = "Did you follow through on intended actions and commitments?",
            description = "Consistency isn't robotic perfection. It's honoring the commitments you made to yourself and others given today's realities.",
            keySignal = "Alignment between morning intent and evening reality"
        ),
        EvaluationDimension(
            name = "Effort",
            subtitle = "Genuine Output",
            question = "How much genuine effort did you demonstrate relative to your circumstances?",
            description = "Pushing through a hard day with low energy counts for immense effort, even if the absolute output was modest.",
            keySignal = "Energy exerted relative to physical and mental resistance"
        ),
        EvaluationDimension(
            name = "Discipline",
            subtitle = "Impulse Management",
            question = "Did you manage distractions and impulses reasonably well?",
            description = "Resisting the urge to abandon obligations for instant gratification. Distinguishing between genuine fatigue and avoidance.",
            keySignal = "Conscious decisions to stay the course"
        ),
        EvaluationDimension(
            name = "Time Usage",
            subtitle = "Intentional Hours",
            question = "Was time generally used intentionally rather than passively lost?",
            description = "Intentional rest is productive. Unconscious 4-hour doomscrolling is lost time. DayScore knows the difference.",
            keySignal = "Conscious agency over how hours unfolded"
        ),
        EvaluationDimension(
            name = "Growth",
            subtitle = "Learning & Expansion",
            question = "Did you learn, improve, reflect, or push your boundaries today?",
            description = "Did you gain a new insight, practice a skill, or expand your understanding of your craft or yourself?",
            keySignal = "Knowledge acquired, skill practiced, reflection made"
        ),
        EvaluationDimension(
            name = "Healthy Balance",
            subtitle = "Sustained Wellbeing",
            question = "Did the day include appropriate sleep, rest, activity, and connection?",
            description = "Studying 14 hours while skipping meals and sleep leads to burnout. DayScore rewards sustainable living.",
            keySignal = "Rest, nourishment, sleep, and physical maintenance"
        ),
        EvaluationDimension(
            name = "Growth Mindset",
            subtitle = "Grace & Recovery",
            question = "Did you acknowledge mistakes, learn from them, or attempt to recover?",
            description = "When things went sideways, did you spiral into self-loathing or take a breath and try to salvage the remaining hours?",
            keySignal = "Pivoting after setbacks without self-punishment"
        )
    )

    val sampleScenarios = listOf(
        SampleScenario(
            id = "balanced_student",
            title = "The Balanced Student",
            badge = "Strong Day (75-89)",
            prompt = "Studied 4 hours for chemistry exam tomorrow. Went for a 30-minute jog, helped make dinner with my family, slept 8 hours last night, and relaxed with 2 hours of YouTube in the evening.",
            scoreEstimate = "82/100"
        ),
        SampleScenario(
            id = "circumstance_care",
            title = "Family Care Emergency",
            badge = "Circumstance Protected",
            prompt = "Planned to finish 3 work reports today, but my mother came down with a high fever. Spent 6 hours caring for her, driving to the clinic and pharmacy. Only managed 1 hour of emails and felt guilty, but did what I had to do.",
            scoreEstimate = "76/100"
        ),
        SampleScenario(
            id = "deep_work_flow",
            title = "Deep Focus Flow",
            badge = "Exceptional Day (90-100)",
            prompt = "Woke up at 6:30am. Tackled high-priority architecture document for 4 unbroken hours. Ran 5k at noon, mentored an intern for 45 mins, cooked healthy dinner, and read 25 pages of my book before bed.",
            scoreEstimate = "94/100"
        ),
        SampleScenario(
            id = "procrastination_struggle",
            title = "Procrastination Spiral",
            badge = "Weak Day (40-59)",
            prompt = "Had an important project due in two days. Sat at my desk all afternoon but kept checking TikTok and scrolling Twitter. Avoided the hardest slide deck, ordered fast food at midnight, and feel behind.",
            scoreEstimate = "46/100"
        ),
        SampleScenario(
            id = "recovery_sunday",
            title = "Intentional Rest Sunday",
            badge = "Healthy Balance",
            prompt = "Exhausted after an intense 70-hour sprint week. Slept 9 hours, did laundry, had coffee on the porch, took a long walk in the woods with a friend. Intentionally did zero work to prevent burnout.",
            scoreEstimate = "84/100"
        )
    )

    val trackerComparisons = listOf(
        TrackerComparison(
            title = "When You Take Care of a Sick Family Member",
            traditionalWay = "❌ Streak broken. Task uncompleted. 0% progress recorded.",
            dayScoreWay = "✨ Acknowledges real-life responsibility, empathy, and priority balance without penalty.",
            iconName = "family"
        ),
        TrackerComparison(
            title = "When You Grind for 14 Hours With No Sleep",
            traditionalWay = "🔥 100% completed! Gold badge awarded! Encourages dangerous burnout.",
            dayScoreWay = "⚠️ Identifies unhealthy imbalance, recommends recovery sleep and manageable pacing.",
            iconName = "balance"
        ),
        TrackerComparison(
            title = "When You Have a Day Dominated by Distraction",
            traditionalWay = "📉 Shame-inducing red charts, guilt notifications, abandoned apps.",
            dayScoreWay = "🌱 Honest diagnosis of the distraction trigger, plus 1 achievable micro-challenge for tomorrow.",
            iconName = "growth"
        )
    )
}
