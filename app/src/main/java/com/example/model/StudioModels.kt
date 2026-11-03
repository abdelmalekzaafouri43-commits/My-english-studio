package com.example.model

enum class SchoolLevel(val displayName: String) {
    ALL("All Levels"),
    PRIMARY("Primary (K-5)"),
    MIDDLE("Middle School (6-8)"),
    HIGH("High School (9-12)"),
    ADULT("Adult & Business")
}

enum class WorksheetCategory(val displayName: String) {
    GRAMMAR("Grammar & Tenses"),
    VOCABULARY("Vocabulary"),
    READING("Reading & Comprehension"),
    PHONICS("Phonics & Spelling"),
    SPEAKING("Speaking & Roleplay")
}

data class Worksheet(
    val id: String,
    val title: String,
    val category: WorksheetCategory,
    val level: SchoolLevel,
    val fileType: String, // "PDF" or "IMAGE" / "PHOTO"
    val pageCount: Int,
    val description: String,
    val tags: List<String>,
    val dateAdded: String,
    val thumbnailColorHex: Long = 0xFF6366F1,
    val fileName: String = "",
    val fileUriString: String? = null,
    val customTopic: String = "",
    val isUserUploaded: Boolean = false
)

data class LessonStep(
    val phase: String, // e.g. "Warm-up (5m)", "Presentation (10m)", "Practice (15m)", "Production (10m)"
    val instructions: String
)

data class Lesson(
    val id: String,
    val title: String,
    val level: SchoolLevel,
    val topic: String,
    val durationMinutes: Int,
    val objectives: List<String>,
    val steps: List<LessonStep>,
    val attachedWorksheetIds: List<String> = emptyList(),
    val attachedScriptIds: List<String> = emptyList(),
    val targetGrammar: String = ""
)

enum class ScriptRole {
    TEACHER,
    STUDENT,
    NARRATOR,
    ROBOT
}

data class ScriptLine(
    val id: String,
    val characterName: String,
    val role: ScriptRole,
    val text: String,
    val phoneticNote: String = "",
    val teacherTip: String = ""
)

data class TeachingScript(
    val id: String,
    val title: String,
    val topic: String,
    val level: SchoolLevel,
    val description: String,
    val lines: List<ScriptLine>,
    val relatedWorksheetId: String? = null
)

enum class CharacterRole(val displayName: String) {
    TEACHER("Lead Teacher"),
    STUDENT_BEGINNER("Beginner Student"),
    STUDENT_INTERMEDIATE("Intermediate Student"),
    STUDENT_ADVANCED("Advanced Student"),
    AI_ASSISTANT("AI Companion")
}

data class CharacterModel(
    val id: String,
    val name: String,
    val role: CharacterRole,
    val ageGroup: String,
    val voiceTone: String,
    val catchphrase: String,
    val personalityTraits: List<String>,
    val avatarColorHex: Long,
    val avatarEmoji: String
)

enum class VideoStatus(val displayName: String) {
    PLANNING("Planning"),
    SCRIPTED("Scripted"),
    VOICEOVER("Voiceover Ready"),
    SUBTITLED("Subtitled"),
    FINISHED("Finished MP4")
}

data class SubtitleLine(
    val timestampMs: Long,
    val durationMs: Long,
    val speaker: String,
    val text: String
)

data class VideoProject(
    val id: String,
    val title: String,
    val targetLevel: SchoolLevel,
    val status: VideoStatus,
    val durationSeconds: Int,
    val scriptTitle: String,
    val voiceoverVoice: String,
    val subtitles: List<SubtitleLine>,
    val videoResolution: String = "1080p Full HD",
    val thumbnailColorHex: Long = 0xFF8B5CF6
)
