package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudioRepository {

    private val _worksheets = MutableStateFlow<List<Worksheet>>(SampleData.sampleWorksheets)
    val worksheets: StateFlow<List<Worksheet>> = _worksheets.asStateFlow()

    private val _lessons = MutableStateFlow<List<Lesson>>(SampleData.sampleLessons)
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _scripts = MutableStateFlow<List<TeachingScript>>(SampleData.sampleScripts)
    val scripts: StateFlow<List<TeachingScript>> = _scripts.asStateFlow()

    private val _characters = MutableStateFlow<List<CharacterModel>>(SampleData.sampleCharacters)
    val characters: StateFlow<List<CharacterModel>> = _characters.asStateFlow()

    private val _videoProjects = MutableStateFlow<List<VideoProject>>(SampleData.sampleVideoProjects)
    val videoProjects: StateFlow<List<VideoProject>> = _videoProjects.asStateFlow()

    // Search query across studio
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Worksheets operations
    fun addWorksheet(worksheet: Worksheet) {
        _worksheets.update { listOf(worksheet) + it }
    }

    fun deleteWorksheet(id: String) {
        _worksheets.update { list -> list.filterNot { it.id == id } }
    }

    // Lessons operations
    fun addLesson(lesson: Lesson) {
        _lessons.update { listOf(lesson) + it }
    }

    fun deleteLesson(id: String) {
        _lessons.update { list -> list.filterNot { it.id == id } }
    }

    // Scripts operations
    fun addScript(script: TeachingScript) {
        _scripts.update { listOf(script) + it }
    }

    fun addLineToScript(scriptId: String, line: ScriptLine) {
        _scripts.update { list ->
            list.map { s ->
                if (s.id == scriptId) {
                    s.copy(lines = s.lines + line)
                } else s
            }
        }
    }

    fun deleteScript(id: String) {
        _scripts.update { list -> list.filterNot { it.id == id } }
    }

    // Characters operations
    fun addCharacter(character: CharacterModel) {
        _characters.update { listOf(character) + it }
    }

    fun deleteCharacter(id: String) {
        _characters.update { list -> list.filterNot { it.id == id } }
    }

    // Video Projects operations
    fun addVideoProject(project: VideoProject) {
        _videoProjects.update { listOf(project) + it }
    }

    fun updateVideoStatus(projectId: String, newStatus: VideoStatus) {
        _videoProjects.update { list ->
            list.map { v ->
                if (v.id == projectId) v.copy(status = newStatus) else v
            }
        }
    }

    fun deleteVideoProject(id: String) {
        _videoProjects.update { list -> list.filterNot { it.id == id } }
    }
}
