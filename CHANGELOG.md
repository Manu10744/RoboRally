### Known Issues
* Ability to choose map not implemented yet
* Event for INFORMED_ABOUT_ALREADY_CONNECTED_PLAYERS needed because informer message about ready status of already connected players comes in too fast -
  information of already connected players is not 100% processed at that time.
* Maybe event for INFORMED_ABOUT_READYSTATUS_OF_ALREADY_CONNECTED_PLAYERS if needed
* Shop needs to be implemented
* AI needs to be implemented
* SelectionFinished currently being sent for each player finished programming. Needs to be changed (Send it only once)
* Timer before map starts needs to be implemented (10 secs)
* Remaining moves need to be implemented (7/9)

## Version [0.5.0]
### New Features
* Implemented ability to play 'TurnLeft' and 'TurnRight' cards - *(Mia, Manu)*
* When setting a StartPoint, a robot is now always looking towards the map. - *(Manu)*
* Implemented ability to play 'MoveI', 'MoveII', 'MoveIII', 'BackUp' and 'UTurn' - *(Verena, Manu)*
* When a player finishes programming, a 30 second timer is started which determines the end of the programming phase. - *(Manu)*

### Changes


### Bugfixes

## Version [0.4.0] 
### New Features 
* Freshly connected clients are now informed about ready status of already connected clients, but only by a workaround (making the Thread sleep). - *(Manu)*
* Map can now callback when it was completely loaded. - *(Manu)*
* All maps have been added to the game. - *(Jessie, Verena, Vincent)*
* Each players cards are now loaded in a box - *(Verena, Manu)*
* Theres a Timer before the map loads - *(Vincent)*
* Antenna methods implemented to determine distance between map objects by fields or vector length - *(Ivan)*

### Changes
* Server and client are now creating and updating own player (and robot) instances simultaneously. Additionally, the client also keeps track of other players. - *(Mia, Manu)*
* All maps can now be displayed by calculating the map dimensions and giving that information to the loading method. - *(Verena)*
* Gaps in maps can now be displayed - *(Jessie, Verena)*
* Map parameters were removed. - *(Manu)*
* For each Tile of type StartPoint setting an ID dynamically consisting out of the coordinates is now working. Startpoints can now be set no matter their position on the map. - (*Verena*)
* Walls are now displayed on top of lasers instead of vice versa. - *(Manu)*
* Reimplemented the deck methods - *(Vincent)*

### Bugfixes
* Fixed bug that happened when player was disallowed to set StartPoint after trying to set an already taken StartPoint - *(Manu)*
* Player is now able to enter a username again after trying to choose an already taken username. - *(Manu)*
* Drag and Drop was fixed, is now working with one click - *(Verena)*

## Version [0.3.2] 
### New Features 
* Added new Maps (riskyCrossing, highOctane, sprintCramp, corridorBlitz, fractionation, burnout, lostBearings, twister, dodgeThis, heavyMergeArea, deathTrap, passingLane) - *(Jessie, Verena, Vincent)*

### Changes
* There is Parameter support for passingLane map - *(Vincent)*

### Bugfixes
* Choosing robot by one click enabled - *(Ivan)*

## Version [0.3.1] 
### New Features 
* Robot can now be set by clicking on robots in the chooseRobot view - *(Verena, Jessi)*
* Player avatars are now set after choosing robot - *(Verena)*
* Already assigned robots are now disabled in each freshly connected client's view. - *(Manu)*
* Lobby background view added. - *(Verena)*
* Player can now set his StartPoint individually. - *(Mia, Verena, Manu)*

### Changes
* Replaced font to RoboRally font - *(Verena)*
* Proper view sequence implemented (StartScreen -> chooseRobot -> Lobby -> Map -> ScoreBoard) - *(A-Team)*

### Bugfixes
* Ready Button's width is now correct. - *(Ivan)*
* New connecting clients are now informed about ready status of already connected players - *(Ivan)*
* Responsiveness for PlayerMat (completely implemented) and OpponentMat (detailed example code and description) - *(Ivan)*
* Program terminates by set on close request when windows are closed. - *(Ivan)*

## Version [0.3.0] 
### New Features
* 'GameStarted' protocol message can now be properly deserialized, which makes sending maps to the client possible. - *(Manu)*
* Responsive ChooseRobot view added - *(Verena, Jessie)*
* Responsive ScoreBoard view added - *(Verena, Jessie)*
* Maps can now be displayed after receiving 'GameStarted' messages - *(Mia, Manu, Ivan)*
* Deck functionality finished - *(Vincent)*
* Game Wiki is completed now *(Verena)*
* Zooming and Scrolling works smoother now, Map can be reset to its default position by pressing 'Z' - *(Ivan)*
* Private messages implemented - *(Ivan)*

