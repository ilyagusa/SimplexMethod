/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import static javafx.application.Platform.exit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 *
 * @author Ilya
 */
public class View {

    private BorderPane border;
    private GridPane grid;
    private GridPane gridSimplex;
    private GridPane gridArtificial;
    private Text welcome;
    private HBox hbox;
    private VBox vbox;
    private ComboBox comboBoxX;
    private ComboBox comboBoxRestrictions;
    private ToggleGroup group;
    private ToggleGroup groupFractionOrNubmer;
    private boolean artificialend = false;
    private HBox boxButtonMode;
    public String mode;
    TextField[][] arr;
    TextField[] x0;
    private int count;
    TableView<String[]> table;

    public View(Stage primaryStage) throws ClassNotFoundException, NullPointerException, WrongNumException {
        setElements(primaryStage);
    }

    private void setElements(Stage primaryStage) throws WrongNumException {
        border = new BorderPane();
        border.setBackground(new Background(new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY)));
        createPane();
        createMenu();
        border.setCenter(grid);
        Scene scene = new Scene(border, 1100, 800);
        scene.getStylesheets().add(("style2.css"));
        primaryStage.setTitle("Simplex Method");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setMainGrid() {
        createPane();
        border.setCenter(grid);
    }

    private void createMenu() {
        MenuBar menuBar = new MenuBar();
        //Меню файл
        Menu file = new Menu("Файл");
        MenuItem mainPage = new MenuItem("Начальная страница");
        mainPage.setOnAction((ActionEvent t) -> {
            InputInfo.clear();
            setMainGrid();
        });
        MenuItem exit = new MenuItem("Выход");
        exit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        exit.setOnAction((ActionEvent t) -> {
            Platform.exit();
        });
        file.getItems().addAll(mainPage, new SeparatorMenuItem(), exit);
        //Меню справка
        Menu help = new Menu("Справка");
        MenuItem helpContent = new MenuItem("Содержание справки");
        helpContent.setOnAction((ActionEvent t) -> {
            createHelpContent();
        });
        help.getItems().add(helpContent);
        menuBar.getMenus().addAll(file, help);
        border.setTop(menuBar);
    }

    private void createHelpContent() {
        Stage stageInfo = new Stage();
        GridPane paneInfo = new GridPane();

        Label labelHead = new Label("\t\t\t\tCПРАВКА");
        labelHead.setStyle("-fx-font: 26 arial;");
        paneInfo.add(labelHead, 0, 0);

        Label labelInfo = new Label("С помощью данного приложения вы можете решать задачу линейного программирования для дробей и чисел с плавающей точкой(Для них существует погрешность!!!).\nПри запуске приложения появляется данный интерфейс:");
        labelInfo.setMaxWidth(655);
        labelInfo.setWrapText(true);
        labelInfo.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfo, 0, 1);

        Image mainWindowPic = new Image("file:data/mainWindowPic.jpg");
        ImageView mwPic = new ImageView();
        mwPic.setFitHeight(200);
        mwPic.setFitWidth(655);
        mwPic.setPreserveRatio(true);
        mwPic.setImage(mainWindowPic);
        paneInfo.add(mwPic, 0, 2);

