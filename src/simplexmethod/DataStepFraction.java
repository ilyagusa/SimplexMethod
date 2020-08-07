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
public class DataStepFraction {

    private ArrayList<StepSimplexFraction> stepsSimplex;
    
    public DataStepFraction() {
        stepsSimplex = new ArrayList<>();
    }

    public void add(StepSimplexFraction a) {
        this.stepsSimplex.add(a);
    }

    public StepSimplexFraction getStep(int index) {
        return this.stepsSimplex.get(index);
    }

    public void removeLast() {
        this.stepsSimplex.remove(stepsSimplex.size() - 1);
    }

    public int size() {
        return this.stepsSimplex.size();
    }

    public StepSimplexFraction getLastStep() {
        return this.stepsSimplex.get(size() - 1);
    }

}
