package sample.view;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.interfaces.FileAction;

import java.io.IOException;

public class AddFileDialog
{
    private final AddFileDialogController controller;
    private final Stage stage = new Stage();
    
    public AddFileDialog(Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("create_form.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.setResizable(false);
        controller = loader.getController();
        stage.setOnHidden(event -> controller.discardChanges());
    }
    
    public void setTitle(String title) {
        stage.setTitle(title);
    }
    
    public void showDialog() {
        stage.showAndWait();
    }
    
    public void setAddNewFileAction(FileAction Action) {
        controller.setAddNewFileAction(Action);
    }
    
    public void setAddExistingFileAction(FileAction action) {
        controller.setAddExistingFileAction(action);
    }
    
    public void close() {
        stage.close();
    }
}
