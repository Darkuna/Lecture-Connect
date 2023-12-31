package at.qe.memori.g7.t1.ui.controllers;

import at.qe.memori.g7.t1.model.card.Difficulty;
import at.qe.memori.g7.t1.model.card.UserCardData;
import at.qe.memori.g7.t1.services.UserCardDataService;
import at.qe.memori.g7.t1.ui.beans.PlayBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Queue;

@Component
@Scope("view")
public class PlayController {

    private final UserCardDataService userCardDataService;

    private final PlayBean playBean;

    private Queue<UserCardData> learnCards;

    private UserCardData cardData;

    private Boolean showResult = false;

    private Boolean empyDeck;

    public PlayController(UserCardDataService userCardDataService, PlayBean playBean) {
        this.userCardDataService = userCardDataService;
        this.playBean = playBean;

        getCardsData();
    }

    private void getCardsData() {
        // initialize data
        this.learnCards = userCardDataService.getLearnableCards(playBean.getPlayingUserDeckData());

        if (this.learnCards.isEmpty()) {
            this.empyDeck = true;
        } else {
            this.empyDeck = false;
        }

        nextCard();
    }

    public void goHome() {
        ControllerUtils.redirect("welcome.xhtml");
    }

    public void voteCard(Difficulty difficulty) {
        if (cardData.vote(difficulty)) {
            // not sufficiently learned
            this.learnCards.offer(cardData);
        }
        userCardDataService.saveUserCardData(cardData);
        nextCard();
    }

    public void nextCard() {
        if (!learnCards.isEmpty()) {
            this.cardData = learnCards.poll();
        } else {
            setShowResult();
        }
    }

    public String showQuestion() {
        return cardData.isReversed() ? cardData.getCard().getBack() : cardData.getCard().getFront();
    }

    public String showAnswer() {
        return cardData.isReversed() ? cardData.getCard().getFront() : cardData.getCard().getBack();
    }

    public UserCardData getCard() {
        return cardData;
    }

    public Boolean getShowResult() {
        return showResult;
    }

    public void setShowResult() {
        this.showResult = true;
    }

    public Boolean getEmpyDeck() {
        return empyDeck;
    }

    public void setEmpyDeck(Boolean empyDeck) {
        this.empyDeck = empyDeck;
    }
}
