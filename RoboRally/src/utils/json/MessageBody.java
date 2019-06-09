package utils.json;

import javafx.beans.property.IntegerProperty;

/**
 * This class is the wrapper class for the body of the JSON messages according to the
 * protocol.
 *
 * @author Manuel Neumayer
 */
public class MessageBody {


    // For Connection Setups
    private String protocol;
    private String group;
    private Boolean isAI;

    // For Player-Server communication before the game
    private Integer playerID;
    private String name;
    private Integer figure;
    private Boolean ready;

    // For messages
    private String message;
    private Integer to;
    private Integer from;
    private Boolean isPrivate;

    // For error messages
    private String error;

    // For playing cards
    private String card;
    private Integer phase;
    private Integer x;
    private Integer y;
    private Integer register;
    private String direction;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Boolean getAI() {
        return isAI;
    }

    public void setAI(Boolean AI) {
        isAI = AI;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFigure() {
        return figure;
    }

    public void setFigure(Integer figure) {
        this.figure = figure;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getRegister() {
        return register;
    }

    public void setRegister(Integer register) {
        this.register = register;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }





    // TODO: cardsInHand[],  playerIDs[],  cards[], activeCards[],
}