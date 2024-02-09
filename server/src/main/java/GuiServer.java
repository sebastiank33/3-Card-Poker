import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
//import insets
import javafx.geometry.Insets;



public class GuiServer extends Application {

	ListView<String> game_state; // this is the list view that will display the game state
	Server serverConnection;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	void display_server_scene(Stage primaryStage) {
		Label state = new Label("State of Game");

		// Add radio buttons to turn server on and off
		RadioButton on_button = new RadioButton("Server ON");
		RadioButton off_button = new RadioButton("Server OFF");
		ToggleGroup toggle_group = new ToggleGroup();
		on_button.setToggleGroup(toggle_group);
		off_button.setToggleGroup(toggle_group);
		on_button.setSelected(true);

		toggle_group.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> { // this is the listener for the radio buttons
			if (newVal == on_button) { // if the server is on, allow clients to connect
				serverConnection.setAllowClients(true);
			} else if (newVal == off_button) { // if the server is off, do not allow clients to connect
				serverConnection.setAllowClients(false);
			}
		});

		HBox button_box = new HBox(20, on_button, off_button); // create a box to hold the radio buttons
		button_box.setAlignment(Pos.CENTER);

		VBox server_box = new VBox(20, state, button_box, game_state); // create a box to hold all components
		server_box.setAlignment(Pos.CENTER);
		Scene scene = new Scene(server_box, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("This is the Server");
		primaryStage.show();
	}

	void display_welcome_scene(Stage primaryStage) {
		Label enter_port = new Label("Please enter the port number to listen to");
		TextField port_input = new TextField();
		port_input.setMaxWidth(200);
		Button start_server = new Button("Start Server");

		start_server.setOnAction(e -> { // this is the listener for the start server button
			display_server_scene(primaryStage); // display the server scene
			int port_number = Integer.parseInt(port_input.getText()); // get the port number from the text field
			System.out.println("Port is " + port_number); // print the port number to the console

			serverConnection = new Server(data -> { // create a new server
				Platform.runLater(()->{
					game_state.getItems().add(data.toString()); // add the data to the list view
				});
			}, port_number);
		});

		// create VBox to hold all components
		VBox server = new VBox(30, enter_port, port_input, start_server);
		server.setAlignment(Pos.CENTER);

		// set up scene and display
		Scene scene = new Scene(server, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		game_state = new ListView<String>();
		display_welcome_scene(primaryStage);
	}
}
