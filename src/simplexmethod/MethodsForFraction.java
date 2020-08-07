/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ilya
 */
public class MethodsForFraction {

    public static int gcdThing(int a, int b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    public static Fraction check(String str) throws WrongNumException {
        String strArray[];
        {
            if (str.contains("/")) {
                strArray = str.split("/");
                return new Fraction(Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]));
            } else {
                return new Fraction(Integer.parseInt(str), 1);
            }
        }

    }

    public static String[][] getSimplexTable(FractionsList matrix, ArrayList<Integer> position) throws WrongNumException {
        int countCol = 1;
        int countRow = 1;
        int sizeEq = matrix.size() - 1;
        String table[][] = new String[sizeEq + 2][position.size() - sizeEq + 2];
        //0 0  элемент обозначающий итерацию симплекс метода!
        table[0][0] = "it=" + InputInfo.stepBaseFraction.size();
        //Последний столбец 0 строки Бета
        table[0][position.size() - sizeEq + 1] = "B";
        //строка с свободными иксами
        for (int j = sizeEq; j < position.size(); j++) {
            table[0][countCol] = "x" + position.get(j);
            table[sizeEq + 1][countCol] = matrix.getFractions(matrix.size() - 1).getFraction(countCol - 1).toString();
            countCol++;
        }

        //заполнение нижней строки
        for (int i = 1; i < position.size() - sizeEq + 2; i++) {
            table[sizeEq + 1][i] = matrix.getFractions(matrix.size() - 1).getFraction(i - 1).toString();
        }

        //столбец с базисными иксами
        for (int j = 0; j < sizeEq; j++) {
            table[countRow][0] = "x" + position.get(j);
            countRow++;
        }
        countRow = 1;
        table[sizeEq + 1][0] = "f0";
        //Заполнение значений при иксах
        for (int i = 0; i < sizeEq; i++) {
            countCol = 1;
            for (int j = sizeEq; j < position.size(); j++) {
                table[countRow][countCol] = matrix.getFractions(i).getFraction(countCol - 1).toString();
                countCol++;
            }
            countRow++;
        }
        //Заполнение Бет

        int sizeTarget = matrix.getFractions(0).size() - 1;

        for (int i = 0; i < sizeEq; i++) {
            table[i + 1][position.size() - sizeEq + 1] = matrix.getFractions(i).getFraction(sizeTarget).toString();
        }
        return table;
    }

    //проверка вроде работает ( на бесконечность )
    public static boolean checkInfinity(StepSimplexFraction tmpStep) {
        boolean result = false;
        for (int i = 0; i < tmpStep.getMatrix().getFractions(0).size() - 1; i++) {
            int count = 0;
            if (tmpStep.getLastString().getFraction(i).getNum() >= 0) {
                continue;
            }
            for (int j = 0; j < tmpStep.getMatrix().size() - 1; j++) {
                if (tmpStep.getMatrix().getFractions(j).getFraction(i).getNum() <= 0) {
                    count++;
                }
            }
            if (count == tmpStep.getMatrix().size() - 1) {
                result = true;
            }
        }
        return result;
    }

    //public StepSimplex nextStep
    public static ArrayList supportElmTmpStep(StepSimplexFraction tmpStep) throws WrongNumException {
        ArrayList<Fraction> supportArray;
        supportArray = new ArrayList();
        ArrayList allSupportElm;
        allSupportElm = new ArrayList();
        for (int i = 0; i < tmpStep.getMatrix().getFractions(0).size() - 1; i++) {
            if (tmpStep.getLastString().getFraction(i).getNum() >= 0) {
                supportArray.add(new Fraction(-1, 1));
                continue;
            }
            Fraction min = tmpStep.getMatrix().getMax();
            for (int j = 0; j < tmpStep.getMatrix().size() - 1; j++) {
                //проверка для присваивания min нужного значения
                int count = 0;
                if (tmpStep.getMatrix().getFractions(j).getFraction(i).getNum() > 0) {
                    if (min.equals((tmpStep.getMatrix().getFractions(j).getFraction(tmpStep.getMatrix().getFractions(0).size() - 1).divide(tmpStep.getMatrix().getFractions(j).getFraction(i)))) == 1) {
                        min = tmpStep.getMatrix().getFractions(j).getFraction(tmpStep.getMatrix().getFractions(0).size() - 1).divide(tmpStep.getMatrix().getFractions(j).getFraction(i));
                    }
                }
            }
            supportArray.add(min);
        }
        for (int i = 0; i < tmpStep.getMatrix().getFractions(0).size() - 1; i++) {
            for (int j = 0; j < tmpStep.getMatrix().size() - 1; j++) {
                String s = "";
                if (tmpStep.getMatrix().getFractions(j).getFraction(i).getNum() > 0) {
                    if (supportArray.get(i).equals((tmpStep.getMatrix().getFractions(j).getFraction(tmpStep.getMatrix().getFractions(0).size() - 1).divide(tmpStep.getMatrix().getFractions(j).getFraction(i)))) == 0) {
                        s = "[" + j + "][" + i + "]=" + supportArray.get(i);
                        // s = "[x" + (Integer.parseInt(tmpStep.getPosition().get(j).toString())) + "][x" + (Integer.parseInt(tmpStep.getPosition().get(i + tmpStep.getMatrix().size() - 1).toString())) + "]=" + supportArray.get(i);
                        allSupportElm.add(s);
                    }
                }
            }
        }

        return allSupportElm;
    }

    //numRow numCol индексы опорного элемента
    public static StepSimplexFraction nextStepArtificialFraction(StepSimplexFraction tmpStep, int numRow, int numCol) throws WrongNumException {
        Fraction divSupp = new Fraction(1, 1).divide(tmpStep.getMatrix().getFractions(numRow).getFraction(numCol));

        Fractions supRow = new Fractions();
        for (int j = 0; j < tmpStep.getLastString().size(); j++) {
            if (j == numCol) {
                supRow.add(divSupp);
            } else {
                supRow.add(tmpStep.getMatrix().getFractions(numRow).getFraction(j).multiply(divSupp));
            }
        }
        FractionsList matrix = createVoidTable(tmpStep.getMatrix().size(), tmpStep.getLastString().size());
        matrix.setFractions(numRow, supRow);
        for (int i = 0; i < tmpStep.getMatrix().size(); i++) {
            if (i == numRow) {
                continue;
            }
            Fractions newRow = new Fractions();
            for (int j = 0; j < tmpStep.getLastString().size(); j++) {
                if (j == numCol) {
                    newRow.add(divSupp.multiply(new Fraction(-1, 1)).multiply(tmpStep.getMatrix().getFractions(i).getFraction(j)));
                } else {
                    newRow.add(tmpStep.getMatrix().getFractions(i).getFraction(j).sub((supRow.getFraction(j)).multiply(tmpStep.getMatrix().getFractions(i).getFraction(numCol))));
                }
            }
            matrix.setFractions(i, newRow);
        }
        matrix.getFractions(numRow).setFraction(numCol, divSupp);

        int numCol1 = numCol + tmpStep.getMatrix().size() - 1;
        ArrayList position = new ArrayList();
        for (int i = 0; i < tmpStep.getPosition().size(); i++) {
            if (i == numCol1) {
                position.add(i, tmpStep.getPosition().get(numRow));
            } else if (i == numRow) {
                position.add(i, tmpStep.getPosition().get(numCol1));
            } else {
                position.add(i, tmpStep.getPosition().get(i));
            }
        }
////////////

        int sizeEq = matrix.size() - 1;
        int sizePosition = position.size();
        for (int i = sizeEq; i < sizePosition; i++) {
            if (Integer.parseInt(position.get(i).toString()) > (InputInfo.stepBaseFraction.getStep(0).getLastString().size() - 1)) {
                position.remove(i);
                matrix.removeCol(i - sizeEq);
                break;
            }
        }
/////////////
        return new StepSimplexFraction(matrix, position);
    }

    public static StepSimplexFraction nextStep(StepSimplexFraction tmpStep, int numRow, int numCol) throws WrongNumException {
        Fraction divSupp = new Fraction(1, 1).divide(tmpStep.getMatrix().getFractions(numRow).getFraction(numCol));

        Fractions supRow = new Fractions();
        for (int j = 0; j < tmpStep.getLastString().size(); j++) {
            if (j == numCol) {
                supRow.add(divSupp);
            } else {
                supRow.add(tmpStep.getMatrix().getFractions(numRow).getFraction(j).multiply(divSupp));
            }
        }
        FractionsList matrix = createVoidTable(tmpStep.getMatrix().size(), tmpStep.getLastString().size());
        matrix.setFractions(numRow, supRow);
        for (int i = 0; i < tmpStep.getMatrix().size(); i++) {
            if (i == numRow) {
                continue;
            }
            Fractions newRow = new Fractions();
            for (int j = 0; j < tmpStep.getLastString().size(); j++) {
                if (j == numCol) {
                    newRow.add(divSupp.multiply(new Fraction(-1, 1)).multiply(tmpStep.getMatrix().getFractions(i).getFraction(j)));
                } else {
                    newRow.add(tmpStep.getMatrix().getFractions(i).getFraction(j).sub((supRow.getFraction(j)).multiply(tmpStep.getMatrix().getFractions(i).getFraction(numCol))));
                }
            }
            matrix.setFractions(i, newRow);
        }
        matrix.getFractions(numRow).setFraction(numCol, divSupp);

        int numCol1 = numCol + tmpStep.getMatrix().size() - 1;
        ArrayList position = new ArrayList();
        for (int i = 0; i < tmpStep.getPosition().size(); i++) {
            if (i == numCol1) {
                position.add(i, tmpStep.getPosition().get(numRow));
            } else if (i == numRow) {
                position.add(i, tmpStep.getPosition().get(numCol1));
            } else {
                position.add(i, tmpStep.getPosition().get(i));
            }
        }

        return new StepSimplexFraction(matrix, position);
    }

    private static FractionsList createVoidTable(int sizeEq, int sizeX) throws WrongNumException {
        FractionsList a = new FractionsList();
        for (int i = 0; i < sizeEq; i++) {
            Fractions row = new Fractions();
            for (int j = 0; j < sizeX; j++) {
                row.add(new Fraction(0, 1));
            }
            a.add(row);
        }
        return a;
    }

    public static Fractions createVoidRow(int size) throws WrongNumException {
        Fractions row = new Fractions();
        for (int j = 0; j < size; j++) {
            row.add(new Fraction(0, 1));
        }
        return row;
    }

    public static int[] needSupport(String str) {
        String[] strSupport;
        strSupport = str.split("=");
        String resultString;
        resultString = strSupport[0].replaceAll("\\D+", " ").trim();
        String split[] = resultString.split(" ");
        int[] indexSupport = new int[2];
        indexSupport[0] = Integer.parseInt(split[0]);
        indexSupport[1] = Integer.parseInt(split[1]);
        return indexSupport;
    }

    public static void autoMethodArtificialFraction() throws WrongNumException {
        while (!InputInfo.stepBaseFraction.getLastStep().endArtificial() || checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            InputInfo.stepBaseFraction.add(nextStepArtificialFraction(InputInfo.stepBaseFraction.getLastStep(), needSupport((String) supportElmTmpStep(InputInfo.stepBaseFraction.getLastStep()).get(0))[0], needSupport((String) supportElmTmpStep(InputInfo.stepBaseFraction.getLastStep()).get(0))[1]));
            if (InputInfo.stepBaseFraction.getLastStep().end() && (InputInfo.stepBaseFraction.getLastStep().getF0().getNum() != 0)) {
                break;
            }
        }
    }

    public static void autoSimplexMethodFraction() throws WrongNumException {
        while (!InputInfo.stepBaseFraction.getLastStep().end() || checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            if (checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
                break;
            }
            InputInfo.stepBaseFraction.add(nextStep(InputInfo.stepBaseFraction.getLastStep(), needSupport((String) supportElmTmpStep(InputInfo.stepBaseFraction.getLastStep()).get(0))[0], needSupport((String) supportElmTmpStep(InputInfo.stepBaseFraction.getLastStep()).get(0))[1]));
        }
    }

    public static StepSimplexFraction createStepAfterArtificialMethodFraction(StepSimplexFraction tmpStep, SourceTaskFraction task, Fractions targetSource) throws WrongNumException {
        int sizeEq = tmpStep.getMatrix().size() - 1;
        FractionsList matrixSolution = createVoidTable(task.sizeX() - 1, task.sizeX());
        for (int i = 0; i < task.sizeEq(); i++) {
            Fractions row = createVoidRow(task.sizeX());
            for (int j = sizeEq; j < tmpStep.getPosition().size(); j++) {
                row.setFraction(Integer.parseInt(tmpStep.getPosition().get(j).toString()) - 1, tmpStep.getMatrix().getFractions(i).getFraction(j - task.sizeEq() + 1).multiply(new Fraction(-1, 1)));
            }
            row.setFraction(row.size() - 1, tmpStep.getMatrix().getFractions(i).getFraction(tmpStep.getLastString().size() - 1));
            matrixSolution.setFractions((Integer.parseInt(tmpStep.getPosition().get(i).toString()) - 1), row);
        }
        FractionsList matrix = createVoidTable(tmpStep.getMatrix().size(), tmpStep.getLastString().size());
        //заполняем матрицу значениями из матрицы предыдущего шага
        for (int i = 0; i < sizeEq; i++) {
            matrix.setFractions(i, tmpStep.getMatrix().getFractions(i));
        }

        //берём целевую функцию из исходной задачи
        Fractions target = new Fractions();
        for (int i = 0; i < targetSource.size(); i++) {
            target.add(targetSource.getEl(i));
        }
        target.add(new Fraction(0, 1));
        Fractions resultTarget = target;
        //подставяем наши замены в целевую функцию
        for (int i = 0; i < task.sizeX() - 1; i++) {
            resultTarget.sum(matrixSolution.getFractions(i), target.getEl(i));
        }

        //Убираем лишние значения в целевой функции
        for (int i = 0; i < sizeEq; i++) {
            resultTarget.setFraction(Integer.parseInt(tmpStep.getPosition().get(i).toString()) - 1, new Fraction(0, 1));
        }

        Fractions lastRow = createVoidRow(matrix.getFractions(0).size());

        //формируем последнюю строку таблицы
        for (int i = sizeEq; i < tmpStep.getPosition().size(); i++) {
            lastRow.setFraction(i - (sizeEq), resultTarget.getFraction(Integer.parseInt(tmpStep.getPosition().get(i).toString()) - 1));
        }
        lastRow.setFraction(lastRow.size() - 1, resultTarget.getFraction(resultTarget.size() - 1).multiply(new Fraction(-1, 1)));
        matrix.remove(matrix.size() - 1);
        matrix.add(lastRow);
        return new StepSimplexFraction(matrix, tmpStep.getPosition());
    }

    public static boolean checkDependence(FractionsList matrix) {
        int sizeMatrix = matrix.size();
        FractionsList copyMatrix = new FractionsList();
        for (int i = 0; i < sizeMatrix; i++) {
            Fractions row = new Fractions();
            for (int j = 0; j < matrix.getFractions(0).size(); j++) {
                row.add(matrix.getFractions(i).getFraction(j));
            }
            copyMatrix.add(row);
        }

        try {
            bottomTriangle(copyMatrix);
        } catch (WrongNumException ex) {
            Logger.getLogger(MethodsForFraction.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean result = false;

        for (int i = 0; i < copyMatrix.size(); i++) {
            int countZero = 0;
            for (int j = 0; j < copyMatrix.getFractions(0).size() - 1; j++) {
                if (copyMatrix.getFractions(i).getFraction(j).getNum() == 0) {
                    countZero++;
                }
            }
            if (countZero == copyMatrix.getFractions(0).size() - 1) {
                result = true;
            }
        }

        // РОБОТАЕТ
        for (int i = 0; i < copyMatrix.size(); i++) {
        }

        return result;

    }

    //мне это нужно для испрвленмч
    public static void createMatrixByBasis(FractionsList matrix, ArrayList position) throws WrongNumException {
        //index = position.get(k)-1
        //преобразование к нижн.треуг.
        int index = 0;
        Fraction max;
        for (int k = 0; k < matrix.size(); k++) {

            index = k;
            max = matrix.getFractions(k).getFraction(Integer.parseInt(position.get(k).toString()) - 1).absFraction();

            for (int i = k + 1; i < matrix.size(); i++) {
                if (matrix.getFractions(i).getFraction(Integer.parseInt(position.get(k).toString()) - 1).absFraction().equals(max) == 1) {
                    max = matrix.getFractions(i).getFraction(Integer.parseInt(position.get(k).toString()) - 1).absFraction();
                    index = i;
                }
            }

            swapRow(index, k, matrix);

            for (int i = k + 1; i < matrix.size(); i++) {
                if (matrix.getFractions(k).getFraction(Integer.parseInt(position.get(k).toString()) - 1).getNum() == 0) {
                    continue;
                }
                Fraction mu = matrix.getFractions(i).getFraction(Integer.parseInt(position.get(k).toString()) - 1).divide(matrix.getFractions(k).getFraction(Integer.parseInt(position.get(k).toString()) - 1));
                for (int j = 0; j < matrix.getFractions(0).size(); j++) {
                    matrix.getFractions(i).setFraction(j, matrix.getFractions(i).getFraction(j).sub((matrix.getFractions(k).getFraction(j).multiply(mu))));
                }
            }
        }

        //преобразование к верхн.треуг.
        for (int k = matrix.size() - 1; k >= 0; k--) {

            index = k;
            //abs[k][pos[k]-1]
            max = matrix.getFractions(k).getFraction(Integer.parseInt(position.get(k).toString()) - 1).absFraction();
            //max = Math.abs(this.matrixEquations[k][k]);

            for (int i = k + 1; i < matrix.size(); i++) {
                if (matrix.getFractions(i).getFraction(Integer.parseInt(position.get(k).toString()) - 1).absFraction().equals(max) == 1) {
                    max = matrix.getFractions(i).getFraction(Integer.parseInt(position.get(k).toString()) - 1).absFraction();
                    index = i;
                }
            }

            swapRow(index, k, matrix);
            for (int i = k - 1; i >= 0; i--) {
                if (matrix.getFractions(k).getFraction(Integer.parseInt(position.get(k).toString()) - 1).getNum() == 0) {
                    continue;
                }
                Fraction mu = matrix.getFractions(i).getFraction(Integer.parseInt(position.get(k).toString()) - 1).divide(matrix.getFractions(k).getFraction(Integer.parseInt(position.get(k).toString()) - 1));
                for (int j = 0; j < matrix.getFractions(0).size(); j++) {
                    matrix.getFractions(i).setFraction(j, matrix.getFractions(i).getFraction(j).sub((matrix.getFractions(k).getFraction(j).multiply(mu))));
                }
            }
        }

        //преобразование минора к едининичной матрице
        for (int i = 0; i < matrix.size(); i++) {
            matrix.getFractions(i).divideByFraction(matrix.getFractions(i).getFraction(Integer.parseInt(position.get(i).toString()) - 1));
        }

    }

    private static void bottomTriangle(FractionsList matrix) throws WrongNumException {
        int index = 0;
        Fraction max;
        for (int k = 0; k < matrix.size(); k++) {
            index = k;
            //abs[k][pos[k]-1]
            max = matrix.getFractions(k).getFraction(k).absFraction();
            //max = Math.abs(this.matrixEquations[k][k]);
            for (int i = k + 1; i < matrix.size(); i++) {
                if (matrix.getFractions(i).getFraction(k).absFraction().equals(max) == 1) {
                    max = matrix.getFractions(i).getFraction(k).absFraction();
                    index = i;
                }
            }

            swapRow(index, k, matrix);

            for (int i = k + 1; i < matrix.size(); i++) {
                if (matrix.getFractions(k).getFraction(k).getNum() == 0) {
                    continue;
                }
                Fraction mu = matrix.getFractions(i).getFraction(k).divide(matrix.getFractions(k).getFraction(k));
                for (int j = 0; j < matrix.getFractions(0).size(); j++) {
                    matrix.getFractions(i).setFraction(j, matrix.getFractions(i).getFraction(j).sub((matrix.getFractions(k).getFraction(j).multiply(mu))));
                }
            }
        }
    }

    private static void swapRow(int index, int k, FractionsList matrix) {
        Fractions tmp;
        tmp = matrix.getFractions(k);
        matrix.setFractions(k, matrix.getFractions(index));
        matrix.setFractions(index, tmp);
    }

    public static StepSimplexFraction createFirstStep(SourceTaskFraction task) throws WrongNumException {
        createMatrixByBasis(task.getMatrix(), task.getPosition());
        FractionsList matrixSolution = createVoidTable(task.sizeEq(), task.sizeTaskX());
        //создаем матрицу  для подстановки в целевую функцию
        for (int i = 0; i < task.sizeEq(); i++) {
            Fractions row = createVoidRow(task.sizeTaskX());
            for (int j = 0; j < task.sizeTaskX() - 1; j++) {
                row.setFraction(j, task.getMatrix().getFractions(i).getFraction(j).multiply(new Fraction(-1, 1)));
            }
            row.setFraction(task.sizeTaskX() - 1, task.getMatrix().getFractions(i).getFraction(task.sizeTaskX() - 1));
            matrixSolution.setFractions(i, row);
        }

        //добавляем 0 элемент к целевой функции(свободный член)
        task.getTarget().add(new Fraction(0, 1));
        Fractions resultTarget = createVoidRow(task.getTarget().size());
        //копируем целевую функцию
        for (int i = 0; i < task.sizeX(); i++) {
            resultTarget.setFraction(i, task.getTarget().getFraction(i));
        }

        //подставяем наши замены в целевую функцию
        for (int i = 0; i < task.sizeEq(); i++) {
            resultTarget.sum(matrixSolution.getFractions(i), task.getTarget().getFraction(Integer.parseInt(task.getPosition().get(i).toString()) - 1));
        }

        //Убираем лишние значения в целевой функции(ставим вместо них нули), которые позже удалим вместе со столбцами
        for (int i = 0; i < task.sizeEq(); i++) {
            resultTarget.setFraction(Integer.parseInt(task.getPosition().get(i).toString()) - 1, new Fraction(0, 1));
        }

        //добавляем новую посчитанную целевую функцию в исходную симплекс таблицу
        task.getMatrix().add(resultTarget);

        //домножаем нижний левый элемент на -1
        task.getMatrix().getFractions(task.getMatrix().size() - 1).setFraction(task.getMatrix().getFractions(0).size() - 1, task.getMatrix().getFractions(task.getMatrix().size() - 1).getFraction(task.getMatrix().getFractions(0).size() - 1).multiply(new Fraction(-1, 1)));
        //удаление из таблицы столбцов соотвествующих базисным переменным
        //task.getMatrix().siz1 - 1 т.к мы учитываем то что мы уже добавили новую строку в матрицу исходной задачи
        int sizeMatrixDelete = task.getMatrix().size() - 1;
        for (int i = 0; i < sizeMatrixDelete; i++) {
            //-(i+1) т.к нужно учитывать то что при удалении столбца уменьшится размер матрицы
            int removedIndex = Integer.parseInt(task.getPosition().get(i).toString()) - (i + 1);
            task.getMatrix().removeCol(removedIndex);
        }

        StepSimplexFraction firstStep = new StepSimplexFraction(task.getMatrix(), task.getPosition());
        return firstStep;
    }

    public static boolean checkTrueBasis(SourceTaskFraction task) throws WrongNumException {
        Fractions basisCopy = createVoidRow(task.getBasis().size());
        //копируем базис
        for (int i = 0; i < task.getBasis().size(); i++) {
            basisCopy.setFraction(i, task.getBasis().getFraction(i));
        }
        //удаляем из копии нули
        int sizeBasisCopy = task.getBasis().size();
        for (int i = 0; i < sizeBasisCopy; i++) {
            if (basisCopy.getFraction(i).getNum() < 0) {
                basisCopy.remove(i);
                i--;
                sizeBasisCopy--;
            }
        }

        boolean resultBasis = true;

        for (int i = 0; i < task.getMatrix().size() - 1; i++) {
            if (basisCopy.getFraction(i).equals(task.getMatrix().getFractions(i).getFraction(task.getMatrix().getFractions(0).size() - 1)) != 0) {
                resultBasis = false;
            }
        }
        return resultBasis;
    }

    public static boolean checkCorrectMinorBssis(SourceTaskFraction task) throws WrongNumException {
        boolean resultCorrect = true;
        FractionsList minorBasis = createVoidTable(task.getMatrix().size(), task.getMatrix().size());
        for (int i = 0; i < task.getMatrix().size(); i++) {
            Fractions row = createVoidRow(task.getMatrix().size());
            for (int j = 0; j < task.getMatrix().size(); j++) {
                row.setFraction(j, task.getMatrix().getFractions(i).getFraction(Integer.parseInt(task.getPosition().get(j).toString()) - 1));
            }
            minorBasis.setFractions(i, row);
        }
        bottomTriangle(minorBasis);
        for (int i = 0; i < task.getMatrix().size(); i++) {
        }
        for (int i = 0; i < minorBasis.size(); i++) {
            int count = 0;
            for (int j = 0; j < minorBasis.size(); j++) {
                if (minorBasis.getFractions(i).getFraction(j).getNum() == 0) {
                    count++;
                }
            }
            if (count == minorBasis.size()) {
                resultCorrect = false;
            }
        }
        return resultCorrect;
    }

}