        Label labelInfo1 = new Label("Нажав на кнопку [Ввод задачи], предварительно выбрав режим решения(дроби или числа с плавающей точкой) появится окошко с выбором размерзности задачи, после чего можно будет ввести интересующую вас задачу.\n"
                + "Нажав на кнопку [Чтение из файла] матрица считывается из файлов data/inFr.txt для дробей  и data/inNum для чисел с плавающей точкой.\nПути к файлам относительны исполняемого файла!");
        labelInfo1.setMaxWidth(655);
        labelInfo1.setWrapText(true);
        labelInfo1.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfo1, 0, 3);

        Image inputSizePic = new Image("file:data/inputSizePic.jpg");
        ImageView isPic = new ImageView();
        isPic.setFitHeight(200);
        isPic.setFitWidth(655);
        isPic.setPreserveRatio(true);
        isPic.setImage(inputSizePic);
        paneInfo.add(isPic, 0, 4);

        Image inputTablePic = new Image("file:data/inputTablePic.jpg");
        ImageView itPic = new ImageView();
        itPic.setFitHeight(200);
        itPic.setFitWidth(655);
        itPic.setPreserveRatio(true);
        itPic.setImage(inputTablePic);
        paneInfo.add(itPic, 0, 5);

        HBox diaBox = new HBox();
        Image dialogPic = new Image("file:data/dialogPic.jpg");
        ImageView dPic = new ImageView();
        dPic.setFitHeight(460);
        dPic.setFitWidth(250);
        dPic.setPreserveRatio(true);
        dPic.setImage(dialogPic);
        Label labelInfoDia = new Label("Слева расположен диалог который появится после ввода матрицы/чтения матрицы из файла.\nВ нем минус единицей помечаются свободные переменные, "
                + "для того чтобы нулевые базисные переменные могли быть явно заданы."
                + "\n\nРадио-кнопками можно выбрать режим решения задачи(пошаговый / автоматический)."
                + "\n\nЯвно введя базис можно нажать кнопку [Продолжить], после чего будет запущен симплес-алгоритм."
                + "\n\nНажав кнопку [Продолжить используя искусственный базис] будет запущен соответсвующий алгоритм , который найдет возможный базис, "
                + "после чего вы сможете продолжить используя найденный базис");
        labelInfoDia.setMaxWidth(400);
        labelInfoDia.setWrapText(true);
        labelInfoDia.setStyle("-fx-font: 13 arial;");

        diaBox.getChildren().addAll(dPic, labelInfoDia);
        paneInfo.add(diaBox, 0, 6);

        Label labelInfoTable = new Label("После нажатия одной из кнопок диалога появится окно с первой симплекс-таблицой. Ниже будут доступны опорные элементы, в зависимости от которых "
                + "будет произведен следующий шаг симплекс-алгоритма");
        labelInfoTable.setMaxWidth(655);
        labelInfoTable.setWrapText(true);
        labelInfoTable.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfoTable, 0, 7);

        Image tablePic = new Image("file:data/tablePic.jpg");
        ImageView tPic = new ImageView();
        tPic.setFitHeight(500);
        tPic.setFitWidth(655);
        tPic.setPreserveRatio(true);
        tPic.setImage(tablePic);
        paneInfo.add(tPic, 0, 8);

        Label labelInfoReset = new Label("По завершении работы алгоритма, вы можете с помощью меню вернуться на главную страницу и решить другую задачу,либо в меню выйти из программы нажава на соответствующую кнопку или нажав(ctrl+x).");
        labelInfoReset.setMaxWidth(655);
        labelInfoReset.setWrapText(true);
        labelInfoReset.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfoReset, 0, 9);

        paneInfo.setPadding(new Insets(10, 10, 10, 10));
        paneInfo.setHgap(10);
        paneInfo.setVgap(10);
        ScrollPane scrollInfo = new ScrollPane();
        scrollInfo.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollInfo.setContent(paneInfo);
        scrollInfo.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Scene sceneHelp = new Scene(scrollInfo, 690, 400);
        stageInfo.setScene(sceneHelp);
        stageInfo.show();
    }

    private void createPane() {
        grid = new GridPane();
        createRadioButtonForMode();
        hbox = new HBox();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        welcome = new Text();
        welcome.setText("Приложение , позволяющее решать решать задачу\nлинейного программирования симплекс-методом.\n\nЧтобы выбрать режим нажмите на нужную кнопку");
        welcome.setStyle("-fx-font: 26 arial;");
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setVisible(true);
        Button simplexButton = new Button();
        simplexButton.getStyleClass().add("simplexButton");
        simplexButton.setText("Ввод задачи");

        simplexButton.setOnAction((ActionEvent event) -> {
            RadioButton selectionFrOrNum = (RadioButton) groupFractionOrNubmer.getSelectedToggle();
            mode = selectionFrOrNum.getText();
            grid.setVisible(false);
            createGridSimplex();
            border.setCenter(gridSimplex);
        });
        Button readFileButton = new Button();
        readFileButton.getStyleClass().add("artificialButton");
        readFileButton.setText("Чтение из файла");
        readFileButton.setOnAction((ActionEvent event) -> {
            RadioButton selectionFrOrNum = (RadioButton) groupFractionOrNubmer.getSelectedToggle();
            mode = selectionFrOrNum.getText();
            grid.setVisible(false);

            if ("Работа с дробями".equals(mode)) {

                try {
                    InputInfo.readFileFraction();
                } catch (WrongNumException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (InputInfo.sTableFraction == null) {
                    grid.setVisible(true);
                    border.setCenter(grid);
                    Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                    alertDependence.setTitle("Ошибка");
                    alertDependence.setHeaderText("Ошибка связанная с файлом(ошибка ввода/отсутствие файла)");
                    alertDependence.setContentText("Файл содержит некорректную информацию, пожалуйста введите задачу правильно\n1 строка: целевая функция (n значений)\n>1 строки: ограничения (n+1 значений)");
                    alertDependence.showAndWait();
                    return;
                }

                if (!MethodsForFraction.checkDependence(InputInfo.sTableFraction.getMatrix())) {
                    ChoiceBasisDialogFraction a = new ChoiceBasisDialogFraction(InputInfo.sTableFraction);
                } else {
                    InputInfo.sTableFraction = null;
                    grid.setVisible(true);
                    border.setCenter(grid);
                    Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                    alertDependence.setTitle("Ошибка");
                    alertDependence.setHeaderText("Ошибка в матрице");
                    alertDependence.setContentText("В матрице из файла, ограничения являются линейно-зависимыми. Введите подходящую матрицу(линейно-независмую)");
                    alertDependence.showAndWait();
                }
            } else if ("Работа с числами".equals(mode)) {
                try {
                    InputInfo.readFileNumber();
                } catch (WrongNumException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (InputInfo.sTableNumber == null) {
                    grid.setVisible(true);
                    border.setCenter(grid);
                    Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                    alertDependence.setTitle("Ошибка");
                    alertDependence.setHeaderText("Ошибка связанная с файлом\n(ошибка ввода/отсутствие файла)");
                    alertDependence.setContentText("Файл содержит некорректную информацию, пожалуйста введите задачу правильно\n1 строка: целевая функция (n значений)\n>1 строки: ограничения (n+1 значений)");
                    alertDependence.showAndWait();
                    return;
                }
                if (!MethodsForNumber.checkDependence(InputInfo.sTableNumber.getMatrix())) {
                    ChoiceBasisDialogNumber a = new ChoiceBasisDialogNumber(InputInfo.sTableNumber);
                } else {
                    InputInfo.sTableNumber = null;
                    grid.setVisible(true);
                    border.setCenter(grid);
                    Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                    alertDependence.setTitle("Ошибка");
                    alertDependence.setHeaderText("Ошибка в файле");
                    alertDependence.setContentText("В матрице из файла, ограничения являются линейно-зависимыми. Введите подходящую матрицу(линейно-независмую)");
                    alertDependence.showAndWait();
                }
            }

        });
        hbox.getChildren().addAll(simplexButton, boxButtonMode, readFileButton);
        grid.add(hbox, 1, 1);
        grid.add(welcome, 1, 0);
        grid.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
    }

    private void createGridSimplex() {
        gridSimplex = new GridPane();
        gridSimplex.setPadding(new Insets(10, 10, 10, 10));
        comboBoxX = new ComboBox();
        comboBoxX.getItems().addAll(
                "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"
        );
        comboBoxX.setValue("3");
        comboBoxRestrictions = new ComboBox();
        comboBoxRestrictions.getItems().addAll(
                "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"
        );
        comboBoxRestrictions.setValue("2");
        Button okButton = new Button("Ввод");
        Stage stage = new Stage();
        stage.setTitle("Ввод параметров");
        Scene scene = new Scene(new HBox(20), 587, 40);
        HBox box1 = (HBox) scene.getRoot();
        box1.setPadding(new Insets(5, 5, 5, 5));
        box1.getChildren().addAll(new Text("Кол-во переменных\t"), comboBoxX, new Text("\tКол-во ограничений\t"), comboBoxRestrictions, new Text("\t"), okButton);
        stage.setScene(scene);
        stage.show();
        okButton.setOnAction((ActionEvent event) -> {
            if (Integer.parseInt((String) comboBoxX.getValue()) > Integer.parseInt((String) comboBoxRestrictions.getValue())) {
                stage.close();
                createInputTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка с размерами");
                alert.setContentText("\"Количество переменных должно быть > количества ограничений\"");
                alert.showAndWait();
            }
        });
        //при закрытии окна с вводом размерности вернуться на главную страницу
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Controller.view.setMainGrid();
            }
        });
    }

    public void createInputTable() {
        x0 = new TextField[(Integer.parseInt((String) comboBoxX.getValue()))];
        arr = new TextField[(Integer.parseInt((String) comboBoxRestrictions.getValue()) + 1)][(Integer.parseInt((String) comboBoxX.getValue()) + 1)];
        int idi = 0;
        for (int i = 0; i < Integer.parseInt((String) comboBoxRestrictions.getValue()) + 1; i++) {
            int idj = 0;
            Text txName = new Text("Целевая Функция:");
            if (idi != 0) {
                txName.setText("Ограничение №" + (i));
            }
            gridSimplex.add(txName, 0, idi);
            idi++;
            for (int j = 0; j < Integer.parseInt((String) comboBoxX.getValue()) + 1; j++) {
                arr[i][j] = new TextField();
                arr[i][j].setMinWidth(30);
                if ((i != 0 || j != Integer.parseInt((String) comboBoxX.getValue()))) {
                    gridSimplex.add(arr[i][j], idj, idi);
                } else {
                    gridSimplex.add(new Text("===> MIN"), idj, idi);
                }
                idj++;
                Text tx = new Text("*x" + (j + 1) + "+");
                if (j == Integer.parseInt((String) comboBoxX.getValue()) - 1) {
                    tx.setText("*x" + (j + 1) + "=");
                }
                if (j == Integer.parseInt((String) comboBoxX.getValue())) {
                    tx.setText("");
                }
                gridSimplex.add(tx, idj, idi);
                idj++;
            }
            idi++;
        }
        arr[0][Integer.parseInt((String) comboBoxX.getValue())].setText("0");
        gridSimplex.add(new Text(""), 0, (2 * Integer.parseInt((String) comboBoxRestrictions.getValue())) + 3);
        Button btnInput = new Button("Ввод");
        btnInput.setOnAction((ActionEvent event) -> {
            if ("Работа с дробями".equals(mode)) {
                try {
                    if (checkInputValueFraction()) {
                        InputInfo.readInputFraction(arr, Integer.parseInt((String) comboBoxRestrictions.getValue()), Integer.parseInt((String) comboBoxX.getValue()));
                        gridSimplex.setVisible(false);
                        if (!MethodsForFraction.checkDependence(InputInfo.sTableFraction.getMatrix())) {
                            ChoiceBasisDialogFraction a = new ChoiceBasisDialogFraction(InputInfo.sTableFraction);
                        } else {
                            InputInfo.sTableFraction = null;
                            Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                            alertDependence.setTitle("Ошибка");
                            alertDependence.setHeaderText("Ошибка в матрице");
                            alertDependence.setContentText("В введенной вами матрице , ограничения являются линейно-зависимыми. Введите подходящую матрицу(линейно-независмую)");
                            alertDependence.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Ошибка с форматом ввода");
                        alert.setContentText("На ввод должно поступать целое число, либо обыкновенная дробь \nФормат числа[1,213,-123...]\nФормат дроби[1/2,-1/3,-1/-3,0/2...]");
                        alert.showAndWait();
                    }

                } catch (IOException | WrongNumException | NullPointerException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ("Работа с числами".equals(mode)) {
                //Здесь те же самые действия и провери что и для дробей!
                if (checkInputValueNumber()) {
                    try {
                        InputInfo.readInputNumber(arr, Integer.parseInt((String) comboBoxRestrictions.getValue()), Integer.parseInt((String) comboBoxX.getValue()));
                        if (!MethodsForNumber.checkDependence(InputInfo.sTableNumber.getMatrix())) {
                            ChoiceBasisDialogNumber a = new ChoiceBasisDialogNumber(InputInfo.sTableNumber);
                        } else {
                            InputInfo.sTableNumber = null;
                            Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                            alertDependence.setTitle("Ошибка");
                            alertDependence.setHeaderText("Ошибка в матрице");
                            alertDependence.setContentText("В введенной вами матрице , ограничения являются линейно-зависимыми. Введите подходящую матрицу(линейно-независмую)");
                            alertDependence.showAndWait();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (WrongNumException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка с форматом ввода");
                    alert.setContentText("На ввод должно поступать число вида [1.43 , -2.32, 1 , 2 ....]");
                    alert.showAndWait();
                }
            }
        });
        gridSimplex.add(btnInput, 0, (2 * Integer.parseInt((String) comboBoxRestrictions.getValue())) + 4);
    }

    public void createSimplexStepTableArtificial() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {

            if (!MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
                if (!InputInfo.stepBaseFraction.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForArtificialFraction();
        } else if ("Работа с числами".equals(mode)) {
            if (!MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
                if (!InputInfo.stepBaseNumber.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForArtificialNumber();
        }
        border.setCenter(vbox);
    }

    public void createSimplexTableAutoArtificial() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        TableView<String[]> table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {

            createButtonForAutoArtificialFraction();
        } else if ("Работа с числами".equals(mode)) {
            createButtonForAutoArtificialNumber();
        }
        border.setCenter(vbox);
    }

    public void createSimplexStepTableStandard() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {

            if (!MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
                if (!InputInfo.stepBaseFraction.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForStandardFraction();
        } else if ("Работа с числами".equals(mode)) {
            if (!MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
                if (!InputInfo.stepBaseNumber.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForStandardNumber();
        }
        border.setCenter(vbox);
    }

    public void createSimplexStepTableAutoStandard() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            //
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        TableView<String[]> table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {
            createButtonForAutoStandardFraction();
        } else if ("Работа с числами".equals(mode)) {
            createButtonForAutoStandardNumber();
        }
        border.setCenter(vbox);
    }

    private void createButtonForArtificialFraction() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexFraction nextStep;
                nextStep = MethodsForFraction.nextStepArtificialFraction(InputInfo.stepBaseFraction.getLastStep(), MethodsForFraction.needSupport(selectedString)[0], MethodsForFraction.needSupport(selectedString)[1]);
                InputInfo.stepBaseFraction.add(nextStep);
                createSimplexStepTableArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseFraction.removeLast();
                createSimplexStepTableArtificial();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseFraction.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseFraction.getLastStep().end() || MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            nextButton.setVisible(false);
        }
        if (InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            nextButton.setVisible(false);
        }
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        //если это конец искувственного базиса
        if (InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            space.setText("\t\tРешение методом искуственного базиса закончено, нажмите кнопку продолжить!\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis());
            space.setStyle("-fx-font: 16 arial;");
            hboxBtn.getChildren().remove(nextButton);
            Button next = new Button("Продолжить");
            hboxBtn.getChildren().add(next);
            next.setOnAction((ActionEvent event) -> {
                vbox.getChildren().removeAll(vbox.getChildren());
                try {
                    count++;
                    InputInfo.stepBaseFraction.add(MethodsForFraction.createStepAfterArtificialMethodFraction(InputInfo.stepBaseFraction.getLastStep(), InputInfo.sTableFraction, InputInfo.sTableFraction.getTarget()));
                    createSimplexStepTableArtificial();
                } catch (WrongNumException | InterruptedException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        if (InputInfo.stepBaseFraction.getLastStep().end() && !InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            //если это конец обычного алгоритма count - счётчик нажатия кнопки продолжить, которая появляется только если найден какой-то базис с помощью искусственного базиса
            if (count != 0) {
                space.setText("\t\tРешение достигунуто.Симплекс-алгоритм закончен\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
                space.setStyle("-fx-font: 16 arial;");
                createButtonExit();
            } else if (count == 0) {
                //если система огр. противоречива, то есть алгоритм искуственного базиса закончен, но кнопки продолжить не появилось,  то есть базис не был найден()
                space.setText("\t\tРешения не существует. Система ограничений противоречива\n\t\t");
                space.setStyle("-fx-font: 16 arial;");
                hboxBtn.getChildren().add(createHelperBadSolution());
                createButtonExit();
            }
        }

        //если решение не может быть достигнуто
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("\t\tРешение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как в таблице присутствует столбец,\n\tсодержащий отрциательный элемент f[k] и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForAutoArtificialFraction() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);
        if (InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            space.setText("Решение методом искуственного базиса закончено, нажмите кнопку продолжить!\n" + InputInfo.stepBaseFraction.getLastStep().basis());
        }
        space.setStyle("-fx-font: 16 arial;");
        Button next = new Button("Продолжить");
        hboxBtn.getChildren().addAll(space, next);
        if (InputInfo.stepBaseFraction.getLastStep().end() && !InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            if (count != 0) {
                space.setMinWidth(987);
                space.setText("Решение достигунуто.Симплекс-алгоритм закончен\n" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
                space.setStyle("-fx-font: 18 arial;");
                hboxBtn.getChildren().removeAll(next);
                createButtonExit();
            } else if (count == 0) {
                space.setText("Решения не существует. Система ограничений противоречива(Метод искуственного базиса не закончился).\n\t\t");
                space.setStyle("-fx-font: 16 arial;");
                hboxBtn.getChildren().removeAll(next);
                createButtonExit();
            }
        }
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("Решение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            hboxBtn.getChildren().add(createHelperBadSolution());
            createButtonExit();
        }
        next.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                count++;
                //
                InputInfo.stepBaseFraction.add(MethodsForFraction.createStepAfterArtificialMethodFraction(InputInfo.stepBaseFraction.getLastStep(), InputInfo.sTableFraction, InputInfo.sTableFraction.getTarget()));
                MethodsForFraction.autoSimplexMethodFraction();
                //
                createSimplexTableAutoArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForAutoArtificialNumber() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);
        if (InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            space.setText("Решение методом искуственного базиса закончено, нажмите кнопку продолжить!\n" + InputInfo.stepBaseNumber.getLastStep().basis());
        }
        space.setStyle("-fx-font: 16 arial;");
        Button next = new Button("Продолжить");
        hboxBtn.getChildren().addAll(space, next);
        if (InputInfo.stepBaseNumber.getLastStep().end() && !InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            if (count != 0) {
                space.setMinWidth(987);
                space.setText("Решение достигунуто.Симплекс-алгоритм закончен\n" + InputInfo.stepBaseNumber.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
                space.setStyle("-fx-font: 18 arial;");
                hboxBtn.getChildren().removeAll(next);
                count = 0;
                createButtonExit();
            } else if (count == 0) {
                space.setText("Решения не существует. Система ограничений противоречива(Метод искуственного базиса не закончился).\n\t\t");
                space.setStyle("-fx-font: 16 arial;");
                hboxBtn.getChildren().removeAll(next);
                hboxBtn.getChildren().add(createHelperBadSolution());
                createButtonExit();
            }
        }
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            space.setText("Решение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как в таблице присутствует столбец,\n\tсодержащий отрциательный элемент f[k] и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            hboxBtn.getChildren().addAll(space, btnToolTip);
            createButtonExit();
        }
        next.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                count++;
                InputInfo.stepBaseNumber.add(MethodsForNumber.createStepAfterArtificialMethodNumber(InputInfo.stepBaseNumber.getLastStep(), InputInfo.sTableNumber, InputInfo.sTableNumber.getTarget()));
                MethodsForNumber.autoSimplexMethodNumber();
                createSimplexTableAutoArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForStandardFraction() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexFraction nextStep;
                nextStep = MethodsForFraction.nextStep(InputInfo.stepBaseFraction.getLastStep(), MethodsForFraction.needSupport(selectedString)[0], MethodsForFraction.needSupport(selectedString)[1]);
                InputInfo.stepBaseFraction.add(nextStep);
                createSimplexStepTableStandard();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseFraction.removeLast();
                createSimplexStepTableStandard();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseFraction.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseFraction.getLastStep().end() || MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            nextButton.setVisible(false);
        }

        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        if (InputInfo.stepBaseFraction.getLastStep().end()) {
            //если это конец обычного алгоритма а
            space.setText("\t\tРешение достигунуто.Симплекс-алгоритм закончен\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
            space.setStyle("-fx-font: 16 arial;");
            createButtonExit();
        }

        //если решение не может быть достигнуто
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("\t\tРешение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как в таблице присутствует столбец,\n\tсодержащий отрциательный элемент f[k] и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForAutoStandardFraction() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);

        space.setStyle("-fx-font: 16 arial;");
        hboxBtn.getChildren().addAll(space);
        if (InputInfo.stepBaseFraction.getLastStep().end() && !InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            space.setMinWidth(987);
            space.setText("Решение достигунуто.Симплекс-алгоритм закончен\n" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
            space.setStyle("-fx-font: 18 arial;");

        }
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("Решение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как в таблице присутствует столбец,\n\tсодержащий отрциательный элемент f[k] и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            hboxBtn.getChildren().addAll(space, btnToolTip);
        }
        vbox.getChildren().addAll(hboxBtn);
        createButtonExit();
    }

    private void createButtonForStandardNumber() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexNumber nextStep;
                nextStep = MethodsForNumber.nextStep(InputInfo.stepBaseNumber.getLastStep(), MethodsForNumber.needSupport(selectedString)[0], MethodsForNumber.needSupport(selectedString)[1]);
                InputInfo.stepBaseNumber.add(nextStep);
                createSimplexStepTableStandard();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseNumber.removeLast();
                createSimplexStepTableStandard();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseNumber.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseNumber.getLastStep().end() || MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            nextButton.setVisible(false);
        }

        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        if (InputInfo.stepBaseNumber.getLastStep().end()) {
            //если это конец обычного алгоритма а
            space.setText("\t\tРешение достигунуто.Симплекс-алгоритм закончен\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis() + "\n\t\tf0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
            space.setStyle("-fx-font: 16 arial;");
            createButtonExit();
        }

        //если решение не может быть достигнуто
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep()) == true) {
            space.setText("\t\tРешение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как в таблице присутствует столбец,\n\tсодержащий отрциательный элемент f[k] и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForAutoStandardNumber() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);

        space.setStyle("-fx-font: 16 arial;");
        hboxBtn.getChildren().addAll(space);
        if (InputInfo.stepBaseNumber.getLastStep().end() && !InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            space.setMinWidth(987);
            space.setText("Решение достигунуто.Симплекс-алгоритм закончен\n" + InputInfo.stepBaseNumber.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
            space.setStyle("-fx-font: 18 arial;");

        }
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            space.setText("Решение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как в таблице присутствует столбец,\n\tсодержащий отрциательный элемент f[k] и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            hboxBtn.getChildren().addAll(space, btnToolTip);
        }
        vbox.getChildren().addAll(hboxBtn);
        createButtonExit();
    }

    private void createButtonForArtificialNumber() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexNumber nextStep;
                nextStep = MethodsForNumber.nextStepArtificialNumber(InputInfo.stepBaseNumber.getLastStep(), MethodsForNumber.needSupport(selectedString)[0], MethodsForNumber.needSupport(selectedString)[1]);
                InputInfo.stepBaseNumber.add(nextStep);
                createSimplexStepTableArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseNumber.removeLast();
                createSimplexStepTableArtificial();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseNumber.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseNumber.getLastStep().end() || MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            nextButton.setVisible(false);
        }
        if (InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            nextButton.setVisible(false);
        }
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        //если это конец искувственного базиса
        if (InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            space.setText("\t\tРешение методом искуственного базиса закончено, нажмите кнопку продолжить!\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis());
            space.setStyle("-fx-font: 16 arial;");
            hboxBtn.getChildren().remove(nextButton);
            Button next = new Button("Продолжить");
            hboxBtn.getChildren().add(next);
            next.setOnAction((ActionEvent event) -> {
                vbox.getChildren().removeAll(vbox.getChildren());
                try {
                    count++;
                    InputInfo.stepBaseNumber.add(MethodsForNumber.createStepAfterArtificialMethodNumber(InputInfo.stepBaseNumber.getLastStep(), InputInfo.sTableNumber, InputInfo.sTableNumber.getTarget()));
                    createSimplexStepTableArtificial();
                } catch (WrongNumException | InterruptedException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        if (InputInfo.stepBaseNumber.getLastStep().end() && !InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            //если это конец обычного алгоритма count - счётчик нажатия кнопки продолжить, которая появляется только если найден какой-то базис с помощью искусственного базиса
            if (count != 0) {
                space.setText("\t\tРешение достигунуто.Симплекс-алгоритм закончен\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
                space.setStyle("-fx-font: 16 arial;");
                createButtonExit();
            } else if (count == 0) {
                //если система огр. противоречива, то есть алгоритм искуственного базиса закончен, но кнопки продолжить не появилось,  то есть базис не был найден()
                space.setText("\t\tРешения не существует. Система ограничений противоречива\n");
                space.setStyle("-fx-font: 16 arial;");
                space.setMinWidth(350);
                hboxBtn.getChildren().add(createHelperBadSolution());
                createButtonExit();
            }
        }

        //если решение не может быть достигнуто
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            space.setText("\t\tРешение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как в таблице присутствует столбец,\n\tсодержащий отрциательный элемент f[k] и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonExit() {
        Button exit = new Button("Начальная страница");
        exit.setOnAction((ActionEvent event) -> {
            InputInfo.clear();
            setMainGrid();
        });
        vbox.getChildren().add(exit);
    }

    private void createRadioButtonForMode() {
        RadioButton fractionBtn = new RadioButton("Работа с дробями");
        RadioButton numberBtn = new RadioButton("Работа с числами");
        groupFractionOrNubmer = new ToggleGroup();
        fractionBtn.setToggleGroup(groupFractionOrNubmer);
        numberBtn.setToggleGroup(groupFractionOrNubmer);
        fractionBtn.setSelected(true);
        boxButtonMode = new HBox();
        boxButtonMode.getChildren().addAll(new Text("\t\t"), fractionBtn, new Text("\t\t\t"), numberBtn, new Text("\t\t"));
        boxButtonMode.setPadding(new Insets(5, 5, 5, 5));
    }

    private Button createHelperBadSolution() {
        Button btnToolTip = new Button("  ?  ");
        btnToolTip.getStyleClass().add("helper");
        Tooltip helperTooltip = new Tooltip("\tПри окончании алгоритма искусственного базиса f0 > 0.\n\tЭто означает что система противоречива!");
        helperTooltip.setStyle("-fx-font: 16 arial;");
        helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
        hackTooltipStartTiming(helperTooltip);
        btnToolTip.setTooltip(helperTooltip);
        return btnToolTip;
    }

    private boolean checkInputValueFraction() {
        boolean result = true;
        for (TextField[] elm : arr) {
            for (TextField input : elm) {
                //проверка числа/дроби
                if (!input.getText().matches("-?[0-9]+(['/']{1}-?[1-9]{1}[0-9]*)?")) {
                    result = false;
                }
            }
        }
        return result;
    }

    private boolean checkInputValueNumber() {
        boolean result = true;
        for (TextField[] elm : arr) {
            for (TextField input : elm) {
                //проверка числа/дроби
                if (!input.getText().matches("^[-+]?[0-9]*[.]?[0-9]+(?:[eE][-+]?[0-9]+)?$")) {
                    result = false;
                }
            }
        }
        return result;
    }

    private void createHelper() throws InterruptedException {
        HBox helper = new HBox();
        Label label = new Label("Чтобы продолжить вам необоходимо выбрать один из опорных элементов \nОпорный элемент выбирается минимальным из B[i]/A[i][j].");
        Button btnToolTip = new Button("  ?  ");
        btnToolTip.getStyleClass().add("helper");
        Tooltip helperTooltip = new Tooltip("Такие значения выбираются только для:\nотрицательных f[j] и положительных A[i][j].\nПример:[0][2] = 1 : Здесь 0 - строка 2 - столбец \n1 - значение B[0]/A[0][2]");
        helperTooltip.setStyle("-fx-font: 16 arial;");
        helperTooltip.setAnchorLocation(AnchorLocation.WINDOW_BOTTOM_LEFT);
        hackTooltipStartTiming(helperTooltip);
        btnToolTip.setTooltip(helperTooltip);
        helper.getChildren().addAll(label, btnToolTip);
        label.setStyle("-fx-font: 16 arial;");
        vbox.getChildren().add(helper);
    }

    private void createSupportElm() throws WrongNumException {
        ArrayList support = null;
        if ("Работа с дробями".equals(mode)) {
            support = MethodsForFraction.supportElmTmpStep(InputInfo.stepBaseFraction.getLastStep());
        } else if ("Работа с числами".equals(mode)) {
            support = MethodsForNumber.supportElmTmpStep(InputInfo.stepBaseNumber.getLastStep());
        }
        group = new ToggleGroup();
        RadioButton[] btn = new RadioButton[support.size()];
        HBox hboxSup = new HBox();
        for (int i = 0; i < support.size(); i++) {
            btn[i] = new RadioButton(support.get(i).toString() + "\t");
            btn[i].setStyle("-fx-font: 16 arial;");
            btn[i].setMinWidth(70);
            btn[i].setToggleGroup(group);
            hboxSup.getChildren().add(btn[i]);
        }
  
        if ("Работа с числами".equals(mode)) {
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                    // Has selection.
                    if (old_toggle != null) {
                        RadioButton oldButton = (RadioButton) old_toggle;
                        String oldSelected = oldButton.getText();
                        TableColumn temp = table.getColumns().get(MethodsForNumber.needSupport(oldSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForNumber.needSupport(oldSelected)[0]) {
                                                this.setStyle("");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForNumber.needSupport(oldSelected)[1] + 1, temp);
                    }

                    RadioButton newButton = (RadioButton) new_toggle;
                    String newSelected = newButton.getText();
                    if (new_toggle != null) {
                        TableColumn temp = table.getColumns().get(MethodsForNumber.needSupport(newSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForNumber.needSupport(newSelected)[0]) {
                                                this.setStyle("-fx-background-color: #10C872;");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForNumber.needSupport(newSelected)[1] + 1, temp);
                    }
                }
            });
        } else if ("Работа с дробями".equals(mode)) {
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                    // Has selection.
                    if (old_toggle != null) {
                        RadioButton oldButton = (RadioButton) old_toggle;
                        String oldSelected = oldButton.getText();
                        TableColumn temp = table.getColumns().get(MethodsForFraction.needSupport(oldSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForFraction.needSupport(oldSelected)[0]) {
                                                this.setStyle("");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForFraction.needSupport(oldSelected)[1] + 1, temp);
                    }

                    RadioButton newButton = (RadioButton) new_toggle;
                    String newSelected = newButton.getText();
                    if (new_toggle != null) {
                        TableColumn temp = table.getColumns().get(MethodsForFraction.needSupport(newSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForFraction.needSupport(newSelected)[0]) {
                                                this.setStyle("-fx-background-color: #10C872;");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForFraction.needSupport(newSelected)[1] + 1, temp);
                    }
                }
            });
        }
        btn[0].setSelected(true);
        ScrollPane scroll = new ScrollPane(hboxSup);
        scroll.setFitToHeight(false);
        scroll.setPrefViewportHeight(27.5);
        scroll.setMaxWidth(1080);
        vbox.getChildren().add(scroll);
    }

    private void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(100)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
