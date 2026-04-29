// Janaath Vijithavarnan
// W1979142

package com.example.dermoinspect.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermoinspect.R
import com.example.dermoinspect.ui.theme.PrimaryCyan
import com.example.dermoinspect.ui.theme.PrimaryNavy




@Composable
fun HomeContentScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryCyan),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {


            // App Name
            Image(
                painter = painterResource(id = R.drawable.logocyanbackground),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .width(400.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(15.dp))

            // This is the slogan
            Text(
                text = stringResource(R.string.slogan_1),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.slogan_2),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.slogan_3),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy,
                textAlign = TextAlign.Center
            )
        }
    }
}