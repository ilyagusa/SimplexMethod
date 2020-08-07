/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Ilya
 */
public class SourceTaskFraction {
    
    private Fractions target;
    private FractionsList matrixEq;
    private Fractions basis;
    //идексы базисных и свободных переменных
    private ArrayList<Integer> position;
    private boolean artificial = false;
    private Fractions targetMain;
    
    public SourceTaskFraction(Fractions targetFunc, FractionsList matrixEq) throws WrongNumException {
        this.target = targetFunc;
        this.targetMain = targetFunc;
        this.matrixEq = matrixEq;
        for (int i = 0; i < matrixEq.size(); i++) {
            if (this.matrixEq.getFractions(i).getFraction(matrixEq.getFractions(i).size() - 1).getNum() < 0) {  
                matrixEq.getFractions(i).multByFraction(new Fraction(-1, 1));
            }
        }
    }
    
    public FractionsList getMatrix() {
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
    
    public Fractions getTarget() {
        return this.targetMain;
    }
    
    public void setX0(Fractions basis) {
        this.basis = basis;
        position = new ArrayList();
        int i = 0;
        for (int j = 0; j < this.basis.size(); j++) {
            if (this.basis.getEl(j).getNum() >= 0) {
                position.add(i, (j + 1));
                i++;
            }
        }
        for (int j = 0; j < this.basis.size(); j++) {
            if (this.basis.getEl(j).getNum() < 0) {
                position.add(i, (j + 1));
                i++;
            }
        }
    }
    
    public Fractions getBasis() {
        return this.basis;
    }
    
    public ArrayList getPosition() {
        return this.position;
    }
    
    public void createTargetArtificial() throws WrongNumException {
        Fractions targetCopy = new Fractions();
        
        for (int i = 0; i < this.sizeX() + 1; i++) {
            targetCopy.add(new Fraction(0, 1));
        }
        
        for (int i = 0; i < this.sizeX() + 1; i++) {
            for (int j = 0; j < this.sizeEq(); j++) {
                targetCopy.setFraction(i, targetCopy.getFraction(i).add(this.matrixEq.getFractions(j).getFraction(i)));
            }
            targetCopy.setFraction(i, targetCopy.getFraction(i).multiply(new Fraction(-1, 1)));
        }
        this.target = targetCopy;
    }
    
    private String toStringX0() {
        String str = "(";
        for (int i = 0; i < this.basis.size(); i++) {
            str += "x" + (i + 1) + "=" + basis.getFraction(i) + " ";
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
    
    public FractionsList createFullTable() {
        FractionsList matr = this.matrixEq;
        matr.add(this.target);
        return matr;
    }
    
    @Override
    public String toString() {
        return "\ntargerfunc: " + this.target + "\nsimplex table: " + this.matrixEq + "\nОпорная точка" + toStringX0() + "\n pos = " + toStringPos();
    }
}
