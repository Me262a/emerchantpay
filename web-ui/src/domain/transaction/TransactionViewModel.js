class TransactionViewModel {
    constructor(uuid, belongsTo, status, transactionType, referenceId, customerEmail, customerPhone, amount) {
        this.uuid = uuid;
        this.belongsTo = belongsTo;
        this.status = status;
        this.transactionType = transactionType;
        this.referenceId = referenceId;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.amount = amount;
    }

    toPlainObject() {
        return {
            uuid: this.uuid,
            belongsTo: this.belongsTo,
            status: this.status,
            referenceId: this.referenceId,
            customerEmail: this.customerEmail,
            customerPhone: this.customerPhone,
            amount: this.amount
        };
    }
}

export default TransactionViewModel;
