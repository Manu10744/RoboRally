package server.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import server.game.Tiles.Pit;
import server.game.Tiles.PushPanel;
import server.game.Tiles.Wall;

import java.util.Map;

/**
 * This class defines what a card shall be in the game. <br>
 * There are more specific cards defined in the classes that inherit from Card.
 *
 * @author Vincent Tafferner
 * @author Verena Sadtler
 */
public abstract class Card {

    @Expose
    @SerializedName("card")
    public String cardName;

    //This is the constructor :D
    public Card() {
        cardName = "Card";
    }

    /**
     * This is the method that is called, when the Card's effect is activated. <br>
     * It will be overwritten in each subclass.
     */
    public abstract void activateCard(Player player, Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap);

    public abstract boolean isDamageCard();
    /**
     * This method checks if there is a Pit, Wall or PushPanel in the robots way.
     * @param pitMap hashmap with all the pits of the map
     * @param wallMap hashmap with all the walls of the map
     * @param pushPanelMap hashmap with all the pushPanels of the map
     * @param oldPos player position before moving
     * @param newPos player position after moving
     * @param oppositeOwnOrientation opposite players lineOfSight
     * @param ownOrientation players lineOfSight
     * @return true if robot is allowed to move
     */

    public boolean isValidMove(Map<String, Pit> pitMap, Map<String, Wall> wallMap, Map<String, PushPanel> pushPanelMap, String oldPos, String newPos, String oppositeOwnOrientation, String ownOrientation) {
        Pit currentFieldPit = pitMap.get(oldPos);
        Wall currentFieldWall = wallMap.get(oldPos);
        PushPanel currentFieldPush = pushPanelMap.get(oldPos);

        Pit nextFieldPit = pitMap.get(newPos);
        Wall nextFieldWall = wallMap.get(newPos);
        PushPanel nextFieldPush = pushPanelMap.get(newPos);

        // Current field is Pit
        if (currentFieldPit != null) {
            return false;
        }

            // Current field has Wall or PushPanel
            if (currentFieldWall != null || currentFieldPush != null) {
                //Current field has Wall
                if (currentFieldWall != null) {
                    // Wall is in front of own robot
                    if (currentFieldWall.getOrientations().contains(ownOrientation)) {
                        return false;
                    } else {
                        // Check if Wall or PushPanel on the next field blocks the way
                        if (nextFieldWall != null || nextFieldPush != null) {
                            if (nextFieldWall != null) {
                                // Wall on next field blocks the way
                                if (nextFieldWall.getOrientations().contains(oppositeOwnOrientation)) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                            // Next field has PushPanel
                            else {
                                // Check if PushPanel on next field blocks the way
                                if (nextFieldPush.getOrientations().contains(ownOrientation)) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }

                        }
                    }
                }

                // Current field has PushPanel
                if (currentFieldPush != null) {
                    // PushPanel is in front of own robot
                    if (currentFieldPush.getOrientations().contains(oppositeOwnOrientation)) {
                        return false;
                    } else {
                        // Check if Wall or PushPanel on the next field blocks the way
                        if (nextFieldWall != null || nextFieldPush != null) {
                            if (nextFieldWall != null) {
                                //Wall on next field block the way
                                if (nextFieldWall.getOrientations().contains(oppositeOwnOrientation)) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                            //  Next field has PushPanel
                            else
                                // Check if PushPanel on next field blocks the way {
                                if (nextFieldPush.getOrientations().contains(ownOrientation)) {
                                    return false;
                                } else {
                                    return true;
                                }
                        }
                    }
                }
            }

            // Current field has no Wall or PushPanel
            if (currentFieldWall == null || currentFieldPush == null) {
                // Check if next field also has no Wall or PushPanel
                if (nextFieldWall == null || nextFieldPush == null) {
                    // Next field has no Wall
                    if (nextFieldWall == null) {
                        // Check if next field has PushPanel
                        if (nextFieldPush != null) {
                            // Check if PushPanel on next field blocks the way
                            if (nextFieldPush.getOrientations().contains(ownOrientation)) {
                                return false;
                            } else {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                    // Next Field has no PushPanel
                    if (nextFieldPush == null) {
                        // Check if next field has Wall
                        if (nextFieldWall != null) {
                            if (nextFieldWall.getOrientations().contains(oppositeOwnOrientation)) {
                                return false;
                            } else {
                                return true;
                            }

                        } else {
                            return true;
                        }
                    }
                }
            }
            return true;
        }


        /**
         * This method simply returns the name of the card.
         * @return cardName
         */
        public String getCardName () {
            return cardName;
        }

    }
