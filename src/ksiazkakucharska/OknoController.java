package ksiazkakucharska;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class OknoController implements Initializable {

    @FXML
    Button przyciskOk;
    
    @FXML
    Button przyciskAnuluj;
    
    @FXML
    Label messageLabel;
    
    private boolean wasYesClicked = false;

    public boolean wasYesClicked() {
        return wasYesClicked;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        przyciskAnuluj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wasYesClicked = false;
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
        });
        przyciskOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wasYesClicked = true;
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
        });
    }
}
