package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.model.CharacterModel
import com.example.model.CharacterRole
import com.example.ui.components.StudioSearchBar
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseAccent
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharactersScreen(
    repository: StudioRepository,
    modifier: Modifier = Modifier
) {
    val characters by repository.characters.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf<CharacterRole?>(null) }
    var showAddCharacterDialog by remember { mutableStateOf(false) }

    val filteredCharacters = characters.filter { ch ->
        (searchQuery.isEmpty() || ch.name.contains(searchQuery, ignoreCase = true) || ch.catchphrase.contains(searchQuery, ignoreCase = true)) &&
                (selectedRoleFilter == null || ch.role == selectedRoleFilter)
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
                    text = "Cartoon Characters",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Organize teacher & student avatars for dialogue scripts",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = { showAddCharacterDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = RoseAccent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("add_character_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Character")
            }
        }

        // Search bar
        StudioSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholderText = "Search character by name or catchphrase..."
        )

        // Role Filter Chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedRoleFilter == null,
                onClick = { selectedRoleFilter = null },
                label = { Text("All Roles") },
                shape = RoundedCornerShape(10.dp)
            )

            CharacterRole.values().forEach { role ->
                FilterChip(
                    selected = selectedRoleFilter == role,
                    onClick = { selectedRoleFilter = if (selectedRoleFilter == role) null else role },
                    label = { Text(role.displayName) },
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Character Cards Grid
        if (filteredCharacters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No characters found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 220.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCharacters) { character ->
                    CharacterCard(
                        character = character,
                        onDelete = { repository.deleteCharacter(character.id) }
                    )
                }
            }
        }
    }

    if (showAddCharacterDialog) {
        AddCharacterDialog(
            onDismiss = { showAddCharacterDialog = false },
            onAdd = { newChar ->
                repository.addCharacter(newChar)
                showAddCharacterDialog = false
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterCard(
    character: CharacterModel,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlayingVoice by remember { mutableStateOf(false) }

    // Simulated audio speech animation timer
    LaunchedEffect(isPlayingVoice) {
        if (isPlayingVoice) {
            delay(2500)
            isPlayingVoice = false
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .testTag("character_card_${character.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(character.avatarColorHex).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = character.avatarEmoji,
                        fontSize = 28.sp
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Character",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${character.role.displayName} • ${character.ageGroup}",
                style = MaterialTheme.typography.labelMedium,
                color = Color(character.avatarColorHex),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Voice Tone: ${character.voiceTone}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Catchphrase Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(character.avatarColorHex).copy(alpha = 0.08f)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Catchphrase:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(character.avatarColorHex)
                    )
                    Text(
                        text = "\"${character.catchphrase}\"",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Personality Traits
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                character.personalityTraits.forEach { trait ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = trait,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Play voice button
            Button(
                onClick = { isPlayingVoice = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(character.avatarColorHex)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("play_voice_${character.id}")
            ) {
                Icon(
                    imageVector = if (isPlayingVoice) Icons.Default.GraphicEq else Icons.Default.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isPlayingVoice) "Speaking catchphrase..." else "Listen Sample Voice",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AddCharacterDialog(
    onDismiss: () -> Unit,
    onAdd: (CharacterModel) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(CharacterRole.STUDENT_BEGINNER) }
    var voiceTone by remember { mutableStateOf("Enthusiastic & Clear") }
    var catchphrase by remember { mutableStateOf("I love learning new English words!") }
    var avatarEmoji by remember { mutableStateOf("👦") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Cartoon Character", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Character Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = voiceTone,
                    onValueChange = { voiceTone = it },
                    label = { Text("Voice Profile / Tone") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = catchphrase,
                    onValueChange = { catchphrase = it },
                    label = { Text("Signature Catchphrase") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = avatarEmoji,
                    onValueChange = { avatarEmoji = it },
                    label = { Text("Avatar Emoji (e.g. 👩‍🏫, 👦, 🤖)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(
                            CharacterModel(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                role = role,
                                ageGroup = "Student",
                                voiceTone = voiceTone,
                                catchphrase = catchphrase,
                                personalityTraits = listOf("Fun", "Active Learner"),
                                avatarColorHex = 0xFF8B5CF6,
                                avatarEmoji = avatarEmoji.ifBlank { "🧑" }
                            )
                        )
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Add Character")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
