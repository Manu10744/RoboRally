### Known Issues
* Chat - tooltip is not working
* StartScreen is not responsive and doesnt take up 100% width
* Automatic filling of GridPane by reading deserialized GameStarted message not implemented yet
* Ability to choose map not implemented yet

## Version [0.3.0] 
### New Features
* 'GameStarted' protocol message can now be properly deserialized, which makes sending maps to the client possible. - *(Manu)*
* Timer method in ProgrammingPhase is now available - *(Vincent)*
* ChooseRobot view added - *(Jessie)*
* ScoreBoard view added - *(Jessie)*
* Deck functionality finished - *(Vincent)*
* Basic game Logic created - *(Vincent)*

### Changes
* Private messages implemented - *(Ivan)*
* StartScreen is FullHD now - *(Vincent)*
* StartScreen without RoboRally-Text was added - *(Vincent)*

### Bugfixes

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