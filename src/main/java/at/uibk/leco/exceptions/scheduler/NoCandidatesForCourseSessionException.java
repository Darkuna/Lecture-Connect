package at.uibk.leco.exceptions.scheduler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoCandidatesForCourseSessionException extends RuntimeException {
    public NoCandidatesForCourseSessionException(String message) {
        super(message);
    }
}
