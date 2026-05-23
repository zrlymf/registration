package com.example.registration.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registration.R
import com.example.registration.ui.theme.*
import com.example.registration.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BoneWhite)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.its_logo),
            contentDescription = "Logo Kampus",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Portal Admin Ditmawa",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = NavyBlue,
            fontSize = 24.sp
        )
        Text(
            text = "Sistem Registrasi Mahasiswa Baru",
            style = MaterialTheme.typography.bodySmall,
            color = Charcoal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = PureWhite),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Masuk ke Akun Anda",
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = { Text("Email Admin") },
                    placeholder = { Text("e.g., admin.ditmawa@its.ac.id") },
                    leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyBlue,
                        focusedLabelColor = NavyBlue,
                        cursorColor = NavyBlue
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                            val icon = if (viewModel.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            Icon(icon, contentDescription = null)
                        }
                    },
                    visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyBlue,
                        focusedLabelColor = NavyBlue,
                        cursorColor = NavyBlue
                    )
                )

                if (viewModel.loginError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = viewModel.loginError!!,
                        color = RedDelete,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (viewModel.performLogin()) {
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                ) {
                    Text(text = "Login ke Dashboard", fontWeight = FontWeight.Bold, color = PureWhite)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1.5f))

        Text(
            text = "Lupa password? Hubungi DPTSI.",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}