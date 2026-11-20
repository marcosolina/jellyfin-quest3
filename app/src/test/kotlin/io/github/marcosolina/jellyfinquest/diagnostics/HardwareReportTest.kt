package io.github.marcosolina.jellyfinquest.diagnostics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HardwareReportTest {
    @Test
    fun `hardware decoders win over software ones, even smaller ones`() {
        val decoders =
            listOf(
                DecoderInfo("c2.android.hevc", "video/hevc", isHardware = false, maxWidth = 16384, maxHeight = 16384),
                DecoderInfo(
                    "c2.qti.hevc",
                    "video/hevc",
                    isHardware = true,
                    maxWidth = 8192,
                    maxHeight = 4352,
                    tenBit = true,
                ),
            )

        val hevc = HardwareReport.support(Codec.HEVC, decoders)

        assertEquals(Availability.HARDWARE, hevc.availability)
        assertEquals(8192, hevc.maxWidth)
        assertEquals(4352, hevc.maxHeight)
        assertTrue(hevc.tenBit)
    }

    @Test
    fun `a codec with only software decoders is reported as software`() {
        val decoders =
            listOf(
                DecoderInfo("c2.android.mpeg4", "video/mp4v-es", isHardware = false, maxWidth = 2048, maxHeight = 2048),
            )

        val mpeg4 = HardwareReport.support(Codec.MPEG4, decoders)

        assertEquals(Availability.SOFTWARE, mpeg4.availability)
        assertEquals(2048, mpeg4.maxWidth)
    }

    @Test
    fun `a codec without decoders is not available`() {
        val dts = HardwareReport.support(Codec.DTS, emptyList())

        assertEquals(Availability.NONE, dts.availability)
        assertNull(dts.maxWidth)
        assertFalse(dts.hdr)
    }

    @Test
    fun `any of a codec's mime types counts, whatever its case`() {
        val decoders = listOf(DecoderInfo("c2.dolby.eac3-joc", "audio/EAC3-JOC", isHardware = false))

        assertEquals(Availability.SOFTWARE, HardwareReport.support(Codec.EAC3, decoders).availability)
    }

    @Test
    fun `the report covers every codec and starts with the display`() {
        val report = HardwareReport.of(emptyList(), displayHdr = false)

        assertEquals(Codec.entries.size, report.codecs.size)
        assertEquals("display HDR: no", report.lines().first())
        assertEquals("DTS: none", report.lines().first { it.startsWith("DTS") })
    }

    @Test
    fun `log lines describe video abilities`() {
        val decoders =
            listOf(
                DecoderInfo(
                    "c2.qti.av1",
                    "video/av01",
                    isHardware = true,
                    maxWidth = 8192,
                    maxHeight = 4352,
                    tenBit = true,
                    hdr = true,
                ),
            )

        val line = HardwareReport.of(decoders, displayHdr = false).lines().first { it.startsWith("AV1") }

        assertEquals("AV1: hardware, max 8192x4352, 10 bit yes, HDR yes, 8K60 no", line)
    }
}
