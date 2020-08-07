/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.math.BigDecimal;
import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.BigDecimal.ROUND_HALF_UP;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 *
 * @author Ilya
 */
public class MethodsForNumber {

    public static void multByNumber(ArrayList<Double> row, double elm) throws WrongNumException {
        BigDecimal mult = BigDecimal.valueOf(elm);
        for (int i = 0; i < row.size(); i++) {
            BigDecimal rowI = BigDecimal.valueOf(row.get(i));
            row.set(i, rowI.multiply(mult).doubleValue());
        }
    }

    public static void divideByNumber(ArrayList<Double> row, double elm) throws WrongNumException {
        for (int i = 0; i < row.size(); i++) {
            row.set(i, row.get(i) / elm);
        }
    }

    public static String[][] getSimplexTable(ArrayList<ArrayList<Double>> matrix, ArrayList<Integer> position) throws WrongNumException {
        int countCol = 1;
        int countRow = 1;
        int sizeEq = matrix.size() - 1;
        String table[][] = new String[sizeEq + 2][position.size() - sizeEq + 2];
        //0 0  элемент обозначающий итерацию симплекс метода!
        table[0][0] = "it=" + InputInfo.stepBaseNumber.size();
        //Последний столбец 0 строки Бета
        table[0][position.size() - sizeEq + 1] = "B";
        //строка с свободными иксами
        for (int j = sizeEq; j < position.size(); j++) {
            table[0][countCol] = "x" + position.get(j);
            table[sizeEq + 1][countCol] = matrix.get(matrix.size() - 1).get(countCol - 1).toString();
            countCol++;
        }

        //заполнение нижней строки
        for (int i = 1; i < position.size() - sizeEq + 2; i++) {
            table[sizeEq + 1][i] = matrix.get(matrix.size() - 1).get(i - 1).toString();
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
                table[countRow][countCol] = matrix.get(i).get(countCol - 1).toString();
                countCol++;
            }
            countRow++;
        }
        //Заполнение Бет

        int sizeTarget = matrix.get(0).size() - 1;

