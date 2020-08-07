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
public class Fractions {

    ArrayList<Fraction> data;

    public Fractions() {
        data = new ArrayList<>();
    }

    public Fraction getEl(int i) {
        return data.get(i);
    }

    public void add(Fraction el) {
        data.add(el);
    }

    public void sum(Fractions a, Fraction elm) throws WrongNumException {
        for (int i = 0; i < this.data.size(); i++) {
            this.data.set(i, data.get(i).add((a.getEl(i)).multiply(elm)));
        }
    }

    public void multByFraction(Fraction elm) throws WrongNumException {
        for (int i = 0; i < this.data.size(); i++) {
            this.data.set(i, data.get(i).multiply(elm));
        }
    }

    public void divideByFraction(Fraction elm) throws WrongNumException {
        for (int i = 0; i < this.data.size(); i++) {
            this.data.set(i, data.get(i).divide(elm));
        }
    }

    public void remove(int index) {
        data.remove(index);
    }

    public int size() {
        return data.size();
    }

    public Fraction getFraction(int index) {
        return data.get(index);
    }

    public void setFraction(int index, Fraction a) {
        this.data.set(index, a);
    }

    @Override

    public String toString() {
        return data.toString() + "";
    }

}
