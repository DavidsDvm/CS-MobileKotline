package com.test.tadia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.tadia.R
import com.test.tadia.data.User

@Composable
fun HomeScreen(
    user: User,
    onLogout: () -> Unit,
    onNavigateToReservations: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(BottomTab.Home) }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                selected = selectedTab,
                onSelected = { selectedTab = it }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header image (Dog)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dog_header),
                    contentDescription = "Dog header",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(16.dp))

            // Welcome title
            Text(
                text = "Bienvenido a TadIA",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // Menu cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuCard(
                    title = "Agendamiento de espacios",
                    onClick = onNavigateToReservations
                )
                Spacer(Modifier.height(24.dp))
                MenuCard(
                    title = "TadIA- Chat con\nInteligencia artificial",
                    onClick = { /* TODO: navigate */ }
                )
                Spacer(Modifier.height(24.dp))
                MenuCard(
                    title = "Crea tu noticia",
                    onClick = { /* TODO: navigate */ }
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MenuCard(
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
        border = ButtonDefaults.outlinedButtonBorder
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            user = User(
                email = "test@example.com",
                name = "Test User"
            ),
            onLogout = {},
            onNavigateToReservations = {}
        )
    }
}

