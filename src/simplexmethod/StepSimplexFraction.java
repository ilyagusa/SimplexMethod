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
public class StepSimplexFraction {

    private FractionsList matrix;
    private ArrayList<Integer> position;

    public StepSimplexFraction(FractionsList matrix, ArrayList position) {
        this.matrix = matrix;
        this.position = position;
    }

    public FractionsList getMatrix() {
        return this.matrix;
    }

    public ArrayList getPosition() {
        return this.position;
    }

    public Fractions getLastString() {
        return this.matrix.getFractions(this.matrix.size() - 1);
    }

    public boolean end() {
        for (int i = 0; i < this.matrix.getFractions(0).size() - 1; i++) {
            if (this.matrix.getFractions(this.matrix.size() - 1).getFraction(i).getNum() < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean endArtificial() {
        for (int i = 0; i < this.matrix.getFractions(0).size() ; i++) {
            if (this.matrix.getFractions(this.matrix.size() - 1).getFraction(i).getNum() != 0) {
                return false;
            }
        }
        return true;
    }


    public Fraction getF0() throws WrongNumException {
        return this.matrix.getFractions(this.matrix.size() - 1).getFraction(this.matrix.getFractions(0).size() - 1).multiply(new Fraction(-1, 1));
    }

    public String basis() throws WrongNumException {
        Fractions basis = MethodsForFraction.createVoidRow(position.size());
        for (int i = 0; i < this.matrix.size() - 1; i++) {
            basis.setFraction(this.position.get(i) - 1, this.matrix.getFractions(i).getFraction(this.matrix.getFractions(0).size() - 1));
        }
        return "Найденный базис:" + basis.toString();
    }

}
