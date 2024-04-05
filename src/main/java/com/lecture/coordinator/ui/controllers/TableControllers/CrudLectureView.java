package com.lecture.coordinator.ui.controllers.TableControllers;

import com.lecture.coordinator.model.Course;
import com.lecture.coordinator.services.CourseService;
import com.lecture.coordinator.ui.beans.CourseTypeBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    private final CourseTypeBean bean;

    public CrudLectureView(CourseService courseService, CourseTypeBean bean) {
        this.courseService = courseService;
        this.bean = bean;
    }

    @PostConstruct
    private void init() {
        this.courses = courseService.loadAllCourses();
        this.selectedCourses = new ArrayList<>();
        this.selectedCourse = null;
    }

    public void openNew() {
        this.selectedCourse = new Course();
    }

    public void createLecture() {
        //parameter werden in lectures.xhtml :dialogs:roomCreationDialog gesetzt
        try {
            this.selectedCourse.setCourseType(bean.getSelectedCourseType());
            selectedCourse = this.courseService.createCourse(selectedCourse);
            this.courses.add(selectedCourse);
        } catch (Exception e) {
        } finally {
            this.selectedCourse = null;
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
}
