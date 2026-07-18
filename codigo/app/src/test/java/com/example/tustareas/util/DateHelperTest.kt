package com.example.tustareas.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Tests unitarios para DateHelper.
 * Cubre: timestampToString() y fechaMediaNocheUTC().
 */
class DateHelperTest {
    /**
     * T7: timestampToString con fecha válida → devuelve formato dd/MM/yyyy correcto.
     */
    @Test
    fun `T7 timestampToString con fecha valida devuelve formato ddMMyyyy`() {
        // Arrange: crear una fecha conocida (15 de julio de 2026)
        val cal =
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                set(2026, Calendar.JULY, 15, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
        val fecha: Date = cal.time

        // Act
        val resultado = DateHelper.timestampToString(fecha)

        // Assert
        assertEquals("El formato debería ser dd/MM/yyyy", "15/07/2026", resultado)
    }

    /**
     * T8: timestampToString con fecha nula → devuelve cadena vacía.
     */
    @Test
    fun `T8 timestampToString con fecha nula devuelve cadena vacia`() {
        // Act
        val resultado = DateHelper.timestampToString(null)

        // Assert
        assertEquals("Debería devolver una cadena vacía", "", resultado)
    }

    /**
     * T9: timestampToString con diferentes fechas → cada una formateada correctamente.
     */
    @Test
    fun `T9 timestampToString con multiples fechas formatea correctamente`() {
        // Arrange
        val cal1 =
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                set(2024, Calendar.JANUARY, 1, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
        val cal2 =
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                set(2025, Calendar.DECEMBER, 31, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }

        // Act & Assert
        assertEquals("01/01/2024", DateHelper.timestampToString(cal1.time))
        assertEquals("31/12/2025", DateHelper.timestampToString(cal2.time))
    }

    /**
     * T10: timestampToString con fecha que tiene hora/minuto/segundo → ignora la hora y solo muestra la fecha.
     */
    @Test
    fun `T10 timestampToString con hora no nula solo muestra la fecha`() {
        // Arrange: fecha con hora específica
        val cal =
            Calendar.getInstance().apply {
                set(2026, Calendar.MARCH, 5, 14, 30, 45)
            }

        // Act
        val resultado = DateHelper.timestampToString(cal.time)

        // Assert: debería ser la fecha sin hora (el formatter usa UTC y solo dd/MM/yyyy)
        assertEquals("05/03/2026", resultado)
    }

    /**
     * T11: fechaMediaNocheUTC devuelve una fecha con hora 00:00:00 en UTC.
     */
    @Test
    fun `T11 fechaMediaNocheUTC devuelve medianoche UTC`() {
        // Act
        val resultado = DateHelper.fechaMediaNocheUTC()

        // Assert: verificar que es una fecha válida y no nula
        assertNotEquals("La fecha no debería ser nula", 0L, resultado.time)
        // La fecha debería estar en el año/mes/día actual (sin hora)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.time = resultado
        assertEquals(
            "La hora debería ser 0",
            0,
            cal.get(Calendar.HOUR_OF_DAY),
        )
        assertEquals(
            "Los minutos deberían ser 0",
            0,
            cal.get(Calendar.MINUTE),
        )
        assertEquals(
            "Los segundos deberían ser 0",
            0,
            cal.get(Calendar.SECOND),
        )
    }

    /**
     * T12: fechaMediaNocheUTC devuelve la fecha de hoy (no mañana ni ayer).
     */
    @Test
    fun `T12 fechaMediaNocheUTC devuelve la fecha de hoy`() {
        // Arrange: obtener la fecha actual en UTC
        val cal =
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

        // Act
        val resultado = DateHelper.fechaMediaNocheUTC()

        // Assert: comparar año, mes y día
        assertEquals(
            "El año debería coincidir",
            cal.get(Calendar.YEAR),
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { time = resultado }.get(Calendar.YEAR),
        )
        assertEquals(
            "El mes debería coincidir",
            cal.get(Calendar.MONTH),
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { time = resultado }.get(Calendar.MONTH),
        )
        assertEquals(
            "El día debería coincidir",
            cal.get(Calendar.DAY_OF_MONTH),
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { time = resultado }.get(Calendar.DAY_OF_MONTH),
        )
    }

    /**
     * T13: timestampToString con fecha en diferentes meses → formatea correctamente.
     */
    @Test
    fun `T13 timestampToString con fechas de diferentes meses`() {
        // Arrange & Act & Assert
        val pruebas =
            mapOf(
                Pair(2026, Calendar.JANUARY) to "01/01/2026",
                Pair(2026, Calendar.FEBRUARY) to "01/02/2026",
                Pair(2026, Calendar.DECEMBER) to "01/12/2026",
            )

        pruebas.forEach { (yearMonth, esperado) ->
            val cal =
                Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                    set(yearMonth.first, yearMonth.second, 1, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            assertEquals(esperado, DateHelper.timestampToString(cal.time))
        }
    }

    /**
     * T14: fechaMediaNocheUTC siempre devuelve una fecha en el futuro cercano (hoy a medianoche).
     */
    @Test
    fun `T14 fechaMediaNocheUTC devuelve fecha valida en cualquier momento del dia`() {
        // Act: llamar varias veces con diferentes horas
        val resultado1 = DateHelper.fechaMediaNocheUTC()
        val resultado2 = DateHelper.fechaMediaNocheUTC()

        // Assert: ambas deberían ser fechas válidas y no nulas
        assertNotEquals("La fecha 1 no debería ser nula", 0L, resultado1.time)
        assertNotEquals("La fecha 2 no debería ser nula", 0L, resultado2.time)
    }
}
