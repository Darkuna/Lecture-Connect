package com.lecture.coordinator.ui.beans;

import com.lecture.coordinator.model.enums.CourseType;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;

@ManagedBean
@Scope("view")
public class CourseTypeBean {
    private CourseType selectedCourseType;

    public CourseTypeBean() {
        this.selectedCourseType = CourseType.VO;
    }

    public CourseType getSelectedCourseType() {
        return selectedCourseType;
    }

    public void setSelectedCourseType(CourseType selectedCourseType) {
        this.selectedCourseType = selectedCourseType;
    }

    public CourseType[] getAvailableCourseTypes() {
        return CourseType.values();
    }


}
