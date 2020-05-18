package sample.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.interfaces.FileAction;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddFileDialogController implements Initializable
{
    public TextField nameField;
    public TextField dirField;
    public RadioButton existingRb;
    public RadioButton newRb;
    public TextField filePathField;
    public HBox existingPanel;
    public GridPane newPanel;
    
    private boolean initialized;
    private FileAction addNewFileAction = (file) -> {};
    private FileAction addExistingFileAction = (file) -> {};
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        existingRb.setToggleGroup(group);
        newRb.setToggleGroup(group);
        existingRb.selectedProperty().addListener((observable, oldValue, newValue) -> updateView());
        newRb.selectedProperty().addListener((observable, oldValue, newValue) -> updateView());
        existingRb.setSelected(true);
        initialized = true;
    }
    
    public void discardChanges() {
        existingRb.setSelected(true);
        nameField.clear();
        dirField.clear();
        filePathField.clear();
    }
    
    public void setAddNewFileAction(FileAction action) {
        addNewFileAction = action;
    }
    
    public void setAddExistingFileAction(FileAction action) {
        addExistingFileAction = action;
    }
    
    @FXML
    private void onChooseDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(getStage());
        if (file != null) {
            dirField.setText(file.getAbsolutePath());
        }
    }
    
    @FXML
    private void onAdd() {
        File file = getFile();
        if (existingRb.isSelected()) {
            addExistingFileAction.performAction(file);
        } else if (newRb.isSelected()) {
            addNewFileAction.performAction(file);
        }
    }
    
    public File getFile() {
        if (existingRb.isSelected()) {
            return new File(filePathField.getText());
        } else {
            return new File(dirField.getText() + File.separator + nameField.getText());
        }
    }
    
    @FXML
    private void onChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор файла");
        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }
    
    @FXML
    private void onCancel() {
        getStage().close();
    }
    
    private void updateView() {
        existingPanel.setVisible(existingRb.isSelected());
        existingPanel.setManaged(existingRb.isSelected());
        newPanel.setVisible(newRb.isSelected());
        newPanel.setManaged(newRb.isSelected());
        if (initialized) {
            Stage stage = getStage();
            stage.sizeToScene();
        }
    }
    
    private Stage getStage() {
        return (Stage) nameField.getScene().getWindow();
    }
}
