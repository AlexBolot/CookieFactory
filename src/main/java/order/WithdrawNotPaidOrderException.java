package order;

class WithdrawNotPaidOrderException extends RuntimeException {
     WithdrawNotPaidOrderException(String message) {
        super(message);
    }
}
