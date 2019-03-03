package Game;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.PointLight;
import Handlers.GameHandler;
import Objects.DiceField;
import Objects.FullBoard;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Covece extends Application {
	
	public Kamera kamera;
	public FullBoard FB;
	public GameHandler GH = new GameHandler(this);
	public Group root;
	public Scene scena;
	public DiceField DF;
	public boolean FieldFixedCamera = true;

	public PointLight napraviSvetlo(Group g){
		PointLight svetlo = new PointLight();
		svetlo.setTranslateY(-150);
		svetlo.setColor(Color.WHITE);
		g.getChildren().addAll(svetlo);
		return svetlo;
	}
	
	private void AddElements(Group root){
		GH.LoadMainMenu();
	}
	
	@Override
	public void start(Stage prozor) throws Exception {
		root = new Group();
//		FB = new FullBoard(4, new int[] {1, 2, 3, 4});
//		root.getChildren().addAll(FB);
//		PointLight svetlo = napraviSvetlo(root);
		SceneAntialiasing glatko = SceneAntialiasing.BALANCED;
		scena = new Scene(root, 900, 800, true, glatko);
		scena.addEventHandler(KeyEvent.KEY_PRESSED, event -> handleE(event));
		scena.addEventHandler(KeyEvent.KEY_RELEASED, event -> handleE(event));
		scena.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> MouseClickHandle(event));
		kamera = new Kamera(root, scena, this);
//		kamera.FixKamera();
		scena.setCamera(kamera.kamera);
		AddElements(root);
		prozor.setTitle("Ne ljuti se covece");
		prozor.setScene(scena);
		prozor.show();
	}
	
	public void handleE(KeyEvent event) {
		if (event.getCode() == KeyCode.UP && event.getEventType() == KeyEvent.KEY_PRESSED){
        	GH.UP();
        } else if (event.getCode() == KeyCode.DOWN && event.getEventType() == KeyEvent.KEY_PRESSED){
        	GH.DOWN();
        } else if (event.getCode() == KeyCode.ENTER && event.getEventType() == KeyEvent.KEY_PRESSED){
        	GH.ENTER();
        } else if (event.getCode() == KeyCode.SPACE && event.getEventType() == KeyEvent.KEY_PRESSED){
        	GH.Click();
        }
	}
	
	private void MouseClickHandle(MouseEvent event){
//		GH.Click();
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
