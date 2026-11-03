package com.example.data

import com.example.model.*

object SampleData {

    val sampleWorksheets = listOf(
        Worksheet(
            id = "ws_1",
            title = "Present Perfect vs. Past Simple Challenge",
            category = WorksheetCategory.GRAMMAR,
            level = SchoolLevel.MIDDLE,
            fileType = "PDF",
            pageCount = 3,
            description = "Complete exercises comparing 'have done' vs 'did' with timeline graphs and practice sentences.",
            tags = listOf("Grammar", "Tenses", "Middle School", "Fill-in-blanks"),
            dateAdded = "2026-10-28",
            thumbnailColorHex = 0xFF4F46E5
        ),
        Worksheet(
            id = "ws_2",
            title = "Food & Restaurant Dialogue Cards",
            category = WorksheetCategory.SPEAKING,
            level = SchoolLevel.PRIMARY,
            fileType = "IMAGE",
            pageCount = 2,
            description = "Cut-out cards featuring food items, ordering phrases ('I would like...', 'Could I get...'), and waiter responses.",
            tags = listOf("Roleplay", "Food", "Elementary", "Flashcards"),
            dateAdded = "2026-10-29",
            thumbnailColorHex = 0xFF10B981
        ),
        Worksheet(
            id = "ws_3",
            title = "Phonics Fun: Magic E Silent Rules",
            category = WorksheetCategory.PHONICS,
            level = SchoolLevel.PRIMARY,
            fileType = "PDF",
            pageCount = 4,
            description = "Illustration worksheets showing CVC to CVCe vowel shifts (mat -> mate, pin -> pine, tub -> tube).",
            tags = listOf("Phonics", "Vowels", "Early Reading"),
            dateAdded = "2026-10-30",
            thumbnailColorHex = 0xFFF59E0B
        ),
        Worksheet(
            id = "ws_4",
            title = "Business Email Etiquette & Useful Phrases",
            category = WorksheetCategory.READING,
            level = SchoolLevel.ADULT,
            fileType = "PDF",
            pageCount = 5,
            description = "Professional email templates, formal openings, requesting action, polite closing phrases, and error corrections.",
            tags = listOf("Business English", "Adults", "Writing", "Formal"),
            dateAdded = "2026-11-01",
            thumbnailColorHex = 0xFF06B6D4
        ),
        Worksheet(
            id = "ws_5",
            title = "Idioms in Action: Weather & Climate Expressions",
            category = WorksheetCategory.VOCABULARY,
            level = SchoolLevel.HIGH,
            fileType = "PDF",
            pageCount = 2,
            description = "Learn expressions like 'Under the weather', 'Rain on my parade', 'Break the ice', with context matching.",
            tags = listOf("Idioms", "High School", "Vocabulary"),
            dateAdded = "2026-11-02",
            thumbnailColorHex = 0xFFEC4899
        )
    )

    val sampleCharacters = listOf(
        CharacterModel(
            id = "char_1",
            name = "Teacher Emma",
            role = CharacterRole.TEACHER,
            ageGroup = "Adult",
            voiceTone = "Warm, Encouraging & Clear (RP Accent)",
            catchphrase = "Wonderful attempt! Let's practice that vowel sound one more time together!",
            personalityTraits = listOf("Patient", "Energetic", "Structured", "Warm"),
            avatarColorHex = 0xFF4F46E5,
            avatarEmoji = "👩‍🏫"
        ),
        CharacterModel(
            id = "char_2",
            name = "Student Leo",
            role = CharacterRole.STUDENT_BEGINNER,
            ageGroup = "8 years old",
            voiceTone = "Curious & Enthusiastic",
            catchphrase = "Teacher Emma! Is 'cat' a Magic E word too?",
            personalityTraits = listOf("Eager", "Playful", "Visual Learner"),
            avatarColorHex = 0xFF10B981,
            avatarEmoji = "👦"
        ),
        CharacterModel(
            id = "char_3",
            name = "Student Maya",
            role = CharacterRole.STUDENT_INTERMEDIATE,
            ageGroup = "13 years old",
            voiceTone = "Thoughtful & Confident",
            catchphrase = "I think I should use the Present Perfect here because the result matters now!",
            personalityTraits = listOf("Analytical", "Attentive", "Loves Grammar"),
            avatarColorHex = 0xFFF59E0B,
            avatarEmoji = "👧"
        ),
        CharacterModel(
            id = "char_4",
            name = "Student Alex",
            role = CharacterRole.STUDENT_ADVANCED,
            ageGroup = "17 years old",
            voiceTone = "Expressive & Fluent",
            catchphrase = "Could you explain the nuance between 'consequently' and 'therefore'?",
            personalityTraits = listOf("Articulate", "Goal-oriented", "Essay Writer"),
            avatarColorHex = 0xFFEC4899,
            avatarEmoji = "🧑"
        ),
        CharacterModel(
            id = "char_5",
            name = "Spark Robot",
            role = CharacterRole.AI_ASSISTANT,
            ageGroup = "Robot Mascot",
            voiceTone = "Friendly Robotic Beeps & Clear Voice",
            catchphrase = "Beep boop! Pronunciation score: 98%! Superb articulation!",
            personalityTraits = listOf("Tech-savvy", "Interactive", "Fun Feedback"),
            avatarColorHex = 0xFF06B6D4,
            avatarEmoji = "🤖"
        )
    )

