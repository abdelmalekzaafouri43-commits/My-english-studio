package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudioRepository
import com.example.model.SchoolLevel
import com.example.model.SubtitleLine
import com.example.model.VideoProject
import com.example.model.VideoStatus
import com.example.ui.components.LevelBadge
import com.example.ui.components.StudioSearchBar
import com.example.ui.theme.VioletPurple
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoProjectsScreen(
    repository: StudioRepository,
    modifier: Modifier = Modifier
) {
    val videoProjects by repository.videoProjects.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf<VideoStatus?>(null) }
    var selectedVideoForStudio by remember { mutableStateOf<VideoProject?>(null) }
    var showAddVideoDialog by remember { mutableStateOf(false) }

    val filteredVideos = videoProjects.filter { v ->
        (searchQuery.isEmpty() || v.title.contains(searchQuery, ignoreCase = true) || v.scriptTitle.contains(searchQuery, ignoreCase = true)) &&
                (selectedStatusFilter == null || v.status == selectedStatusFilter)
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
                    text = "Video Projects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Manage video scripts, voiceovers, subtitles & MP4 exports",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { showAddVideoDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = VioletPurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_video_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Video")
            }
        }

        // Search bar
        StudioSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholderText = "Search video projects by title..."
        )

        // Status Filter Chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedStatusFilter == null,
                onClick = { selectedStatusFilter = null },
                label = { Text("All Statuses") },
                shape = RoundedCornerShape(10.dp)
            )

            VideoStatus.values().forEach { st ->
                FilterChip(
                    selected = selectedStatusFilter == st,
                    onClick = { selectedStatusFilter = if (selectedStatusFilter == st) null else st },
                    label = { Text(st.displayName) },
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Video Projects List
        if (filteredVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No video projects found",
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
                items(filteredVideos) { video ->
                    VideoProjectCard(
                        video = video,
                        onOpenStudio = { selectedVideoForStudio = video },
                        onStatusChange = { newStatus -> repository.updateVideoStatus(video.id, newStatus) },
                        onDelete = { repository.deleteVideoProject(video.id) }
                    )
                }
            }
        }
    }

    // Interactive Video Player & Subtitle Studio Modal
    selectedVideoForStudio?.let { video ->
        VideoStudioPlayerModal(
            video = video,
            onDismiss = { selectedVideoForStudio = null }
        )
    }

    if (showAddVideoDialog) {
        AddVideoProjectDialog(
            onDismiss = { showAddVideoDialog = false },
            onAdd = { newVid ->
                repository.addVideoProject(newVid)
                showAddVideoDialog = false
            }
        )
    }
}

@Composable
fun VideoProjectCard(
    video: VideoProject,
    onOpenStudio: () -> Unit,
    onStatusChange: (VideoStatus) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onOpenStudio() }
            .testTag("video_card_${video.id}"),
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
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VioletPurple.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = VioletPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Script: ${video.scriptTitle} • ${video.durationSeconds}s",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                LevelBadge(level = video.targetLevel)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status Badge & Voiceover info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = video.status)

                Text(
                    text = "Voice: ${video.voiceoverVoice}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onOpenStudio,
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPurple),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("open_video_studio_${video.id}")
                ) {
                    Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Open Video Player")
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Video",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: VideoStatus) {
    val (bgColor, textColor) = when (status) {
        VideoStatus.PLANNING -> Color(0xFFFEF3C7) to Color(0xFF92400E)
        VideoStatus.SCRIPTED -> Color(0xFFE0E7FF) to Color(0xFF3730A3)
        VideoStatus.VOICEOVER -> Color(0xFFCCFBF1) to Color(0xFF115E59)
        VideoStatus.SUBTITLED -> Color(0xFFFCE7F3) to Color(0xFF9D174D)
        VideoStatus.FINISHED -> Color(0xFFD1FAE5) to Color(0xFF065F46)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = status.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun VideoStudioPlayerModal(
    video: VideoProject,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSubtitleIndex by remember { mutableStateOf(0) }
    var isSubtitlesVisible by remember { mutableStateOf(true) }

    // Simulated video subtitle ticker loop
    LaunchedEffect(isPlaying, currentSubtitleIndex) {
        if (isPlaying && video.subtitles.isNotEmpty()) {
            delay(3000)
            if (currentSubtitleIndex < video.subtitles.size - 1) {
                currentSubtitleIndex += 1
            } else {
                currentSubtitleIndex = 0
                isPlaying = false
            }
        }
    }

    val activeSub = if (video.subtitles.isNotEmpty()) video.subtitles[currentSubtitleIndex] else null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(video.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Video Studio & Subtitle Preview", fontSize = 12.sp, color = VioletPurple, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
            ) {
                // Interactive Video Canvas Screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top video info bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color.Red
                            ) {
                                Text(
                                    text = if (isPlaying) "● PLAYING MP4" else "READY",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = video.videoResolution,
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Center cartoon video graphics animation placeholder
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.GraphicEq else Icons.Default.PlayCircleFilled,
                                contentDescription = null,
                                tint = VioletPurple,
                                modifier = Modifier.size(56.dp)
                            )
                        }

                        // Bottom Live Subtitle Display Box
                        if (isSubtitlesVisible && activeSub != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.Black.copy(alpha = 0.8f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = activeSub.speaker,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF59E0B)
                                    )
                                    Text(
                                        text = activeSub.text,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // Video Controls & Subtitle Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(VioletPurple)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause Video",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isPlaying) "Playing..." else "Paused",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FilterChip(
                        selected = isSubtitlesVisible,
                        onClick = { isSubtitlesVisible = !isSubtitlesVisible },
                        label = { Text("Subtitles CC") },
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                // Subtitles List Preview
                Text(
                    text = "Synced Subtitles Track (${video.subtitles.size} lines):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(video.subtitles) { idx: Int, sub: SubtitleLine ->
                        val isCurrent = idx == currentSubtitleIndex
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isCurrent) VioletPurple.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${sub.speaker}: ${sub.text}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                if (isCurrent) {
                                    Text("● ACTIVE", fontSize = 9.sp, color = VioletPurple, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { /* Export subtitles .srt */ },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Subtitles, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export SRT")
                    }

                    Button(
                        onClick = { /* Export MP4 */ },
                        colors = ButtonDefaults.buttonColors(containerColor = VioletPurple),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export MP4")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close Studio") }
        }
    )
}

@Composable
fun AddVideoProjectDialog(
    onDismiss: () -> Unit,
    onAdd: (VideoProject) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var scriptTitle by remember { mutableStateOf("Ordering Food at a Cafe Dialogue") }
    var level by remember { mutableStateOf(SchoolLevel.PRIMARY) }
    var voiceoverVoice by remember { mutableStateOf("Emma & Leo") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Video Project", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Video Project Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = scriptTitle,
                    onValueChange = { scriptTitle = it },
                    label = { Text("Base Teaching Script Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = voiceoverVoice,
                    onValueChange = { voiceoverVoice = it },
                    label = { Text("Voiceover Voice Profile") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(
                            VideoProject(
                                id = UUID.randomUUID().toString(),
                                title = title,
                                targetLevel = level,
                                status = VideoStatus.PLANNING,
                                durationSeconds = 120,
                                scriptTitle = scriptTitle,
                                voiceoverVoice = voiceoverVoice,
                                subtitles = listOf(
                                    SubtitleLine(0, 3000, "Teacher Emma", "Welcome to our video lesson!"),
                                    SubtitleLine(3100, 6000, "Student Leo", "Let's learn together!")
                                )
                            )
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Create Video Project")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
