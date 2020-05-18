package sample.view;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.File;

class FileListCallback implements Callback<ListView<File>, ListCell<File>>
{
    @Override
    public ListCell<File> call(ListView<File> param) {
        return new ListCell<File>()
        {
            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                updateTextFill();
            }
            
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getName());
                    updateTextFill();
                } else {
                    setText("");
                }
            }
            
            private void updateTextFill() {
                if (getItem().exists()) {
                    setTextFill(Color.BLACK);
                } else {
                    setTextFill(Color.INDIANRED);
                }
            }
        };
    }
}
