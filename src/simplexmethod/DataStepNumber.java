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
public class DataStepNumber {
    
    private ArrayList<StepSimplexNumber> stepsSimplex;
    
    public DataStepNumber() {
        stepsSimplex = new ArrayList<>();
    }

    public void add(StepSimplexNumber a) {
        this.stepsSimplex.add(a);
    }

    public StepSimplexNumber getStep(int index) {
        return this.stepsSimplex.get(index);
    }

    public void removeLast() {
        this.stepsSimplex.remove(stepsSimplex.size() - 1);
    }

    public int size() {
        return this.stepsSimplex.size();
    }

    public StepSimplexNumber getLastStep() {
        return this.stepsSimplex.get(size() - 1);
    }
}
