package com.example.presentation.sector_items

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import com.example.presentation.components.AppIcons
import com.example.ui.theme.ThemeState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.components.EmptyStateView
import com.example.presentation.components.QuantityInputDialog
import com.example.presentation.components.SearchInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectorItemsScreen(
    viewModel: SectorItemsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var selectedItemForDirectInput by remember { mutableStateOf<MaterialItemUiModel?>(null) }

    fun triggerHaptic(strong: Boolean = false) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val duration = if (strong) 70L else 30L
                it.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(30)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = viewModel.sectorName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تحديد مستلزمات الكهرباء",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع"
                        )
                    }
                },
                actions = {
                    val systemDark = isSystemInDarkTheme()
                    val isDark = ThemeState.isDarkTheme(context, systemDark)
                    IconButton(
                        onClick = {
                            triggerHaptic(strong = false)
                            ThemeState.toggle(context, systemDark)
                        },
                        modifier = Modifier.testTag("theme_toggle_sector_items_button")
                    ) {
                        Icon(
                            imageVector = if (isDark) AppIcons.LightMode else AppIcons.DarkMode,
                            contentDescription = if (isDark) "تفعيل الثيم الفاتح" else "تفعيل الثيم الداكن"
                        )
                    }
                    Button(
                        onClick = {
                            triggerHaptic(strong = true)
                            onNavigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("done_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تم", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Live instant search field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                SearchInputField(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = "ابحث في قائمة المواد الـ 28 المعتمدة..."
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.materials.isEmpty()) {
                EmptyStateView(
                    icon = AppIcons.ElectricalServices,
                    title = "لا توجد مواد مطابقة للبحث",
                    description = "جرب البحث باسم آخر مثل: بريز، شمبر، لمبة، كبل...",
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("materials_lazy_column")
                ) {
                    items(
                        items = uiState.materials,
                        key = { it.materialId },
                        contentType = { "material_item" }
                    ) { item ->
                        MaterialCounterRow(
                            item = item,
                            onIncrement = {
                                triggerHaptic()
                                viewModel.incrementQuantity(item.materialId)
                            },
                            onDecrement = {
                                triggerHaptic()
                                viewModel.decrementQuantity(item.materialId)
                            },
                            onQuantityClick = {
                                selectedItemForDirectInput = item
                            }
                        )
                    }
                }
            }
        }
    }

    // Direct manual quantity input dialog
    selectedItemForDirectInput?.let { item ->
        QuantityInputDialog(
            itemName = item.name,
            currentQuantity = item.quantity,
            unit = item.unit,
            onConfirm = { qty ->
                triggerHaptic(strong = true)
                viewModel.setQuantity(item.materialId, qty)
                selectedItemForDirectInput = null
            },
            onDismiss = { selectedItemForDirectInput = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MaterialCounterRow(
    item: MaterialItemUiModel,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onQuantityClick: () -> Unit
) {
    val isSelected = item.quantity > 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("material_card_${item.materialId}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Material name and unit badge
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = item.unit,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Stepper controls: (-) [ Quantity ] (+)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Decrement button
                FilledIconButton(
                    onClick = onDecrement,
                    enabled = item.quantity > 0,
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (item.quantity > 0) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        },
                        contentColor = if (item.quantity > 0) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        }
                    ),
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("decrement_${item.materialId}")
                ) {
                    Icon(
                        imageVector = AppIcons.Remove,
                        contentDescription = "إنقاص الكمية",
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Quantity Display (Interactive, clickable for direct numeric entry)
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (item.quantity > 0) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            }
                        )
                        .combinedClickable(
                            onClick = onQuantityClick,
                            onLongClick = onQuantityClick
                        )
                        .testTag("quantity_display_${item.materialId}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${item.quantity}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (item.quantity > 0) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        textAlign = TextAlign.Center
                    )
                }

                // Increment button
                FilledIconButton(
                    onClick = onIncrement,
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("increment_${item.materialId}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "زيادة الكمية",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
