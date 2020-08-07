/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

/**
 *
 * @author НИколай Соболев
 */
public class WrongNumException extends Exception {

    public String s = "Делитель не может быть нулём";

    @Override
    public String toString() {
        return s;
    }

}
