package at.uibk.leco.scheduling;

/**
 * This enum class is used in the collision check to give feedback about the different kinds of collisions a courseSession
 * can have.
 */
public enum CollisionType {
    COURSE_TIMING_CONSTRAINTS,
    SEMESTER_INTERSECTION,
    ROOM_COMPUTERS,
    ROOM_CAPACITY
}

