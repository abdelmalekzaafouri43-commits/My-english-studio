package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudioRepository
import com.example.model.Lesson
import com.example.model.LessonStep
import com.example.model.SchoolLevel
import com.example.ui.components.LevelBadge
import com.example.ui.components.StudioSearchBar
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LessonsScreen(
    repository: StudioRepository,
    modifier: Modifier = Modifier
) {
    val lessons by repository.lessons.collectAsState()
    val worksheets by repository.worksheets.collectAsState()
    val scripts by repository.scripts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedLevelFilter by remember { mutableStateOf<SchoolLevel?>(null) }

    var expandedLessonId by remember { mutableStateOf<String?>(null) }
    var showAddLessonDialog by remember { mutableStateOf(false) }

    val filteredLessons = lessons.filter { l ->
        (searchQuery.isEmpty() || l.title.contains(searchQuery, ignoreCase = true) || l.topic.contains(searchQuery, ignoreCase = true)) &&
                (selectedLevelFilter == null || selectedLevelFilter == SchoolLevel.ALL || l.level == selectedLevelFilter)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Lessons",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Organize lesson plans by school level and topic",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { showAddLessonDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_lesson_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Lesson")
            }
        }

        // Search Bar
        StudioSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholderText = "Search lessons by topic or title..."
        )

        // School Level Filter Row
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedLevelFilter == null || selectedLevelFilter == SchoolLevel.ALL,
                onClick = { selectedLevelFilter = SchoolLevel.ALL },
                label = { Text("All School Levels") },
                shape = RoundedCornerShape(10.dp)
            )

            SchoolLevel.values().filter { it != SchoolLevel.ALL }.forEach { lvl ->
                FilterChip(
                    selected = selectedLevelFilter == lvl,
                    onClick = { selectedLevelFilter = if (selectedLevelFilter == lvl) null else lvl },
                    label = { Text(lvl.displayName) },
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Lessons List
        if (filteredLessons.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No lesson plans found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredLessons) { lesson ->
                    val isExpanded = expandedLessonId == lesson.id
                    val linkedWorksheets = worksheets.filter { lesson.attachedWorksheetIds.contains(it.id) }
                    val linkedScripts = scripts.filter { lesson.attachedScriptIds.contains(it.id) }

                    LessonExpandableCard(
                        lesson = lesson,
                        isExpanded = isExpanded,
                        linkedWorksheets = linkedWorksheets,
                        linkedScripts = linkedScripts,
                        onToggleExpand = {
                            expandedLessonId = if (isExpanded) null else lesson.id
                        },
                        onDelete = { repository.deleteLesson(lesson.id) }
                    )
                }
            }
        }
    }

    if (showAddLessonDialog) {
        AddLessonDialog(
            onDismiss = { showAddLessonDialog = false },
            onAdd = { newLesson ->
                repository.addLesson(newLesson)
                showAddLessonDialog = false
            }
        )
    }
}

@Composable
fun LessonExpandableCard(
    lesson: Lesson,
    isExpanded: Boolean,
    linkedWorksheets: List<com.example.model.Worksheet>,
    linkedScripts: List<com.example.model.TeachingScript>,
    onToggleExpand: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggleExpand() }
            .testTag("lesson_card_${lesson.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${lesson.topic} • ${lesson.durationMinutes} mins",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LevelBadge(level = lesson.level)
                    IconButton(onClick = onToggleExpand) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand Lesson Details"
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Target Focus / Grammar
                    if (lesson.targetGrammar.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = IndigoPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Target Grammar: ${lesson.targetGrammar}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // Learning Objectives
                    Text(
                        text = "Learning Objectives:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    lesson.objectives.forEach { obj ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text("• ", fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                            Text(
                                text = obj,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Step-by-Step Breakdown
                    Text(
                        text = "Lesson Execution Steps:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    lesson.steps.forEachIndexed { idx, step ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "${idx + 1}. ${step.phase}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSuccess
                                )
                                Text(
                                    text = step.instructions,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Linked Resources (Worksheets & Scripts)
                    if (linkedWorksheets.isNotEmpty() || linkedScripts.isNotEmpty()) {
                        Text(
                            text = "Linked Studio Materials:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            linkedWorksheets.forEach { ws ->
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text("📄 ${ws.title}") }
                                )
                            }
                            linkedScripts.forEach { sc ->
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text("🎙️ ${sc.title}") }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete Plan")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddLessonDialog(
    onDismiss: () -> Unit,
    onAdd: (Lesson) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("Grammar & Speaking") }
    var level by remember { mutableStateOf(SchoolLevel.MIDDLE) }
    var duration by remember { mutableStateOf("45") }
    var objInput by remember { mutableStateOf("Master target structure, Practice pair dialogue") }
    var targetGrammar by remember { mutableStateOf("Present Perfect Simple") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Lesson Plan", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Lesson Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Topic Area") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duration (mins)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = targetGrammar,
                    onValueChange = { targetGrammar = it },
                    label = { Text("Target Grammar / Vocabulary Focus") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = objInput,
                    onValueChange = { objInput = it },
                    label = { Text("Objectives (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val objectives = objInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onAdd(
                            Lesson(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                level = level,
                                topic = topic,
                                durationMinutes = duration.toIntOrNull() ?: 45,
                                objectives = if (objectives.isEmpty()) listOf("Learn core vocabulary") else objectives,
                                targetGrammar = targetGrammar,
                                steps = listOf(
                                    LessonStep("Warm-up (5m)", "Review previous lesson flashcards."),
                                    LessonStep("Presentation (15m)", "Explain $targetGrammar rule on board."),
                                    LessonStep("Practice (15m)", "Complete pair practice exercise."),
                                    LessonStep("Wrap-up (10m)", "Group feedback & homework assignment.")
                                )
                            )
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Save Lesson")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