    val sampleScripts = listOf(
        TeachingScript(
            id = "script_1",
            title = "Ordering Food at a Cafe Dialogue",
            topic = "Food & Politeness",
            level = SchoolLevel.PRIMARY,
            description = "A friendly dialogue between Teacher Emma (Waitress) and Student Leo (Customer) practicing polite modal verbs.",
            relatedWorksheetId = "ws_2",
            lines = listOf(
                ScriptLine(
                    id = "l1",
                    characterName = "Teacher Emma",
                    role = ScriptRole.TEACHER,
                    text = "Welcome to Sunny Cafe! How can I help you today?",
                    phoneticNote = "/ˈwɛlkəm tuː ˈsʌni kæˈfeɪ/",
                    teacherTip = "Emphasize friendly greeting pitch."
                ),
                ScriptLine(
                    id = "l2",
                    characterName = "Student Leo",
                    role = ScriptRole.STUDENT,
                    text = "Hello! Could I please have an apple juice and a sandwich?",
                    phoneticNote = "/kʊd aɪ pliːz hæv æn ˈæpl̩ dʒuːs/",
                    teacherTip = "Highlight polite request phrase 'Could I please...'"
                ),
                ScriptLine(
                    id = "l3",
                    characterName = "Teacher Emma",
                    role = ScriptRole.TEACHER,
                    text = "Of course! Would you like cheese or tuna in your sandwich?",
                    phoneticNote = "/wʊd juː laɪk tʃiːz ɔːr ˈtuːnə/",
                    teacherTip = "Demonstrate choice question intonation."
                ),
                ScriptLine(
                    id = "l4",
                    characterName = "Student Leo",
                    role = ScriptRole.STUDENT,
                    text = "Tuna, please! How much is it?",
                    phoneticNote = "/ˈtuːnə pliːz haʊ mʌtʃ ɪz ɪt/",
                    teacherTip = "Remind student about currency pronunciation."
                ),
                ScriptLine(
                    id = "l5",
                    characterName = "Teacher Emma",
                    role = ScriptRole.TEACHER,
                    text = "That will be four dollars. Here is your juice!",
                    phoneticNote = "/ðæt wɪl biː fɔːr ˈdɒlɚz/",
                    teacherTip = "Model handing item phrase."
                )
            )
        ),
        TeachingScript(
            id = "script_2",
            title = "Present Perfect vs. Past Simple Teacher Explanation",
            topic = "Grammar Mastery",
            level = SchoolLevel.MIDDLE,
            description = "Teacher Emma explains when to use 'have visited' vs 'visited' with interactive student prompts.",
            relatedWorksheetId = "ws_1",
            lines = listOf(
                ScriptLine(
                    id = "l2_1",
                    characterName = "Teacher Emma",
                    role = ScriptRole.TEACHER,
                    text = "Class, notice the difference: 'I went to London in 2022' specifies a finished time in the past.",
                    teacherTip = "Draw timeline on board."
                ),
                ScriptLine(
                    id = "l2_2",
                    characterName = "Student Maya",
                    role = ScriptRole.STUDENT,
                    text = "So if I say 'I have visited London', does that mean any time in my life?",
                    teacherTip = "Praise student for key insight."
                ),
                ScriptLine(
                    id = "l2_3",
                    characterName = "Teacher Emma",
                    role = ScriptRole.TEACHER,
                    text = "Exactly, Maya! We use Present Perfect for life experiences when the exact time isn't mentioned.",
                    teacherTip = "Highlight formula: Have/Has + Past Participle."
                )
            )
        ),
        TeachingScript(
            id = "script_3",
            title = "Magic E Sound Transformation Roleplay",
            topic = "Phonics",
            level = SchoolLevel.PRIMARY,
            description = "Robot Spark turns short vowels into long vowels using the Magic E wand!",
            relatedWorksheetId = "ws_3",
            lines = listOf(
                ScriptLine(
                    id = "l3_1",
                    characterName = "Spark Robot",
                    role = ScriptRole.ROBOT,
                    text = "Beep boop! Look at the word C-A-P: CAP! Tap the Magic E wand!",
                    phoneticNote = "/kæp/"
                ),
                ScriptLine(
                    id = "l3_2",
                    characterName = "Student Leo",
                    role = ScriptRole.STUDENT,
                    text = "Magic E! Now it spells C-A-P-E: CAPE!",
                    phoneticNote = "/keɪp/"
                ),
                ScriptLine(
                    id = "l3_3",
                    characterName = "Teacher Emma",
                    role = ScriptRole.TEACHER,
                    text = "Great job Leo! The Magic E makes the 'A' say its own name!",
                    teacherTip = "Wave hands to show vowel stretch."
                )
            )
        )
    )

