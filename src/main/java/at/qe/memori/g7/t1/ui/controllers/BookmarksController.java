package at.qe.memori.g7.t1.ui.controllers;

import at.qe.memori.g7.t1.model.deck.Deck;
import at.qe.memori.g7.t1.model.deck.UserDeckData;
import at.qe.memori.g7.t1.services.UserDeckDataService;
import at.qe.memori.g7.t1.ui.beans.PlayBean;
import at.qe.memori.g7.t1.ui.beans.SessionInfoBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;

@Component
@Scope("view")
public class BookmarksController {
    private final UserDeckDataService userDeckDataService;

    private final SessionInfoBean sessionInfoBean;

    private final PlayBean playBean;

    public BookmarksController(UserDeckDataService userDeckDataService, SessionInfoBean sessionInfoBean, PlayBean playBean) {
        this.userDeckDataService = userDeckDataService;
        this.sessionInfoBean = sessionInfoBean;
        this.playBean = playBean;
    }

    public void deleteBookmark(UserDeckData bookmark) {
        userDeckDataService.deleteUserDeckData(bookmark);

        ControllerUtils.addMessage(FacesMessage.SEVERITY_INFO, "Delete", bookmark.getDeck().getName() + " bookmark deleted!");
    }

    public Iterable<UserDeckData> getBookmarks() {
        return userDeckDataService.get(sessionInfoBean.getCurrentUser());
    }

    public void playDeck(UserDeckData deck) {
        playBean.setPlayingUserDeckData(deck);
        sessionInfoBean.setDeck(deck.getDeck());
        ControllerUtils.redirect("play.xhtml");
    }

    public void bookmarkDeck(Deck deck) {
        if (isBookmarked(deck)) {
            ControllerUtils.addMessage(FacesMessage.SEVERITY_INFO, "Bookmark", deck.getName() + " is already bookmarked!");
            return;
        }

        UserDeckData newBookmark = new UserDeckData(sessionInfoBean.getCurrentUser(), deck);

        userDeckDataService.createUserDeckData(newBookmark);

        ControllerUtils.addMessage(FacesMessage.SEVERITY_INFO, "Bookmark", deck.getName() + " was bookmarked!");
    }

    public boolean isBookmarked(Deck deck) {
        return userDeckDataService.isBookmarked(sessionInfoBean.getCurrentUser(), deck);
    }


}
