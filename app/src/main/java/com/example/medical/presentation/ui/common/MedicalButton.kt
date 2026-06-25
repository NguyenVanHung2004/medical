package com.example.medical.presentation.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medical.presentation.theme.GenZButtonColor
import com.example.medical.presentation.theme.LocalThemeMode
import com.example.medical.presentation.theme.NeonBlue
import com.example.medical.presentation.theme.NeonGreen

@Composable
fun rememberGenZBorder(): BorderStroke {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val brush = Brush.linearGradient(
        colors = listOf(
            NeonGreen,
            NeonBlue,
            NeonGreen
        ),
        start = Offset(offset, offset),
        end = Offset(offset + 500f, offset + 500f)
    )
    return BorderStroke(1.dp, brush)
}


@Composable
fun MedicalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: ButtonColors? = null,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val isGenZ = LocalThemeMode.current == "genz"
    val containerColor = if (isGenZ) GenZButtonColor else (colors?.containerColor ?: MaterialTheme.colorScheme.primary)
    val contentColor = if (isGenZ) Color.White else (colors?.contentColor ?: Color.White)
    
    val genZBorder = if (isGenZ) rememberGenZBorder() else null
    val finalBorder = if (isGenZ) genZBorder else border

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        elevation = elevation,
        border = finalBorder,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun MedicalOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: ButtonColors? = null,
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val isGenZ = LocalThemeMode.current == "genz"
    
    if (isGenZ) {
        val genZBorder = rememberGenZBorder()
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = GenZButtonColor,
                contentColor = Color.White,
                disabledContainerColor = GenZButtonColor.copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            elevation = elevation,
            border = genZBorder,
            contentPadding = contentPadding,
            content = content
        )
    } else {
        val contentColor = colors?.contentColor ?: MaterialTheme.colorScheme.primary
        val finalBorder = border ?: BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = contentColor,
                disabledContentColor = contentColor.copy(alpha = 0.5f)
            ),
            elevation = elevation,
            border = finalBorder,
            contentPadding = contentPadding,
            content = content
        )
    }
}

@Composable
fun MedicalTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: ButtonColors? = null,
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val isGenZ = LocalThemeMode.current == "genz"
    
    if (isGenZ) {
        val genZBorder = rememberGenZBorder()
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = GenZButtonColor,
                contentColor = Color.White,
                disabledContainerColor = GenZButtonColor.copy(alpha = 0.5f),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            elevation = elevation,
            border = genZBorder,
            contentPadding = contentPadding,
            content = content
        )
    } else {
        val contentColor = colors?.contentColor ?: MaterialTheme.colorScheme.primary
        TextButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.textButtonColors(
                contentColor = contentColor,
                disabledContentColor = contentColor.copy(alpha = 0.5f)
            ),
            elevation = elevation,
            border = border,
            contentPadding = contentPadding,
            content = content
        )
    }
}

@Composable
fun MedicalIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    content: @Composable () -> Unit
) {
    val isGenZ = LocalThemeMode.current == "genz"
    val contentColor = if (isGenZ) NeonBlue else colors.contentColor

    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.containerColor,
            contentColor = contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        content = content
    )
}


@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val isGenZ = LocalThemeMode.current == "genz"
    val containerColor = if (isGenZ) GenZButtonColor else MaterialTheme.colorScheme.primary
    val contentColor = Color.White
    
    val genZBorder = if (isGenZ) rememberGenZBorder() else null

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = genZBorder
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = contentColor,
                strokeWidth = 3.dp
            )
        } else {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    color: Color? = null
) {
    val isGenZ = LocalThemeMode.current == "genz"
    
    if (isGenZ) {
        val genZBorder = rememberGenZBorder()
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = GenZButtonColor,
                contentColor = Color.White
            ),
            border = genZBorder
        ) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }
    } else {
        val contentColor = color ?: MaterialTheme.colorScheme.primary
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = enabled,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = contentColor
            ),
            border = BorderStroke(1.dp, contentColor)
        ) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }
    }
}