    val sampleLessons = listOf(
        Lesson(
            id = "les_1",
            title = "Ordering Food & Cafe Expressions",
            level = SchoolLevel.PRIMARY,
            topic = "Speaking & Roleplay",
            durationMinutes = 45,
            objectives = listOf(
                "Use polite request forms ('Could I get...', 'I'd like...')",
                "Identify common food and drink vocabulary",
                "Roleplay cafe transactions in pairs with cartoon character cards"
            ),
            targetGrammar = "Polite Modal Verbs (Would like, Could)",
            attachedWorksheetIds = listOf("ws_2"),
            attachedScriptIds = listOf("script_1"),
            steps = listOf(
                LessonStep("Warm-up (5 mins)", "Show food flashcards on screen. Ask students their favorite breakfast."),
                LessonStep("Presentation (10 mins)", "Introduce dialogue with Teacher Emma & Student Leo audio script."),
                LessonStep("Practice (15 mins)", "Students fill out food order worksheet cards in pairs."),
                LessonStep("Production (10 mins)", "Perform mini cafe roleplays using props or character badges."),
                LessonStep("Wrap-up (5 mins)", "Quick vocabulary speed drill and praise points.")
            )
        ),
        Lesson(
            id = "les_2",
            title = "Mastering Present Perfect vs. Past Simple",
            level = SchoolLevel.MIDDLE,
            topic = "Grammar & Timeline Analysis",
            durationMinutes = 60,
            objectives = listOf(
                "Distinguish between finished past time indicators and unspecified life experience",
                "Form positive, negative and question sentences using Have/Has + V3",
                "Complete timeline gap-fill exercises accurately"
            ),
            targetGrammar = "Present Perfect Simple vs Past Simple",
            attachedWorksheetIds = listOf("ws_1"),
            attachedScriptIds = listOf("script_2"),
            steps = listOf(
                LessonStep("Warm-up (10 mins)", "Timeline puzzle game on whiteboard: Past vs Present."),
                LessonStep("Presentation (15 mins)", "Teach rule contrast using teaching script dialogue."),
                LessonStep("Practice (20 mins)", "Worksheet #1 exercise sets A & B individually."),
                LessonStep("Production (10 mins)", "Pair interview: 'Have you ever...?' survey card."),
                LessonStep("Wrap-up (5 mins)", "Summary exit ticket question.")
            )
        ),
        Lesson(
            id = "les_3",
            title = "Phonics Masterclass: Magic Silent E",
            level = SchoolLevel.PRIMARY,
            topic = "Phonics & Spelling",
            durationMinutes = 35,
            objectives = listOf(
                "Understand how adding silent E changes short vowels to long vowels",
                "Read 10 target CVCe words with confidence",
                "Sing the Magic E chant with Robot Spark"
            ),
            targetGrammar = "Silent E Vowel Rules",
            attachedWorksheetIds = listOf("ws_3"),
            attachedScriptIds = listOf("script_3"),
            steps = listOf(
                LessonStep("Warm-up (5 mins)", "Phonics chant with Spark Robot."),
                LessonStep("Presentation (10 mins)", "Demonstrate cap -> cape, kit -> kite on magnetic board."),
                LessonStep("Practice (12 mins)", "Worksheet matching game."),
                LessonStep("Wrap-up (8 mins)", "Individual reading test with teacher feedback.")
            )
        ),
        Lesson(
            id = "les_4",
            title = "Professional Email Writing for Adults",
            level = SchoolLevel.ADULT,
            topic = "Business Communication",
            durationMinutes = 50,
            objectives = listOf(
                "Write formal email greetings and sign-offs",
                "Softening requests using 'I was wondering if...'",
                "Proofread and correct 5 common ESL business email mistakes"
            ),
            targetGrammar = "Indirect Questions & Formal Register",
            attachedWorksheetIds = listOf("ws_4"),
            attachedScriptIds = emptyList(),
            steps = listOf(
                LessonStep("Warm-up (10 mins)", "Analyze informal vs formal email samples."),
                LessonStep("Presentation (15 mins)", "Key phrase bank breakdown for action requests."),
                LessonStep("Practice (15 mins)", "Rewrite poor email prompts using worksheet templates."),
                LessonStep("Production (10 mins)", "Compose realistic response to a client complaint scenario.")
            )
        )
    )