        for (int i = 0; i < sizeEq; i++) {
            table[i + 1][position.size() - sizeEq + 1] = matrix.get(i).get(sizeTarget).toString();
        }
        return table;
    }

    //проверка вроде работает ( на бесконечность )
    public static boolean checkInfinity(StepSimplexNumber tmpStep) {
        boolean result = false;
        for (int i = 0; i < tmpStep.getMatrix().get(0).size() - 1; i++) {
            int count = 0;
            if (tmpStep.getLastString().get(i) >= 0) {
                continue;
            }

            for (int j = 0; j < tmpStep.getMatrix().size() - 1; j++) {
                if (tmpStep.getMatrix().get(j).get(i) <= 0) {
                    count++;
                }
            }
            if (count == tmpStep.getMatrix().size() - 1) {
                result = true;
            }
        }
        return result;
    }

    public static boolean checkDependence(ArrayList<ArrayList<Double>> matrix) {

        int sizeMatrix = matrix.size();
        ArrayList<ArrayList<Double>> copyMatrix = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < sizeMatrix; i++) {
            ArrayList<Double> row = new ArrayList<Double>();
            for (int j = 0; j < matrix.get(0).size(); j++) {
                row.add(matrix.get(i).get(j));
            }
            copyMatrix.add(row);
        }

        bottomTriangle(copyMatrix);

        boolean result = false;
        double eps = 0.00000001;

        for (int i = 0; i < copyMatrix.size(); i++) {
            int countZero = 0;
            for (int j = 0; j < copyMatrix.get(0).size() - 1; j++) {
                if (Math.abs(copyMatrix.get(i).get(j)) < eps) {
                    countZero++;
                }
            }
            if (countZero == (copyMatrix.get(0).size() - 1)) {
                result = true;
            }
        }

        return result;

    }

    private static void bottomTriangle(ArrayList<ArrayList<Double>> matrix) {
        double eps = 0.00000001;
        int index = 0;
        Double max;
        for (int k = 0; k < matrix.size(); k++) {
            index = k;
            max = Math.abs(matrix.get(k).get(k));
            for (int i = k + 1; i < matrix.size(); i++) {
                if (Math.abs(matrix.get(i).get(k)) > max) {
                    max = Math.abs(matrix.get(i).get(k));
                    index = i;
                }
            }

            if (max < eps) {
                return;
            }

            swapRow(index, k, matrix);

            for (int i = k + 1; i < matrix.size(); i++) {
                if (matrix.get(k).get(k) < eps) {
                    continue;
                }

                double mu = (double) matrix.get(i).get(k) / (double) matrix.get(k).get(k);
                for (int j = 0; j < matrix.get(0).size(); j++) {
                    matrix.get(i).set(j, (matrix.get(i).get(j)) - (matrix.get(k).get(j) * mu));
                }
            }
        }
    }

    private static void swapRow(int index, int k, ArrayList<ArrayList<Double>> matrix) {
        ArrayList<Double> tmp;
        tmp = matrix.get(k);
        matrix.set(k, matrix.get(index));
        matrix.set(index, tmp);

    }

    public static boolean checkCorrectMinorBssis(SourceTaskNumber task) throws WrongNumException {
        double eps = 0.00000001;
        boolean resultCorrect = true;
        ArrayList<ArrayList<Double>> minorBasis = createVoidTable(task.getMatrix().size(), task.getMatrix().size());
        for (int i = 0; i < task.getMatrix().size(); i++) {
            ArrayList<Double> row = createVoidRow(task.getMatrix().size());
            for (int j = 0; j < task.getMatrix().size(); j++) {
                row.set(j, task.getMatrix().get(i).get(Integer.parseInt(task.getPosition().get(j).toString()) - 1));
            }
            minorBasis.set(i, row);
        }
        bottomTriangle(minorBasis);

        for (int i = 0; i < minorBasis.size(); i++) {
            int count = 0;
            for (int j = 0; j < minorBasis.size(); j++) {
                if (Math.abs(minorBasis.get(i).get(j)) < eps) {
                    count++;
                }
            }
            if (count == minorBasis.size()) {
                resultCorrect = false;
            }
        }
        return resultCorrect;
    }

    public static boolean checkTrueBasis(SourceTaskNumber task) throws WrongNumException {
        double eps = 0.00000001;
        ArrayList<Double> basisCopy = createVoidRow(task.getBasis().size());
        //копируем базис
        for (int i = 0; i < task.getBasis().size(); i++) {
            basisCopy.set(i, task.getBasis().get(i));
        }
        //удаляем из копии нули(здесь удаляются отрицательные числа, которые как бы являются не базисными нулями)
        int sizeBasisCopy = task.getBasis().size();
        for (int i = 0; i < sizeBasisCopy; i++) {
            if (basisCopy.get(i) < 0) {
                basisCopy.remove(i);
                i--;
                sizeBasisCopy--;
            }
        }

        boolean resultBasis = true;

        for (int i = 0; i < task.getMatrix().size() - 1; i++) {
            if (Math.abs(basisCopy.get(i) - task.getMatrix().get(i).get(task.getMatrix().get(0).size() - 1)) > eps) {
                resultBasis = false;
            }
        }
        return resultBasis;
    }

    public static void createMatrixByBasis(ArrayList<ArrayList<Double>> matrix, ArrayList position) throws WrongNumException {
        //преобразование к нижн.треуг.
        double eps = 0.00000001;
        int index = 0;
        double max;

        for (int k = 0; k < matrix.size(); k++) {

            index = k;
            max = Math.abs(matrix.get(k).get(Integer.parseInt(position.get(k).toString()) - 1));
            for (int i = k + 1; i < matrix.size(); i++) {
                if (Math.abs(matrix.get(i).get(Integer.parseInt(position.get(k).toString()) - 1)) > max) {
                    max = Math.abs(matrix.get(i).get(Integer.parseInt(position.get(k).toString()) - 1));
                    index = i;
                }
            }

            swapRow(index, k, matrix);

            for (int i = k + 1; i < matrix.size(); i++) {
                if (Math.abs(matrix.get(k).get(Integer.parseInt(position.get(k).toString()) - 1)) < eps) {
                    continue;
                }

                double mu = (double) matrix.get(i).get(Integer.parseInt(position.get(k).toString()) - 1) / (double) matrix.get(k).get(Integer.parseInt(position.get(k).toString()) - 1);
                if (Math.abs(mu) < eps) {
                    continue;
                }
                for (int j = 0; j < matrix.get(0).size(); j++) {
                    matrix.get(i).set(j, (BigDecimal.valueOf(matrix.get(i).get(j))).subtract(BigDecimal.valueOf(matrix.get(k).get(j) * mu)).doubleValue());
                }
            }
        }

        //преобразование к верхн.треуг.
        for (int k = matrix.size() - 1; k >= 0; k--) {

            index = k;
            max = Math.abs(matrix.get(k).get(Integer.parseInt(position.get(k).toString()) - 1));
            for (int i = k + 1; i < matrix.size(); i++) {
                if (Math.abs(matrix.get(i).get(Integer.parseInt(position.get(k).toString()) - 1)) > max) {
                    max = Math.abs(matrix.get(i).get(Integer.parseInt(position.get(k).toString()) - 1));
                    index = i;
                }
            }

            swapRow(index, k, matrix);
            for (int i = k - 1; i >= 0; i--) {
                if (Math.abs(matrix.get(k).get(Integer.parseInt(position.get(k).toString()) - 1)) < eps) {
                    continue;
                }

                double mu = (double) matrix.get(i).get(Integer.parseInt(position.get(k).toString()) - 1) / (double) matrix.get(k).get(Integer.parseInt(position.get(k).toString()) - 1);
                if (Math.abs(mu) < eps) {
                    continue;
                }
                for (int j = 0; j < matrix.get(0).size(); j++) {
                    //  matrix.get(i).set(j, (matrix.get(i).get(j)) - (matrix.get(k).get(j) * mu));
                    matrix.get(i).set(j, (BigDecimal.valueOf(matrix.get(i).get(j))).subtract(BigDecimal.valueOf(matrix.get(k).get(j) * mu)).doubleValue());
                }
            }
        }

        //преобразование минора к едининичной матрице
        for (int i = 0; i < matrix.size(); i++) {
            MethodsForNumber.divideByNumber(matrix.get(i), matrix.get(i).get(Integer.parseInt(position.get(i).toString()) - 1));
        }

    }

    private static void sum(ArrayList<Double> main, ArrayList<Double> a, double mult) throws WrongNumException {
        for (int i = 0; i < main.size(); i++) {
            main.set(i, BigDecimal.valueOf(main.get(i)).add(BigDecimal.valueOf(a.get(i) * mult)).doubleValue());
        }
    }

    //нужно сделать метод для создания первого шага
    public static StepSimplexNumber createFirstStep(SourceTaskNumber task) throws WrongNumException {
        createMatrixByBasis(task.getMatrix(), task.getPosition());
        ArrayList<ArrayList<Double>> matrixSolution = createVoidTable(task.sizeEq(), task.sizeTaskX());
        //создаем матрицу  для подстановки в целевую функцию
        for (int i = 0; i < task.sizeEq(); i++) {
            ArrayList<Double> row = createVoidRow(task.sizeTaskX());
            for (int j = 0; j < task.sizeTaskX() - 1; j++) {
                row.set(j, task.getMatrix().get(i).get(j) * (-1));
            }
            row.set(task.sizeTaskX() - 1, task.getMatrix().get(i).get(task.sizeTaskX() - 1));
            matrixSolution.set(i, row);
        }

        //добавляем 0 элемент к целевой функции(свободный член)
        task.getTarget().add(0.0);
        ArrayList<Double> resultTarget = createVoidRow(task.getTarget().size());
        //копируем целевую функцию
        for (int i = 0; i < task.sizeX(); i++) {
            resultTarget.set(i, task.getTarget().get(i));
        }

        //подставяем наши замены в целевую функцию
        for (int i = 0; i < task.sizeEq(); i++) {
            sum(resultTarget, matrixSolution.get(i), task.getTarget().get(Integer.parseInt(task.getPosition().get(i).toString()) - 1));
        }

        //Убираем лишние значения в целевой функции(ставим вместо них нули), которые позже удалим вместе со столбцами
        for (int i = 0; i < task.sizeEq(); i++) {
            resultTarget.set(Integer.parseInt(task.getPosition().get(i).toString()) - 1, 0.0);
        }

        //добавляем новую посчитанную целевую функцию в исходную симплекс таблицу
        task.getMatrix().add(resultTarget);

        //домножаем нижний левый элемент на -1
        task.getMatrix().get(task.getMatrix().size() - 1).set(task.getMatrix().get(0).size() - 1, task.getMatrix().get(task.getMatrix().size() - 1).get(task.getMatrix().get(0).size() - 1) * (-1));
        //удаление из таблицы столбцов соотвествующих базисным переменным
        //task.getMatrix().siz1 - 1 т.к мы учитываем то что мы уже добавили новую строку в матрицу исходной задачи
        int sizeMatrixDelete = task.getMatrix().size() - 1;
        for (int i = 0; i < sizeMatrixDelete; i++) {
            //-(i+1) т.к нужно учитывать то что при удалении столбца уменьшится размер матрицы
            int removedIndex = Integer.parseInt(task.getPosition().get(i).toString()) - (i + 1);
            for (int j = 0; j < task.getMatrix().size(); j++) {
                task.getMatrix().get(j).remove(removedIndex);
            }

        }

        StepSimplexNumber firstStep = new StepSimplexNumber(task.getMatrix(), task.getPosition());
        return firstStep;
    }

    public static ArrayList supportElmTmpStep(StepSimplexNumber tmpStep) throws WrongNumException {
        ArrayList<Double> supportArray;
        supportArray = new ArrayList();
        ArrayList allSupportElm;
        allSupportElm = new ArrayList();
        for (int i = 0; i < tmpStep.getMatrix().get(0).size() - 1; i++) {
            if (tmpStep.getLastString().get(i) >= 0) {
                supportArray.add(-1.0);
                continue;
            }
            double min = Double.MAX_VALUE;
            for (int j = 0; j < tmpStep.getMatrix().size() - 1; j++) {
                //проверка для присваивания min нужного значения
                int count = 0;
                if (tmpStep.getMatrix().get(j).get(i) > 0) {
                    if (min > (tmpStep.getMatrix().get(j).get(tmpStep.getMatrix().get(0).size() - 1) / tmpStep.getMatrix().get(j).get(i))) {
                        min = tmpStep.getMatrix().get(j).get(tmpStep.getMatrix().get(0).size() - 1) / (tmpStep.getMatrix().get(j).get(i));
                    }
                }
            }
            supportArray.add(min);
        }

        for (int i = 0; i < tmpStep.getMatrix().get(0).size() - 1; i++) {
            for (int j = 0; j < tmpStep.getMatrix().size() - 1; j++) {
                String s = "";
                if (tmpStep.getMatrix().get(j).get(i) > 0) {
                    if (supportArray.get(i) == (((tmpStep.getMatrix().get(j).get(tmpStep.getMatrix().get(0).size() - 1)) / (tmpStep.getMatrix().get(j).get(i))))) {
                        s = "[" + j + "][" + i + "]=" + BigDecimal.valueOf(supportArray.get(i)).setScale(5, RoundingMode.HALF_UP);
                        // s = "[x" + (Integer.parseInt(tmpStep.getPosition().get(j).toString())) + "][x" + (Integer.parseInt(tmpStep.getPosition().get(i + tmpStep.getMatrix().size() - 1).toString())) + "]=" + supportArray.get(i);
                        allSupportElm.add(s);
                    }
                }
            }
        }

        return allSupportElm;
    }

    public static StepSimplexNumber nextStep(StepSimplexNumber tmpStep, int numRow, int numCol) throws WrongNumException {
        double divSupp = (1 / (tmpStep.getMatrix().get(numRow).get(numCol)));
        ArrayList<Double> supRow = new ArrayList<Double>();
        for (int j = 0; j < tmpStep.getLastString().size(); j++) {
            if (j == numCol) {
                supRow.add(divSupp);
            } else {
                supRow.add(tmpStep.getMatrix().get(numRow).get(j) * (divSupp));
            }
        }
        ArrayList<ArrayList<Double>> matrix = createVoidTable(tmpStep.getMatrix().size(), tmpStep.getLastString().size());
        matrix.set(numRow, supRow);
        for (int i = 0; i < tmpStep.getMatrix().size(); i++) {
            if (i == numRow) {
                continue;
            }
            ArrayList<Double> newRow = new ArrayList<Double>();
            for (int j = 0; j < tmpStep.getLastString().size(); j++) {
                if (j == numCol) {
                    newRow.add(divSupp * (-1.0) * (tmpStep.getMatrix().get(i).get(j)));
                } else {
                    newRow.add(BigDecimal.valueOf(tmpStep.getMatrix().get(i).get(j)).subtract(BigDecimal.valueOf(supRow.get(j) * tmpStep.getMatrix().get(i).get(numCol))).doubleValue());
                }
            }
            matrix.set(i, newRow);
        }
        matrix.get(numRow).set(numCol, divSupp);

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

        return new StepSimplexNumber(matrix, position);
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

    public static StepSimplexNumber nextStepArtificialNumber(StepSimplexNumber tmpStep, int numRow, int numCol) throws WrongNumException {
        double divSupp = 1.0 / (tmpStep.getMatrix().get(numRow).get(numCol));

        ArrayList<Double> supRow = new ArrayList<Double>();
        for (int j = 0; j < tmpStep.getLastString().size(); j++) {
            if (j == numCol) {
                supRow.add(divSupp);
            } else {
                supRow.add(tmpStep.getMatrix().get(numRow).get(j) * (divSupp));
            }
        }
        ArrayList<ArrayList<Double>> matrix = createVoidTable(tmpStep.getMatrix().size(), tmpStep.getLastString().size());
        matrix.set(numRow, supRow);
        for (int i = 0; i < tmpStep.getMatrix().size(); i++) {
            if (i == numRow) {
                continue;
            }
            ArrayList<Double> newRow = new ArrayList<Double>();
            for (int j = 0; j < tmpStep.getLastString().size(); j++) {
                if (j == numCol) {
                    newRow.add(divSupp * (-1) * (tmpStep.getMatrix().get(i).get(j)));
                } else {
                    newRow.add(BigDecimal.valueOf(tmpStep.getMatrix().get(i).get(j)).subtract(BigDecimal.valueOf(supRow.get(j) * tmpStep.getMatrix().get(i).get(numCol))).doubleValue());
                }
            }
            matrix.set(i, newRow);
        }
        matrix.get(numRow).set(numCol, divSupp);

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
            if (Integer.parseInt(position.get(i).toString()) > (InputInfo.stepBaseNumber.getStep(0).getLastString().size() - 1)) {
                position.remove(i);
                //удаление ненужного столбца(искуственной перемнной)
                for (int j = 0; j < matrix.size(); j++) {
                    matrix.get(j).remove(i - sizeEq);
                }

                break;
            }
        }
/////////////
        return new StepSimplexNumber(matrix, position);
    }

    public static StepSimplexNumber createStepAfterArtificialMethodNumber(StepSimplexNumber tmpStep, SourceTaskNumber task, ArrayList<Double> targetSource) throws WrongNumException {
        int sizeEq = tmpStep.getMatrix().size() - 1;
        ArrayList<ArrayList<Double>> matrixSolution = createVoidTable(task.sizeX() - 1, task.sizeX());
        for (int i = 0; i < task.sizeEq(); i++) {
            ArrayList<Double> row = createVoidRow(task.sizeX());
            for (int j = sizeEq; j < tmpStep.getPosition().size(); j++) {
                row.set(Integer.parseInt(tmpStep.getPosition().get(j).toString()) - 1, tmpStep.getMatrix().get(i).get(j - task.sizeEq() + 1) * (-1));
            }
            row.set(row.size() - 1, tmpStep.getMatrix().get(i).get(tmpStep.getLastString().size() - 1));
            matrixSolution.set((Integer.parseInt(tmpStep.getPosition().get(i).toString()) - 1), row);
        }
        ArrayList<ArrayList<Double>> matrix = createVoidTable(tmpStep.getMatrix().size(), tmpStep.getLastString().size());
        //заполняем матрицу значениями из матрицы предыдущего шага
        for (int i = 0; i < sizeEq; i++) {
            matrix.set(i, tmpStep.getMatrix().get(i));
        }

        //берём целевую функцию из исходной задачи
        ArrayList<Double> target = new ArrayList<Double>();
        for (int i = 0; i < targetSource.size(); i++) {
            target.add(targetSource.get(i));
        }
        target.add(0.0);
        ArrayList<Double> resultTarget = target;
        //подставяем наши замены в целевую функцию
        for (int i = 0; i < task.sizeX() - 1; i++) {
            for (int j = 0; j < resultTarget.size(); j++) {
                resultTarget.set(j, BigDecimal.valueOf(resultTarget.get(j)).add(BigDecimal.valueOf(matrixSolution.get(i).get(j) * target.get(i))).doubleValue());
            }
        }

        //Убираем лишние значения в целевой функции
        for (int i = 0; i < sizeEq; i++) {
            resultTarget.set(Integer.parseInt(tmpStep.getPosition().get(i).toString()) - 1, 0.0);
        }

        ArrayList<Double> lastRow = createVoidRow(matrix.get(0).size());

        //формируем последнюю строку таблицы
        for (int i = sizeEq; i < tmpStep.getPosition().size(); i++) {
            lastRow.set(i - (sizeEq), resultTarget.get(Integer.parseInt(tmpStep.getPosition().get(i).toString()) - 1));
        }
        lastRow.set(lastRow.size() - 1, resultTarget.get(resultTarget.size() - 1) * (-1));
        matrix.remove(matrix.size() - 1);
        matrix.add(lastRow);
        return new StepSimplexNumber(matrix, tmpStep.getPosition());
    }

    public static void autoMethodArtificialNumber() throws WrongNumException {
        while (!InputInfo.stepBaseNumber.getLastStep().endArtificial() || checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            InputInfo.stepBaseNumber.add(nextStepArtificialNumber(InputInfo.stepBaseNumber.getLastStep(), needSupport((String) supportElmTmpStep(InputInfo.stepBaseNumber.getLastStep()).get(0))[0], needSupport((String) supportElmTmpStep(InputInfo.stepBaseNumber.getLastStep()).get(0))[1]));
            if (InputInfo.stepBaseNumber.getLastStep().end() && (InputInfo.stepBaseNumber.getLastStep().getF0() != 0)) {
                break;
            }
        }
    }

    private static ArrayList<ArrayList<Double>> createVoidTable(int sizeEq, int sizeX) throws WrongNumException {
        ArrayList<ArrayList<Double>> a = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < sizeEq; i++) {
            ArrayList<Double> row = new ArrayList<Double>();
            for (int j = 0; j < sizeX; j++) {
                row.add(0.0);
            }
            a.add(row);
        }
        return a;
    }

    public static ArrayList<Double> createVoidRow(int size) throws WrongNumException {
        ArrayList<Double> row = new ArrayList<Double>();
        for (int j = 0; j < size; j++) {
            row.add(0.0);
        }
        return row;
    }

    public static void autoSimplexMethodNumber() throws WrongNumException {
        while (!InputInfo.stepBaseNumber.getLastStep().end() || checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            if (checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
                break;
            }
            InputInfo.stepBaseNumber.add(nextStep(InputInfo.stepBaseNumber.getLastStep(), needSupport((String) supportElmTmpStep(InputInfo.stepBaseNumber.getLastStep()).get(0))[0], needSupport((String) supportElmTmpStep(InputInfo.stepBaseNumber.getLastStep()).get(0))[1]));
        }
    }

    public static void roundingDoubleMatrix(ArrayList<ArrayList<Double>> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                matrix.get(i).set(j, BigDecimal.valueOf(matrix.get(i).get(j)).setScale(5, RoundingMode.HALF_UP).doubleValue());
            }
        }
    }

    public static void roundingDoubleRow(ArrayList<Double> row) {
        for (int i = 0; i < row.size(); i++) {
            row.set(i, BigDecimal.valueOf(row.get(i)).setScale(5, RoundingMode.HALF_UP).doubleValue());
        }
    }

}
