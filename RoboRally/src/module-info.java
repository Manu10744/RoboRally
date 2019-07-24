/**
 * Module declaration is defined in module-info. The keywords exports, module, opens, requires, uses aso are restricted
 * keywords.
 *
 * requires: A requires module directive specifies that this module depends on another module—this relationship is
 * called a module dependency. Each module must explicitly state its dependencies.
 *
 * opens: An opens module directive indicates that a specific package’s public types are accessible to code in other
 * modules at runtime only. Also, all the types in the specified package are accessible via reflection.
 *
 * uses: A uses module directive specifies a service used by this module — making the module a service consumer. A
 * service is an object of a class that implements the interface or extends the abstract class specified in the uses
 * directive.
 *
 * exports: An exports module directive specifies one of the module’s packages whose public types should be accessible
 * to code in all other modules.
 *
 * @author Ivan Dovecar
 */

module RoboRally {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;
    requires java.desktop;
    requires gson;
    requires java.sql;
    requires javafx.media;

    // opens all project packages
    opens client;
    opens resources.css;
    opens resources.images.backgrounds;
    opens resources.images.cards;
    opens resources.images.mapelements;
    opens resources.images.others;
    opens resources.images.raw;
    opens resources.images.robots;
    opens resources.maps;
    opens server;
    opens server.game;
    opens server.game.DamageCards;
    opens server.game.ProgrammingCards;
    opens server.game.Tiles;
    opens utils;
    opens utils.json;
    opens utils.json.protocol;
    opens viewmodels;
    opens views;
    opens main;

    // prevents issues related to reflection
    uses java.sql.Driver;

    // exports main
    exports main;
}