package com.example.internationalisation_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.activity.viewModels
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import kotlin.getValue

// --- VIEW MODEL (Minimal for Plural Demo) ---
class InventoryViewModel : ViewModel() {
    // State to drive the plural example (0, 1, or 5)
    private val counts = listOf(0, 1, 5)
    var currentCountIndex by mutableIntStateOf(2) // Start at 5
        private set

    val currentCount: Int
        get() = counts[currentCountIndex]

    fun cycleCount() {
        currentCountIndex = (currentCountIndex + 1) % counts.size
    }
}

// --- COMPOSE UI COMPONENTS ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternationalisationScreen(viewModel: InventoryViewModel) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.welcome_title)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // 1. String Resource Example (Slide 5)
            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.headlineMedium
            )

            // 2. Plural Resource Example (Slide 8)
            CartCounter(viewModel = viewModel)
//
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            // 3. Manual RTL Check Example (Slide 11)
            ManualRTLCheck()

            // 4. Standard Layout (Automatic RTL) Example (Slide 10)
            StandardRtlLayout()
        }
    }
}

/**
 * Demonstrates the use of pluralStringResource for quantity-based strings.
 */
@Composable
fun CartCounter(viewModel: InventoryViewModel) {
    val count = viewModel.currentCount

    // Uses R.plurals.cart_count, automatically selecting 'one' or 'other' based on 'count'
    val inventoryText = pluralStringResource(
        id = R.plurals.cart_count,
        count = count,
        count // Argument passed to the %d placeholder
    )

    Card(
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = inventoryText,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = viewModel::cycleCount) {
                Text("Cycle Count (Current: $count)")
            }
        }
    }
}

/**
 * Demonstrates manual checking of LayoutDirection (Slide 11) for custom logic.
 */
@Composable
fun ManualRTLCheck() {
    // Get the current layout direction provided by the system
    val layoutDirection = LocalLayoutDirection.current

    // Logic: If RTL, the arrow should point "back" (visually left in RTL mode),
    // otherwise it points "forward" (visually right in LTR mode).
    val arrowIcon = remember(layoutDirection) {
        if (layoutDirection == LayoutDirection.Rtl) {
            Icons.AutoMirrored.Filled.ArrowBack
        } else {
            Icons.AutoMirrored.Filled.ArrowForward
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.rtl_demo_label))

        // This button's icon explicitly changes based on layout direction
        Button(onClick = { /* Handle navigation */ }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.continue_checkout))
                Spacer(Modifier.width(8.dp))
                Icon(arrowIcon, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }
    }
}

/**
 * Demonstrates how standard Compose Row/Column automatically handles RTL (Slide 10).
 */
@Composable
fun StandardRtlLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // In LTR, this is on the left. In RTL, it automatically moves to the right.
        Text(
            text = "Price: £49.99",
            style = MaterialTheme.typography.headlineSmall
        )

        // In LTR, this is on the right. In RTL, it automatically moves to the left.
        Button(onClick = { /* Add to cart */ }) {
            Text(stringResource(R.string.add_to_cart_button))
        }
    }
}

// --- MAIN ACTIVITY ---

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel : InventoryViewModel by viewModels()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                InternationalisationScreen(viewModel)
            }
        }
    }
}

// --- PREVIEWS ---

// Preview for English (Default)
@Preview(showBackground = true, locale = "en")
@Composable
fun InternationalisationScreenPreviewEn() {
    MaterialTheme {
        InternationalisationScreen(InventoryViewModel())
    }
}

// Preview for Spanish (Localization check)
@Preview(showBackground = true, locale = "es")
@Composable
fun InternationalisationScreenPreviewEs() {
    MaterialTheme {
        InternationalisationScreen(InventoryViewModel())
    }
}

// Preview for Arabic (RTL check)
@Preview(showBackground = true, locale = "ar")
@Composable
fun InternationalisationScreenPreviewAr() {
    MaterialTheme {
        InternationalisationScreen(InventoryViewModel())
    }
}