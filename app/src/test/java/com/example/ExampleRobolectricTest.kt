package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.DefaultMaterials
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("مستلزمات الكهرباء", appName)
    }

    @Test
    fun `verify default materials contains 28 items`() {
        val items = DefaultMaterials.items
        assertEquals(28, items.size)
        assertTrue(items.any { it.name.contains("بريز") })
        assertTrue(items.any { it.name.contains("فقسات") })
        assertTrue(items.any { it.name.contains("شمبر") })
        assertTrue(items.any { it.name.contains("كبل") })
    }
}

