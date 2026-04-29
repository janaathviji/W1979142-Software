// Janaath Vijithavarnan
// W1979142

package com.example.dermoinspect.ui.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dermoinspect.R
import com.example.dermoinspect.data.model.User
import com.example.dermoinspect.data.repository.AuthRepository
import com.example.dermoinspect.ui.theme.PrimaryCyan
import com.example.dermoinspect.ui.theme.PrimaryNavy
import com.example.dermoinspect.ui.theme.SecondaryBlue
import com.example.dermoinspect.ui.theme.CardLight
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    // These are the editable fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // This is the image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
        }
    }

    // This is to load user data
    LaunchedEffect(Unit) {
        scope.launch {
            val currentUser = authRepository.getCurrentUser()
            if (currentUser != null) {
                authRepository.getUserData(currentUser.uid).fold(
                    onSuccess = { userData ->
                        user = userData
                        firstName = userData.firstName
                        lastName = userData.lastName
                        profileImageUri = userData.profileImageUrl?.let { Uri.parse(it) }
                        isLoading = false
                    },
                    onFailure = {
                        isLoading = false
                    }
                )
            } else {
                isLoading = false
            }
        }
    }

    // This is to save the profile changes
    fun saveProfile() {
        if (firstName.isBlank() || lastName.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        isSaving = true
        scope.launch {
            user?.let { currentUser ->
                val updatedUser = currentUser.copy(
                    firstName = firstName,
                    lastName = lastName,
                    profileImageUrl = profileImageUri?.toString()
                )

                authRepository.updateUserProfile(updatedUser).fold(
                    onSuccess = {
                        user = updatedUser
                        isEditing = false
                        isSaving = false
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { exception ->
                        isSaving = false
                        Toast.makeText(context, "Failed to update: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // This is the header with Edit button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profile),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )

            if (!isLoading && !isEditing) {
                IconButton(
                    onClick = { isEditing = true },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SecondaryBlue)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.Black
                    )
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SecondaryBlue)
            }
        } else {
            // This is the profile picture
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (isEditing) {
                                    imagePickerLauncher.launch("image/*")
                                }
                            },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(SecondaryBlue)
                            .clickable {
                                if (isEditing) {
                                    imagePickerLauncher.launch("image/*")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            tint = Color.Black,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                // This is the edit icon
                if (isEditing) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryNavy)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Change Picture",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (isEditing) {
                Text(
                    text = "Tap to change picture",
                    fontSize = 12.sp,
                    color = PrimaryNavy.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // This is the user Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardLight)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    if (isEditing) {
                        // This is the editable First Name
                        Text(
                            text = "First Name:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.LightGray,
                                focusedBorderColor = SecondaryBlue,
                                unfocusedBorderColor = PrimaryNavy.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )

                        // this is the editable Last Name
                        Text(
                            text = "Last Name:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.LightGray,
                                focusedBorderColor = SecondaryBlue,
                                unfocusedBorderColor = PrimaryNavy.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )
                    } else {
                        // First Name
                        Text(
                            text = "Firstname:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                        Text(
                            text = user?.firstName ?: "N/A",
                            fontSize = 18.sp,
                            color = PrimaryNavy,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Last Name
                        Text(
                            text = "Lastname:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                        Text(
                            text = user?.lastName ?: "N/A",
                            fontSize = 18.sp,
                            color = PrimaryNavy,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Email
                    Text(
                        text = "Email:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                    Text(
                        text = user?.email ?: "N/A",
                        fontSize = 18.sp,
                        color = PrimaryNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // this is the Action Buttons
            if (isEditing) {
                // Save and Cancel buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // this is the Cancel Button
                    OutlinedButton(
                        onClick = {
                            // Reset to original values
                            firstName = user?.firstName ?: ""
                            lastName = user?.lastName ?: ""
                            profileImageUri = user?.profileImageUrl?.let { Uri.parse(it) }
                            isEditing = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PrimaryNavy
                        ),
                        enabled = !isSaving
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Save Button
                    Button(
                        onClick = { saveProfile() },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = "Save",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            } else {
                // this is the Logout Button
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue)
                ) {
                    Text(
                        text = stringResource(R.string.logout),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}