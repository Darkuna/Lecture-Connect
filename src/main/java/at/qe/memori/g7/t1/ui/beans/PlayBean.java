package at.qe.memori.g7.t1.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.memori.g7.t1.model.deck.UserDeckData;

@Component
@Scope("session")
public class PlayBean {
    private UserDeckData playingUserDeckData;

    public UserDeckData getPlayingUserDeckData() {
        return playingUserDeckData;
    }

    public void setPlayingUserDeckData(UserDeckData playingUserDeckData) {
        this.playingUserDeckData = playingUserDeckData;
    }

}
