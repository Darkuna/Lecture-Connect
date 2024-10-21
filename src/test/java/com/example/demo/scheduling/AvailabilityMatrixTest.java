package com.example.demo.scheduling;

import com.example.demo.constants.TimingConstants;
import com.example.demo.models.AvailabilityMatrix;
import com.example.demo.models.CourseSession;
import com.example.demo.models.RoomTable;
import com.example.demo.models.Timing;
import com.example.demo.models.enums.Day;
import com.example.demo.models.enums.TimingType;
import com.example.demo.services.CourseSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("dev")
public class AvailabilityMatrixTest {

    @Test
    public void testTimeToSlotIndex(){
        int n = 50;
        List<LocalTime> timings = new ArrayList<>();
        LocalTime startTime = TimingConstants.START_TIME;
        for(int i = 0; i < n; i++){
            timings.add(startTime.plusMinutes(15*i));
        }

        List<Integer> slotIndexes = new ArrayList<>();
        for(LocalTime time : timings){
            slotIndexes.add(AvailabilityMatrix.timeToSlotIndex(time));
        }

        for(int i = 0; i < n; i++){
            assertEquals(i, slotIndexes.get(i));
        }

        System.out.println(AvailabilityMatrix.timeToSlotIndex(TimingConstants.END_TIME));
    }

    @Test
    public void testInitializeAvailabilityMatrixWithOneAssignedCourseSession(){
        long minusTime = Duration.between(TimingConstants.START_TIME, TimingConstants.END_TIME).toMinutes();
        long timeBefore = 5 * Duration.between(TimingConstants.START_TIME, TimingConstants.END_TIME).toMinutes();
        Timing timing = new Timing(TimingConstants.START_TIME, TimingConstants.END_TIME, Day.MONDAY, TimingType.ASSIGNED);
        CourseSession courseSession = new CourseSession();
        courseSession.setId(444444L);
        courseSession.setName("AssignedCourseSession");
        courseSession.setTiming(timing);

        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(null, List.of(courseSession));

        assertEquals(timeBefore - minusTime, availabilityMatrix.getTotalAvailableTime());
    }

    @Test
    public void testInitializeAvailabilityMatrixWithOneTimingConstraint(){
        Timing timing = new Timing(TimingConstants.START_TIME, TimingConstants.END_TIME, Day.MONDAY, TimingType.BLOCKED);

        RoomTable roomTable = new RoomTable();
        roomTable.setComputersAvailable(false);
        roomTable.setCapacity(20);
        roomTable.setRoomId("Test");
        roomTable.setTimingConstraints(List.of(timing));

        AvailabilityMatrix availabilityMatrix = new AvailabilityMatrix(List.of(timing), null);

    }
}
