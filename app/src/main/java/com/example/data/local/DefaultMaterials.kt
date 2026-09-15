package com.example.data.local

import com.example.data.local.entity.MaterialEntity

/**
 * The standard 28 pre-populated electrical materials.
 */
object DefaultMaterials {
    val items = listOf(
        "بريز",
        "فقسات (دعسات)",
        "شمبر",
        "شمبر سباعي",
        "بلاك",
        "شاسي أليون",
        "أليون",
        "سوكة",
        "لمبة",
        "كبل سحب كهرباء 3ملم",
        "كبل سحب كهرباء 1.5ملم",
        "كبل سحب لدات 0.5ملم",
        "لمبة 9 واط",
        "لمبة 15 واط",
        "لمبة 20 واط",
        "لد ربع متر",
        "لزيق",
        "قواطع",
        "غطا علبة تفتيش",
        "فقسة جرس كاملة",
        "علب ماجيك مفرد",
        "علبة ماجيك مجوز",
        "علبة ماجيك سباعي",
        "علبة تفتيش 10×10",
        "علبة تفتيش 12×10",
        "علبة تفتيش 12×17",
        "تيب عريض",
        "تيب رفيع"
    ).mapIndexed { index, name ->
        val unit = when {
            name.contains("كبل") || name.contains("لد") -> "متر"
            name.contains("لزيق") || name.contains("تيب") -> "رول"
            else -> "قطعة"
        }
        MaterialEntity(
            id = (index + 1).toLong(),
            name = name,
            unit = unit,
            isDefault = true
        )
    }
}
