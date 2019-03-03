package Objects;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class DiceField extends Group {
	
	public Box Kockica;
	public Box podijum;
	private Rotate rX = new Rotate(), rY = new Rotate(), rZ = new Rotate();
	
	public void Pomeri(double korakX, double korakZ){
		Kockica.setTranslateX(Kockica.getTranslateX() + korakX);
		Kockica.setTranslateZ(Kockica.getTranslateZ() + korakZ);
	}
	
	public void Kotrljaj(){
		Kockica.setRotationAxis(Rotate.Y_AXIS);
		Kockica.setRotate(45);
		KeyValue PrviX = new KeyValue(rX.angleProperty(), 0);
//		KeyValue PrviY = new KeyValue(rY.angleProperty(), 0);
		KeyValue PrviZ = new KeyValue(rZ.angleProperty(), 0);
		KeyValue PrviXT = new KeyValue(Kockica.translateXProperty(), Kockica.getTranslateX());
		KeyValue PrviZT = new KeyValue(Kockica.translateZProperty(), Kockica.getTranslateZ());
		
		KeyValue DrugiX = new KeyValue(rX.angleProperty(), -360);
		KeyValue DrugiY = new KeyValue(rY.angleProperty(), 0);
		KeyValue DrugiZ = new KeyValue(rZ.angleProperty(), 90);
		KeyValue DrugiXT = new KeyValue(Kockica.translateXProperty(), Kockica.getTranslateX() + (150 - Kockica.getTranslateX() - 20));
		KeyValue DrugiZT = new KeyValue(Kockica.translateZProperty(), Kockica.getTranslateZ() + (0 - Kockica.getTranslateZ()));
		
		KeyValue TreciX = new KeyValue(rX.angleProperty(), -360);
		KeyValue TreciY = new KeyValue(rY.angleProperty(), -90);
		KeyValue TreciZ = new KeyValue(rZ.angleProperty(), -360);
		KeyValue TreciXT = new KeyValue(Kockica.translateXProperty(), Kockica.getTranslateX() + (0 - Kockica.getTranslateX()));
		KeyValue TreciZT = new KeyValue(Kockica.translateZProperty(), Kockica.getTranslateZ() + (150 - Kockica.getTranslateZ() - 20));
		
		KeyValue CetvrtiX = new KeyValue(rX.angleProperty(), 0);
//		KeyValue CetvrtiY = new KeyValue(rY.angleProperty(), -1080);
		KeyValue CetvrtiZ = new KeyValue(rZ.angleProperty(), -1080);
		KeyValue CetvrtiXT = new KeyValue(Kockica.translateXProperty(), Kockica.getTranslateX() + (-150 - Kockica.getTranslateX() + 20));
		KeyValue CetvrtiZT = new KeyValue(Kockica.translateZProperty(), Kockica.getTranslateZ() + (0 - Kockica.getTranslateZ()));
		
		
		KeyFrame Prvi = new KeyFrame(Duration.millis(0), PrviX, PrviZ, PrviXT, PrviZT);
		KeyFrame Drugi = new KeyFrame(Duration.millis(1000), DrugiX, DrugiZ, DrugiXT, DrugiZT, DrugiY);
		KeyFrame Treci = new KeyFrame(Duration.millis(2000), TreciX, TreciZ, TreciXT, TreciZT, TreciY);
		KeyFrame Cetvrti = new KeyFrame(Duration.millis(3000), CetvrtiX, CetvrtiXT, CetvrtiZT);
		
		Timeline T = new Timeline(Prvi, Drugi, Treci, Cetvrti);
		T.play();
	}
	
	private void napraviKockicu(){
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(Color.WHITE);
		
		Kockica = new Box(30, 30, 30);
		rX.setAxis(Rotate.X_AXIS);
		rY.setAxis(Rotate.Y_AXIS);
		rZ.setAxis(Rotate.Z_AXIS);
		rX.setAngle(0);
		rY.setAngle(0);
		rZ.setAngle(0);
		
		Kockica.getTransforms().addAll(rX, rY, rZ);
		Kockica.setTranslateY(-30);
		Kockica.setMaterial(mat);
		
		
		getChildren().addAll(Kockica);
	}
	
	private void napraviTabluZaKocicu(){
		PhongMaterial mat = new PhongMaterial();
//		mat.setDiffuseColor(Color.FORESTGREEN);
		mat.setDiffuseMap(new Image("DiceTekstura.jpg"));
		
		podijum = new Box(300, 30, 300);		
		podijum.setMaterial(mat);
		
		getChildren().addAll(podijum);
	}
	
	public DiceField(){
		napraviTabluZaKocicu();
		napraviKockicu();
		setTranslateX(-500);
//		setTranslateZ(700);
	}
}
