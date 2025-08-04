package com.example.cropmonitor.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cropmonitor.AppContainer
import com.example.cropmonitor.R
import com.example.cropmonitor.viewmodels.LoginViewModel
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory

@Composable
fun AccesoScreen(
    onLoginSuccess: () -> Unit,
    onCrearCuentaClick: () -> Unit,
    appContainer: AppContainer
) {
    val viewModel: LoginViewModel = viewModel(factory = appContainer.modulosViewModelFactory)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.mountain_river_background),
            contentDescription = "Fondo de montañas y río",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(text = "Acceder", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = viewModel.correo,
                onValueChange = { viewModel.correo = it },
                label = { Text("Correo", color = Color.White) },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White, unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White, unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White, focusedTextColor = Color.White, unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.contrasena,
                onValueChange = { viewModel.contrasena = it },
                label = { Text("Contraseña", color = Color.White) },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White, unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White, unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White, focusedTextColor = Color.White, unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            viewModel.errorMensaje?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(bottom = 16.dp))
            }
            Button(
                onClick = { viewModel.login(onLoginSuccess) },
                enabled = !viewModel.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF47ADDE)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(text = "Acceder", fontSize = 18.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onCrearCuentaClick,
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(Color(0xFFA8E0B9))),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFA8E0B9)),
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp)
            ) {
                Text(text = "Crear cuenta", fontSize = 18.sp)
            }
        }
    }
}