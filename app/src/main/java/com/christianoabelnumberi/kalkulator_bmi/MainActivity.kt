package com.christianoabelnumberi.kalkulator_bmi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BMIApp()
        }
    }
}

@Composable
fun BMIApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "input") {

        composable("input") {
            InputScreen(navController)
        }

        composable("result/{bmi}") { backStackEntry ->
            val bmi = backStackEntry.arguments
                ?.getString("bmi")
                ?.toDoubleOrNull() ?: 0.0

            ResultScreen(navController, bmi)
        }
    }
}

@Composable
fun InputScreen(navController: NavHostController) {
    var berat by remember { mutableStateOf("") }
    var tinggi by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ✅ GAMBAR
        Image(
            painter = painterResource(id = R.drawable.kalkulatorbmi),
            contentDescription = "Gambar BMI",
            modifier = Modifier
                .size(150.dp)
                .padding(10.dp)
        )

        Text("Kalkulator BMI", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = berat,
            onValueChange = { berat = it },
            label = { Text("Berat (kg)") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = tinggi,
            onValueChange = { tinggi = it },
            label = { Text("Tinggi (cm)") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (berat.isEmpty() || tinggi.isEmpty()) {
                error = "Input tidak boleh kosong!"
            } else {
                val b = berat.toDouble()
                val t = tinggi.toDouble() / 100
                val bmi = b / t.pow(2)

                navController.navigate("result/$bmi")
            }
        }) {
            Text("Hitung BMI")
        }
    }
}

@Composable
fun ResultScreen(navController: NavHostController, bmi: Double) {

    val context = LocalContext.current

    val kategori = when {
        bmi < 18.5 -> "Kurus"
        bmi < 25 -> "Normal"
        bmi < 30 -> "Gemuk"
        else -> "Obesitas"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Hasil BMI", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text("BMI: %.2f".format(bmi))
        Text("Kategori: $kategori")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Kembali")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=BMI")
            )
            context.startActivity(intent)
        }) {
            Text("Cari Info BMI")
        }
    }
}