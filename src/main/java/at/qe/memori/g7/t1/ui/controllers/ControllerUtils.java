package at.qe.memori.g7.t1.ui.controllers;

import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ControllerUtils {

    private ControllerUtils() {
    }

    public static void redirect(String path) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(path);
        } catch (IOException err) {
            // take the not so clean way to redirect if the other does not work
            PrimeFaces.current().executeScript("window.location.href=%s".formatted(path));
        }
    }

    public static void addMessage(FacesMessage.Severity severity, String title, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, detail));
    }
}
