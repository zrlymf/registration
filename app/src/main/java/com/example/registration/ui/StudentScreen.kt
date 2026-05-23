package com.example.registration.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.registration.data.Student
import com.example.registration.ui.theme.*
import com.example.registration.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    val students by viewModel.students.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val genderStats by viewModel.genderStats.collectAsState()
    val provinceStats by viewModel.provinceStats.collectAsState()
    val facultyStats by viewModel.facultyStats.collectAsState()
    val departmentStats by viewModel.departmentStats.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showFormDialog by remember { mutableStateOf(false) }
    var studentToDelete by remember { mutableStateOf<Student?>(null) }

    var provinceExpanded by remember { mutableStateOf(false) }
    var facultyExpanded by remember { mutableStateOf(false) }
    var departmentExpanded by remember { mutableStateOf(false) }

    val uniqueProvincesCount = students.map { it.province.trim().uppercase() }.distinct().size

    val indonesiaProvinces = listOf(
        "Aceh", "Sumatera Utara", "Sumatera Barat", "Riau", "Kepulauan Riau",
        "Jambi", "Bengkulu", "Sumatera Selatan", "Kepulauan Bangka Belitung", "Lampung",
        "DKI Jakarta", "Jawa Barat", "Banten", "Jawa Tengah", "DI Yogyakarta", "Jawa Timur",
        "Bali", "Nusa Tenggara Barat", "Nusa Tenggara Timur",
        "Kalimantan Barat", "Kalimantan Tengah", "Kalimantan Selatan", "Kalimantan Timur", "Kalimantan Utara",
        "Sulawesi Utara", "Gorontalo", "Sulawesi Tengah", "Sulawesi Barat", "Sulawesi Selatan", "Sulawesi Tenggara",
        "Maluku", "Maluku Utara", "Papua", "Papua Barat", "Papua Selatan", "Papua Tengah", "Papua Pegunungan", "Papua Barat Daya"
    )

    val faculties = listOf(
        "FTSPK", "FSAD", "FTEIC", "FTK", "FDKBD", "FKK", "FTIRS", "VOKASI"
    )

    val departments = listOf(
        "Fisika", "Kimia", "Matematika", "Statistika", "Biologi", "Sains Aktuaria",
        "Teknik Mesin", "Teknik Kimia", "Teknik Fisika", "Teknik Sistem dan Industri",
        "Teknik Material dan Metalurgi", "Teknik Pangan", "Teknik Sipil", "Arsitektur",
        "Teknik Lingkungan", "Teknik Geomatika", "Perencanaan Wilayah dan Kota", "Teknik Geofisika",
        "Teknik Perkapalan", "Teknik Sistem Perkapalan", "Teknik Kelautan", "Teknik Transportasi Laut", "Teknik Lepas Pantai",
        "Teknik Elektro", "Teknik Informatika", "Sistem Informasi", "Teknik Komputer", "Teknik Biomedik", "Teknologi Informasi", "Teknik Telekomunikasi",
        "Desain Produk", "Desain Interior", "Desain Komunikasi Visual", "Manajemen Bisnis", "Studi Pembangunan", "Sains Komunikasi",
        "Teknik Infrastruktur Sipil", "Teknik Mesin Industri", "Teknik Elektro Otomasi", "Teknik Kimia Industri", "Teknik Instrumentasi", "Statistika Bisnis",
        "Teknologi Kedokteran", "Kedokteran", "Profesi Dokter"
    ).sorted()

    val filteredProvinces = indonesiaProvinces.filter {
        it.contains(viewModel.province, ignoreCase = true)
    }

    val filteredFaculties = faculties.filter {
        it.contains(viewModel.faculty, ignoreCase = true)
    }

    val filteredDepartments = departments.filter {
        it.contains(viewModel.department, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = PureWhite,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Outlined.BarChart, contentDescription = null) },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PureWhite,
                        selectedTextColor = NavyBlue,
                        indicatorColor = NavyBlue,
                        unselectedIconColor = Charcoal,
                        unselectedTextColor = Charcoal
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Outlined.Group, contentDescription = null) },
                    label = { Text("Data Mahasiswa") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PureWhite,
                        selectedTextColor = NavyBlue,
                        indicatorColor = NavyBlue,
                        unselectedIconColor = Charcoal,
                        unselectedTextColor = Charcoal
                    )
                )
            }
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = { showFormDialog = true },
                    containerColor = NavyBlue,
                    contentColor = PureWhite,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        },
        containerColor = BoneWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.School, contentDescription = null, tint = NavyBlue, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Sistem Registrasi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = NavyBlue)
                    Text("Manajemen Data Mahasiswa Baru", style = MaterialTheme.typography.bodySmall, color = Charcoal)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (selectedTab == 0) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            MiniCards(totalStudents = students.size, totalProvinces = uniqueProvincesCount)
                        }
                        item {
                            GenderStatistik(maleCount = genderStats.first, femaleCount = genderStats.second, total = students.size)
                        }
                        item {
                            FacultyStatistik(facultyStats = facultyStats, total = students.size)
                        }
                        item {
                            DepartmentStatistik(departmentStats = departmentStats, total = students.size)
                        }
                        item {
                            ProvinceStatistik(provinceStats = provinceStats, total = students.size)
                        }
                    }
                    Text(
                        text = "Visualisasi data diproses secara real-time dari basis data lokal.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
            } else {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Cari nama mahasiswa...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Charcoal) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = PureWhite,
                        focusedContainerColor = PureWhite,
                        focusedBorderColor = NavyBlue
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Daftar Mahasiswa Aktif", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NavyBlue)
                    Text("Total: ${students.size}", style = MaterialTheme.typography.bodySmall, color = Charcoal, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (students.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Group, contentDescription = null, modifier = Modifier.size(72.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Daftar Mahasiswa Kosong", fontWeight = FontWeight.Bold, color = Charcoal)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(students) { student ->
                            StudentCard(
                                student = student,
                                onEdit = {
                                    viewModel.editStudent(student)
                                    showFormDialog = true
                                },
                                onDelete = { studentToDelete = student }
                            )
                        }
                    }
                }
            }
        }
    }

    if (studentToDelete != null) {
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            title = {
                Text("Konfirmasi Hapus", fontWeight = FontWeight.Bold, color = NavyBlue)
            },
            text = {
                Text("Apakah Anda yakin ingin menghapus data atas nama ${studentToDelete?.name}?", color = Charcoal)
            },
            confirmButton = {
                TextButton(onClick = {
                    studentToDelete?.let { viewModel.deleteStudent(it) }
                    studentToDelete = null
                }) {
                    Text("Hapus", color = RedDelete, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) {
                    Text("Batal", color = Charcoal, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showFormDialog) {
        Dialog(
            onDismissRequest = {
                showFormDialog = false
                provinceExpanded = false
                facultyExpanded = false
                departmentExpanded = false
                if (viewModel.editingStudent == null) {
                    viewModel.clearForm()
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(24.dp),
                color = PureWhite,
                tonalElevation = 4.dp
            ) {
                LazyColumn(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (viewModel.editingStudent == null) "Tambah Data Baru" else "Edit Data",
                                fontWeight = FontWeight.Bold,
                                color = NavyBlue,
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(onClick = {
                                showFormDialog = false
                                viewModel.clearForm()
                            }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Charcoal)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        OutlinedTextField(
                            value = viewModel.name,
                            onValueChange = { viewModel.updateName(it) },
                            label = { Text("Nama Lengkap") },
                            placeholder = { Text("e.g., Alice Wijaya") },
                            leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = null) },
                            isError = viewModel.nameError != null,
                            supportingText = { viewModel.nameError?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, focusedLabelColor = NavyBlue, cursorColor = NavyBlue)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = viewModel.email,
                            onValueChange = { viewModel.updateEmail(it) },
                            label = { Text("Email ITS Resmi") },
                            placeholder = { Text("nrp@student.its.ac.id") },
                            leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null) },
                            isError = viewModel.emailError != null,
                            supportingText = { viewModel.emailError?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, focusedLabelColor = NavyBlue, cursorColor = NavyBlue)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = viewModel.school,
                            onValueChange = { viewModel.updateSchool(it) },
                            label = { Text("Asal Sekolah (SMA/SMK/MA)") },
                            placeholder = { Text("e.g., SMAN 1 Surabaya") },
                            leadingIcon = { Icon(Icons.Default.Apartment, contentDescription = null) },
                            isError = viewModel.schoolError != null,
                            supportingText = { viewModel.schoolError?.let { Text(it) } },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyBlue, focusedLabelColor = NavyBlue, cursorColor = NavyBlue)
                        )
                    }

                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = viewModel.province,
                                onValueChange = {
                                    viewModel.updateProvince(it)
                                    provinceExpanded = true
                                },
                                label = { Text("Provinsi Asal") },
                                placeholder = { Text("e.g., Jawa Timur") },
                                leadingIcon = { Icon(Icons.Default.Map, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { provinceExpanded = !provinceExpanded }) {
                                        Icon(if (provinceExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                },
                                isError = viewModel.provinceError != null,
                                supportingText = { viewModel.provinceError?.let { Text(it) } },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { if (it.isFocused) provinceExpanded = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NavyBlue,
                                    focusedLabelColor = NavyBlue,
                                    cursorColor = NavyBlue
                                )
                            )

                            DropdownMenu(
                                expanded = provinceExpanded && filteredProvinces.isNotEmpty(),
                                onDismissRequest = { provinceExpanded = false },
                                properties = androidx.compose.ui.window.PopupProperties(focusable = false),
                                modifier = Modifier
                                    .background(PureWhite)
                                    .heightIn(max = 250.dp)
                            ) {
                                filteredProvinces.forEach { provinceName ->
                                    DropdownMenuItem(
                                        text = { Text(provinceName, color = Charcoal) },
                                        onClick = {
                                            viewModel.updateProvince(provinceName)
                                            provinceExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = viewModel.faculty,
                                onValueChange = {
                                    viewModel.updateFaculty(it)
                                    facultyExpanded = true
                                },
                                label = { Text("Fakultas") },
                                placeholder = { Text("e.g., FTEIC") },
                                leadingIcon = { Icon(Icons.Default.Domain, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { facultyExpanded = !facultyExpanded }) {
                                        Icon(if (facultyExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                },
                                isError = viewModel.facultyError != null,
                                supportingText = { viewModel.facultyError?.let { Text(it) } },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { if (it.isFocused) facultyExpanded = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NavyBlue,
                                    focusedLabelColor = NavyBlue,
                                    cursorColor = NavyBlue
                                )
                            )

                            DropdownMenu(
                                expanded = facultyExpanded && filteredFaculties.isNotEmpty(),
                                onDismissRequest = { facultyExpanded = false },
                                properties = androidx.compose.ui.window.PopupProperties(focusable = false),
                                modifier = Modifier
                                    .background(PureWhite)
                                    .heightIn(max = 250.dp)
                            ) {
                                filteredFaculties.forEach { facultyName ->
                                    DropdownMenuItem(
                                        text = { Text(facultyName, color = Charcoal) },
                                        onClick = {
                                            viewModel.updateFaculty(facultyName)
                                            facultyExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = viewModel.department,
                                onValueChange = {
                                    viewModel.updateDepartment(it)
                                    departmentExpanded = true
                                },
                                label = { Text("Program Studi") },
                                placeholder = { Text("e.g., Teknik Informatika") },
                                leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { departmentExpanded = !departmentExpanded }) {
                                        Icon(if (departmentExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                },
                                isError = viewModel.departmentError != null,
                                supportingText = { viewModel.departmentError?.let { Text(it) } },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { if (it.isFocused) departmentExpanded = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NavyBlue,
                                    focusedLabelColor = NavyBlue,
                                    cursorColor = NavyBlue
                                )
                            )

                            DropdownMenu(
                                expanded = departmentExpanded && filteredDepartments.isNotEmpty(),
                                onDismissRequest = { departmentExpanded = false },
                                properties = androidx.compose.ui.window.PopupProperties(focusable = false),
                                modifier = Modifier
                                    .background(PureWhite)
                                    .heightIn(max = 250.dp)
                            ) {
                                filteredDepartments.forEach { departmentName ->
                                    DropdownMenuItem(
                                        text = { Text(departmentName, color = Charcoal) },
                                        onClick = {
                                            viewModel.updateDepartment(departmentName)
                                            departmentExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text("Jenis Kelamin", style = MaterialTheme.typography.bodySmall, color = Charcoal, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            listOf("Laki-laki", "Perempuan").forEach { text ->
                                val isSelected = viewModel.gender == text
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) NavyBlue else BoneWhite)
                                        .clickable { viewModel.updateGender(text) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text, color = if (isSelected) PureWhite else Charcoal, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.saveStudent()
                                if (viewModel.nameError == null && viewModel.emailError == null && viewModel.schoolError == null && viewModel.provinceError == null && viewModel.facultyError == null && viewModel.departmentError == null) {
                                    showFormDialog = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                        ) {
                            Text("Simpan Data", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MiniCards(totalStudents: Int, totalProvinces: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ElevatedCard(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.elevatedCardColors(containerColor = PureWhite),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(NavyBlue.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Group, contentDescription = null, tint = NavyBlue, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Total Mahasiswa", color = Charcoal, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("$totalStudents", fontWeight = FontWeight.ExtraBold, color = NavyBlue, style = MaterialTheme.typography.headlineMedium)
            }
        }

        ElevatedCard(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.elevatedCardColors(containerColor = PureWhite),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(NavyBlue.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Map, contentDescription = null, tint = NavyBlue, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Jangkauan Provinsi", color = Charcoal, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("$totalProvinces", fontWeight = FontWeight.ExtraBold, color = NavyBlue, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

@Composable
fun GenderStatistik(maleCount: Int, femaleCount: Int, total: Int) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(containerColor = PureWhite), shape = RoundedCornerShape(20.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Demografi Gender", fontWeight = FontWeight.Bold, color = NavyBlue, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            if (total == 0) Text("Belum ada data.", color = Color.Gray, style = MaterialTheme.typography.bodySmall) else {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(90.dp)) {
                        val maleAngle = 360f * (maleCount.toFloat() / total)
                        drawArc(color = ChartBlue, startAngle = -90f, sweepAngle = maleAngle, useCenter = true)
                        drawArc(color = ChartPink, startAngle = -90f + maleAngle, sweepAngle = 360f - maleAngle, useCenter = true)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(start = 24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(ChartBlue)); Spacer(modifier = Modifier.width(8.dp))
                            Text("Laki-Laki ($maleCount)", style = MaterialTheme.typography.bodySmall, color = Charcoal, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(ChartPink)); Spacer(modifier = Modifier.width(8.dp))
                            Text("Perempuan ($femaleCount)", style = MaterialTheme.typography.bodySmall, color = Charcoal, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FacultyStatistik(facultyStats: List<Pair<String, Int>>, total: Int) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = PureWhite),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Distribusi Fakultas", fontWeight = FontWeight.Bold, color = NavyBlue, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (total == 0) {
                Text("Belum ada data.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val chartColors = listOf(
                        ChartBlue, ChartCyan, ChartTeal, ChartGreen, ChartYellow,
                        ChartOrange, ChartDeepOrange, ChartRed, ChartPink, ChartPurple
                    )

                    Canvas(modifier = Modifier.size(100.dp)) {
                        var currentAngle = -90f
                        facultyStats.forEachIndexed { index, data ->
                            val sweepAngle = 360f * (data.second.toFloat() / total)
                            val sliceColor = if (data.first == "DLL") ChartGrey else chartColors[index % chartColors.size]
                            drawArc(color = sliceColor, startAngle = currentAngle, sweepAngle = sweepAngle, useCenter = true)
                            currentAngle += sweepAngle
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(start = 24.dp)
                    ) {
                        facultyStats.forEachIndexed { index, data ->
                            val dotColor = if (data.first == "DLL") ChartGrey else chartColors[index % chartColors.size]
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(dotColor))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${data.first} (${data.second})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Charcoal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DepartmentStatistik(departmentStats: List<Pair<String, Int>>, total: Int) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = PureWhite),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Top 10 Program Studi", fontWeight = FontWeight.Bold, color = NavyBlue, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (total == 0) {
                Text("Belum ada data.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val chartColors = listOf(
                        ChartBlue, ChartCyan, ChartTeal, ChartGreen, ChartYellow,
                        ChartOrange, ChartDeepOrange, ChartRed, ChartPink, ChartPurple
                    )

                    Canvas(modifier = Modifier.size(100.dp)) {
                        var currentAngle = -90f
                        departmentStats.forEachIndexed { index, data ->
                            val sweepAngle = 360f * (data.second.toFloat() / total)
                            val sliceColor = if (data.first == "DLL") ChartGrey else chartColors[index % chartColors.size]
                            drawArc(color = sliceColor, startAngle = currentAngle, sweepAngle = sweepAngle, useCenter = true)
                            currentAngle += sweepAngle
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(start = 24.dp)
                    ) {
                        departmentStats.forEachIndexed { index, data ->
                            val dotColor = if (data.first == "DLL") ChartGrey else chartColors[index % chartColors.size]
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(dotColor))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${data.first} (${data.second})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Charcoal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProvinceStatistik(provinceStats: List<Pair<String, Int>>, total: Int) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = PureWhite),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Peta Distribusi Provinsi Asal", fontWeight = FontWeight.Bold, color = NavyBlue, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))

            if (total == 0) {
                Text("Belum ada data.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val chartColors = listOf(
                        ChartBlue, ChartCyan, ChartTeal, ChartGreen, ChartYellow,
                        ChartOrange, ChartDeepOrange, ChartRed, ChartPink, ChartPurple
                    )

                    Canvas(modifier = Modifier.size(100.dp)) {
                        var currentAngle = -90f
                        provinceStats.forEachIndexed { index, data ->
                            val sweepAngle = 360f * (data.second.toFloat() / total)
                            val sliceColor = if (data.first == "DLL") ChartGrey else chartColors[index % chartColors.size]
                            drawArc(color = sliceColor, startAngle = currentAngle, sweepAngle = sweepAngle, useCenter = true)
                            currentAngle += sweepAngle
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(start = 24.dp)
                    ) {
                        provinceStats.forEachIndexed { index, data ->
                            val dotColor = if (data.first == "DLL") ChartGrey else chartColors[index % chartColors.size]
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(dotColor))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${data.first} (${data.second})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Charcoal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentCard(student: Student, onEdit: () -> Unit, onDelete: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(containerColor = PureWhite), shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.padding(14.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(if (student.gender == "Laki-laki") ChartBlue else ChartPink), contentAlignment = Alignment.Center) {
                Text(student.name.firstOrNull()?.uppercase() ?: "?", color = PureWhite, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(student.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = NavyBlue)
                Text("${student.department} • ${student.faculty}", style = MaterialTheme.typography.bodySmall, color = Charcoal, fontWeight = FontWeight.SemiBold)
                Text(student.email, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Edit, contentDescription = null, tint = Charcoal, modifier = Modifier.size(18.dp)) }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) { Icon(Icons.Default.Delete, contentDescription = null, tint = RedDelete, modifier = Modifier.size(18.dp)) }
        }
    }
}