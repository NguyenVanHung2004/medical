package com.example.medical.presentation.ui.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember

data class IntroPage(
    val title: String,
    val description: String,
    val icon: ImageVector
)

val introPages = listOf(
    IntroPage(
        title = "Chào mừng đến với\nMediConnect",
        description = "Đặt lịch khám bác sĩ dễ dàng và quản lý hồ sơ sức khỏe thông minh ngay trên điện thoại của bạn",
        icon = Icons.Default.Favorite
    ),
    IntroPage(
        title = "Quản lý lịch hẹn\nthông minh",
        description = "Theo dõi và nhắc nhở lịch khám tự động, giúp bạn không bao giờ bỏ lỡ cuộc hẹn quan trọng với bác sĩ.",
        icon = Icons.Default.CalendarMonth
    ),
    IntroPage(
        title = "Hồ sơ sức khỏe\ntrong tầm tay",
        description = "Lưu trữ đơn thuốc, kết quả xét nghiệm và lịch sử khám bệnh một cách an toàn và bảo mật tuyệt đối.",
        icon = Icons.Default.FolderSpecial
    )
)

@Composable
fun IntroScreen(
    onFinishIntro: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { introPages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TextButton(
            onClick = onFinishIntro,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "Bỏ qua",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                IntroPageContent(introPage = introPages[page])
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                Modifier
                    .height(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(introPages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(width = width, height = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (pagerState.currentPage < introPages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onFinishIntro()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (pagerState.currentPage < introPages.size - 1) "Tiếp tục" else "Bắt đầu ngay",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun IntroPageContent(introPage: IntroPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = introPage.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(100.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = introPage.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = introPage.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

