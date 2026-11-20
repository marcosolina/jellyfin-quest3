package io.github.marcosolina.jellyfinquest

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.meta.spatial.compose.ComposeFeature
import com.meta.spatial.compose.ComposeViewPanelRegistration
import com.meta.spatial.core.Entity
import com.meta.spatial.core.SpatialFeature
import com.meta.spatial.isdk.IsdkGrabMovementType
import com.meta.spatial.isdk.IsdkGrabbable
import com.meta.spatial.isdk.IsdkPanelGrabHandle
import com.meta.spatial.toolkit.AppSystemActivity
import com.meta.spatial.toolkit.DpPerMeterDisplayOptions
import com.meta.spatial.toolkit.Panel
import com.meta.spatial.toolkit.PanelRegistration
import com.meta.spatial.toolkit.PanelStyleOptions
import com.meta.spatial.toolkit.QuadShapeOptions
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.UIPanelSettings
import com.meta.spatial.vr.LocomotionSystem
import com.meta.spatial.vr.VRFeature
import io.github.marcosolina.jellyfinquest.diagnostics.DecoderProbe
import io.github.marcosolina.jellyfinquest.diagnostics.HardwareReport
import io.github.marcosolina.jellyfinquest.home.HomePanel
import io.github.marcosolina.jellyfinquest.scene.PlaceAheadOfViewer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** The app's only activity: an immersive Spatial SDK scene with one floating panel. */
class JellyfinQuestActivity : AppSystemActivity() {
    private val scope = MainScope()
    private var passthroughOn by mutableStateOf(true)
    private var hardware by mutableStateOf<HardwareReport?>(null)
    private var panelPlacement: PlaceAheadOfViewer? = null

    override fun registerFeatures(): List<SpatialFeature> = listOf(VRFeature(this), ComposeFeature())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayHdr = display?.isHdr == true
        scope.launch {
            val report = withContext(Dispatchers.Default) { HardwareReport.of(DecoderProbe.probe(), displayHdr) }
            report.lines().forEach { Log.i(TAG, it) }
            hardware = report
        }
    }

    override fun onSceneReady() {
        super.onSceneReady()
        // The panel stays where it is put: the thumbsticks must not move the viewer away from it.
        systemManager.findSystem<LocomotionSystem>().enableLocomotion(false)
        scene.enablePassthrough(passthroughOn)
        // The handle bar under the panel moves it, like Horizon OS windows; the panel turns to face the viewer and stays
        // upright while it moves.
        val panel =
            Entity.create(
                Panel(R.id.home_panel),
                Transform(),
                IsdkGrabbable(enabled = true, movementType = IsdkGrabMovementType.AxialBillboard),
                IsdkPanelGrabHandle(),
            )
        val placement =
            PlaceAheadOfViewer(panel, viewerPose = { scene.getViewerPose() }, distanceMeters = 1.2f, dropMeters = 0.15f)
        systemManager.registerSystem(placement)
        panelPlacement = placement
    }

    // The app may start while the headset lies on the desk: put the panel in front of the viewer once it is worn.
    override fun onHMDMounted() {
        super.onHMDMounted()
        panelPlacement?.placeAgain()
    }

    override fun registerPanels(): List<PanelRegistration> =
        listOf(
            ComposeViewPanelRegistration(
                R.id.home_panel,
                composeViewCreator = { _, context ->
                    ComposeView(context).apply {
                        setContent {
                            HomePanel(
                                version = BuildConfig.VERSION_NAME,
                                passthroughOn = passthroughOn,
                                onTogglePassthrough = ::togglePassthrough,
                                hardware = hardware,
                            )
                        }
                    }
                },
                settingsCreator = {
                    UIPanelSettings(
                        shape = QuadShapeOptions(width = PANEL_WIDTH_M, height = PANEL_HEIGHT_M),
                        style = PanelStyleOptions(themeResourceId = R.style.PanelTheme),
                        display = DpPerMeterDisplayOptions(dpPerMeter = DP_PER_METER),
                    )
                },
            ),
        )

    private fun togglePassthrough() {
        passthroughOn = !passthroughOn
        scene.enablePassthrough(passthroughOn)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private companion object {
        const val TAG = "JellyfinQuest"
        const val PANEL_WIDTH_M = 1.1f
        const val PANEL_HEIGHT_M = 0.85f

        // 800 dp per meter at 1.2 m: 48 dp text is about 2.9 degrees tall, comfortable to read.
        const val DP_PER_METER = 800f
    }
}
