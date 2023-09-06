import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {Link} from "react-router-dom";
import {getTransactionsPage} from "../../modules/transactions";

const TransactionList = () => {
  const dispatch = useDispatch();
  const transactionPage = useSelector(state => state.transactions.transactionPage); // Assuming the transaction slice of your state is named 'transactions'
  const transactions = transactionPage ? transactionPage.content : [];
  const totalPages = transactionPage ? transactionPage.totalPages : 0;

  // Pagination state
  const [page, setPage] = useState(0);
  const size = 10;
  const sort = 'uuid';

  useEffect(() => {
    dispatch(getTransactionsPage(page, size, sort));
  }, [dispatch, page]);

  return (
      <div className="row mt-3">
        <div className="col-sm-12">
          <h1 className="mb-3">All Transactions</h1>
          <table className="table table-bordered table-striped table-hover">
            <thead className="bg-primary text-white">
            <tr>
              <th>UUID</th>
              <th>Belongs To</th>
              <th>Status</th>
              <th>Type</th>
              <th>Reference ID</th>
              <th>Customer Email</th>
              <th>Customer Phone</th>
              <th>Amount</th>
            </tr>
            </thead>
            <tbody>
            {transactions.map((transaction) => (
                <tr key={transaction.uuid}>
                  <td>
                    <Link to={`/transactions/view/${transaction.uuid}`}>{transaction.uuid}</Link>
                  </td>
                  <td>{transaction.belongsTo}</td>
                  <td>{transaction.status}</td>
                  <td>{transaction.transactionType}</td>
                  <td>{transaction.referenceId}</td>
                  <td>{transaction.customerEmail}</td>
                  <td>{transaction.customerPhone}</td>
                  <td>{transaction.amount}</td>
                </tr>
            ))}
            </tbody>
          </table>
          <div className="d-flex justify-content-between">
            <button className="btn btn-outline-primary" onClick={() => setPage(prev => Math.max(prev - 1, 0))}>
              Previous
            </button>
            <span className="align-self-center">Page: {page + 1}</span>
            <button
                className={`btn btn-outline-primary ${page >= totalPages - 1 ? 'invisible' : ''}`}
                onClick={() => setPage(prev => prev + 1)}
            >
              Next
            </button>
          </div>
        </div>
      </div>
  );
}

export default TransactionList;
