import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";
import { Link } from "react-router-dom";
import { createSelector } from "reselect";
import { allTransactions } from '../../modules/transactions';
import TransactionViewModel from "../../domain/transaction/TransactionViewModel";

const TransactionDetails = () => {
  const { transactionId } = useParams();
  const dispatch = useDispatch();
  const transactions = useSelector(createSelector((s) => s.transactions, (transactions) => transactions));
  const [transaction, setTransaction] = useState(undefined);

  useEffect(() => {
    dispatch(allTransactions());
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  useEffect(() => {
    const transactionData = transactions.find(transaction => transaction.uuid === transactionId);
    if (transactionData) {
      const transactionObj = new TransactionViewModel(
          transactionData.uuid,
          transactionData.belongsTo,
          transactionData.status,
          transactionData.referenceId,
          transactionData.customerEmail,
          transactionData.customerPhone,
          transactionData.amount
      );
      setTransaction(transactionObj);
    }
  }, [transactionId, transactions]);

  return transaction ? (
      <div className="row">
        <div className="col-sm-12">
          <h1>Details for Transaction ID {transaction.uuid}</h1>
          <hr/>
          <h3>Belongs To</h3>
          <p className="lead">{transaction.belongsTo}</p>
          <h3>Status</h3>
          <p className="lead">{transaction.status}</p>
          <h3>Reference ID</h3>
          <p className="lead">{transaction.referenceId}</p>
          <h3>Customer Email</h3>
          <p className="lead">{transaction.customerEmail}</p>
          <h3>Customer Phone</h3>
          <p className="lead">{transaction.customerPhone}</p>
          <h3>Amount</h3>
          <p className="lead">{transaction.amount}</p>
          <hr/>
          <p>
            <Link to="/">&laquo; back to list</Link>
          </p>
        </div>
      </div>
  ) : null;
}

export default TransactionDetails;
