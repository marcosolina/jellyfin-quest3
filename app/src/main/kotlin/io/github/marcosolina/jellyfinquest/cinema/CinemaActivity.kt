package io.github.marcosolina.jellyfinquest.cinema

import android.content.Context
import android.content.Intent
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
import io.github.marcosolina.jellyfinquest.R
import io.github.marcosolina.jellyfinquest.scene.PlaceAheadOfViewer
import io.github.marcosolina.jellyfinquest.window.WindowActivity

/**
 * The immersive part of the hybrid app (ADR-003). For now an empty room over passthrough with one control panel;
 * later the cinema screen and 180/360 video live here.
 */
class CinemaActivity : AppSystemActivity() {
    private var passthroughOn by mutableStateOf(true)
    private var panelPlacement: PlaceAheadOfViewer? = null

    override fun registerFeatures(): List<SpatialFeature> = listOf(VRFeature(this), ComposeFeature())

    override fun onSceneReady() {
        super.onSceneReady()
        // The panel stays where it is put: the thumbsticks must not move the viewer away from it.
        systemManager.findSystem<LocomotionSystem>().enableLocomotion(false)
        scene.enablePassthrough(passthroughOn)
        // The handle bar under the panel moves it; the panel turns to face the viewer and stays upright while it moves.
        val panel =
            Entity.create(
                Panel(R.id.cinema_panel),
                Transform(),
                IsdkGrabbable(enabled = true, movementType = IsdkGrabMovementType.AxialBillboard),
                IsdkPanelGrabHandle(),
            )
        val placement =
            PlaceAheadOfViewer(panel, viewerPose = { scene.getViewerPose() }, distanceMeters = 1.2f, dropMeters = 0.15f)
        systemManager.registerSystem(placement)
        panelPlacement = placement
    }

    // The headset may have been taken off and put on again: bring the panel back in front of the viewer.
    override fun onHMDMounted() {
        super.onHMDMounted()
        panelPlacement?.placeAgain()
    }

    override fun registerPanels(): List<PanelRegistration> =
        listOf(
            ComposeViewPanelRegistration(
                R.id.cinema_panel,
                composeViewCreator = { _, context ->
                    ComposeView(context).apply {
                        setContent {
                            CinemaPanel(
                                passthroughOn = passthroughOn,
                                onTogglePassthrough = ::togglePassthrough,
                                onBackToWindow = { WindowActivity.returnFromImmersive(this@CinemaActivity) },
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

    companion object {
        private const val PANEL_WIDTH_M = 0.8f
        private const val PANEL_HEIGHT_M = 0.45f

        // 800 dp per meter at 1.2 m: 48 dp text is about 2.9 degrees tall, comfortable to read.
        private const val DP_PER_METER = 800f

        /** Opens the cinema from the window, as Meta's hybrid apps do. */
        fun start(context: Context) {
            val cinema = Intent(context, CinemaActivity::class.java)
            cinema.action = Intent.ACTION_MAIN
            cinema.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(cinema)
        }
    }
}
