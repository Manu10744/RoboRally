package utils.json;

import com.google.gson.*;
import modelserver.game.Card;
import modelserver.game.DamageCards.Spam;
import modelserver.game.Game;
import modelserver.game.ProgrammingCards.ProgrammingCard;
import modelserver.game.ProgrammingCards.TurnLeft;
import utils.instructions.*;
import utils.json.protocol.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class is responsible for the deserialization (JSON -> Java) of JSON-Messages.<br>
 * It makes use of the Gson library. A customized Gson instance using a TypeAdapter is used to properly parse the
 * messageBody object (can be e.g. of type HelloServerBody, HelloClientBody, etc.) while deserializing.
 *
 * @author Manuel Neumayer
 * @author Mia
 */
public class JSONDecoder {

    /** This method deserializes a JSON String into a Java Object. It makes use of a
     * customized Gson instance that decides how to properly parse the messageBody object.
     *
     * @param jsonString The JSON String that gets deserialized.
     * @return The Java object created by deserializing the JSON String.
     */
    public static JSONMessage deserializeJSON(String jsonString) {
        // GsonBuilder allows to set settings before parsing stuff
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Register TypeAdapter so Gson knows how to parse the messageBody (java.lang.Object)
        gsonBuilder.registerTypeAdapter(JSONMessage.class, customDeserializer);
        // Gson instance must be created AFTER setting TypeAdapter
        Gson customGson = gsonBuilder.create();

        // Map the received JSON String message into a JSONMessage object
        JSONMessage messageObj = customGson.fromJson(jsonString, JSONMessage.class);

        return messageObj;
    }

