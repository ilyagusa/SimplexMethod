/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.awt.Event;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author nadezhda
 */
public class ChoiceBasisDialogFraction {
    
    private SourceTaskFraction task;
    private Stage dialog;
    private TextField arrText[];
    private Font fontDialog;
    private GridPane rootDialog;
    private Fractions posDialog;
    private ScrollPane scrollPaneDialog;
    private int idIt;
    private ToggleGroup group;
    
    private void createNameText() {
        arrText = new TextField[(task.sizeX())];
        idIt = 1;
        for (int i = 0; i < task.sizeX(); i++) {
            Text tx = new Text("x" + (i + 1) + " = ");
            arrText[i] = new TextField();
            arrText[i].setText("-1");
            rootDialog.add(tx, 0, idIt);
            idIt++;
            rootDialog.add(arrText[i], 0, idIt);
            idIt++;
        }
    }
    
    private void createChoiceStep(int i, int j) {
        RadioButton stepBtn = new RadioButton("Пошаговый режим");
        RadioButton nostepBtn = new RadioButton("Автоматический режим");
        group = new ToggleGroup();
        stepBtn.setToggleGroup(group);
        nostepBtn.setToggleGroup(group);
        stepBtn.setSelected(true);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(stepBtn, nostepBtn);
        rootDialog.add(hbox, i, j);
    }
    
