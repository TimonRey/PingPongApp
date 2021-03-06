package com.commercetools.pingpong.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class GameTest {



    public Player getLeftDummy(int setScore, int matchScore, boolean serve, boolean beginningServe) {
        Player leftDummyPlayer = new Player();

        for (leftDummyPlayer.getSetScore(); leftDummyPlayer.getSetScore() < setScore;) {
            leftDummyPlayer.addPoint();
        }

        for (leftDummyPlayer.getMatchScore(); leftDummyPlayer.getMatchScore() < matchScore;) {
            leftDummyPlayer.addMatchScore();
        }

        if (serve) {
            leftDummyPlayer.setServe();
        } else {
            leftDummyPlayer.unsetServe();
        }

        if (beginningServe) {
            leftDummyPlayer.setBeginningServe();
        } else {
            leftDummyPlayer.unsetBeginningServe();
        }

        return leftDummyPlayer;
    }


    public Player getRightDummy(int setScore, int matchScore, boolean serve, boolean beginningServe) {
        Player rightDummyPlayer = new Player();

        for (rightDummyPlayer.getSetScore(); rightDummyPlayer.getSetScore() < setScore;) {
            rightDummyPlayer.addPoint();
        }

        for (rightDummyPlayer.getMatchScore(); rightDummyPlayer.getMatchScore() < matchScore;) {
            rightDummyPlayer.addMatchScore();
        }

        if (serve) {
            rightDummyPlayer.setServe();
        } else {
            rightDummyPlayer.unsetServe();
        }

        if (beginningServe) {
            rightDummyPlayer.setBeginningServe();
        } else {
            rightDummyPlayer.unsetBeginningServe();
        }

        return rightDummyPlayer;
    }




    @Test
    public void shouldReturnNewGame() {
        Player leftTestPlayer = new Player("Hugo");
        Player rightTestPlayer = new Player("Boss");

        Game testGame = new Game(leftTestPlayer, rightTestPlayer);

        assertThat("It should return the name of the leftPlayer", testGame.getPlayerOne().getName(), is("Hugo"));
        assertThat("It should return the name of the rightPlayer", testGame.getPlayerTwo().getName(), is("Boss"));

    }

    @Test
    public void shouldAddSetScoreOfPlayer() {
        Message testMessage = new Message("1", 1);
        Player testplayer = new Player();
        Game testMatch = new Game(testplayer, testplayer);

        testMatch.updateScoreOfPlayer(testplayer, testMessage.getActionType());

        assertThat("The actionType 1 should cause the active player to receive a setpoint", testplayer.getSetScore(), is(1));

    }

    @Test
    public void shouldSubSetScoreOfPlayer() {
        Message testMessageOne = new Message("1", 1);
        Message testMesssageTwo = new Message("1", 2);
        Player testplayer = new Player();
        Game testMatch = new Game(testplayer, testplayer);

        testMatch.updateScoreOfPlayer(testplayer, testMessageOne.getActionType());
        testMatch.updateScoreOfPlayer(testplayer, testMessageOne.getActionType());
        testMatch.updateScoreOfPlayer(testplayer, testMesssageTwo.getActionType());

        assertThat("It should remove one setpoint only for the acting player", testplayer.getSetScore(), is(1));

    }

    @Test
    public void shouldAddMatchPointOfLeftPlayer() {
        Game testMatch = new Game(getLeftDummy(11, 0, true, false), getRightDummy(7, 0, false, true));
        testMatch.updateMatchScoreOfPlayers();
        assertThat("It should remove one matchpoint only for the left player", testMatch.getPlayerOne().getMatchScore(), is(1));

    }

    @Test
    public void shouldAddMatchPointOfRightPlayer() {
        Game testMatch = new Game(getLeftDummy(7, 0, false, true), getRightDummy(11, 0, true, false));
        testMatch.updateMatchScoreOfPlayers();
        assertThat("It should remove one matchpoint only for the right player", testMatch.getPlayerTwo().getMatchScore(), is(1));

    }

    @Test
    public void shouldChangeServingPlayer() {
        Game testMatch = new Game(getLeftDummy(8,0,true,true), getRightDummy(6,0,false,false));
        testMatch.changeServingPlayer(testMatch.getPlayerTwo(),testMatch.getPlayerOne());

        assertThat("It should change the serving value of the leftPlayer into false", testMatch.getPlayerOneServe(), is(false));
        assertThat("It should change the serving value of the rightPlayer into true", testMatch.getPlayerTwoServe(), is(true));
    }

    @Test
    public void shouldSetBeginningServingPlayer() {
        Game testMatch = new Game(getLeftDummy(0,0,false,false), getRightDummy(0,0,false,false));
        testMatch.changeBeginningServingPlayer(testMatch.getPlayerOne(),testMatch.getPlayerTwo());

        assertThat("It should set the very first serve for the leftPlayer into true", testMatch.getPlayerOne().didIServeAtBeginning(), is(true));
        assertThat("It should set the very first serve for the rightPlayer into true", testMatch.getPlayerTwo().didIServeAtBeginning(), is(false));

        assertThat("It should set the normal serve value of the leftPlayer into the same value like above", testMatch.getPlayerOne().amIServing(), is(true));
        assertThat("It should set the normal serve value of the rightPlayer into the same value like above", testMatch.getPlayerTwo().amIServing(), is(false));
    }

    @Test
    public void shouldResetGameLeftPlayer() {
        Message testMessage = new Message("1", 3);
        Game testMatch = new Game(getLeftDummy(5,1,true,false), getRightDummy(10,1,false,true));

        testMatch.updateScoreOfPlayer(testMatch.getPlayerOne(), testMessage.getActionType());

        assertThat("", testMatch.getPlayerOne().getSetScore(), is(0));
        assertThat("", testMatch.getPlayerOne().getMatchScore(), is(0));
        assertThat("", testMatch.getPlayerOne().amIServing(), is(false));
        assertThat("", testMatch.getPlayerOne().didIServeAtBeginning(), is(false));
        // After the reset, variables of the leftPlayer should be default

    }

    @Test
    public void shouldResetGameRightPlayer() {
        Message testMessage = new Message("1", 3);
        Game testMatch = new Game(getLeftDummy(5,1,true,false), getRightDummy(10,1,false,true));

        testMatch.updateScoreOfPlayer(testMatch.getPlayerTwo(), testMessage.getActionType());

        assertThat("", testMatch.getPlayerTwo().getSetScore(), is(0));
        assertThat("", testMatch.getPlayerTwo().getMatchScore(), is(0));
        assertThat("", testMatch.getPlayerTwo().amIServing(), is(false));
        assertThat("", testMatch.getPlayerTwo().didIServeAtBeginning(), is(false));
        // After the reset, variables of the rightPlayer should be default

    }

    @Test
    public void shouldResetEveryValue() {
        Game testMatch = new Game(getLeftDummy(11,1,true,true), getRightDummy(6,1,false,false));
        testMatch.gameOver();

        assertThat("", testMatch.getPlayerOne().getSetScore(), is(0));
        assertThat("", testMatch.getPlayerOne().getMatchScore(), is(0));
        assertThat("", testMatch.getPlayerOne().amIServing(), is(false));
        assertThat("", testMatch.getPlayerOne().didIServeAtBeginning(), is(false));

        assertThat("", testMatch.getPlayerTwo().getSetScore(), is(0));
        assertThat("", testMatch.getPlayerTwo().getMatchScore(), is(0));
        assertThat("", testMatch.getPlayerTwo().amIServing(), is(false));
        assertThat("", testMatch.getPlayerTwo().didIServeAtBeginning(), is(false));
        // After a game ends, each value should be reset automatically
    }

    @Test
    public void shouldCheckIfTheSetScoreDifferenceIsTwo() {
        Game testMatch = new Game(getLeftDummy(14,1,true,false), getRightDummy(12,1,false,true));

        assertThat("It checks if the leftPlayer has two setPoints more than the rightPlayer", testMatch.isTwoPointDifference(testMatch.getPlayerOne(), testMatch.getPlayerTwo()), is(true));
        assertThat("It checks if the rightPlayer has two setPoints more than the leftPlayer", testMatch.isTwoPointDifference(testMatch.getPlayerTwo(), testMatch.getPlayerOne()), is(false));
    }

    @Test
    public void shouldFindOutWhoServesAfterDoubleClickCaseOne() {
        Game scenarioOne = new Game(getLeftDummy(8,0,true,true), getRightDummy(6,0,false,false));
        scenarioOne.whoServesAfterDoubleClick();

        assertThat("It should find out if the leftPlayer is serving after a setPoint was removed", scenarioOne.getPlayerOne().amIServing(), is(true));
        assertThat("It should find out if the rightPlayer is serving after a setPoint was removed", scenarioOne.getPlayerTwo().amIServing(), is(false));
    }

    @Test
    public void shouldFindOutWhoServesAfterDoubleClickCaseTwo() {
        Game scenarioTwo = new Game(getLeftDummy(7,0,true,true), getRightDummy(6,0,false,false));
        scenarioTwo.whoServesAfterDoubleClick();

        assertThat("It should find out if the leftPlayer is serving after a setPoint was removed", scenarioTwo.getPlayerOne().amIServing(), is(false));
        assertThat("It should find out if the leftPlayer is serving after a setPoint was removed", scenarioTwo.getPlayerTwo().amIServing(), is(true));
    }

    @Test
    public void shouldFindOutWhoServesAfterDoubleClickCaseThree() {
        Game scenarioThree = new Game(getLeftDummy(7,0,false,true), getRightDummy(6,0,true,false));
        scenarioThree.whoServesAfterDoubleClick();

        assertThat("It should find out if the leftPlayer is serving after a setPoint was removed", scenarioThree.getPlayerOne().amIServing(), is(true));
        assertThat("It should find out if the leftPlayer is serving after a setPoint was removed", scenarioThree.getPlayerTwo().amIServing(), is(false));
    }

    @Test
    public void shouldSetTheFirstServe() {
        Game testMatch = new Game(getLeftDummy(0,0,false,false), getRightDummy(0,0,false,false));
        testMatch.setFirstServe(testMatch.getPlayerOne());

        assertThat("", testMatch.getPlayerOne().didIServeAtBeginning(), is(true));
        assertThat("", testMatch.getPlayerOne().amIServing(), is(true));
        // After a button was pressed the first time in a game, the acting player should change both serving values into 'true'
    }

    @Test
    public void shouldDecideWhoWillBeginWithServingCaseOne() {
        Game scenarioOne = new Game(getLeftDummy(0,1,false,false), getRightDummy(0,1,true,true));
        scenarioOne.whoBeginsServing();

        assertThat("", scenarioOne.getPlayerOne().amIServing(), is(true));
        assertThat("", scenarioOne.getPlayerOne().didIServeAtBeginning(), is(true));

        assertThat("", scenarioOne.getPlayerTwo().amIServing(), is(false));
        assertThat("", scenarioOne.getPlayerTwo().didIServeAtBeginning(), is(false));
        // After a match was played, the method should decide who will start the next match and set all serving values into the correct boolean
        // CASE: the beginningServe of the rightPlayer is 'true'
    }

    @Test
    public void shouldDecideWhoWillBeginWithServingCaseTwo() {
        Game scenarioTwo = new Game(getLeftDummy(0,1,true,true), getRightDummy(0,1,false,false));
        scenarioTwo.whoBeginsServing();

        assertThat("", scenarioTwo.getPlayerOne().amIServing(), is(false));
        assertThat("", scenarioTwo.getPlayerOne().didIServeAtBeginning(), is(false));

        assertThat("", scenarioTwo.getPlayerTwo().amIServing(), is(true));
        assertThat("", scenarioTwo.getPlayerTwo().didIServeAtBeginning(), is(true));
        // After a match was played, the method should decide who will start the next match and set all serving values into the correct boolean
        // CASE: the beginningServe of the leftPlayer is 'true'
    }

    @Test
    public void shouldDecideWhoServesCaseOne() {
        Game scenarioOne = new Game(getLeftDummy(0,0,false,false), getRightDummy(0,0,false,false));
        scenarioOne.decideWhoServes();

        assertThat("", scenarioOne.getPlayerOneServe(), is(false));
        assertThat("", scenarioOne.getPlayerTwoServe(), is(false));
        // This should test which player is having the next serve
        // CASE: The match has not started yet

    }

    @Test
    public void shouldDecideWhoServesCaseTwo() {
        Game scenarioTwo = new Game(getLeftDummy(12, 0, true, true), getRightDummy(11, 1, false, false));
        scenarioTwo.decideWhoServes();

        assertThat("", scenarioTwo.getPlayerOneServe(), is(false));
        assertThat("", scenarioTwo.getPlayerTwoServe(), is(true));
        // This should test which player is having the next serve
        // CASE: the match is on overtime and the leftPlayer had the last serve
    }

    @Test
    public void shouldDecideWhoServesCaseThree() {
        Game scenarioThree = new Game(getLeftDummy(12, 0, false, false), getRightDummy(11, 1, true, true));
        scenarioThree.decideWhoServes();

        assertThat("", scenarioThree.getPlayerOneServe(), is(true));
        assertThat("", scenarioThree.getPlayerTwoServe(), is(false));
        // This should test which player is having the next serve
        // CASE: The match is on overtime and the rightPlayer had the last serve
    }

    @Test
    public void shouldDecideWhoServesCaseFour() {
        Game scenarioFour = new Game(getLeftDummy(8, 1, true, false), getRightDummy(6, 1, false, true));
        scenarioFour.decideWhoServes();

        assertThat("", scenarioFour.getPlayerOneServe(), is(false));
        assertThat("", scenarioFour.getPlayerTwoServe(), is(true));
        // This should test which player is having the next serve
        // CASE: The match is taking its normal way and the leftPlayer had the last serve
    }

    @Test
    public void shouldDecideWhoServesCaseFive() {
        Game scenarioFive = new Game(getLeftDummy(8, 1, false, true), getRightDummy(10, 1, true, false));
        scenarioFive.decideWhoServes();

        assertThat("", scenarioFive.getPlayerOneServe(), is(true));
        assertThat("", scenarioFive.getPlayerTwoServe(), is(false));
        // This should test which player is having the next serve
        // CASE: The match is taking its normal way and the rightPlayer had the last serve
    }

}
