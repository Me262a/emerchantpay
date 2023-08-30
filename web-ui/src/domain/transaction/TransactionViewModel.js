class TransactionViewModel {
    constructor(uuid, belongsTo, status, referenceId, customerEmail, customerPhone, amount) {
        this.uuid = uuid;
        this.belongsTo = belongsTo;
        this.status = status;
        this.referenceId = referenceId;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.amount = amount;
    }
}

export default TransactionViewModel;
