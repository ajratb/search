/*
 * ROSCOSMOS CORP. PROPERTY. 
 * Don't use without permission
 */
package ru.npopm.dep715.searchdocs.gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.Label;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.util.Callback;
import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.npopm.dep715.searchdocs.lucene.SearchFiles;
import ru.npopm.dep715.searchdocs.utils.NIO2Utils;
import ru.npopm.dep715.searchdocs.lucene.IndexFiles;

/**
 * Контроллер главного окна приложения
 *
 * Методы, используемые в событиях компонентов, находятся в классе
 * ControllerMethods
 *
 * @author ayrat
 */
public class MainController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @FXML
    private TableView<Document> tableDocs;
    @FXML
    private TableColumn<Document, String> docPathColumn;
    @FXML
    private TableColumn searchWeightColumn;

    @FXML
    private TextArea txtContext;

    @FXML
    private TableColumn<Document, String> docNumberColumn;
//       @FXML
//    private TableView<Document> tableDocs;
    @FXML
    private TableColumn docFieldColumn;
    @FXML
    private TableColumn fieldValueColumn;
    @FXML
    private TextField textSearchValueInput;
    @FXML
    private Label labelOperationTime;

    private String docsDir = "docs";
    private String indexDir = "index";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        labelOperationTime.setText("");

        docPathColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> param
            ) {
                Document doc = param.getValue();
                // сюда нужно String подходящий

                return new SimpleStringProperty(doc.get("path"));
            }
        }
        );

        // если папки с документами нет, то выходим
        Path docsPath = Paths.get(docsDir);
        if (Files.exists(docsPath)) {

        } else {
//            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ПРЕДУПРЕЖДЕНИЕ");
            alert.setHeaderText("Не найдена папка с документами");
            alert.setContentText("Создайте папку \"docs\" рядом с файлом запуска "
                    + "программы и разместите в ней файлы документов. После "
                    + "этого снова запустите программу");

            alert.showAndWait();
            System.exit(0);
        }

        //если папки индекс нет, то создаём
        Path indexPath = Paths.get(indexDir);
        if (!Files.exists(indexPath)) {
            try {
                Files.createFile(indexPath);
            } catch (IOException ex) {
                LOG.error("Ошибка создания папки Index", ex);
            }
        }

        //очищаем папку index
        try {
            NIO2Utils.deleteDirectoryContent(indexDir);
        } catch (IOException ex) {
            showWarningAlert("При удалении папки(Index) возникли проблемы", "Возможно "
                    + "содержимое папки заблокировано сторонней программой");
            LOG.error("Ошибка удаления папки Index: ", ex);
        }

        //индексируем папку docs
        IndexFiles.startIndexDocs(indexDir, true, docsDir);

        tableDocs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
//                System.out.println("im here");
                txtContext.setText(obs.getValue().get("path"));
            }
        });
    }

    /**
     * Выбор папки с документами для индексации и поиска В режиме использования
     * папки "files" кнопка не активна
     *
     * @param event
     */
    /**
     *
     * @param indexIsReady
     */
    @FXML
    void handleButtonSearch(ActionEvent event) {
        ObservableList<Document> docs = tableDocs.getItems();
        docs.clear();
        try {
            docs.addAll(
                    SearchFiles.getSearchDocuments(indexDir, textSearchValueInput.getText()));
        } catch (Exception ex) {
            LOG.error("", ex);
        }
    }

    private void showWarningAlert(String headMsg, String msg) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("ПРЕДУПРЕЖДЕНИЕ");
        alert.setHeaderText(headMsg);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
