package com.castorama.krakathon.core.presentation.format

/** 34900 -> "349,00 zł" */
fun formatPrice(grosze: Long): String {
    val zloty = grosze / 100
    val rest = (grosze % 100).toString().padStart(2, '0')
    val grouped = zloty.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$grouped,$rest zł"
}
