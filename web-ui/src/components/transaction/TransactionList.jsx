import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { createSelector } from "reselect";
import { allTransactions } from "../../modules/transactions";

const TransactionList = () => {

  const dispatch = useDispatch();
  const transactions = useSelector(createSelector((s) => s.transactions, (transactions) => transactions));

  useEffect(() => {
    dispatch(allTransactions());
  }, [dispatch]);

  return (
      <div className="row">
        <div className="col-sm-12">
          <h1>All Transactions</h1>
          <table className="table table-striped align-middle">
            <thead>
            <tr>
              <th>ID</th>
              <th>Belongs To</th>
              <th>Status</th>
              <th>Reference ID</th>
              <th>Customer Email</th>
              <th>Customer Phone</th>
              <th>Amount</th>
              <th>Action</th>
            </tr>
            </thead>
            <tbody>
            {transactions.map((transaction) => (
                <tr key={transaction.uuid}>
                  <td>
                    <Link to={`/transactions/${transaction.uuid}`}>{transaction.uuid}</Link>
                  </td>
                  <td>{transaction.belongsTo}</td>
                  <td>{transaction.status}</td>
                  <td>{transaction.referenceId}</td>
                  <td>{transaction.customerEmail}</td>
                  <td>{transaction.customerPhone}</td>
                  <td>{transaction.amount}</td>
                </tr>
            ))}
            </tbody>
          </table>
        </div>
      </div>
  );
}

export default TransactionList;
