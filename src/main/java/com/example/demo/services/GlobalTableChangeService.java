package com.example.demo.services;

import com.example.demo.beans.SessionInfoBean;
import com.example.demo.models.GlobalTableChange;
import com.example.demo.models.TimeTable;
import com.example.demo.models.Userx;
import com.example.demo.models.enums.ChangeType;
import com.example.demo.repositories.GlobalTableChangeRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Scope("session")
public class GlobalTableChangeService {
    private final GlobalTableChangeRepository globalTableChangeRepository;
    private final SessionInfoBean sessionInfoBean;

    public GlobalTableChangeService(GlobalTableChangeRepository globalTableChangeRepository, SessionInfoBean sessionInfoBean) {
        this.globalTableChangeRepository = globalTableChangeRepository;
        this.sessionInfoBean = sessionInfoBean;
    }

    public void create(ChangeType changeType, TimeTable timeTable, String description){
        Userx user = sessionInfoBean.getCurrentUser();
        System.out.println("this is the user"+user);
        GlobalTableChange globalTableChange = new GlobalTableChange();
        globalTableChange.setChangeType(changeType);
        globalTableChange.setTimeTable(timeTable.getName());
        globalTableChange.setChangeUser(user.getUsername());
        globalTableChange.setDescription(description);
        globalTableChange.setDate(LocalDateTime.now());

        globalTableChangeRepository.save(globalTableChange);
    }

    public List<GlobalTableChange> loadAll(){
        return globalTableChangeRepository.findAll();
    }

    public List<GlobalTableChange> loadAllByTimeTable(TimeTable timeTable){
        return globalTableChangeRepository.findAllByTimeTable(timeTable.getName());
    }
}
