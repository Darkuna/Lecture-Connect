package com.lecture.coordinator.ui.controllers.TableControllers;

import com.lecture.coordinator.model.Room;
import com.lecture.coordinator.services.RoomService;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
public class CrudRoomView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private List<Room> rooms;

    private Room selectedRoom;

    private List<Room> selectedRooms;

    private final RoomService roomService;

    public CrudRoomView(RoomService RoomService) {
        this.roomService = RoomService;
    }

    @PostConstruct
    private void init() {
        this.rooms = roomService.getAllRooms();
        this.selectedRooms = new ArrayList<>();
        this.selectedRoom = null;
    }

    public void openNew() {
        this.selectedRoom = new Room();
    }

    public void createRoom() {
        //parameter werden in rooms.xhtml :dialogs:roomCreationDialog gesetzt
        try {
            selectedRoom = this.roomService.createRoom(selectedRoom);
            this.rooms.add(selectedRoom);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Room created"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        } finally {
            this.selectedRoom = null;
            PrimeFaces.current().ajax().update("form:messages", "form:dt-Rooms");
            PrimeFaces.current().executeScript("PF('dtRooms').clearFilters()");
        }
    }

    /*
    public void doSaveUser() throws UserAlreadyExistsException, UserRequiredFieldEmptyException, UserInvalidEmailException {
        selectedUser = this.Roomservice.saveUser(selectedUser);
        Rooms.add(this.selectedUser);
        init();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Updated"));
        PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Rooms");
    }
    */

    public void doDeleteRoom() {
        this.roomService.deleteRoom(selectedRoom);
        this.rooms.remove(selectedRoom);
        selectedRoom = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Room deleted"));
        PrimeFaces.current().executeScript("PF('manageuserDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Rooms");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedRooms()) {
            int size = this.selectedRooms.size();
            return size > 1 ? size + " Rooms selected" : "1 user selected";
        }

        return "Delete";
    }

    public boolean hasSelectedRooms() {
        return this.selectedRooms != null && !this.selectedRooms.isEmpty();
    }

    public void deleteSelectedRooms() {
        roomService.deleteMultipleRooms(selectedRooms);
        this.rooms.removeAll(this.selectedRooms);
        this.selectedRooms = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("multiple Rooms removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Rooms");
        PrimeFaces.current().executeScript("PF('dtRooms').clearFilters()");
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public List<Room> getSelectedRooms() {
        return selectedRooms;
    }

    public void setSelectedRooms(List<Room> selectedRooms) {
        this.selectedRooms = selectedRooms;
    }
}
