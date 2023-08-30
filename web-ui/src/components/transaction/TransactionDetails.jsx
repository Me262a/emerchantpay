import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router";
import { Link } from "react-router-dom";
import { getTransaction } from '../../modules/transactions';
import TransactionViewModel from "../../domain/transaction/TransactionViewModel";

const TransactionDetails = () => {
  const { transactionId } = useParams();
  const dispatch = useDispatch();
  const transactionData = useSelector(state => state.transactions.currentTransactionDetails); // Assuming you have a slice for the current transaction in your state
  const [transaction, setTransaction] = useState(undefined);

  useEffect(() => {
    dispatch(getTransaction(transactionId));
  }, [dispatch, transactionId]);

  useEffect(() => {
    if (transactionData) {
      const transactionObj = new TransactionViewModel(
          transactionData.uuid,
          transactionData.belongsTo,
          transactionData.status,
          transactionData.transactionType,
          transactionData.referenceId,
          transactionData.customerEmail,
          transactionData.customerPhone,
          transactionData.amount
      );
      setTransaction(transactionObj);
    }
  }, [transactionData]);

  return transaction ? (
      <div className="row">
        <div className="col-sm-12">
          <h3>Details for Transaction ID {transaction.uuid}</h3>
          <hr/>
          <h5>Belongs To</h5>
          <p className="lead">{transaction.belongsTo}</p>
          <h5>Status</h5>
          <p className="lead">{transaction.status}</p>
          <h5>Type</h5>
          <p className="lead">{transaction.transactionType}</p>
          <h5>Reference ID</h5>
          <p className="lead">{transaction.referenceId ? transaction.referenceId : '-'}</p>
          <h5>Customer Email</h5>
          <p className="lead">{transaction.customerEmail ? transaction.customerEmail : '-'}</p>
          <h5>Customer Phone</h5>
          <p className="lead">{transaction.customerPhone ? transaction.customerPhone : '-'}</p>
          <h5>Amount</h5>
          <p className="lead">{transaction.amount ?? '-'}</p>
          <hr/>
          <p>
            <Link to="/">&laquo; back to list</Link>
          </p>
        </div>
      </div>
  ) : null;
}

export default TransactionDetails;