### Changes
* GridPane of Map now contains one group per field to achieve more convenient editing of map - *(Mia)*
* Each group is stored in a HashMap to access each field efficiently - *(Mia)*
* Basic Game Logic removed from Cards and they were made non-static - *(Vincent)*

### Bugfixes
* Chat - tooltips are now working - *(Ivan)*

## Version [0.2.0]
### New Features
* Transfered logic for each JSON message to a Distributer with corresponding handler functions and thus replaced switch-case blocks in both Server and Client. - *(Manu)*
* By implementing module-info (reflection sensitive) a.o.t. no more vm-option declaration is needed - *(Ivan)*
* Map is zoomable (keyboard) and scrollable (by Mouse or keyboard). *(Ivan)*

### Changes
* Instructions and everything related to them was completely removed. Everything runs solely according to the JSON message protocol now. - *(Manu)*

### Bugfixes
* Fixed bug that happened when GridPane was filled but map elements were not shown once the application was started. - *(Manu)*
* Fixed bug of GridPane starting in mid while filling it with ImageViews. Now starting in bottom left corner. - *(Manu)*
* Map (GridPane and its' ImageViews) is fully responsive - *(Ivan)*


## Version [0.1.1]
### New Features
* Game Wiki can now be opened by clicking the '?' Button - *(Verena)*
* Registration and Chat via protocol. - *(Ivan)*
* Player can now set his ready status and inform the server about it. - *(Manu)*
 
### Changes
* Adjusted package names (view -> views, viewmodel -> viewmodels, modelclient -> client, modelserver - server) - *(Manu)*

### Bugfixes
* Fixed playerID bug - ID x was given to the client, but ID x + 1 was given to his ClientWrapper object. - *(Manu)*

## Version [0.1.0]

### New Features
* First wiki draft created - *(Verena, Jessie)*
* Switching between wiki tabs is now possible - *(Verena)*
* GUI adapts automatically to users' screen setup - *(Ivan)*
* GUI was completely refactored to enable fully responsiveness - *(Ivan)*
* Added possibility to exclude fields from deserialization and serialization by the Gson @Expose tag - Fields without that identifier are ignored by Gson. - *(Manu)*

### Changes
* Main - runs server and client parallel (with two GUIs ATM -> developer mode) - *(Ivan)*
* No longer needed code related to Start, Init and Join is removed and replaced by Ready which controls player status - *(Ivan)*
* Existing Server and Client instructions are adapted to new RoboRally instructions (in Server- and Client-classes), not yet implemented instructions are now created in Server- and Client-classes (ATM most without function) - *(Ivan)*
* JSON Deserializing and Serializing is now working - *(Manu)*
* Getting instructions by messageType of JSON objects is now working - *(Manu, Mia)*
* Fixed Robot names - *(Vincent)*
* Completely restructured and enhanced JSON<->Java protocol communication system by customizing Gson deserialization: 
  Added class for each protocol message and customized Gson so it parses messageBody smart and easy. - *(Manu)*

### Bugfixes
* Fixed trouble with socket creation and thus Server - Client connection by restoration of original server port parameter *(Ivan)*
* Issue with running server thread after application was closed and thereby blocking server port for successful 
application restart is solved by setting server threat to daemon. As a positive side effect server now  automatically terminates its own
 thread after last non-daemon thread (here GUI) is gone by closing last GUI window *(Ivan)*
* Client BufferReader issue was replaced by StringBuilder - *(Ivan)*

## Version [0.0.1]
### New Features
* Added Programming and Damage Cards - *(Vincent)*
* Implemented Chat - Server, Client, Instructions aso. - *(Ivan)*
* Player registration, game-init, -join with age and -start works - *(Ivan)*

### Changes
* Chat - preset dummy-server IP+Port - *(Ivan)*
* Chat - activate text fields in proper order - *(Ivan)*
* Chat - implement button actions - *(Ivan)*

### Bugfixes


## Version [0.0.0]
### New Features
* Basic project structure added - *(Team)*
* Views and corresponding Controllers added - *(Manu, Anna-Maria)*
* Game Images added - *(Vincent, Verena)*

### Changes

### Bugfixes