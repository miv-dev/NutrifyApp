package ru.nutrify.presentation.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

val weekLabels = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

@Composable
fun MainContent() {
    val weekData = listOf(63.5, 62.0, 62.5, 63.2, 60.5, 62.0, 65.5)
    val goal = 70.0
    val theme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    Scaffold { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = theme.primary,
                contentColor = theme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Статистика веса", style = typography.headlineMedium)
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Stats(
                            weekData.first().roundToInt(),
                            "кг",
                            "Сейчас"
                        )
                        Stats(
                            goal.roundToInt(),
                            "кг",
                            "Цель"
                        )
                        Stats(
                            23,
                            "%",
                            "Прогресс"
                        )
                    }

                    LineChart(
                        weekData.map { it.toFloat() }, Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun LineChart(data: List<Float>, modifier: Modifier = Modifier) {
    val maxDataValue = data.maxOrNull() ?: 0f
    val minDataValue = data.minOrNull() ?: 0f
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Calculate the step between points
        val stepX = width / (data.size - 1)
        val normalizedData = data.map { (it - minDataValue) / (maxDataValue - minDataValue) }

        // Create the path for the line
        // Create the path for the curve
        val path = Path().apply {
            normalizedData.forEachIndexed { index, value ->
                val x = index * stepX
                val y = (height - (value * height))

                if (index == 0) {
                    moveTo(x, y) // Start at the first point
                } else {
                    val previousX = (index - 1) * stepX
                    val previousY = height - (normalizedData[index - 1] * height)

                    // Calculate control points for the cubic Bézier curve
                    val controlPoint1X = (previousX + x) / 2
                    val controlPoint1Y = previousY
                    val controlPoint2X = (previousX + x) / 2
                    val controlPoint2Y = y

                    cubicTo(
                        controlPoint1X,
                        controlPoint1Y,
                        controlPoint2X,
                        controlPoint2Y,
                        x,
                        y
                    )
                }
            }
        }

        // Draw the line
        val step = height / 3
        for (i in 1..3) {
            drawLine(
                Color.White.copy(0.5f),
                start = Offset(0f, step * i),
                end = Offset(width, step * i),
            )
        }

        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw points
        normalizedData.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - (value * height)
            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = Offset(x, y)
            )

        }
    }
}


@Composable
fun Stats(
    value: Int,
    unit: String,
    description: String

) {
    val typography = MaterialTheme.typography

    Column {
        Text(buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 26.sp)) {
                append(value.toString())
            }
            append(" $unit")
        }, style = typography.titleMedium)
        Text(description)
    }
}