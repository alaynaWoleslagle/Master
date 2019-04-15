import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserInterface extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Group root = new Group();
		
		setUpGameBoard(root);
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		
		Scene scene = new Scene(root, 0, 0);
		stage.setScene(scene);
		
		stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth() * .8);
        stage.setHeight(primaryScreenBounds.getHeight());

		
		stage.show();
		
		
	}
	
	private void setUpGameBoard(Group root) {
		
		// Build vertical hallways
        for (int x = 0 ; x < 500 ; x+=200) {
            for (int y = 0 ; y < 400 ; y+=200) {
                Line line1 = new Line(); 
                line1.setStartX(x + 35); 
                line1.setStartY(y); 
                line1.setEndX(x + 35); 
                line1.setEndY(y + 200);
                
                Line line2 = new Line(); 
                line2.setStartX(x + 60); 
                line2.setStartY(y); 
                line2.setEndX(x + 60); 
                line2.setEndY(y + 200);
                
                root.getChildren().addAll(line1, line2);
            }
        }
        
		// Build horizontal hallways
        for (int x = 0 ; x < 400 ; x+=200) {
            for (int y = 0 ; y < 500 ; y+=200) {
                Line line1 = new Line(); 
                line1.setStartX(x); 
                line1.setStartY(y + 35); 
                line1.setEndX(x + 200); 
                line1.setEndY(y + 35);
                
                Line line2 = new Line(); 
                line2.setStartX(x); 
                line2.setStartY(y + 60); 
                line2.setEndX(x + 200); 
                line2.setEndY(y + 60);
                
                root.getChildren().addAll(line1, line2);
            }
        }
        
        // Build rooms
		String[] roomNames = {"Study", "Library", "Conservatory", "Hall", "Billiard Room", "Ballroom", "Lounge",
				"Dining Room", "Kitchen"};
		int i = 0;
		
        for (int x = 0 ; x < 600 ; x+=200) {
            for (int y = 0 ; y < 600 ; y+=200) {
                Rectangle square = new Rectangle(x, y, 100, 100);
                Text roomName = new Text(roomNames[i]);
                roomName.setTranslateY(y + 50);
                roomName.setTranslateX(x + 10);
                square.setFill(Color.WHITE);
                square.setStroke(Color.BLACK);
                root.getChildren().addAll(square, roomName);
                ++i;
            }
        }
        
        // shift board slightly, so it isn't right on the left edge of window
        root.setTranslateX(root.getTranslateX() + 50);
        root.setTranslateY(root.getTranslateY() + 50);
	}

}
