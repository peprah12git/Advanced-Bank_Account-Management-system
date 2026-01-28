package transactions;

public interface Transactable {

    /**
     *
     * @param amount
     * @param type
     * @return
     */
    boolean processTransaction(double amount, String type);
}
