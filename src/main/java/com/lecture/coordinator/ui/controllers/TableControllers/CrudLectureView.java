package com.lecture.coordinator.ui.controllers.TableControllers;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.services.CourseService;
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
public class CrudLectureView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Course> courses;

    private Course selectedCourse;

    private List<Course> selectedCourses;

    private final CourseService courseService;

    private String courseType = "VO";
    private String tmpName = "";


    public CrudLectureView(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostConstruct
    private void init() {
        this.courses = courseService.getAllCourses();
        this.selectedCourses = new ArrayList<>();
        this.selectedCourse = null;
    }

    public void openNew() {
        this.selectedCourse = new Course();
    }

    public void createLecture() {
        //parameter werden in rooms.xhtml :dialogs:roomCreationDialog gesetzt
        nameCourse();
        try {
            selectedCourse = this.courseService.createCourse(selectedCourse);
            this.courses.add(selectedCourse);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Kurs erstellt"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        } finally {
            this.selectedCourse = null;
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

    public void doDeleteCourse() {
        this.courseService.deleteCourse(selectedCourse);
        this.courses.remove(selectedCourse);
        selectedCourse = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Room deleted"));
        PrimeFaces.current().executeScript("PF('manageuserDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Rooms");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedCourses()) {
            int size = this.selectedCourses.size();
            return size > 1 ? size + " Rooms selected" : "1 user selected";
        }

        return "Delete";
    }

    public boolean hasSelectedCourses() {
        return this.selectedCourses != null && !this.selectedCourses.isEmpty();
    }

    public void deleteSelectedCourses() {
        courseService.deleteMultipleCourses(selectedCourses);
        this.courses.removeAll(selectedCourses);
        this.selectedCourse = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("multiple Rooms removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Rooms");
        PrimeFaces.current().executeScript("PF('dtRooms').clearFilters()");
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public List<Course> getSelectedCourses() {
        return selectedCourses;
    }

    public void setSelectedCourses(List<Course> selectedCourses) {
        this.selectedCourses = selectedCourses;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getTmpName() {
        return tmpName;
    }

    public void setTmpName(String tmpName) {
        this.tmpName = tmpName;
    }

    public void nameCourse() {
        this.selectedCourse.setName(courseType + tmpName);
    }
}
