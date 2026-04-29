// Janaath Vijithavarnan
// W1979142.

package com.example.dermoinspect.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermoinspect.R
import com.example.dermoinspect.ui.theme.PrimaryCyan
import com.example.dermoinspect.ui.theme.PrimaryNavy
import com.example.dermoinspect.ui.theme.CardLight

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.about_us),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryNavy,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Who We Are Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardLight)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.who_we_are),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = stringResource(R.string.who_we_are_text),
                    fontSize = 16.sp,
                    color = PrimaryNavy,
                    lineHeight = 24.sp
                )
            }
        }

        // What We Do Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardLight)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.what_we_do),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = stringResource(R.string.what_we_do_text),
                    fontSize = 16.sp,
                    color = PrimaryNavy,
                    lineHeight = 24.sp
                )
            }
        }

        // This is the Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardLight)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.our_objective),
                    fontSize = 16.sp,
                    color = PrimaryNavy,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}