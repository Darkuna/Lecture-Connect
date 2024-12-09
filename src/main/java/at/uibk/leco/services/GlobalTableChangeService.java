package at.uibk.leco.services;

import at.uibk.leco.beans.SessionInfoBean;
import at.uibk.leco.models.GlobalTableChange;
import at.uibk.leco.models.TimeTable;
import at.uibk.leco.models.Userx;
import at.uibk.leco.models.enums.ChangeType;
import at.uibk.leco.repositories.GlobalTableChangeRepository;
import org.springframework.context.annotation.Scope;
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
