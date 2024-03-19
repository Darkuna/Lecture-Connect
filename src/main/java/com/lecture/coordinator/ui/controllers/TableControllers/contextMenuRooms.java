package com.lecture.coordinator.ui.controllers.TableControllers;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

@Component
@Scope("view")
public class contextMenuRooms implements Serializable {
    private List<Room> rooms;

    private Room selectedRoom;

    private final RoomService roomService;

    public contextMenuRooms(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostConstruct
    public void init() {
        rooms = roomService.getFirstRooms(10);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public Room editRoom(){
        return null;
        //TODO Logik implementieren, damit ein Raum bearbeitet werden kann
    }

    public void deleteRoom() {
        rooms.remove(selectedRoom);
        selectedRoom = null;
        roomService.deleteRoom(selectedRoom);
    }

    public void viewRoom() {
        // no-op
    }
}
