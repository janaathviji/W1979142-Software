// Janaath Vijithavarnan
// W1979142

// This file handles app navigation and the main layout structure
// It displays a top bar with camera, logo, and profile icons, and a bottom navigation bar with four tabs
package com.example.dermoinspect.ui.main

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dermoinspect.R
import com.example.dermoinspect.ui.about.AboutScreen
import com.example.dermoinspect.ui.camera.CameraFlowScreen
import com.example.dermoinspect.ui.history.HistoryScreen
import com.example.dermoinspect.ui.home.HomeContentScreen
import com.example.dermoinspect.ui.information.InformationScreen
import com.example.dermoinspect.ui.information.DiseaseDetailScreen
import com.example.dermoinspect.ui.profile.ProfileScreen
import com.example.dermoinspect.ui.results.ResultsScreen
import com.example.dermoinspect.ui.theme.PrimaryCyan
import com.example.dermoinspect.ui.theme.PrimaryNavy
import com.example.dermoinspect.data.model.ScanHistory

// ✅ ViewModel to hold analysis results
import androidx.lifecycle.ViewModel
import com.example.dermoinspect.data.model.DiseaseInformation

data class AnalysisResult(
    val imageUri: Uri,
    val primaryDisease: String,
    val confidence: Float,
    val topPredictions: List<Pair<String, Float>>
)

class SharedAnalysisViewModel : ViewModel() {
    var analysisResult: AnalysisResult? = null
    var selectedDisease: DiseaseInformation? = null
}

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Information : Screen("information", "Information", Icons.Default.Info)
    object About : Screen("about", "About", Icons.Default.Group)
    object History : Screen("history", "History", Icons.Default.History)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object DiseaseDetail : Screen("disease_detail", "Disease Detail", Icons.Default.Info)
}


@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // This is the SharedAnalysisViewModel shares data between screens
    // like analysis results and disease information.
    val sharedViewModel: SharedAnalysisViewModel = viewModel()
    var selectedHistoryScan by remember { mutableStateOf<ScanHistory?>(null) }


    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Information,
        Screen.About,
        Screen.History
    )

    val currentRoute = currentDestination?.route
    val showBottomNav = currentRoute in bottomNavItems.map { it.route }
    val showTopBar = currentRoute != "camera" && currentRoute != "results" && currentRoute != "disease_detail"

    Scaffold(
        containerColor = PrimaryCyan,
        topBar = {
            if (showTopBar) {
                // This is the top part with Camera, Logo, Profile
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryCyan,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // This is the Camera Button
                        Image(
                            painter = painterResource(id = R.drawable.cameraicon),
                            contentDescription = "Camera",
                            modifier = Modifier
                                .size(70.dp)
                                .clickable {
                                    navController.navigate("camera")
                                },
                            contentScale = ContentScale.Fit
                        )

                        // Logo
                        Image(
                            painter = painterResource(id = R.drawable.logocyanbackground),
                            contentDescription = "Home",
                            modifier = Modifier
                                .height(120.dp)
                                .width(240.dp),
                            contentScale = ContentScale.Fit
                        )

                        // This is the Profile Button
                        Image(
                            painter = painterResource(id = R.drawable.profileicon),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(65.dp)
                                .clickable {
                                    navController.navigate(Screen.Profile.route) {
                                        launchSingleTop = true
                                    }
                                },
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (showBottomNav) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 65.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .width(360.dp)
                            .height(76.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        shadowElevation = 12.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            bottomNavItems.forEach { screen ->
                                val isSelected =
                                    currentDestination?.hierarchy?.any {
                                        it.route == screen.route
                                    } == true

                                Column(
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                        .padding(vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        modifier = Modifier.size(26.dp),
                                        tint = if (isSelected) PrimaryCyan else Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = screen.title,
                                        fontSize = 11.sp,
                                        color = if (isSelected) PrimaryNavy else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->

        // The NavHost manages navigation between all app screens and
        // conditionally shows or hides the top and bottom bars based on the current screen.
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(
                if (showBottomNav && showTopBar) paddingValues
                else if (showTopBar) PaddingValues(top = paddingValues.calculateTopPadding())
                else PaddingValues(0.dp)
            )
        ) {
            composable(Screen.Home.route) {
                HomeContentScreen()
            }

            // This is the Information screen
            composable(Screen.Information.route) {
                InformationScreen(
                    onDiseaseClick = { disease ->
                        sharedViewModel.selectedDisease = disease
                        navController.navigate(Screen.DiseaseDetail.route)
                    }
                )
            }

            composable(Screen.About.route) {
                AboutScreen()
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    onScanClick = { scan ->
                        selectedHistoryScan = scan
                        navController.navigate("history_results")
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = onLogout)
            }

            // This is the Camera flow
            composable("camera") {
                CameraFlowScreen(
                    onAnalyzeImage = { uri, diseaseName, confidence, topPredictions ->
                        sharedViewModel.analysisResult = AnalysisResult(
                            imageUri = uri,
                            primaryDisease = diseaseName,
                            confidence = confidence,
                            topPredictions = topPredictions
                        )

                        navController.navigate("results") {
                            popUpTo("camera") { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            // This is the Results screen
            composable("results") {
                val result = sharedViewModel.analysisResult

                if (result != null) {
                    ResultsScreen(
                        imageUri = result.imageUri,
                        diseaseType = result.primaryDisease,
                        confidence = result.confidence,
                        topPredictions = result.topPredictions,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PrimaryCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No analysis data available",
                                fontSize = 18.sp,
                                color = PrimaryNavy
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate("camera") }
                            ) {
                                Text("Take New Photo")
                            }
                        }
                    }
                }
            }

            // This is the Disease detail screen
            composable(Screen.DiseaseDetail.route) {
                val disease = sharedViewModel.selectedDisease

                if (disease != null) {
                    DiseaseDetailScreen(
                        disease = disease,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    // This is the fallback if no disease selected
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PrimaryCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Disease information not available",
                                fontSize = 18.sp,
                                color = PrimaryNavy
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate(Screen.Information.route) }
                            ) {
                                Text("Back to Information")
                            }
                        }
                    }
                }
            }

            composable("history_results") {
                selectedHistoryScan?.let { scan ->

                    ResultsScreen(
                        imageUri = Uri.parse(scan.imageUrl),
                        diseaseType = scan.diseaseType,
                        confidence = scan.confidence,
                        topPredictions = scan.topPredictions.map {
                            it.diseaseName to it.confidence
                        },
                        onBack = {
                            navController.popBackStack()
                        },
                        isFromHistory = true
                    )
                }
            }
        }
    }
}




