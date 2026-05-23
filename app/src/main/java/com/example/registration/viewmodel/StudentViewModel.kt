package com.example.registration.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registration.data.Student
import com.example.registration.data.StudentDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StudentViewModel(private val dao: StudentDao) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val students = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) dao.getAllStudents() else dao.getStudentsByName(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var school by mutableStateOf("")
        private set
    var province by mutableStateOf("")
        private set
    var faculty by mutableStateOf("")
        private set
    var department by mutableStateOf("")
        private set
    var gender by mutableStateOf("Laki-laki")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var schoolError by mutableStateOf<String?>(null)
        private set
    var provinceError by mutableStateOf<String?>(null)
        private set
    var facultyError by mutableStateOf<String?>(null)
        private set
    var departmentError by mutableStateOf<String?>(null)
        private set

    var editingStudent by mutableStateOf<Student?>(null)
        private set

    val genderStats = students.map { list ->
        val total = list.size
        if (total == 0) Pair(0, 0) else {
            val male = list.count { it.gender == "Laki-laki" }
            Pair(male, total - male)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(0, 0))

    val provinceStats = students.map { list ->
        if (list.isEmpty()) emptyList() else {
            val grouped = list.groupBy { it.province.trim().uppercase() }.map { it.key to it.value.size }.sortedByDescending { it.second }
            val top10 = grouped.take(10)
            val others = grouped.drop(10).sumOf { it.second }
            if (others > 0) top10 + Pair("DLL", others) else top10
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val facultyStats = students.map { list ->
        if (list.isEmpty()) emptyList() else {
            list.groupBy { it.faculty }.map { it.key to it.value.size }.sortedByDescending { it.second }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val departmentStats = students.map { list ->
        if (list.isEmpty()) emptyList() else {
            val grouped = list.groupBy { it.department }.map { it.key to it.value.size }.sortedByDescending { it.second }
            val top10 = grouped.take(10)
            val others = grouped.drop(10).sumOf { it.second }
            if (others > 0) top10 + Pair("DLL", others) else top10
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun updateName(input: String) { name = input; nameError = null }
    fun updateEmail(input: String) { email = input.lowercase().trim(); emailError = null }
    fun updateSchool(input: String) { school = input; schoolError = null }
    fun updateProvince(input: String) { province = input; provinceError = null }
    fun updateFaculty(input: String) { faculty = input; facultyError = null }
    fun updateDepartment(input: String) { department = input; departmentError = null }
    fun updateGender(input: String) { gender = input }

    private fun validateInput(): Boolean {
        var isValid = true
        if (name.isBlank()) { nameError = "Nama wajib diisi"; isValid = false }
        if (school.isBlank()) { schoolError = "Sekolah wajib diisi"; isValid = false }
        if (province.isBlank()) { provinceError = "Provinsi wajib diisi"; isValid = false }
        if (faculty.isBlank()) { facultyError = "Fakultas wajib diisi"; isValid = false }
        if (department.isBlank()) { departmentError = "Prodi wajib diisi"; isValid = false }
        if (!email.endsWith("@student.its.ac.id")) { emailError = "Gunakan email resmi ITS"; isValid = false }
        return isValid
    }

    fun saveStudent() {
        if (!validateInput()) return
        viewModelScope.launch {
            val student = Student(
                id = editingStudent?.id ?: 0,
                name = name,
                email = email,
                school = school,
                province = province,
                faculty = faculty,
                department = department,
                gender = gender
            )
            if (editingStudent == null) dao.insertStudent(student) else dao.updateStudent(student)
            clearForm()
        }
    }

    fun editStudent(student: Student) {
        editingStudent = student
        name = student.name
        email = student.email
        school = student.school
        province = student.province
        faculty = student.faculty
        department = student.department
        gender = student.gender
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            dao.deleteStudent(student)
            if (editingStudent?.id == student.id) clearForm()
        }
    }

    fun clearForm() {
        name = ""; email = ""; school = ""; province = ""; faculty = ""; department = ""; gender = "Laki-laki"
        editingStudent = null
        nameError = null; emailError = null; schoolError = null; provinceError = null; facultyError = null; departmentError = null
    }
}