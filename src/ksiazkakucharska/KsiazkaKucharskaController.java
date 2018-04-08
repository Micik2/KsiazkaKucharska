/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ksiazkakucharska;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import java.text.SimpleDateFormat;

public class KsiazkaKucharskaController implements Initializable {
    int iloscPrzepisow;
    int iterator;
    
    @FXML
    ListView<Przepis> przepisyListView;

    @FXML
    WebView przepisWebView;

    @FXML
    Button przyciskDodajPrzepis;

    @FXML
    Button przyciskUsunPrzepis;

    @FXML
    ListView<Przepis> przepisySzukajListView;

    @FXML
    TextField szukajPoleTekstowe;
    
    @FXML
    Label statystyki;
    
    @FXML
    Label dane;

    ObservableList<Przepis> przepisy = FXCollections.observableArrayList();
    ObservableList<Przepis> przepisySzukaj = FXCollections.observableArrayList();

    void zaladujPrzepisy() {
        try {
            przepisy.clear();
            iterator = 0;
            File[] files = Pliki.listaPlikow("src/ksiazkakucharska/przepisy");
            if (files != null) {
                for (File file : files) {
                    Przepis p = new Przepis(file);
                    przepisy.add(p);
                    //*************************
                    iterator += 1;
                }
                iloscPrzepisow = iterator;
            }
        } catch (IOException ex) {
            Logger.getLogger(KsiazkaKucharskaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void edytujPrzepis(Window owner, Przepis przepis) {
        try {
            if (przepis == null){
                przepis = new Przepis("", "Składniki:<br/><br/>Wykonanie:");
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EdytorPrzepisu.fxml"));
            Pane root = (Pane) loader.load();
            EdytorPrzepisuController ctrl = (EdytorPrzepisuController) loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(przepis.getNazwa().isEmpty() ? "Dodawanie nowego przepisu" : "Edycja przepisu " + przepis.getNazwa());
            
            ctrl.edytorHtml.setHtmlText(przepis.getPrzepis());
            ctrl.tytul.setText(przepis.getNazwa());
            
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
            przepis.setNazwa(ctrl.tytul.getText());
            przepis.setPrzepis(ctrl.edytorHtml.getHtmlText());
            
            zapiszPrzepis(przepis);
        } catch (IOException ex) {
            Logger.getLogger(KsiazkaKucharskaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zapiszPrzepis(Przepis przepis) {
                
        //usuwa stary przepis
        if (przepis.getFile() != null) {
            Pliki.usun(przepis.getFile());
            przepis.setFile(null);
        }

        String fileDir = "src/ksiazkakucharska/przepisy";
        String fileName = przepis.getNazwa();
        String fileContent = przepis.getPrzepis();
        
        fileName = fileName.replaceAll(" ", "_").concat(".html");
        
        try {
            Pliki.zapisz(new File(fileDir + "/" + fileName), fileContent);
        } catch (IOException ex) {
            Logger.getLogger(KsiazkaKucharska.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void usunPrzepis(File file) {
        if (!file.exists()) {
            System.out.println("Nie ma takiego przepisu");
        }

        Pliki.usun(file);
    }

    public boolean confirmYesNo(Window owner, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Okno.fxml"));
            Pane root = (Pane) loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            OknoController ctrl = (OknoController) loader.getController();
            ctrl.messageLabel.setText(message);
            stage.setTitle("Potwierdź");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(owner);
            stage.showAndWait();
            return ctrl.wasYesClicked();
        } catch (IOException ex) {
            Logger.getLogger(KsiazkaKucharskaController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        przepisyListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Przepis> observable, Przepis oldValue, Przepis newValue) -> {
            przepisySzukajListView.getSelectionModel().clearSelection();
            WebEngine webEngine = przepisWebView.getEngine();
            if (newValue != null) {
                webEngine.loadContent(newValue.getPrzepis());
            } else {
                webEngine.loadContent("<h1>Wybierz przepis z listy</h1>");
            }
        });
        
        przepisyListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    Window owner = ((Node) event.getSource()).getScene().getWindow();
                    Przepis przepis = przepisyListView.getSelectionModel().getSelectedItem();
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String data = sdf.format(przepis.getFile().lastModified());
                
                    dane.setText(data);
                    
                    edytujPrzepis(owner, przepis);
                    zaladujPrzepisy();
                    
                    String liczbaPrzepisow = Integer.toString(iloscPrzepisow);
                    statystyki.setText("Liczba przepisów:" + " " + liczbaPrzepisow);
                }
                
                else if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    Przepis przepis = przepisyListView.getSelectionModel().getSelectedItem();
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String data = sdf.format(przepis.getFile().lastModified());
                    
                    dane.setText("Data ostatniej modyfikacji przepisu: " + data);
                    
                    String liczbaPrzepisow = Integer.toString(iloscPrzepisow);
                    statystyki.setText("Liczba przepisów:" + " " + liczbaPrzepisow);
                    
                }
            }
        });

        przepisySzukajListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Przepis> observable, Przepis oldValue, Przepis newValue) -> {
            przepisyListView.getSelectionModel().clearSelection();
            WebEngine webEngine = przepisWebView.getEngine();
            if (newValue != null) {
                webEngine.loadContent(newValue.getPrzepis());
            } else {
                webEngine.loadContent("<h1>Wybierz przepis z listy</h1>");
            }
        });

        szukajPoleTekstowe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String szukanyTekst = szukajPoleTekstowe.getText().toLowerCase();

                przepisySzukaj.clear();
                for (Przepis przepis: przepisy) {
                    String tresc = przepis.getPrzepis().toLowerCase();
                    String tytul = przepis.getNazwa().toLowerCase();
                    boolean found = tresc.contains(szukanyTekst) || tytul.contains(szukanyTekst);

                    if (found) {
                        przepisySzukaj.add(przepis);
                    }
                }
            }
        });

