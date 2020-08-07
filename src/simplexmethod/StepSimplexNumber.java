/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.util.ArrayList;

/**
 *
 * @author Ilya
 */
public class StepSimplexNumber {

    private ArrayList<ArrayList<Double>> matrix;
    private ArrayList<Integer> position;

    public StepSimplexNumber(ArrayList<ArrayList<Double>> matrix, ArrayList position) {
        this.matrix = matrix;
        this.position = position;
        goToZero();
        MethodsForNumber.roundingDoubleMatrix(this.matrix);
    }

    private void goToZero() {
        for (int i = 0; i < this.matrix.size(); i++) {
            for (int j = 0; j < this.matrix.get(0).size(); j++) {
                if (Math.abs(this.matrix.get(i).get(j)) < 0.00000001) {
                    this.matrix.get(i).set(j, 0.0);
                }
            }
        }
    }

    public ArrayList<ArrayList<Double>> getMatrix() {
        return this.matrix;
    }

    public ArrayList getPosition() {
        return this.position;
    }

    public ArrayList<Double> getLastString() {
        return this.matrix.get(this.matrix.size() - 1);
    }

    public boolean end() {
        for (int i = 0; i < this.matrix.get(0).size() - 1; i++) {
            if (this.matrix.get(this.matrix.size() - 1).get(i) < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean endArtificial() {
        for (int i = 0; i < this.matrix.get(0).size(); i++) {
            if (this.matrix.get(this.matrix.size() - 1).get(i) != 0) {
                return false;
            }
        }
        return true;
    }

    public double getF0() throws WrongNumException {
        return (this.matrix.get(this.matrix.size() - 1).get(this.matrix.get(0).size() - 1) * (-1));
    }

    public String basis() throws WrongNumException {
        ArrayList<Double> basis = MethodsForNumber.createVoidRow(position.size());
        for (int i = 0; i < this.matrix.size() - 1; i++) {
            basis.set(this.position.get(i) - 1, this.matrix.get(i).get(this.matrix.get(0).size() - 1));
        }
        return "Найденный базис:" + basis.toString();
    }

}
