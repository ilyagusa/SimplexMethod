/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.System.in;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.TextField;

/**
 *
 * @author Ilya
 */
public class InputInfo {

    static SourceTaskFraction sTableFraction;
    static FractionsList flEqFraction;
    static DataStepFraction stepBaseFraction = new DataStepFraction();
    static ArrayList<ArrayList<Double>> flEqNum;
    static SourceTaskNumber sTableNumber;
    static DataStepNumber stepBaseNumber = new DataStepNumber();

    public static void readFileFraction() throws WrongNumException {
        File in = new File("data/inFr.txt");
        flEqFraction = new FractionsList();
        try (Scanner sc = new Scanner(in)) {
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String[] strParse = str.split(" ");
                Fractions frs = new Fractions();
                for (int i = 0; i < strParse.length; i++) {
                    Fraction B = MethodsForFraction.check(strParse[i]);
                    frs.add(B);
                }
                flEqFraction.add(frs);
            }
            Fractions target = flEqFraction.getFractions(0);
            flEqFraction.remove(0);
            sTableFraction = new SourceTaskFraction(target, flEqFraction);
        } catch (FileNotFoundException ex) {
            System.out.println("Нет файла");
        } catch (WrongNumException ex) {
            System.out.println("Неправильный формат числа");
        } catch (NumberFormatException ex) {
            System.out.println("Неправильный формат ввода");
        }
    }

    public static void readInputFraction(TextField[][] a, int sizeEq, int sizeX) throws IOException, WrongNumException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("data/inFr.txt")));
        for (int j = 0; j < sizeX; j++) {
            writer.write(a[0][j].getText());
            writer.write(" ");
        }
        for (int i = 1; i < sizeEq + 1; i++) {
            writer.write("\r\n");
            for (int j = 0; j < sizeX + 1; j++) {
                writer.write((a[i][j]).getText());
                writer.write(" ");
            }
        }
        writer.flush();
        readFileFraction();
    }

    public static void readInputNumber(TextField[][] a, int sizeEq, int sizeX) throws IOException, WrongNumException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("data/inNum.txt")));
        for (int j = 0; j < sizeX; j++) {
            writer.write(a[0][j].getText());
            writer.write(" ");
        }
        for (int i = 1; i < sizeEq + 1; i++) {
            writer.write("\r\n");
            for (int j = 0; j < sizeX + 1; j++) {
                writer.write((a[i][j]).getText());
                writer.write(" ");
            }
        }
        writer.flush();
        readFileNumber();
    }

    public static void readFileNumber() throws WrongNumException {

        File in = new File("data/inNum.txt");

        flEqNum = new ArrayList<ArrayList<Double>>();
        try (Scanner sc = new Scanner(in)) {
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                String[] strParse = str.split(" ");
                ArrayList<Double> row = new ArrayList<Double>();
                for (int i = 0; i < strParse.length; i++) {
                    Double element = Double.parseDouble(strParse[i]);
                    row.add(element);
                }
                flEqNum.add(row);
            }
            ArrayList<Double> target = flEqNum.get(0);
            flEqNum.remove(0);
            sTableNumber = new SourceTaskNumber(target, flEqNum);
        } catch (FileNotFoundException ex) {
            System.out.println("Нет файла");
        } catch (NumberFormatException ex) {
            System.out.println("Неправильный формат ввода");
        }
    }

    public static void clear() {
        sTableFraction = null;
        flEqFraction = new FractionsList();
        stepBaseFraction = new DataStepFraction();
        flEqNum = new ArrayList<ArrayList<Double>>();
        sTableNumber = null;
        stepBaseNumber = new DataStepNumber();
    }

}
