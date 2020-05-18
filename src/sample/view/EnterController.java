package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.FileList;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class EnterController implements Observer
{
    private final FileList<File> files = new FileList<>(new File("files"));
    public ListView<File> fileList;
    public Label fileLabel;
    public Button openButton;
    public Button deleteButton;
    private AddFileDialog addFileDialog;

    @FXML
    private void initialize() {
        fileList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onSelectItem());
        fileList.setCellFactory(new FileListCallback());
        files.addObserver(this);
        updateListView();
        updateButtonsEnabled();
    }
    
    private void onSelectItem() {
        updatePathLabel();
        updateButtonsEnabled();
    }
    
    private void updatePathLabel() {
        File file = getSelectedItem();
        if (file != null) {
            fileLabel.setText(file.getAbsolutePath());
        } else {
            fileLabel.setText("");
        }
    }
    
    private void lazyAddFileDialogInit() {
        if (addFileDialog == null) {
            try {
                addFileDialog = new AddFileDialog(getStage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        addFileDialog.setTitle("Создание проекта");
        addFileDialog.setAddNewFileAction(file -> {
            try {
                tryCreateFile(file);
                addFileDialog.close();
                showFileCreatedMessage(file);
            } catch (FileAlreadyExistsException e) {
                showFileExistsWarning(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        });
        addFileDialog.setAddExistingFileAction(file -> {
            try {
                tryAddExistingFile(file);
            } catch (FileAlreadyExistsException e) {
                showFileExistsInListWarning(file);
            }
        });
    }
    
    private void tryCreateFile(File file) throws IOException {
        if (!file.exists()) {
            if (file.createNewFile()) {
                files.add(file);
            }
        } else {
            throw new FileAlreadyExistsException("");
        }
    }
    
    private void showFileCreatedMessage(File file) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, file.getAbsolutePath());
        alert.setHeaderText("Файл успешно создан");
        alert.showAndWait();
    }
    
    private void showFileExistsWarning(File file) {
        Alert alert = new Alert(Alert.AlertType.WARNING, file.getAbsolutePath());
        alert.setHeaderText("Файл уже существует");
        alert.showAndWait();
    }
    
    private void showFileExistsInListWarning(File file) {
        Alert alert = new Alert(Alert.AlertType.WARNING, file.getAbsolutePath());
        alert.setHeaderText("Файл уже есть в списке");
        alert.showAndWait();
    }
    
    private void tryAddExistingFile(File file) throws FileAlreadyExistsException {
        if (!files.contains(file)) {
            files.add(file);
        } else {
            throw new FileAlreadyExistsException("");
        }
    }
    
    @FXML
    private void onAdd() {
        lazyAddFileDialogInit();
        addFileDialog.showDialog();
    }
    
    @FXML
    private void onDelete() {
        File file = getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Удалить файл?");
        alert.setContentText(file.getAbsolutePath());
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            if (!file.exists() || file.delete()) {
                files.remove(file);
            }
        }
    }
    
    private File getSelectedItem() {
        return fileList.getSelectionModel().getSelectedItem();
    }
    
    @FXML
    private void onExit() {
        getStage().close();
    }
    
    private Stage getStage() {
        return (Stage) fileList.getScene().getWindow();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        updateListView();
    }
    
    private void updateListView() {
        //long start = System.currentTimeMillis();
        fileList.getItems().setAll(files.readList());
        //System.out.println(System.currentTimeMillis() - start + " мс");
    }
    
    private void updateButtonsEnabled() {
        boolean disabled = getSelectedItem() == null;
        openButton.setDisable(disabled);
        deleteButton.setDisable(disabled);
    }
}
