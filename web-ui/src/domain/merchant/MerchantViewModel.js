import CommonUserViewModel from "./CommonUserViewModel";

class MerchantViewModel extends CommonUserViewModel {
    constructor(id, name, description, email, status, totalTransactionSum) {
        super(id, name, description, email);  // call the parent constructor
        this.status = status;
        this.totalTransactionSum = totalTransactionSum;
    }
    toPlainObject() {
        return {
            ...super.toPlainObject(),
            status: this.status,
            totalTransactionSum: this.totalTransactionSum
        };
    }
}

export default MerchantViewModel;
