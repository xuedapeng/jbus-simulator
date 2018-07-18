package cc.touchuan.simulator;

import java.util.Date;

import cc.touchuan.simulator.plugin.loadtestor.LoadTestor;
import javafx.application.Application;
import javafx.stage.*;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Hello world!
 *
 */
public class AppSimulator extends Application
{
	private Scene scene;
	
	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("Jbus透传模拟器");
        scene = new Scene(new Browser(),900,600, Color.web("#666970"));
        stage.setScene(scene);   
        stage.show();
        
        System.out.print(new Date());

	}
	
	public static void main(String[] args) throws Exception {

		launch(args);
	}
}

class Browser extends Region {

    final WebView browser = new WebView();final WebEngine webEngine = browser.getEngine();

    public Browser() {
        //apply the styles
        getStyleClass().add("browser");
        // load the web page
        webEngine.load("http://pms.bizmsg.net:18083");
        //add the web view to the scene
        getChildren().add(browser);

    }



    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 900;
    }

    @Override protected double computePrefHeight(double width) {
        return 600;
    }
}