    val sampleVideoProjects = listOf(
        VideoProject(
            id = "vid_1",
            title = "Daily English: Ordering at a Cafe",
            targetLevel = SchoolLevel.PRIMARY,
            status = VideoStatus.FINISHED,
            durationSeconds = 125,
            scriptTitle = "Ordering Food at a Cafe Dialogue",
            voiceoverVoice = "Emma (Teacher) & Leo (Kid)",
            thumbnailColorHex = 0xFF4F46E5,
            subtitles = listOf(
                SubtitleLine(0, 3500, "Teacher Emma", "Welcome to Sunny Cafe! How can I help you today?"),
                SubtitleLine(3600, 7800, "Student Leo", "Hello! Could I please have an apple juice and a sandwich?"),
                SubtitleLine(7900, 11500, "Teacher Emma", "Of course! Would you like cheese or tuna in your sandwich?"),
                SubtitleLine(11600, 14200, "Student Leo", "Tuna, please! How much is it?"),
                SubtitleLine(14300, 18000, "Teacher Emma", "That will be four dollars. Here is your juice!")
            )
        ),
        VideoProject(
            id = "vid_2",
            title = "Grammar Focus: Present Perfect Timeline",
            targetLevel = SchoolLevel.MIDDLE,
            status = VideoStatus.VOICEOVER,
            durationSeconds = 180,
            scriptTitle = "Present Perfect vs. Past Simple Teacher Explanation",
            voiceoverVoice = "Emma (Teacher RP Accent)",
            thumbnailColorHex = 0xFF10B981,
            subtitles = listOf(
                SubtitleLine(0, 4500, "Teacher Emma", "Let's explore when to use Present Perfect versus Past Simple!"),
                SubtitleLine(4600, 9200, "Teacher Emma", "Use Past Simple when the time is specified and finished."),
                SubtitleLine(9300, 14000, "Student Maya", "And use Present Perfect for life experience when time isn't key!")
            )
        ),
        VideoProject(
            id = "vid_3",
            title = "Magic E Phonics Animated Lesson",
            targetLevel = SchoolLevel.PRIMARY,
            status = VideoStatus.PLANNING,
            durationSeconds = 90,
            scriptTitle = "Magic E Sound Transformation Roleplay",
            voiceoverVoice = "Spark Robot & Teacher Emma",
            thumbnailColorHex = 0xFFF59E0B,
            subtitles = listOf(
                SubtitleLine(0, 3000, "Spark Robot", "Beep boop! Watch the Magic E wand!"),
                SubtitleLine(3100, 6500, "Student Leo", "Cap turns into Cape!")
            )
        )
    )
}
