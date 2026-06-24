package com.example.medical.presentation.ui.welcome

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.medical.R

@Composable
fun DoctorAnimation() {
    // 1. Load file animation từ thư mục res/raw
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.doctor_anim_welcome_screen))

    // 2. Cấu hình trạng thái chạy (ở đây là lặp lại vô hạn)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // 3. Render ra UI
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(250.dp) // Bạn có thể tuỳ chỉnh kích thước tại đây
    )
}

@Preview
@Composable
fun DoctorAnimationPreview() {
    DoctorAnimation()
}