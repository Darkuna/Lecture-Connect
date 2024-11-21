package com.example.demo.scheduling;

/**
 * This enum class is used in the collision check to give feedback about the different kinds of collisions a courseSession
 * can have.
 */
public enum CollisionType {
    ROOM_CAPACITY,
    COURSE_TIMING_CONSTRAINTS,
    ROOM_COMPUTERS,
    SEMESTER_INTERSECTION
}
