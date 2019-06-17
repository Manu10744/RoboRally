package utils.json;

import com.google.gson.*;
import modelserver.game.Card;

import modelserver.game.ProgrammingCards.*;
import modelserver.game.DamageCards.*;
import utils.json.protocol.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class is responsible for the serialization (Java -> JSON) of {@link JSONMessage} Objects.<br>
 * It makes use of the Gson library. A customized Gson instance using a TypeAdapter is used to properly parse the
 * {@link Card} objects according to the protocol.
 *
 * @author Manuel Neumayer
 */
public class JSONEncoder {

    public static String serializeJSON(JSONMessage messageObj) {
        // GsonBuilder allows to set settings before parsing stuff
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Tell Gson how to parse Card objects by registering a TypeAdapter
        gsonBuilder.registerTypeAdapter(Card.class, customSerializer);
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.setPrettyPrinting();
        // After (!) settings, create Gson instance to serialize
        Gson gson = gsonBuilder.create();

        // Convert the object into a JSON String
        String jsonString = gson.toJson(messageObj);

        return jsonString;
    }

    /**
     * This is a custom Serializer for Gson. Gson needs a way to decide how to properly parse {@link Card} objects into JSON
     * according to the protocol.This Deserializer tells Gson how to parse it by parsing only the cardName as JSON property for each card object
     * that occurs in the messageBody of the {@link JSONMessage} object that needs to be serialized.
     */
    public static JsonSerializer<Card> customSerializer = new JsonSerializer<Card>() {
        @Override
        public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {
            // Parse only the cardName for each Card object
            JsonPrimitive cardName = new JsonPrimitive(card.getCardName());

            return cardName;
        }
    };



    // NOTE: This code is pure example code to show how deserialization works! Will be deleted later.
    // ONLY FOR TESTING!!
    public static void main(String[] args) {
        // Create a new Gson object
        Gson gson = new Gson();

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
        // TODO: GAMESTARTED !

        String json = JSONEncoder.serializeJSON(messages.get(11));
        System.out.println(json);


    }
}
