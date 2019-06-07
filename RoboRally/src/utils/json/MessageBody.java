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
    private boolean isAI;

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


    // Constructor for messageBody for JSON message No.2
    public MessageBody(String protocol) {
        this.protocol = protocol;
    }


    public String getProtocol() {
        return protocol;
    }

    public String getGroup() {
        return group;
    }

    public boolean isAI() {
        return isAI;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }


    public boolean isReady() {
        return ready;
    }


    public String getMessage() {
        return message;
    }

    public int getTo() {
        return to;
    }

    public int getFrom() {
        return from;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getError() {
        return error;
    }

    public String getCard() {
        return card;
    }

    public int getPhase() {
        return phase;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRegister() {
        return register;
    }

    public String getDirection() {
        return direction;
    }


}


// TODO: cardsInHand[],  playerIDs[],  cards[], activeCards[],