/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ksiazkakucharska;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class EdytorPrzepisuController implements Initializable {
    
    @FXML
    HTMLEditor edytorHtml;
    
    @FXML
    Button przyciskZapisz;
    
    @FXML
    TextField tytul;
     
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        przyciskZapisz.setOnAction((ActionEvent event) -> {
            ((Node)(event.getSource())).getScene().getWindow().hide();
        });
    }    
}
