package com.app.shoppinglist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.app.shoppinglist.R

val KronaFontFamily = Font(R.font.krona).toFontFamily()
val NumansFontFamily = Font(R.font.numans).toFontFamily()

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = KronaFontFamily,
        fontWeight = FontWeight.Normal,
        color = Coral,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Normal,
        color = Navy,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Normal,
        color = Navy,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NumansFontFamily,
        fontWeight = FontWeight.Normal,
        color = Navy,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )
)