        przyciskUsunPrzepis.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Window owner = ((Node) event.getSource()).getScene().getWindow();
                Przepis przepis = przepisyListView.getSelectionModel().getSelectedItem();
                if (confirmYesNo(owner, "Czy chcesz usunąć przepis?")) {
                    usunPrzepis(przepis.getFile());
                    przepisy.remove(przepis);
                    
                    iloscPrzepisow -= 1;
                  
                    String liczbaPrzepisow = Integer.toString(iloscPrzepisow);
                    statystyki.setText("Liczba przepisów:" + " " + liczbaPrzepisow);
                }
            }
        });

        przyciskDodajPrzepis.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Window owner = ((Node) event.getSource()).getScene().getWindow();
                edytujPrzepis(owner, null);
                
                iloscPrzepisow += 1;
                zaladujPrzepisy();
                
                String liczbaPrzepisow = Integer.toString(iloscPrzepisow);
                statystyki.setText("Liczba przepisów:" + " " + liczbaPrzepisow);
            }
        });

        class PrzepisListCell implements Callback<ListView<Przepis>, ListCell<Przepis>> {
            @Override
            public ListCell<Przepis> call(ListView<Przepis> param) {
                ListCell<Przepis> cell = new ListCell<Przepis>() {
                    @Override
                    protected void updateItem(Przepis t, boolean bln) {
                        super.updateItem(t, bln);
                        setText(t != null ? t.getNazwa() : "");
                    }
                };
                return cell;
            }            
        }
        
        
        przepisyListView.setItems(przepisy);
        przepisySzukajListView.setItems(przepisySzukaj);
        
        przepisyListView.setCellFactory(new PrzepisListCell());
        przepisySzukajListView.setCellFactory(new PrzepisListCell());

        zaladujPrzepisy();        
        
        przepisyListView.getSelectionModel().selectFirst();
        
        
        String liczbaPrzepisow = Integer.toString(iloscPrzepisow);
        statystyki.setText("Liczba przepisów:" + " " + liczbaPrzepisow);
      
    }
}