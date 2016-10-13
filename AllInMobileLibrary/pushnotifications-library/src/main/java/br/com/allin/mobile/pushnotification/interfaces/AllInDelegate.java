package br.com.allin.mobile.pushnotification.interfaces;

/**
 * @author lucasbrsilva
 *
 * Interface that returns the push action text
 *
 */
public interface AllInDelegate {
    /**
     * Method call that calls the push action
     *
     * @param action Ação do push
     */
    void onAction(String action);
}
