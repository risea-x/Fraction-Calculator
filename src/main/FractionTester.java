package main;

import gui.*;
import java.awt.*;
import java.io.IOException;


/**
 * Our calculator implemented The Model-View-Controller (MVC) design pattern. It is a software architecture pattern that separates an
 * application's data model, user interface, and control logic into three separate components, each with its own defined responsibilities:
 * <p>
 *
 *  Model: The model represents the application's data and business logic, and provides an interface for interacting with and manipulating that data.
 *  In an MVC application, the model is responsible for managing the data, performing any necessary calculations or transformations, and notifying the
 *  view and controller of any changes.
 * <p>
 *
 * View: The view represents the application's user interface, and is responsible for displaying the data and receiving user input.
 * In an MVC application, the view is responsible for rendering the data provided by the model, and sending any user input back
 * to the controller for processing.
 * <p>
 *
 * Controller: The controller acts as an intermediary between the model and the view, and is responsible for interpreting user input and manipulating
 * the model or view accordingly. In an MVC application, the controller is responsible for receiving user input from the view,
 * updating the model based on that input, and updating the view to reflect any changes in the model.
 *
 * @author Neil Briones
 * @author Clarence Cayton
 * @author Junsel Fabe
 * @author Dylan Magpili
 * @author Hans Viduya
 * @version JDK 19
 */
public class FractionTester {

    public static void main(String[] args) {
        try {
            Model model = new Model();
            View view = new View();
            Controller controller = new Controller(model, view);
            view.setController(controller);
            controller.start();
        }
        catch (IOException | FontFormatException exception){
            System.out.println(exception.getMessage());
        }
    }
}
