package sample.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Dialog
{
    private final FXMLLoader loader = new FXMLLoader();
    
    public Dialog(Window owner, String fxmlFile) throws IOException {
        loader.setLocation(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
    }
    
    public Object getController() {
        return loader.getController();
    }
}
