package com.example.tustareas

import com.example.tustareas.util.DateHelper
import org.junit.Test
import java.util.Date

/**
 * Clase que gestiona las pruebas unitarias del date helper
 */
class DateHelperTest {

    @Test
    fun testTimestamp1() {
        val timestamp = 1776031200000
        val fecha = DateHelper.timestampToString(Date(timestamp))
        assert(fecha == "13/04/2026")
    }

    @Test
    fun testTimestamp2() {
        val fecha = DateHelper.timestampToString(null)
        assert(fecha == "")
    }
}