package com.example.tustareas

import com.example.tustareas.util.DateHelper
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Clase que gestiona las pruebas unitarias del date helper
 */
class DateHelperTest {

    @Test
    fun testTimestamp1() {
        val timestamp = 1776031200000
        val fecha = DateHelper.timestampToString(Date(timestamp))
        assert(fecha == "12/04/2026")
    }
    @Test
    fun testTimestamp2() {
        val timestamp =  1776038400000
        val fecha = DateHelper.timestampToString(Date(timestamp))
        assert(fecha == "13/04/2026")
    }

    @Test
    fun testTimestamp3() {
        val fecha = DateHelper.timestampToString(null)
        assert(fecha == "")
    }

    @Test
    fun testFechaMediaNocheUTC() {
        val fecha = DateHelper.fechaMediaNocheUTC()
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.time = fecha
        assert(cal.get(Calendar.HOUR_OF_DAY) == 0)
        assert(cal.get(Calendar.MINUTE) == 0)
        assert(cal.get(Calendar.SECOND) == 0)
        assert(cal.get(Calendar.MILLISECOND) == 0)
    }
}