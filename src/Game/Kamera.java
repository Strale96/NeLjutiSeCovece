package Game;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Kamera {
	public PerspectiveCamera kamera;
	private Group Game;
	private Scene Scena;

	private static final double POCETNO_RASTOJANJE = -100;
	
	private static final double BRZINA_TRANS = 1.0;
	private static final double BRZINA_ROT = 0.5;
	private static final double BRZINA_TOCKA = 1.0;
	private static final double CTRL_FAKTOR = 0.1;
	private static final double ALT_FAKTOR = 10.0;
	
	private static final Translate t = new Translate();
	private static final Rotate rx = new Rotate();
	private static final Rotate ry = new Rotate();
	private static final Rotate rz = new Rotate();
	
	private static double pozX, pozY, pozXKoc, pozYKoc;
	private static double staraPozX, staraPozY, staraPozXKoc, staraPozYKoc;
	private static double korakX, korakY, korakZ;
	private Covece Cov;
	private double R = 1500;
	
	public Kamera(Group Game, Scene Scena, Covece Cov){
		kamera = napraviKameru();
		this.Game = Game;
		this.Scena = Scena;
		this.Cov = Cov;
		
		napraviNosac();
//		obradaDogadjaja();
	}
	
	public void FixKamera(){
		kamera = new PerspectiveCamera(true);
		kamera.setNearClip(0.1);
		kamera.setFarClip(5000.0);
		Translate t = new Translate(0, 0, -R);
		Rotate rX = new Rotate(-90.0, Rotate.X_AXIS);
		Rotate rX1 = new Rotate(45.0, Rotate.X_AXIS);
		Translate K = new Translate(0, 0, -R);
		kamera.getTransforms().addAll(t, rX, K, rX1);
		kamera.setTranslateZ(POCETNO_RASTOJANJE);
		obradaDogadjaja();
	}
	
	private void napraviNosac(){
		rx.setAxis(Rotate.Y_AXIS);
		ry.setAxis(Rotate.X_AXIS);
		rz.setAxis(Rotate.Z_AXIS);
		rx.setAngle(0);
		ry.setAngle(0);
		rz.setAngle(0);
		Game.getTransforms().addAll(t, rx, ry, rz);
	}
	
	private PerspectiveCamera napraviKameru(){
		PerspectiveCamera kamera = new PerspectiveCamera(true);
		kamera.setNearClip(0.1);
		kamera.setFarClip(5000.0);
		Translate t = new Translate(450.0, 400.0, -1400.0);
		kamera.getTransforms().addAll( t);
		kamera.setTranslateZ(POCETNO_RASTOJANJE);
		return kamera;
	}
	
	private void obradaDogadjaja(){
		EventHandler<MouseEvent> r1 = dog ->{
			if (Cov.GH.GamePlaying){
//				if (!Cov.FieldFixedCamera && dog.isPrimaryButtonDown()){
//					Cov.FB.setTranslateX(Cov.FB.getTranslateX() - 500);
//					Cov.DF.setTranslateX(Cov.DF.getTranslateX() - 500);
//					Cov.root.setTranslateX(Cov.root.getTranslateX() + 500);
//					Cov.FieldFixedCamera = true;
//				}
				staraPozXKoc = pozX = dog.getSceneX();
				staraPozYKoc = pozY = dog.getSceneY();
			}
		};
		Cov.FB.setOnMousePressed(r1);
		
		EventHandler<MouseEvent> r1DF = dog ->{
			if (Cov.GH.GamePlaying){
//				if (Cov.FieldFixedCamera && dog.isPrimaryButtonDown()){
//					Cov.FB.setTranslateX(Cov.FB.getTranslateX() + 500);
//					Cov.DF.setTranslateX(Cov.DF.getTranslateX() + 500);
//					Cov.root.setTranslateX(Cov.root.getTranslateX() - 500);
//					Cov.FieldFixedCamera = false;
//				}
				staraPozX = pozX = dog.getSceneX();
				staraPozY = pozY = dog.getSceneY();
			}
		};
		Cov.DF.podijum.setOnMousePressed(r1DF);
		
		EventHandler<MouseEvent> r2 = dog -> {
			if (Cov.GH.GamePlaying){
				staraPozX = pozX;
				staraPozY = pozY;
				pozX = dog.getSceneX();
				pozY = dog.getSceneY();
				korakX = -(pozX - staraPozX);
				if (Math.abs(korakX) < 2) korakX = 0;
				korakY = -(pozY - staraPozY);
				if (Math.abs(korakY) < 2) korakY = 0;
				
				double modifikator = 1.0;
				if (dog.isControlDown()) modifikator = CTRL_FAKTOR;
				if (dog.isAltDown()) modifikator = ALT_FAKTOR;
				if (dog.isPrimaryButtonDown()){			
					rx.setAngle(rx.getAngle() + korakX*BRZINA_ROT*modifikator);
					ry.setAngle(ry.getAngle() - (Math.cos(Math.toRadians(rx.getAngle()))/2)*korakY*BRZINA_ROT*modifikator);
					rz.setAngle(rz.getAngle() - (Math.sin(Math.toRadians(rx.getAngle()))/2)*korakY*BRZINA_ROT*modifikator);
				} else if (dog.isSecondaryButtonDown()){
					double x = kamera.getTranslateX();
					Translate tX = new Translate(korakX*BRZINA_TRANS*modifikator, 0, 0);
					Translate tY = new Translate(0, korakY*BRZINA_TRANS*modifikator, 0);
					kamera.getTransforms().addAll(tX, tY);
				}
			}
		};
		Cov.FB.setOnMouseDragged(r2);
		Cov.DF.podijum.setOnMouseDragged(r2);
		
		EventHandler<MouseEvent> r1Koc = dog ->{
			if (Cov.GH.GamePlaying && Cov.GH.DiceTurn){
				staraPozX = pozXKoc = dog.getSceneX();
				staraPozY = pozYKoc = dog.getSceneY();
				Scena.setCursor(Cursor.NONE);
			}
		};
		Cov.DF.Kockica.setOnMousePressed(r1Koc);
		
		EventHandler<MouseEvent> r3Koc = dog ->{
			if (Cov.GH.GamePlaying && Cov.GH.DiceTurn){
				Scena.setCursor(Cursor.DEFAULT);
				Cov.GH.DiceValue = (int) (Math.random()*6 + 1);
				System.out.println(Cov.GH.DiceValue + " " + Cov.GH.PlayerMove);
				Cov.FB.KockicaT.setText(Integer.toString(Cov.GH.DiceValue));
				Cov.DF.Kotrljaj();
				Cov.GH.Click();
			}
		};
		Cov.DF.Kockica.setOnMouseReleased(r3Koc);
		
		EventHandler<MouseEvent> r2Koc = dog -> {
			if (Cov.GH.GamePlaying && Cov.GH.DiceTurn){
				staraPozXKoc = pozXKoc;
				staraPozYKoc = pozYKoc;
				pozXKoc = dog.getSceneX();
				pozYKoc = dog.getSceneY();
				korakX = pozXKoc - staraPozXKoc;
				korakY = pozYKoc - staraPozYKoc;
				korakY = -korakY;
				Cov.DF.Pomeri(korakX, korakY);
			}
		};
		Cov.DF.Kockica.setOnMouseDragged(r2Koc);
		
		
		EventHandler<ScrollEvent> r3 = dog -> {
			if (Cov.GH.GamePlaying){
				double modifikator = 1.0;
				korakZ = dog.getDeltaY();
				
				if (dog.isControlDown()) modifikator = CTRL_FAKTOR;
				if (dog.isAltDown()) modifikator = ALT_FAKTOR;
				Translate t = new Translate(0, 0, korakZ*BRZINA_TOCKA*modifikator);
				kamera.getTransforms().addAll(t);
			}
			
		};
		Scena.addEventHandler(ScrollEvent.SCROLL, r3);
		
		@SuppressWarnings("incomplete-switch")
		EventHandler<KeyEvent> r4 = dog -> {
			if (Cov.GH.GamePlaying){
				KeyCode k = dog.getCode();
				
				switch(k){
				case HOME:
					kamera.setTranslateZ(POCETNO_RASTOJANJE);
					kamera.setTranslateX(0);
					kamera.setTranslateY(0);
					kamera.setRotationAxis(Rotate.X_AXIS);
					kamera.setRotate(0);
					kamera.setRotationAxis(Rotate.Y_AXIS);
					kamera.setRotate(0);
					kamera.setRotationAxis(Rotate.Z_AXIS);
					kamera.setRotate(0);
					
					t.setX(0.0);
					t.setY(0.0);
					t.setZ(0.0);
					rx.setAngle(0);
					ry.setAngle(0);
					rz.setAngle(0);
					break;
				}	
			}
		};
		Scena.addEventHandler(KeyEvent.KEY_PRESSED, r4);
	}
}
