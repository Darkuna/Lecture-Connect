package com.lecture.coordinator.ui.controllers.TableControllers;

import com.lecture.coordinator.model.Userx;
import com.lecture.coordinator.services.UserService;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
public class CrudUserView implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Userx> users;

    private Userx selectedUser;

    private List<Userx> selectedUsers;

    private final UserService userService;

    public CrudUserView(UserService userxService) {
        this.userService = userxService;
    }

    @PostConstruct
    private void init(){
        this.users = userService.getAllUsers();
        this.selectedUsers = new ArrayList<>();
        this.selectedUser = null;
    }

    public List<Userx> getUsers() {
        return users;
    }

    public Userx getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Userx selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Userx> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Userx> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void openNew() {
        this.selectedUser = new Userx();
    }

    public void saveUser() {
        if (this.selectedUser.getUsername() == null) {
            this.users.add(this.selectedUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Added"));
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageuserDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }

    public void deleteUser() {
        this.users.remove(this.selectedUser);
        this.selectedUsers.remove(this.selectedUser);
        this.selectedUser = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("user Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedUsers()) {
            int size = this.selectedUsers.size();
            return size > 1 ? size + " users selected" : "1 user selected";
        }

        return "Delete";
    }

    public boolean hasSelectedUsers() {
        return this.selectedUsers != null && !this.selectedUsers.isEmpty();
    }

    public void deleteSelectedUsers() {
        this.users.removeAll(this.selectedUsers);
        this.selectedUsers = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("users Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
        PrimeFaces.current().executeScript("PF('dtusers').clearFilters()");
    }


}