    /**
     * This method returns the corresponding {@link ClientInstruction} based on the messageType of a given object
     * representation of a protocol message.
     * @param jsonMessage A {@link JSONMessage} object which is the object representation of a protocol message.
     * @return The corresponding {@link ClientInstruction} of the JSON Object's messageType.
     */
    public static ClientInstruction getClientInstructionByMessageType(JSONMessage jsonMessage) {
        // Declare a instruction and initialize it depending on the message type
        ClientInstruction clientInstruction;

        switch (jsonMessage.getMessageType()) {

            case "HelloServerBody":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.HELLO_SERVER);
                return clientInstruction;
            case "PlayerValues":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.PLAYER_VALUES);
                return clientInstruction;
            case "SetStatus":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SET_STATUS);
                return clientInstruction;
            case "SendChat":
                SendChatBody messageBody = (SendChatBody) jsonMessage.getMessageBody();
                // Is public message
                if (messageBody.getTo() == -1) {
                    clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SEND_CHAT);
                    return clientInstruction;
                } else {
                    // Is private message
                    clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SEND_PRIVATE_CHAT);
                    return clientInstruction;
                }
            case "PlayCard":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.PLAY_CARD);
                return clientInstruction;
            case "SetStartingPoint":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SET_STARTING_POINT);
                return clientInstruction;
            case "SelectCard":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SELECT_CARD);
                return clientInstruction;
            case "CardSelected":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.CARD_SELECTED);
                return clientInstruction;
            case "SelectionFinished":
                clientInstruction = new ClientInstruction(ClientInstruction.ClientInstructionType.SELECTION_FINISHED);
                return clientInstruction;
            default:
                return null;
        }
    }

    /**
     * This method returns the corresponding {@link ServerInstruction} based on the messageType of a given
     * object representation of a protocol message.
     * @param jsonMessage A {@link JSONMessage} object which is the object representation of a protocol message.
     * @return The corresponding {@link ServerInstruction} of the JSON String's messageType.
     */
    public static ServerInstruction getServerInstructionByMessageType(JSONMessage jsonMessage) {
       ServerInstruction serverInstruction;

        switch (jsonMessage.getMessageType()) {
            case "HelloClient":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.HELLO_CLIENT);
                return serverInstruction;
            case "Welcome":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.WELCOME);
                return serverInstruction;
            case "PlayerStatus":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_STATUS);
                return  serverInstruction;
            case "GameStarted":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.GAME_STARTED);
                return serverInstruction;
            case "ReceivedChat":
                ReceivedChatBody messageBody = (ReceivedChatBody) jsonMessage.getMessageBody();
                // Is public message
                if (messageBody.isPrivate() == false) {
                    serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.RECEIVED_CHAT);
                    return serverInstruction;
                } else {
                    // Is private message
                    serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.RECEIVED_PRIVATE_CHAT);
                    return serverInstruction;
                }
            case "Error":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.ERROR);
                return serverInstruction;
            case "StartingPointTaken":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.STARTING_POINT_TAKEN);
                return serverInstruction;
            case "YourCards":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.YOUR_CARDS);
                return serverInstruction;
            case "NotYourCards":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.NOT_YOUR_CARD);
                return serverInstruction;
            case "ShuffleCoding":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.SHUFFLE_CODING);
                return serverInstruction;
            case "CardPlayed":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CARD_PLAYED);
                return serverInstruction;
            case "CurrentPlayer":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CURRENT_PLAYER);
                return serverInstruction;
            case "ActivePhase":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.ACTIVE_PHASE);
                return serverInstruction;
            case "Energy":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.ENERGY);
                return serverInstruction;
            case "CheckPointReached":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CHECKPOINT_REACHED);
                return serverInstruction;
            case "GameFinished":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.GAME_FINISHED);
                return serverInstruction;
            case "PlayerAdded":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_ADDED);
                return serverInstruction;
            case "TimerStarted":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.TIMER_STARTED);
                return serverInstruction;
            case "TimerEnded":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.TIMER_ENDED);
                return serverInstruction;
            case "CardsYouGotNow":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CARDS_YOU_GOT_NOW);
                return serverInstruction;
            case "CurrentCards":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.CURRENT_CARDS);
                return serverInstruction;
            case "Movement":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.MOVEMENT);
                return serverInstruction;
            case "DrawDamage":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.DRAW_DAMAGE);
                return serverInstruction;
            case "PlayerShooting":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_SHOOTING);
                return serverInstruction;
            case "Reboot":
                serverInstruction = new  ServerInstruction(ServerInstruction.ServerInstructionType.REBOOT);
                return serverInstruction;
            case "PlayerTurning":
                serverInstruction = new ServerInstruction(ServerInstruction.ServerInstructionType.PLAYER_TURNING);
                return serverInstruction;

            default:
                return null;
        }
    }

    /**
     * This is a custom Deserializer for Gson. Gson needs a way to decide how to parse the messageBody property of a JSON
     * String as the equivalent java object representation. This can't be done by Gson itself because the
     * messageBody variable of a {@link JSONMessage} is of type {@link java.lang.Object} which can contain any other
     * object type. This Deserializer tells Gson how to parse it by checking the messageType property of the JSON String.
     */
     public static JsonDeserializer<JSONMessage> customDeserializer = new JsonDeserializer<JSONMessage>() {
         @Override
         public JSONMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
             // Get the overall JSON String with type and body
             JsonObject jsonMessage = jsonElement.getAsJsonObject();

             // Get only the messageBody part of the JSON String so we can access its variables
             JsonObject messageBody = jsonMessage.get("messageBody").getAsJsonObject();

             // Get the messageType of the JSON String
             String messageType = jsonMessage.get("messageType").getAsString();

             // For parsing JSON Arrays into Java ArrayLists<?>
             Gson arrayListParser = new GsonBuilder().create();

             if (messageType.equals("HelloClient")) {
                 HelloClientBody helloClientBody = new HelloClientBody(
                         messageBody.get("protocol").getAsString()
                 );

                return new JSONMessage("HelloClient", helloClientBody);
             } else if (messageType.equals("HelloServer")) {
                 HelloServerBody helloServerBody = new HelloServerBody(
                         messageBody.get("group").getAsString(),
                         messageBody.get("isAI").getAsBoolean(),
                         messageBody.get("protocol").getAsString()
                 );

                 return new JSONMessage("HelloServer", helloServerBody);
             } else if (messageType.equals("Welcome")) {

                 WelcomeBody welcomeBody = new WelcomeBody(
                         messageBody.get("playerID").getAsInt()
                 );

                 return new JSONMessage("Welcome", welcomeBody);
             } else if (messageType.equals("PlayerValues")) {

                 PlayerValuesBody playerValuesBody = new PlayerValuesBody(
                         messageBody.get("name").getAsString(),
                         messageBody.get("figure").getAsInt()
                 );

                 return new JSONMessage("PlayerValues", playerValuesBody);
             } else if (messageType.equals("PlayerAdded")) {

                 PlayerAddedBody playerAddedBody = new PlayerAddedBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("name").getAsString(),
                         messageBody.get("figure").getAsInt()
                 );

                 return new JSONMessage("PlayerAdded", playerAddedBody);
             } else if (messageType.equals("SetStatus")) {

                 SetStatusBody setStatusBody = new SetStatusBody(
                         messageBody.get("ready").getAsBoolean()
                 );

                 return new JSONMessage("SetStatus", setStatusBody);
             } else if (messageType.equals("PlayerStatus")) {

                 PlayerStatusBody playerStatusBody = new PlayerStatusBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("ready").getAsBoolean()
                 );

                 return new JSONMessage("PlayerStatus", playerStatusBody);
             } else if (messageType.equals("SendChat")) {

                 SendChatBody sendChatBody = new SendChatBody(
                         messageBody.get("message").getAsString(),
                         messageBody.get("to").getAsInt()
                 );

                 return new JSONMessage("SendChat", sendChatBody);
             } else if (messageType.equals("ReceivedChat")) {

                 ReceivedChatBody receivedChatBody = new ReceivedChatBody(
                         messageBody.get("message").getAsString(),
                         messageBody.get("from").getAsInt(),
                         messageBody.get("isPrivate").getAsBoolean()
                 );

                 return new JSONMessage("ReceivedChat", receivedChatBody);
             } else if (messageType.equals("Error")) {

                 ErrorBody errorBody = new ErrorBody(
                         messageBody.get("error").getAsString()
                 );

                 return new JSONMessage("Error", errorBody);
             } else if (messageType.equals("PlayCard")) {

                 PlayCardBody playCardBody = new PlayCardBody(
                         messageBody.get("card").getAsString()
                 );

                 return new JSONMessage("PlayCard", playCardBody);
             } else if (messageType.equals("CardPlayed")) {

                 CardPlayedBody cardPlayedBody = new CardPlayedBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("card").getAsString()
                 );

                 return new JSONMessage("CardPlayed", cardPlayedBody);
             } else if (messageType.equals("CurrentPlayer")) {

                 CurrentPlayerBody currentPlayerBody = new CurrentPlayerBody(
                         messageBody.get("playerID").getAsInt()
                 );

                 return new JSONMessage("CurrentPlayer", currentPlayerBody);
             } else if (messageType.equals("ActivePhase")) {

                 ActivePhaseBody activePhaseBody = new ActivePhaseBody(
                         messageBody.get("phase").getAsInt()
                 );

                 return new JSONMessage("ActivePhase", activePhaseBody);
             } else if (messageType.equals("SetStartingPoint")) {

                 SetStartingPointBody setStartingPointBody = new SetStartingPointBody(
                         messageBody.get("x").getAsInt(),
                         messageBody.get("y").getAsInt()
                 );

                 return new JSONMessage("SetStartingPoint", setStartingPointBody);
             } else if (messageType.equals("StartingPointTaken")) {

                 StartingPointTakenBody startingPointTakenBody = new StartingPointTakenBody(
                         messageBody.get("x").getAsInt(),
                         messageBody.get("y").getAsInt(),
                         messageBody.get("playerID").getAsInt()
                 );

                 return new JSONMessage("StartingPointTaken", startingPointTakenBody);
             } else if (messageType.equals("YourCards")) {
                 /* CHECK HOW TO HANDLE ARRAYS
                 System.out.println("Is YourCards");

                 JsonArray arr = messageBody.get("cardsInHand").getAsJsonArray();
                 ArrayList<Card> cardsInHand = arrayListParser.fromJson(arr, ArrayList.class);

                 YourCardsBody yourCardsBody = new YourCardsBody(
                           cardsInHand,
                           messageBody.get("cardsInPile").getAsInt()
                 );
                 return new JSONMessage("YourCards", yourCardsBody);
                  */
                 return null; // Temporarily, until fixed
             } else if (messageType.equals("NotYourCards")) {

                 NotYourCardsBody notYourCardsBody = new NotYourCardsBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("cardsInHand").getAsInt(),
                         messageBody.get("cardsInPile").getAsInt()
                 );

                 return new JSONMessage("NotYourCards", notYourCardsBody);
             } else if (messageType.equals("ShuffleCoding")) {

                 ShuffleCodingBody shuffleCodingBody = new ShuffleCodingBody(
                         messageBody.get("playerID").getAsInt()
                 );

                 return new JSONMessage("ShuffleCoding", shuffleCodingBody);
             } else if (messageType.equals("SelectCard")) {

                 SelectCardBody selectCardBody = new SelectCardBody(
                         messageBody.get("card").getAsString(),
                         messageBody.get("register").getAsInt()
                 );

                 return new JSONMessage("SelectCard", selectCardBody);
             } else if (messageType.equals("CardSelected")) {

                 CardSelectedBody cardSelectedBody = new CardSelectedBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("register").getAsInt()
                 );

                 return new JSONMessage("CardSelected", cardSelectedBody);
             } else if (messageType.equals("SelectionFinished")) {

                 SelectionFinishedBody selectionFinishedBody = new SelectionFinishedBody(
                         messageBody.get("playerID").getAsInt()
                 );

                 return new JSONMessage("SelectionFinished", selectionFinishedBody);
             } else if (messageType.equals("TimerStarted")) {

                 TimerStartedBody timerStartedBody = new TimerStartedBody();
                 return new JSONMessage("TimerStarted", timerStartedBody);
             } else if (messageType.equals("TimerEnded")) {
                /* CHECK HOW TO HANDLE ARRAY
                 TimerEndedBody timerEndedBody = new TimerEndedBody(
                         messageBody.get("playerIDs").
                 );

                 return new JSONMessage("TimerEnded", timerEndedBody);
                 */
                return null; // Temporarily, until fixed
             } else if (messageType.equals("CardsYouGotNow")) {
                 /* CHECK HOW TO HANDLE ARRAY
                 CardsYouGotNowBody cardsYouGotNowBody = new CardsYouGotNowBody(
                         messageBody.get("cards").get
                 );

                 return new JSONMessage("CardsYouGotNow", cardsYouGotNowBody);
                  */
                 return null; // Temporarily, until fixed
             } else if (messageType.equals("CurrentCards")) {
                 /* CHECK HOW TO HANDLE ARRAY
                 CurrentCardsBody currentCardsBody = new CurrentCardsBody(
                         messageBody.get("activeCards").get
                 );

                 return new JSONMessage("CurrentCards", currentCardsBody);
                  */
                 return null; // Temporarily, until fixed
             } else if (messageType.equals("Movement")) {

                 MovementBody movementBody = new MovementBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("x").getAsInt(),
                         messageBody.get("y").getAsInt()
                 );

                 return new JSONMessage("Movement", movementBody);
             } else if (messageType.equals("DrawDamage")) {
                 /* CHECK HOW TO HANDLE ARRAY
                 DrawDamageBody drawDamageBody = new DrawDamageBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("cards").get
                 );

                 return new JSONMessage("DrawDamage", drawDamageBody);
                  */
                 return null; // Temporarily, until fixed
             } else if (messageType.equals("PlayerShooting")) {
                 PlayerShootingBody playerShootingBody = new PlayerShootingBody();
                 return new JSONMessage("PlayerShooting", playerShootingBody);
             } else if (messageType.equals("Reboot")) {

                 RebootBody rebootBody = new RebootBody(
                         messageBody.get("playerID").getAsInt()
                 );

                 return new JSONMessage("Reboot", rebootBody);
             } else if (messageType.equals("PlayerTurning")) {

                 PlayerTurningBody playerTurningBody = new PlayerTurningBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("direction").getAsString()
                 );

                 return new JSONMessage("PlayerTurning", playerTurningBody);
             } else if (messageType.equals("Energy")) {

                 EnergyBody energyBody = new EnergyBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("count").getAsInt(),
                         messageBody.get("source").getAsString()
                 );

                 return new JSONMessage("Energy", energyBody);
             } else if (messageType.equals("CheckPointReached")) {

                 CheckPointReachedBody checkPointReachedBody = new CheckPointReachedBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("number").getAsInt()
                 );

                 return new JSONMessage("CheckPointReached", checkPointReachedBody);
             } else if (messageType.equals("GameFinished")) {
                 GameFinishedBody gameFinishedBody = new GameFinishedBody(
                         messageBody.get("playerID").getAsInt()
                 );

                 return new JSONMessage("GameFinished", gameFinishedBody);
             }
             // Something went wrong, could not deserialize!
             return new JSONMessage("Error", new ErrorBody("Fatal error: Could not resolve message." +
                     "Something went wrong while deserializing."));
         }
     };


    // JSON Message No.2 according to the protocol PDF
    // NOTE: This code is pure example code to show how deserialization works! Will be deleted later.
    public static void main(String[] args) {
        JSONMessage message = new JSONMessage("NotYourCards", new NotYourCardsBody(43, 5, 9));
        String s = JSONEncoder.serializeJSON(message);
        System.out.println(s);

        JSONMessage msg = JSONDecoder.deserializeJSON(s);
        System.out.println(msg.getMessageBody());
        System.out.println(msg.getMessageBody().getClass());
    }
}