    private void createButtons() {
        Button btnOk = new Button("Продолжить");
        createChoiceStep(0, idIt);
        rootDialog.add(btnOk, 0, idIt + 1);
        btnOk.setOnAction((ActionEvent e) -> {
            try {
                if (isInputValid() == true) {
                    try {
                        handleOk();
                    } catch (WrongNumException | InterruptedException ex) {
                        Logger.getLogger(ChoiceBasisDialogFraction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка с форматом ввода");
                    alert.setContentText("\"В веденном вами базисе должно содержаться столько неотрицательных элементов, сколько ограничений в исходной задаче"
                            + "\nНа ввод должно поступать неотрицательное целое число, либо  неотрицательная обыкновенная дробь \nФормат числа[1,213,123...]\n"
                            + "Формат дроби[1/2,1/3,1/3,0/2...]\"");
                    alert.showAndWait();
                }
            } catch (WrongNumException ex) {
                Logger.getLogger(ChoiceBasisDialogFraction.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Button btnArtificial = new Button("Продолжить используя искусственный базис");
        rootDialog.add(btnArtificial, 0, idIt + 2);
        btnArtificial.setOnAction((ActionEvent e) -> {
            try {
                handleArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(ChoiceBasisDialogFraction.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public ChoiceBasisDialogFraction(SourceTaskFraction task) {
        this.task = task;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Controller.view.setMainGrid();
                return;
            }
        });
        dialog.setTitle("Выбор режима решения");
        rootDialog = new GridPane();
        rootDialog.setPadding(new Insets(10, 10, 10, 10));
        createHelperBasis();
        rootDialog.setAlignment(Pos.CENTER);
        rootDialog.setHgap(10);
        rootDialog.setVgap(10);
        fontDialog = Font.font("Tahoma", FontWeight.NORMAL, 20);
        scrollPaneDialog = new ScrollPane();
        scrollPaneDialog.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPaneDialog.setContent(rootDialog);
        createNameText();
        createButtons();
        Scene scene = new Scene(scrollPaneDialog, 400, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private boolean isInputValid() throws WrongNumException {
        boolean result = true;
        int count = 0;
        for (TextField elm : arrText) {
            //проверка числа/дроби
            if (!elm.getText().matches("-?[0-9]+(['/']{1}[1-9]{1}[0-9]*)?")) {
                result = false;
            }
        }

        //проверка на  количество ненулевых базисных переменных
        for (int i = 0; i < task.sizeX(); i++) {
            Fraction B = MethodsForFraction.check(arrText[i].getText());
            if (B.getNum() >= 0) {
                count++;
            }
        }
        
        if (count != task.sizeEq()) {
            result = false;
        }
        return result;
    }
    
    private void handleOk() throws WrongNumException, InterruptedException {
        posDialog = new Fractions();
        task.setFalseArtificial();
        for (int i = 0; i < task.sizeX(); i++) {
            Fraction B = MethodsForFraction.check(arrText[i].getText());
            posDialog.add(B);
        }
        task.setX0(posDialog);
        RadioButton selection = (RadioButton) group.getSelectedToggle();
        
        StepSimplexFraction firstStep = null;
        //Если минор не вырожден
        if (MethodsForFraction.checkCorrectMinorBssis(task)) {
            //если введенный базис совпал с найденым
            firstStep = MethodsForFraction.createFirstStep(task);
            if (MethodsForFraction.checkTrueBasis(task)) {
                InputInfo.stepBaseFraction.add(firstStep);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка в введенном базисе");
                alert.setContentText("\"Введённый вами базис не ссответсвует найденному базису, путем приведения нужного минора к единичной матрицу."
                        + "Чтобы решить задачу нужно ввести верный базис или найти его с помощью метода искуственного базиса\"");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка в введенном базисе");
            alert.setContentText("\"Введённый вами базис является вырожденным, то есть через него невозможно выразить необходимые перемнные, т.к он содержит нулевую строку\"");
            alert.showAndWait();
        }
        
        if (InputInfo.stepBaseFraction.size() == 0) {
            InputInfo.readFileFraction();
            this.task = InputInfo.sTableFraction;
            return;
        } else {
            dialog.close();
        }
        
        if ("Пошаговый режим".equals(selection.getText())) {
            Controller.view.createSimplexStepTableStandard();
        }
        if ("Автоматический режим".equals(selection.getText())) {
            MethodsForFraction.autoSimplexMethodFraction();
            Controller.view.createSimplexStepTableAutoStandard();
        }
        
    }
    
    private void handleArtificial() throws WrongNumException, InterruptedException {
        task.setTrueArtificial();
        posDialog = new Fractions();
        for (int i = 0; i < (task.sizeX()); i++) {
            Fraction B = new Fraction(-1, 1);
            posDialog.add(B);
        }
        for (int i = task.sizeX(); i < (task.sizeX() + task.sizeEq()); i++) {
            Fraction B = new Fraction(1, 1);
            posDialog.add(B);
        }
        task.setX0(posDialog);
        task.createTargetArtificial();
        RadioButton selection = (RadioButton) group.getSelectedToggle();
        InputInfo.stepBaseFraction.add(new StepSimplexFraction(InputInfo.sTableFraction.createFullTable(), InputInfo.sTableFraction.getPosition()));
        if ("Пошаговый режим".equals(selection.getText())) {
            Controller.view.createSimplexStepTableArtificial();
            dialog.close();
        }
        if ("Автоматический режим".equals(selection.getText())) {
            dialog.close();
            MethodsForFraction.autoMethodArtificialFraction();
            Controller.view.createSimplexTableAutoArtificial();
        }
    }
    
    private void createHelperBasis() {
        Button btnToolTip = new Button("Что за -1?");
        btnToolTip.setStyle("    -fx-text-fill: rgb(49, 89, 23);\n"
                + "    -fx-border-radius: 15;\n"
                + "    -fx-background-color: #10C872;\n"
                + "    -fx-padding: 5;");
        btnToolTip.getStyleClass().add("helper");
        Tooltip helperTooltip = new Tooltip("Минус единицей, а точнее отрицательным числом помечаются свободные перменные\nЭто сделано для того чтобы можно было явно определить базисную переменную\nсо значением ноль!");
        helperTooltip.setStyle("-fx-font: 16 arial;");
        helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_LEFT);
        hackTooltipStartTiming(helperTooltip);
        btnToolTip.setTooltip(helperTooltip);
        rootDialog.getChildren().add(btnToolTip);
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
