package utils.json;

import com.google.gson.*;
import server.game.Card;
import server.game.ProgrammingCards.*;
import utils.json.protocol.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class is responsible for the deserialization (JSON -> Java) of JSON Messages being in their String representation.<br>
 * It makes use of the Gson library. A customized Gson instance using a TypeAdapter is used to properly parse the
 * messageBody object (can be e.g. of type HelloServerBody, HelloClientBody, etc.) while deserializing. Another customized
 * Gson instance is explicitly used to deserialize JSON Arrays of cards. (For more details see
 * {@link JSONDecoder#cardArrayDeserializer})
 *
 * @author Manuel Neumayer
 * @author Mia
 */
public class JSONDecoder {

    /** This method deserializes a JSON String into a Java Object. It makes use of a
     * customized Gson instance that decides how to properly parse the messageBody object.
     *
     * @param jsonString The JSON String that needs to be deserialized.
     * @return The {@link JSONMessage} created by deserializing the JSON String.
     */
    public static JSONMessage deserializeJSON(String jsonString) {
        // GsonBuilder allows to set settings before parsing stuff
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Register TypeAdapter so Gson knows how to parse the messageBody (java.lang.Object)
        gsonBuilder.registerTypeAdapter(JSONMessage.class, customDeserializer);
        // After (!) settings, create Gson instance to deserialize
        Gson customGson = gsonBuilder.create();

        // Map the received JSON String message into a JSONMessage object
        JSONMessage messageObj = customGson.fromJson(jsonString, JSONMessage.class);

        return messageObj;
    }

    /** This is a custom deserializer for Gson. Gson needs a way to decide how to deserialize JSON Arrays containg cards.
     *  This is where this deserializer comes into action. It deserializes every card that occurs inside JSON into its
     *  equivalent Java object.
     *  For deserializing <b>single</b> cards, see {@link JSONDecoder#deserializeCards(JsonObject, String)}.
     */
    public static JsonDeserializer<ArrayList<Card>> cardArrayDeserializer = new JsonDeserializer<ArrayList<Card>>() {
        @Override
        public ArrayList<Card> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            // We need the whole JSON message first to enter the messageBody
            JsonObject jsonMessage = jsonElement.getAsJsonObject();
            String messageType = jsonMessage.get("messageType").getAsString();

            // Get the messageBody as its a needed parameter for the deserializeCards() method
            JsonObject messageBody = jsonMessage.get("messageBody").getAsJsonObject();

            ArrayList<Card> result = new ArrayList<Card>();

            // JSON Array containing the cards
            JsonArray cardsArray;
            // For 'YourCards' protocol message
            if (messageType.equals("YourCards")) {
                // Get the JSON Array containing the cards
                cardsArray = messageBody.get("cardsInHand").getAsJsonArray();

                // Deserialize each card string into proper card object
                for (JsonElement card : cardsArray) {
                    // Add each card object to the result
                    result.add(deserializeCards(messageBody, card.getAsString()));
                }
                return result;
            }

            // For 'CardsYouGotNow' and 'DrawDamage' protocol messages
            else if (messageType.equals("CardsYouGotNow") || messageType.equals("DrawDamage")) {
                // Get the JSON Array containing the cards
                cardsArray = messageBody.get("cards").getAsJsonArray();

                for (JsonElement card : cardsArray) {
                    result.add(deserializeCards(messageBody, card.getAsString()));
                }
                return result;
            }
            return null; // Error
        }
    };

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
             Gson arrayListParser = new GsonBuilder()
                     .excludeFieldsWithoutExposeAnnotation()
                     .registerTypeAdapter(ArrayList.class, cardArrayDeserializer)
                     .create();

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
                 String cardName = messageBody.get("card").getAsString();

                 PlayCardBody playCardBody = new PlayCardBody(
                        deserializeCards(jsonMessage, cardName)
                 );
                 return new JSONMessage("PlayCard", playCardBody);
             } else if (messageType.equals("CardPlayed")) {
                 String cardName = messageBody.get("card").getAsString();

                 CardPlayedBody cardPlayedBody = new CardPlayedBody(
                         messageBody.get("playerID").getAsInt(),
                         deserializeCards(jsonMessage, cardName)
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
                 ArrayList<Card> cardsInHand = arrayListParser.fromJson(jsonElement, ArrayList.class);

                 YourCardsBody yourCardsBody = new YourCardsBody(
                           cardsInHand,
                           messageBody.get("cardsInPile").getAsInt()
                 );
                 return new JSONMessage("YourCards", yourCardsBody);
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
                 String cardName = messageBody.get("card").getAsString();

                 SelectCardBody selectCardBody = new SelectCardBody(
                         deserializeCards(jsonMessage, cardName),
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
                 // Get the JSON Array containing the IDs
                 JsonArray jsonIDArray = messageBody.get("playerIDs").getAsJsonArray();

                 ArrayList<Integer> result = new ArrayList<Integer>();

                 // Deserialize each ID as int and add to list
                 for (JsonElement id : jsonIDArray) {
                     result.add(id.getAsInt());
                 }

                 TimerEndedBody timerEndedBody = new TimerEndedBody(
                    result
                 );
                 return new JSONMessage("TimerEnded", timerEndedBody);
             } else if (messageType.equals("CardsYouGotNow")) {
                 ArrayList<Card> cards = arrayListParser.fromJson(jsonElement, ArrayList.class);

                 CardsYouGotNowBody cardsYouGotNowBody = new CardsYouGotNowBody(
                         cards
                 );

                 return new JSONMessage("CardsYouGotNow", cardsYouGotNowBody);
             } else if (messageType.equals("CurrentCards")) {
                 // Array containing the objects that contain playerID and card
                 JsonArray activeCardsObjectArray = messageBody.get("activeCards").getAsJsonArray();

                 ArrayList<CurrentCardsBody.ActiveCardsObject> activeCards = new ArrayList<>();
                 for (JsonElement object : activeCardsObjectArray) {
                     /* Cast each JsonElement (being a JSON object containing playerID and card) to
                        a JsonObject so we can enter the playerID and card properties
                      */
                     JsonObject activeCardsObject = object.getAsJsonObject();
                     String cardName = activeCardsObject.get("card").getAsString();
                     // For each JSON object, create a Java object and get the properties in the correct format
                     activeCards.add(
                         new CurrentCardsBody.ActiveCardsObject(
                                 activeCardsObject.get("playerID").getAsInt(),
                                 deserializeCards(activeCardsObject, cardName)
                         )
                     );
                 }

                 CurrentCardsBody currentCardsBody = new CurrentCardsBody(
                         activeCards
                 );

                 return new JSONMessage("CurrentCards", currentCardsBody);
             } else if (messageType.equals("Movement")) {

                 MovementBody movementBody = new MovementBody(
                         messageBody.get("playerID").getAsInt(),
                         messageBody.get("x").getAsInt(),
                         messageBody.get("y").getAsInt()
                 );

                 return new JSONMessage("Movement", movementBody);
             } else if (messageType.equals("DrawDamage")) {
                 ArrayList<Card> cards = arrayListParser.fromJson(jsonElement, ArrayList.class);

                 DrawDamageBody drawDamageBody = new DrawDamageBody(
                         messageBody.get("playerID").getAsInt(),
                         cards
                 );

                 return new JSONMessage("DrawDamage", drawDamageBody);
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

    /** This method deserializes a JSON card into the equivalent Java object.
     * @param jsonMessageBody A part of JSON containing the card that need to be deserialized. Only needed for Gson
     *                        to know the context.
     * @param cardName The name of the card that needs to be deserialized.
     * @return The exact card-subclass of {@link Card} that corresponds to the name of the card.
     */
     public static Card deserializeCards(JsonObject jsonMessageBody, String cardName) {
         Gson gson = new Gson();
         if (cardName.equals("MoveI")) {
             MoveI result = gson.fromJson(jsonMessageBody, MoveI.class);
             return result;
         } else if (cardName.equals("MoveII")) {
             MoveII result = gson.fromJson(jsonMessageBody, MoveII.class);
             return result;
         } else if (cardName.equals("MoveIII")) {
             MoveIII result = gson.fromJson(jsonMessageBody, MoveIII.class);
             return result;
         } else if (cardName.equals("TurnLeft")) {
             TurnLeft result = gson.fromJson(jsonMessageBody, TurnLeft.class);
             return result;
         } else if (cardName.equals("TurnRight")) {
             TurnRight result = gson.fromJson(jsonMessageBody, TurnRight.class);
             return result;
         } else if (cardName.equals("UTurn")) {
             UTurn result = gson.fromJson(jsonMessageBody, UTurn.class);
             return result;
         } else if (cardName.equals("BackUp")) {
             BackUp result = gson.fromJson(jsonMessageBody, BackUp.class);
             return result;
         } else if (cardName.equals("PowerUp")) {
             PowerUp result = gson.fromJson(jsonMessageBody, PowerUp.class);
             return result;
         } else if (cardName.equals("Again")) {
             Again result = gson.fromJson(jsonMessageBody, Again.class);
             return result;
         }
         return null; // Error
     }

    // NOTE: This code is pure example code to show how deserialization works! Will be deleted later.
    // ONLY FOR TESTING!!!
    public static void main(String[] args) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new TurnLeft());
        cards.add(new MoveI());
        cards.add(new Again());

        ArrayList<Integer> IDs = new ArrayList<Integer>();
        IDs.add(1);
        IDs.add(3);
        IDs.add(6);

        ArrayList<CurrentCardsBody.ActiveCardsObject> activeCards = new ArrayList<CurrentCardsBody.ActiveCardsObject>();
        activeCards.add(new CurrentCardsBody.ActiveCardsObject(42, new MoveI()));
        activeCards.add(new CurrentCardsBody.ActiveCardsObject(1337, new Again()));

        ArrayList<JSONMessage> messages = new ArrayList<JSONMessage>();
        /*0*/ messages.add(new JSONMessage("HelloClient", new HelloClientBody("Version 1.0")));
        /*1*/ messages.add(new JSONMessage("HelloServer", new HelloServerBody("TolleTrolle", false, "Version 1.0")));
        /*2*/ messages.add(new JSONMessage("Welcome", new WelcomeBody(42)));
        /*3*/ messages.add(new JSONMessage("PlayerValues", new PlayerValuesBody("Nr. 5", 5)));
        /*4*/ messages.add(new JSONMessage("PlayerAdded", new PlayerAddedBody(42, "Nr. 5",5)));
        /*5*/ messages.add(new JSONMessage("SetStatus", new SetStatusBody(true)));
        /*6*/ messages.add(new JSONMessage("PlayerStatus", new PlayerStatusBody(42, true)));
        /*7*/ messages.add(new JSONMessage("SendChat", new SendChatBody("Yoh Bob! How are you?", 42)));
        /*8*/ messages.add(new JSONMessage("ReceivedChat", new ReceivedChatBody("Yoh Bob! How are you?", 43, true)));
        /*9*/ messages.add(new JSONMessage("Error", new ErrorBody("SOMETHING WENT WRONG!!")));
        /*10*/ messages.add(new JSONMessage("PlayCard", new PlayCardBody(new TurnLeft())));
        /*11*/ messages.add(new JSONMessage("CardPlayed", new CardPlayedBody(2, new MoveI())));
        /*12*/ messages.add(new JSONMessage("CurrentPlayer", new CurrentPlayerBody(7)));
        /*13*/ messages.add(new JSONMessage("ActivePhase", new ActivePhaseBody(3)));
        /*14*/ messages.add(new JSONMessage("SetStartingPoint", new SetStartingPointBody(4, 2)));
        /*15*/ messages.add(new JSONMessage("StartingPointTaken", new StartingPointTakenBody(4,2,42)));
        /*16*/ messages.add(new JSONMessage("YourCards", new YourCardsBody(cards, 9001)));
        /*17*/ messages.add(new JSONMessage("NotYourCards", new NotYourCardsBody(42, 9, 9001)));
        /*18*/ messages.add(new JSONMessage("ShuffleCoding", new ShuffleCodingBody(42)));
        /*19*/ messages.add(new JSONMessage("SelectCard", new SelectCardBody(new Again(), 5)));
        /*20*/ messages.add(new JSONMessage("CardSelected", new CardSelectedBody(42, 5)));
        /*21*/ messages.add(new JSONMessage("SelectionFinished", new SelectionFinishedBody(42)));
        /*22*/ messages.add(new JSONMessage("TimerStarted", new TimerStartedBody()));
        /*23*/ messages.add(new JSONMessage("TimerEnded", new TimerEndedBody(IDs)));
        /*24*/ messages.add(new JSONMessage("CardsYouGotNow", new CardsYouGotNowBody(cards)));
        /*25*/ messages.add(new JSONMessage("CurrentCards", new CurrentCardsBody(activeCards)));
        /*26*/ messages.add(new JSONMessage("Movement", new MovementBody(42,4,2)));
        /*27*/ messages.add(new JSONMessage("DrawDamage", new DrawDamageBody(42, cards)));
        /*28*/ messages.add(new JSONMessage("PlayerShooting", new PlayerShootingBody()));
        /*29*/ messages.add(new JSONMessage("Reboot", new RebootBody(42)));
        /*30*/ messages.add(new JSONMessage("PlayerTurning", new PlayerTurningBody(42, "left")));
        /*31*/ messages.add(new JSONMessage("Energy", new EnergyBody(42, 1, "Field")));
        /*32*/ messages.add(new JSONMessage("CheckPointReached", new CheckPointReachedBody(42, 3)));
        /*33*/ messages.add(new JSONMessage("GameFinished", new GameFinishedBody(42)));
        ///*34*/ messages.add(new JSONMessage("GameStarted", new GameStartedBody(map)));
        // TODO: GAMESTARTED !

        String s = JSONEncoder.serializeJSON(messages.get(33));
        System.out.println("THIS NEEDS TO BE DESERIALIZED: ");
        System.out.println(s);

        JSONMessage msg = JSONDecoder.deserializeJSON(s);
        System.out.println("DESERIALIZED: " + msg.getMessageBody().getClass());
        GameStartedBody msgbody = (GameStartedBody) msg.getMessageBody();

    }
}
