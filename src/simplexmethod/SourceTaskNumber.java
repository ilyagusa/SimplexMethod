/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author Ilya
 */
public class SourceTaskNumber {

    private ArrayList<Double> target;
    private ArrayList<ArrayList<Double>> matrixEq = new ArrayList<ArrayList<Double>>();
    private ArrayList<Double> basis;
    //идексы базисных и свободных переменных
    private ArrayList<Integer> position;
    private boolean artificial = false;
    private ArrayList<Double> targetMain;

    public SourceTaskNumber(ArrayList<Double> targetFunc, ArrayList<ArrayList<Double>> matrixEq) throws WrongNumException  {
        this.target = targetFunc;
        this.targetMain = new ArrayList<Double>();
        for (int t = 0; t < targetFunc.size(); t++) {
            targetMain.add(targetFunc.get(t));
        }
        this.matrixEq = matrixEq;
        for (int i = 0; i < matrixEq.size(); i++) {
            if (this.matrixEq.get(i).get(this.matrixEq.get(i).size() - 1) < 0) {
                MethodsForNumber.multByNumber(this.matrixEq.get(i), -1.0);
            }
        }
        goToZero();
    }

    private void goToZero() {
        for (int i = 0; i < this.matrixEq.size(); i++) {
            for (int j = 0; j < this.matrixEq.get(0).size(); j++) {
                if (Math.abs(this.matrixEq.get(i).get(j)) < 0.00000001) {
                    this.matrixEq.get(i).set(j, 0.0);
                }
            }
        }
    }

    public ArrayList<ArrayList<Double>> getMatrix() {
        return this.matrixEq;
    }

    public int sizeEq() {
        return matrixEq.size();
    }

    public int sizeX() {
        return target.size();
    }

    public int sizeTaskEq() {
        return matrixEq.size() + 1;
    }

    public int sizeTaskX() {
        return target.size() + 1;
    }

    public ArrayList<Double> getTarget() {
        return this.targetMain;
    }

    public void setX0(ArrayList<Double> basis) {
        this.basis = basis;
        position = new ArrayList();
        int i = 0;
        for (int j = 0; j < this.basis.size(); j++) {
            if (this.basis.get(j) >= 0) {
                position.add(i, (j + 1));
                i++;
            }
        }
        for (int j = 0; j < this.basis.size(); j++) {
            if (this.basis.get(j) < 0) {
                position.add(i, (j + 1));
                i++;
            }
        }
    }

    public ArrayList<Double> getBasis() {
        return this.basis;
    }

    public ArrayList getPosition() {
        return this.position;
    }

    public void createTargetArtificial() throws WrongNumException {
        ArrayList<Double> targetCopy = new ArrayList<Double>();

        for (int i = 0; i < this.sizeX() + 1; i++) {
            targetCopy.add(0.0);
        }

        for (int i = 0; i < this.sizeX() + 1; i++) {
            for (int j = 0; j < this.sizeEq(); j++) {
                //реализцаия сложения для точного вычисления
                BigDecimal tmp = BigDecimal.valueOf(targetCopy.get(i));
                BigDecimal next = BigDecimal.valueOf(this.matrixEq.get(j).get(i));
                targetCopy.set(i, (tmp.add(next).doubleValue()));
            }
            targetCopy.set(i, targetCopy.get(i) * (-1.0));
        }
        this.target = targetCopy;
    }

    private String toStringX0() {
        String str = "(";
        for (int i = 0; i < this.basis.size(); i++) {
            str += "x" + (i + 1) + "=" + basis.get(i) + " ";
        }
        return str + ")";
    }

    private String toStringPos() {
        String str = "[";
        for (int i = 0; i < this.basis.size(); i++) {
            str += this.position.get(i) + ",";
        }
        return str + "]";
    }

    public void setTrueArtificial() {
        this.artificial = true;
    }

    public void setFalseArtificial() {
        this.artificial = false;
    }

    public boolean getArtificial() {
        return this.artificial;
    }

    public ArrayList<ArrayList<Double>> createFullTable() {
        goToZero();
        ArrayList<ArrayList<Double>> matr = this.matrixEq;
        matr.add(this.target);
        MethodsForNumber.roundingDoubleMatrix(matr);
        return matr;
    }

    @Override
    public String toString() {
        return "\ntargerfunc: " + this.target + "\nsimplex table: " + this.matrixEq
                + "\n Опорная точка" + toStringX0() + "\n pos = " + toStringPos();
    }
}
