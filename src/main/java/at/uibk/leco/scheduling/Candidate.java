package at.uibk.leco.scheduling;

import at.uibk.leco.models.CourseSession;
import at.uibk.leco.models.RoomTable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Candidate {
    private AvailabilityMatrix availabilityMatrix;
    private int day;
    private int slot;
    private int duration;
    private float preferredRatio;

    public Candidate(AvailabilityMatrix availabilityMatrix, int day, int slot, int duration, float preferredRatio) {
        this.availabilityMatrix = availabilityMatrix;
        this.day = day;
        this.slot = slot;
        this.duration = duration;
        this.preferredRatio = preferredRatio;
    }

    /**
     * This method checks if two candidate's timings are intersecting
     * @param candidate to be compared with
     * @return true if the candidates are intersecting, false if not
     */
    public boolean intersects(Candidate candidate) {
        return AvailabilityMatrix.toTiming(this).intersects(AvailabilityMatrix.toTiming(candidate));
    }

    /**
     * Checks if two candidates are in the same room
     * @param candidate to be compared with
     * @return true if the candidates are from the same room
     */
    public boolean isInSameRoom(Candidate candidate) {
        return this.availabilityMatrix.getRoomTable().equals(candidate.getAvailabilityMatrix().getRoomTable());
    }

    /**
     * Checks if two candidates are from the same day
     * @param candidate to be compared with
     * @return true if their day attribute has the same value, false if not
     */
    public boolean hasSameDay(Candidate candidate) {
        return this.day == candidate.getDay();
    }

    /**
     * This method assigns a courseSession to the candidate's availability matrix
     * @param courseSession to be assigned
     */
    public void assignToCourseSession(CourseSession courseSession){
        availabilityMatrix.assignCourseSession(this, courseSession);
    }

    /**
     * This method clears the candidate in the availability matrix.
     */
    public void clearInAvailabilityMatrix(){
        availabilityMatrix.clearCandidate(this);
    }

    public RoomTable getRoomTable(){
        return availabilityMatrix.getRoomTable();
    }

    public String toString(){
        return String.format("Candidate %s, Day: %d, Slot: %d", availabilityMatrix != null
                ? availabilityMatrix.getRoomTable().getRoomId() : "", day, slot);
    }

    /**
     * This method calculates and return the end slot of the candidate
     * @return slot index of end slot
     */
    public int getEndSlot(){
        return slot + duration / 15 - 1;
    }
}
