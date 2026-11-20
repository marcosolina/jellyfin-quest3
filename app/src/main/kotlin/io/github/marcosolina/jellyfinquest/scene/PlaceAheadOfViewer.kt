package io.github.marcosolina.jellyfinquest.scene

import com.meta.spatial.core.Entity
import com.meta.spatial.core.Pose
import com.meta.spatial.core.SystemBase
import com.meta.spatial.core.Vector3
import com.meta.spatial.toolkit.Transform

/**
 * Puts [entity] in front of the viewer as soon as head tracking delivers a pose, and again after each [placeAgain];
 * in between the entity stays fixed in the room. Until tracking starts the viewer pose is the identity, so placement
 * waits for a real one.
 */
class PlaceAheadOfViewer(
    private val entity: Entity,
    private val viewerPose: () -> Pose,
    private val distanceMeters: Float,
    private val dropMeters: Float,
) : SystemBase() {
    private var placed = false

    /** Places the entity again at the next frame, in front of wherever the viewer looks then. */
    fun placeAgain() {
        placed = false
    }

    override fun execute() {
        if (placed) return
        val viewer = viewerPose()
        if (viewer.isApproximatelyEqual(Pose(), POSE_EPSILON, POSE_EPSILON)) return
        // Only the heading counts, so the panel stands upright whatever the tilt of the head. +Z is the viewer's
        // forward direction and a panel shows its -Z face, so with the viewer's heading it faces the viewer.
        val heading = viewer.removePitchAndRoll()
        entity.setComponent(Transform(heading * Pose(Vector3(0f, -dropMeters, distanceMeters))))
        placed = true
    }

    private companion object {
        const val POSE_EPSILON = 1e-4f
    }
}
