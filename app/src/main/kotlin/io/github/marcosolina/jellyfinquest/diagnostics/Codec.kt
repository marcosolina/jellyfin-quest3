package io.github.marcosolina.jellyfinquest.diagnostics

enum class MediaKind { VIDEO, AUDIO }

/** The formats found in Marco's library (docs/research/JQ-011) and the ones the Quest 3 is said to decode (JQ-018). */
enum class Codec(
    val label: String,
    val kind: MediaKind,
    val mimeTypes: Set<String>,
) {
    H264("H.264", MediaKind.VIDEO, setOf("video/avc")),
    HEVC("HEVC", MediaKind.VIDEO, setOf("video/hevc")),
    VP9("VP9", MediaKind.VIDEO, setOf("video/x-vnd.on2.vp9")),
    AV1("AV1", MediaKind.VIDEO, setOf("video/av01")),
    DOLBY_VISION("Dolby Vision", MediaKind.VIDEO, setOf("video/dolby-vision")),
    MPEG4("MPEG-4 Part 2", MediaKind.VIDEO, setOf("video/mp4v-es")),
    MPEG2("MPEG-2", MediaKind.VIDEO, setOf("video/mpeg2")),
    AAC("AAC", MediaKind.AUDIO, setOf("audio/mp4a-latm")),
    AC3("AC-3", MediaKind.AUDIO, setOf("audio/ac3")),
    EAC3("E-AC-3", MediaKind.AUDIO, setOf("audio/eac3", "audio/eac3-joc")),
    DTS("DTS", MediaKind.AUDIO, setOf("audio/vnd.dts", "audio/vnd.dts.hd")),
    TRUEHD("TrueHD", MediaKind.AUDIO, setOf("audio/true-hd")),
    FLAC("FLAC", MediaKind.AUDIO, setOf("audio/flac")),
    OPUS("Opus", MediaKind.AUDIO, setOf("audio/opus")),
}
