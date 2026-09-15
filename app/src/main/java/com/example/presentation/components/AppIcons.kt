package com.example.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIcons {
    val ElectricBolt: ImageVector by lazy {
        ImageVector.Builder(
            name = "ElectricBolt",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(15f, 2f)
            lineTo(6f, 14f)
            horizontalLineTo(11f)
            verticalLineTo(22f)
            lineTo(20f, 10f)
            horizontalLineTo(15f)
            close()
        }.build()
    }

    val Remove: ImageVector by lazy {
        ImageVector.Builder(
            name = "Remove",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            close()
        }.build()
    }

    val ExpandLess: ImageVector by lazy {
        ImageVector.Builder(
            name = "ExpandLess",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 8f)
            lineTo(6f, 14f)
            lineTo(7.41f, 15.41f)
            lineTo(12f, 10.83f)
            lineTo(16.59f, 15.41f)
            lineTo(18f, 14f)
            close()
        }.build()
    }

    val ExpandMore: ImageVector by lazy {
        ImageVector.Builder(
            name = "ExpandMore",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(16.59f, 8.59f)
            lineTo(12f, 13.17f)
            lineTo(7.41f, 8.59f)
            lineTo(6f, 10f)
            lineTo(12f, 16f)
            lineTo(18f, 10f)
            close()
        }.build()
    }

    val Sort: ImageVector by lazy {
        ImageVector.Builder(
            name = "Sort",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(3f, 18f)
            horizontalLineTo(9f)
            verticalLineTo(16f)
            horizontalLineTo(3f)
            close()
            moveTo(3f, 13f)
            horizontalLineTo(15f)
            verticalLineTo(11f)
            horizontalLineTo(3f)
            close()
            moveTo(3f, 6f)
            verticalLineTo(8f)
            horizontalLineTo(21f)
            verticalLineTo(6f)
            close()
        }.build()
    }

    val Folder: ImageVector by lazy {
        ImageVector.Builder(
            name = "Folder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(10f, 4f)
            horizontalLineTo(4f)
            curveTo(2.9f, 4f, 2f, 4.9f, 2f, 6f)
            verticalLineTo(18f)
            curveTo(2f, 19.1f, 2.9f, 20f, 4f, 20f)
            horizontalLineTo(20f)
            curveTo(21.1f, 20f, 22f, 19.1f, 22f, 18f)
            verticalLineTo(8f)
            curveTo(22f, 6.9f, 21.1f, 6f, 20f, 6f)
            horizontalLineTo(12f)
            close()
        }.build()
    }

    val ContentCopy: ImageVector by lazy {
        ImageVector.Builder(
            name = "ContentCopy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(16f, 1f)
            horizontalLineTo(4f)
            curveTo(2.9f, 1f, 2f, 1.9f, 2f, 3f)
            verticalLineTo(17f)
            horizontalLineTo(4f)
            verticalLineTo(3f)
            horizontalLineTo(16f)
            close()
            moveTo(19f, 5f)
            horizontalLineTo(8f)
            curveTo(6.9f, 5f, 6f, 5.9f, 6f, 7f)
            verticalLineTo(21f)
            curveTo(6f, 22.1f, 6.9f, 23f, 8f, 23f)
            horizontalLineTo(19f)
            curveTo(20.1f, 23f, 21f, 22.1f, 21f, 21f)
            verticalLineTo(7f)
            curveTo(21f, 5.9f, 20.1f, 5f, 19f, 5f)
            close()
            moveTo(19f, 21f)
            horizontalLineTo(8f)
            verticalLineTo(7f)
            horizontalLineTo(19f)
            close()
        }.build()
    }

    val Assessment: ImageVector by lazy {
        ImageVector.Builder(
            name = "Assessment",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 3f)
            horizontalLineTo(5f)
            curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
            verticalLineTo(19f)
            curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
            horizontalLineTo(19f)
            curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
            verticalLineTo(5f)
            curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
            close()
            moveTo(9f, 17f)
            horizontalLineTo(7f)
            verticalLineTo(12f)
            horizontalLineTo(9f)
            close()
            moveTo(13f, 17f)
            horizontalLineTo(11f)
            verticalLineTo(7f)
            horizontalLineTo(13f)
            close()
            moveTo(17f, 17f)
            horizontalLineTo(15f)
            verticalLineTo(10f)
            horizontalLineTo(17f)
            close()
        }.build()
    }

    val Inventory2: ImageVector by lazy {
        ImageVector.Builder(
            name = "Inventory2",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(20f, 2f)
            horizontalLineTo(4f)
            curveTo(2.9f, 2f, 2f, 2.9f, 2f, 4f)
            verticalLineTo(7f)
            curveTo(2f, 7.7f, 2.4f, 8.3f, 3f, 8.7f)
            verticalLineTo(20f)
            curveTo(3f, 21.1f, 3.9f, 22f, 5f, 22f)
            horizontalLineTo(19f)
            curveTo(20.1f, 22f, 21f, 21.1f, 21f, 20f)
            verticalLineTo(8.7f)
            curveTo(21.6f, 8.3f, 22f, 7.7f, 22f, 7f)
            verticalLineTo(4f)
            curveTo(22f, 2.9f, 21.1f, 2f, 20f, 2f)
            close()
            moveTo(15f, 14f)
            horizontalLineTo(9f)
            verticalLineTo(12f)
            horizontalLineTo(15f)
            close()
            moveTo(20f, 7f)
            horizontalLineTo(4f)
            verticalLineTo(4f)
            horizontalLineTo(20f)
            close()
        }.build()
    }

    val Print: ImageVector by lazy {
        ImageVector.Builder(
            name = "Print",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 8f)
            horizontalLineTo(5f)
            curveTo(3.34f, 8f, 2f, 9.34f, 2f, 11f)
            verticalLineTo(17f)
            horizontalLineTo(6f)
            verticalLineTo(21f)
            horizontalLineTo(18f)
            verticalLineTo(17f)
            horizontalLineTo(22f)
            verticalLineTo(11f)
            curveTo(22f, 9.34f, 20.66f, 8f, 19f, 8f)
            close()
            moveTo(16f, 19f)
            horizontalLineTo(8f)
            verticalLineTo(14f)
            horizontalLineTo(16f)
            close()
            moveTo(19f, 12f)
            curveTo(18.45f, 12f, 18f, 11.55f, 18f, 11f)
            curveTo(18f, 10.45f, 18.45f, 10f, 19f, 10f)
            curveTo(19.55f, 10f, 20f, 10.45f, 20f, 11f)
            curveTo(20f, 11.55f, 19.55f, 12f, 19f, 12f)
            close()
            moveTo(18f, 3f)
            horizontalLineTo(6f)
            verticalLineTo(7f)
            horizontalLineTo(18f)
            close()
        }.build()
    }

    val MeetingRoom: ImageVector by lazy {
        ImageVector.Builder(
            name = "MeetingRoom",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 19f)
            verticalLineTo(4f)
            curveTo(19f, 2.9f, 18.1f, 2f, 17f, 2f)
            horizontalLineTo(7f)
            curveTo(5.9f, 2f, 5f, 2.9f, 5f, 4f)
            verticalLineTo(19f)
            horizontalLineTo(3f)
            verticalLineTo(21f)
            horizontalLineTo(21f)
            verticalLineTo(19f)
            horizontalLineTo(19f)
            close()
            moveTo(13f, 19f)
            horizontalLineTo(7f)
            verticalLineTo(4f)
            horizontalLineTo(13f)
            close()
            moveTo(11f, 11f)
            horizontalLineTo(9f)
            verticalLineTo(9f)
            horizontalLineTo(11f)
            close()
        }.build()
    }

    val DoorBack: ImageVector get() = MeetingRoom

    val ElectricalServices: ImageVector by lazy {
        ImageVector.Builder(
            name = "ElectricalServices",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(18f, 13f)
            curveTo(16.9f, 13f, 16f, 13.9f, 16f, 15f)
            verticalLineTo(19f)
            horizontalLineTo(8f)
            verticalLineTo(15f)
            curveTo(8f, 13.9f, 7.1f, 13f, 6f, 13f)
            curveTo(4.9f, 13f, 4f, 13.9f, 4f, 15f)
            verticalLineTo(20f)
            curveTo(4f, 20.6f, 4.4f, 21f, 5f, 21f)
            horizontalLineTo(19f)
            curveTo(19.6f, 21f, 20f, 20.6f, 20f, 20f)
            verticalLineTo(15f)
            curveTo(20f, 13.9f, 19.1f, 13f, 18f, 13f)
            close()
            moveTo(5f, 3f)
            verticalLineTo(9f)
            horizontalLineTo(7f)
            verticalLineTo(3f)
            close()
            moveTo(9f, 3f)
            verticalLineTo(9f)
            horizontalLineTo(11f)
            verticalLineTo(3f)
            close()
            moveTo(17f, 3f)
            verticalLineTo(7f)
            horizontalLineTo(15f)
            verticalLineTo(3f)
            horizontalLineTo(13f)
            verticalLineTo(9f)
            horizontalLineTo(19f)
            verticalLineTo(3f)
            close()
        }.build()
    }

    val DarkMode: ImageVector by lazy {
        ImageVector.Builder(
            name = "DarkMode",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 3f)
            curveTo(6.48f, 3f, 2f, 7.48f, 2f, 13f)
            curveToRelative(0f, 5.52f, 4.48f, 10f, 10f, 10f)
            curveToRelative(5.52f, 0f, 10f, -4.48f, 10f, -10f)
            curveToRelative(0f, -0.34f, -0.02f, -0.67f, -0.05f, -1f)
            curveToRelative(-0.66f, 0.44f, -1.45f, 0.7f, -2.3f, 0.7f)
            curveToRelative(-2.49f, 0f, -4.5f, -2.01f, -4.5f, -4.5f)
            curveToRelative(0f, -1.88f, 1.15f, -3.48f, 2.79f, -4.17f)
            curveTo(16.5f, 3.38f, 14.34f, 3f, 12f, 3f)
            close()
        }.build()
    }

    val LightMode: ImageVector by lazy {
        ImageVector.Builder(
            name = "LightMode",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 7f)
            curveToRelative(-2.76f, 0f, -5f, 2.24f, -5f, 5f)
            reflectiveCurveToRelative(2.24f, 5f, 5f, 5f)
            reflectiveCurveToRelative(5f, -2.24f, 5f, -5f)
            reflectiveCurveToRelative(-2.24f, -5f, -5f, -5f)
            close()
            moveTo(2f, 13f)
            horizontalLineToRelative(2f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            reflectiveCurveToRelative(-0.45f, -1f, -1f, -1f)
            lineTo(2f, 11f)
            curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
            reflectiveCurveToRelative(0.45f, 1f, 1f, 1f)
            close()
            moveTo(20f, 13f)
            horizontalLineToRelative(2f)
            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
            reflectiveCurveToRelative(-0.45f, -1f, -1f, -1f)
            horizontalLineToRelative(-2f)
            curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
            reflectiveCurveToRelative(0.45f, 1f, 1f, 1f)
            close()
            moveTo(11f, 2f)
            verticalLineToRelative(2f)
            curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
            reflectiveCurveToRelative(1f, -0.45f, 1f, -1f)
            lineTo(13f, 2f)
            curveToRelative(0f, -0.55f, -0.45f, -1f, -1f, -1f)
            reflectiveCurveToRelative(-1f, 0.45f, -1f, 1f)
            close()
            moveTo(11f, 20f)
            verticalLineToRelative(2f)
            curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
            reflectiveCurveToRelative(1f, -0.45f, 1f, -1f)
            verticalLineToRelative(-2f)
            curveToRelative(0f, -0.55f, -0.45f, -1f, -1f, -1f)
            reflectiveCurveToRelative(-1f, 0.45f, -1f, 1f)
            close()
            moveTo(5.99f, 4.58f)
            curveToRelative(-0.39f, -0.39f, -1.03f, -0.39f, -1.41f, 0f)
            reflectiveCurveToRelative(-0.39f, 1.03f, 0f, 1.41f)
            lineToRelative(1.06f, 1.06f)
            curveToRelative(0.39f, 0.39f, 1.03f, 0.39f, 1.41f, 0f)
            reflectiveCurveToRelative(0.39f, -1.03f, 0f, -1.41f)
            lineTo(5.99f, 4.58f)
            close()
            moveTo(18.36f, 16.95f)
            curveToRelative(-0.39f, -0.39f, -1.03f, -0.39f, -1.41f, 0f)
            reflectiveCurveToRelative(-0.39f, 1.03f, 0f, 1.41f)
            lineToRelative(1.06f, 1.06f)
            curveToRelative(0.39f, 0.39f, 1.03f, 0.39f, 1.41f, 0f)
            reflectiveCurveToRelative(0.39f, -1.03f, 0f, -1.41f)
            lineToRelative(-1.06f, -1.06f)
            close()
            moveTo(7.05f, 18.36f)
            lineToRelative(-1.06f, 1.06f)
            curveToRelative(-0.39f, 0.39f, -0.39f, 1.03f, 0f, 1.41f)
            reflectiveCurveToRelative(1.03f, 0.39f, 1.41f, 0f)
            lineToRelative(1.06f, -1.06f)
            curveToRelative(0.39f, -0.39f, 0.39f, -1.03f, 0f, -1.41f)
            reflectiveCurveToRelative(-1.02f, -0.39f, -1.41f, 0f)
            close()
            moveTo(16.95f, 7.05f)
            lineToRelative(1.06f, -1.06f)
            curveToRelative(0.39f, -0.39f, 0.39f, -1.03f, 0f, -1.41f)
            reflectiveCurveToRelative(-1.03f, -0.39f, -1.41f, 0f)
            lineToRelative(-1.06f, 1.06f)
            curveToRelative(-0.39f, 0.39f, -0.39f, 1.03f, 0f, 1.41f)
            reflectiveCurveToRelative(1.03f, 0.39f, 1.41f, 0f)
            close()
        }.build()
    }
}
