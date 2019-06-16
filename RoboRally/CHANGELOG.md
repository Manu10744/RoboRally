### Known Issues
* Chat - tooltip is not working
* Sending and Receiving of JSON Strings still needs to be implemented for every instruction
* Deserialization of protocol messages 'YourCards', 'TimerEnded', 'CardsYouGotNow', 'CurrentCards' and 'DrawDamage' not
  not possible yet.

## Version [0.1.1]

### New Features
* First wiki draft created - *(Verena, Jessie)*
* Switching between wiki tabs is now possible - *(Verena)*
* GUI adapts automatically to users' screen setup - *(Ivan)*
* GUI was completely refactored to enable fully responsiveness - *(Ivan)*

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

## Version [0.1.0]

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