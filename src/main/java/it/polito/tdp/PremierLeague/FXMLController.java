/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String s = this.txtGoals.getText();
    	try {
    		double d = Double.parseDouble(s);
    		if(d>0) {
    			model.creaGrafo(d);
    			this.txtResult.appendText("Creato grafo con "+model.getGrafo().vertexSet().size()+" vertici e "+model.getGrafo().edgeSet().size()+" archi.\n");
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		this.txtResult.appendText("Numero dei goal inserito nel formato sbagliato.\n");
		}

    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	String s = this.txtK.getText();
    	try {
    		int i = Integer.parseInt(s);
    		if(i>0) {
    			this.txtResult.appendText("Dream team:\n");
    			for(Player p : model.ricorsione(i)) {
    				this.txtResult.appendText(p+"\n");
    			}
    		}
    	}catch (NumberFormatException e) {
    		e.printStackTrace();
    		this.txtResult.appendText("Numero dei goal inserito nel formato sbagliato.\n");
		}
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	if(model.getGrafo().edgeSet().size()>0) {
    		this.txtResult.appendText("Il Best player è "+model.BestPlayer()+"\nI giocatori battuti sono:\n");
    		for(Player p : model.getSet(model.BestPlayer())) {
    			this.txtResult.appendText(p.getName()+"\n");
    		}
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
