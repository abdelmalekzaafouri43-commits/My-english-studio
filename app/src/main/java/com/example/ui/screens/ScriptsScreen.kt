package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.model.SchoolLevel
import com.example.model.ScriptLine
import com.example.model.ScriptRole
import com.example.model.TeachingScript
import com.example.ui.components.LevelBadge
import com.example.ui.components.StudioSearchBar
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.IndigoPrimary
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun ScriptsScreen(
    repository: StudioRepository,
    modifier: Modifier = Modifier
) {
    val scripts by repository.scripts.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedScriptForPractice by remember { mutableStateOf<TeachingScript?>(null) }
    var showAddScriptDialog by remember { mutableStateOf(false) }

    val filteredScripts = scripts.filter { sc ->
        searchQuery.isEmpty() || sc.title.contains(searchQuery, ignoreCase = true) || sc.topic.contains(searchQuery, ignoreCase = true)
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
                    text = "Teaching Scripts",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Dialogues, teacher explanations & student roleplays",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { showAddScriptDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_script_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Script", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        // Search Bar
        StudioSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholderText = "Search scripts by topic or title..."
        )

        if (filteredScripts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No teaching scripts found",
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
                items(filteredScripts) { script ->
                    ScriptCardItem(
                        script = script,
                        onPractice = { selectedScriptForPractice = script },
                        onDelete = { repository.deleteScript(script.id) }
                    )
                }
            }
        }
    }

    // Interactive Script Practice Reader Dialog
    selectedScriptForPractice?.let { script ->
        ScriptPracticeReaderModal(
            script = script,
            onDismiss = { selectedScriptForPractice = null },
            onAddLine = { line -> repository.addLineToScript(script.id, line) }
        )
    }

    if (showAddScriptDialog) {
        AddScriptDialog(
            onDismiss = { showAddScriptDialog = false },
            onAdd = { newScript ->
                repository.addScript(newScript)
                showAddScriptDialog = false
            }
        )
    }
}

@Composable
fun ScriptCardItem(
    script: TeachingScript,
    onPractice: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onPractice() }
            .testTag("script_card_${script.id}"),
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
                            .background(AmberAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = AmberAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = script.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${script.topic} • ${script.lines.size} Lines",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                LevelBadge(level = script.level)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = script.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPractice,
                    colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("practice_script_${script.id}")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Interactive Reader", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Script",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun ScriptPracticeReaderModal(
    script: TeachingScript,
    onDismiss: () -> Unit,
    onAddLine: (ScriptLine) -> Unit
) {
    var activeLineIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableStateOf("1.0x") }
    var showNewLineInput by remember { mutableStateOf(false) }

    var newLineSpeaker by remember { mutableStateOf("Teacher Emma") }
    var newLineText by remember { mutableStateOf("") }

    // Simulated audio auto-advance loop
    LaunchedEffect(isPlaying, activeLineIndex, playbackSpeed) {
        if (isPlaying && activeLineIndex < script.lines.size - 1) {
            val delayMs = when (playbackSpeed) {
                "0.75x" -> 4000L
                "1.25x" -> 2000L
                else -> 2800L
            }
            delay(delayMs)
            activeLineIndex += 1
        } else if (isPlaying && activeLineIndex >= script.lines.size - 1) {
            isPlaying = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(script.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Roleplay & Dialogue Practice Mode", fontSize = 12.sp, color = AmberAccent, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp)
            ) {
                // Audio Player Control Bar
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(AmberAccent)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play Dialogue",
                                    tint = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isPlaying) "Playing line ${activeLineIndex + 1}/${script.lines.size}" else "Paused",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Speed selector toggle
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("0.75x", "1.0x", "1.25x").forEach { speed ->
                                FilterChip(
                                    selected = playbackSpeed == speed,
                                    onClick = { playbackSpeed = speed },
                                    label = { Text(speed, fontSize = 10.sp) },
                                    shape = RoundedCornerShape(6.dp)
                                )
                            }
                        }
                    }
                }

                // Script Lines Scrollable List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(script.lines) { index, line ->
                        val isCurrent = index == activeLineIndex

                        val containerColor by animateColorAsState(
                            targetValue = if (isCurrent) AmberAccent.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                            label = "lineColor"
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    activeLineIndex = index
                                    isPlaying = false
                                }
                                .border(
                                    width = if (isCurrent) 2.dp else 0.dp,
                                    color = if (isCurrent) AmberAccent else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = containerColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = line.characterName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (line.role == ScriptRole.TEACHER) IndigoPrimary else AmberAccent
                                    )

                                    if (isCurrent && isPlaying) {
                                        Text("🔊 Speaking", fontSize = 10.sp, color = AmberAccent, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = line.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                )

                                if (line.phoneticNote.isNotEmpty()) {
                                    Text(
                                        text = "Phonetics: ${line.phoneticNote}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (line.teacherTip.isNotEmpty()) {
                                    Text(
                                        text = "Tip: ${line.teacherTip}",
                                        fontSize = 10.sp,
                                        color = IndigoPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                // Add line expanded section
                if (showNewLineInput) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = newLineSpeaker,
                            onValueChange = { newLineSpeaker = it },
                            label = { Text("Speaker Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newLineText,
                            onValueChange = { newLineText = it },
                            label = { Text("Dialogue Line Text") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = { showNewLineInput = false }) { Text("Cancel") }
                            Button(
                                onClick = {
                                    if (newLineText.isNotBlank()) {
                                        onAddLine(
                                            ScriptLine(
                                                id = UUID.randomUUID().toString(),
                                                characterName = newLineSpeaker,
                                                role = ScriptRole.STUDENT,
                                                text = newLineText
                                            )
                                        )
                                        newLineText = ""
                                        showNewLineInput = false
                                    }
                                }
                            ) { Text("Save Line") }
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { showNewLineInput = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Line to Dialogue")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close Reader") }
        }
    )
}

@Composable
fun AddScriptDialog(
    onDismiss: () -> Unit,
    onAdd: (TeachingScript) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("Speaking & Phonics") }
    var description by remember { mutableStateOf("") }
    var level by remember { mutableStateOf(SchoolLevel.PRIMARY) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Teaching Script", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Script Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Topic Area") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Short Context / Summary") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(
                            TeachingScript(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                topic = topic,
                                level = level,
                                description = description.ifBlank { "Custom teacher dialogue script." },
                                lines = listOf(
                                    ScriptLine("1", "Teacher Emma", ScriptRole.TEACHER, "Hello everyone! Let's start our dialogue!"),
                                    ScriptLine("2", "Student Leo", ScriptRole.STUDENT, "Great! I'm ready to practice!")
                                )
                            )
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Generate Script")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
