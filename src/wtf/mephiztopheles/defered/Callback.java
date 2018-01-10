/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wtf.mephiztopheles.defered;

/**
 *
 * @author marku
 */
public interface Callback<T> {

    void call(T argument);
}
