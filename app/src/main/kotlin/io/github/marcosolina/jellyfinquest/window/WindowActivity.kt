package io.github.marcosolina.jellyfinquest.window

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import io.github.marcosolina.jellyfinquest.BuildConfig
import io.github.marcosolina.jellyfinquest.cinema.CinemaActivity
import io.github.marcosolina.jellyfinquest.diagnostics.DecoderProbe
import io.github.marcosolina.jellyfinquest.diagnostics.HardwareReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The app's launcher: a normal Horizon OS window, so the system draws its bar (title, close, fullscreen) and handles
 * placement (ADR-003). Browsing happens here; watching happens in the [CinemaActivity].
 */
class WindowActivity : ComponentActivity() {
    private var hardware by mutableStateOf<HardwareReport?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowScreen(
                version = BuildConfig.VERSION_NAME,
                hardware = hardware,
                onEnterCinema = { CinemaActivity.start(this) },
            )
        }
        val displayHdr = display?.isHdr == true
        lifecycleScope.launch {
            val report = withContext(Dispatchers.Default) { HardwareReport.of(DecoderProbe.probe(), displayHdr) }
            report.lines().forEach { Log.i(TAG, it) }
            hardware = report
        }
    }

    companion object {
        private const val TAG = "JellyfinQuest"

        // Horizon OS Home reopens the window from this pending intent when an immersive activity hands back.
        private const val EXTRA_LAUNCH_IN_HOME = "extra_launch_in_home_pending_intent"

        /** Closes the immersive [from] activity and brings the window back in the Horizon OS home environment. */
        fun returnFromImmersive(from: Activity) {
            val window = Intent(from, WindowActivity::class.java)
            window.action = Intent.ACTION_MAIN
            window.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val home = Intent(Intent.ACTION_MAIN)
            home.addCategory(Intent.CATEGORY_HOME)
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            home.putExtra(EXTRA_LAUNCH_IN_HOME, pendingIntent(from, window))
            from.startActivity(home)
            from.finish()
        }

        private fun pendingIntent(
            context: Context,
            intent: Intent,
        ): PendingIntent {
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            return PendingIntent.getActivity(context, 0, intent, flags)
        }
    }
}
