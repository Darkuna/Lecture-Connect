package com.example.demo.services;

import com.example.demo.models.GlobalTableChange;
import com.example.demo.models.TimeTable;
import com.example.demo.models.Userx;
import com.example.demo.models.enums.ChangeType;
import com.example.demo.repositories.GlobalTableChangeRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Scope("session")
public class GlobalTableChangeService {
    private final GlobalTableChangeRepository globalTableChangeRepository;

    public GlobalTableChangeService(GlobalTableChangeRepository globalTableChangeRepository) {
        this.globalTableChangeRepository = globalTableChangeRepository;
    }

    public void create(ChangeType changeType, TimeTable timeTable, String description){
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        GlobalTableChange globalTableChange = new GlobalTableChange();
        globalTableChange.setChangeType(changeType);
        globalTableChange.setTimeTable(timeTable);
        globalTableChange.setUser(user);
        globalTableChange.setDescription(description);
        globalTableChange.setDate(LocalDateTime.now());

        globalTableChangeRepository.save(globalTableChange);
    }
}
