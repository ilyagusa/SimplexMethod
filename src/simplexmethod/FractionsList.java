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
public class FractionsList {

    private ArrayList<Fractions> list;

    public FractionsList() {
        list = new ArrayList<>();
    }

    public void add(Fractions el) {
        list.add(el);
    }   

    public void remove(int index) {
        list.remove(index);
    }

    public void removeCol(int numCol) {
        for (int i = 0; i < this.list.size(); i++) {
            list.get(i).remove(numCol);
        }
    }

    public int size() {
        return list.size();
    }

    public Fractions getFractions(int index) {
        return list.get(index);
    }

    public void setFractions(int index, Fractions a) {
        list.set(index, a);
    }

    public Fraction getMax() throws WrongNumException {
        Fraction max = list.get(0).getFraction(0);
        Fraction min;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).size(); j++) {
                if (list.get(i).getFraction(j).equals(max) == 1) {
                    max = list.get(i).getFraction(j);
                }

            }
        }
        min = max;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).size(); j++) {
                if (list.get(i).getFraction(j).getNum() > 0) {
                    if (list.get(i).getFraction(j).equals(min) == -1) {
                        min = list.get(i).getFraction(j);
                    }
                }
            }
        }
        Fraction result = max.divide(min);
        return result;
    }

    @Override

    public String toString() {
        return list.toString() + "";
    }
}
