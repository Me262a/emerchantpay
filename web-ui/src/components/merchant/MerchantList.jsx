import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import {getMerchantsPage, deleteMerchant, importMerchants, deleteAllMerchants} from "../../modules/merchants";

const MerchantList = () => {
  const dispatch = useDispatch();
  const merchantPage = useSelector(state => state.merchants.merchantPage);
  const merchants = merchantPage ? merchantPage.content : [];
  const totalPages = merchantPage ? merchantPage.totalPages : 0;

  // Pagination state
  const [page, setPage] = useState(0);
  const size = 10;
  const sort = 'id';

  useEffect(() => {
    dispatch(getMerchantsPage(page, size, sort));
  }, [dispatch, page]);

  const handleImport = () => {
    dispatch(importMerchants()).then(() => {
      // After merchants are imported, refresh the list
      dispatch(getMerchantsPage(page, size, sort));
    });
  };

  const handleDelete = (id) => {
    dispatch(deleteMerchant(id));
  };

  const handleDeleteAll = () => {
    dispatch(deleteAllMerchants());
  };

  useEffect(() => {
    if (!merchantPage) {
      dispatch(getMerchantsPage(page, size, sort));
    } else if (!merchants.length && page > 0) {
      // If no merchants on this page(after delete?) and not the first page, navigate to previous page
      setPage(prev => prev - 1);
    }
  }, [dispatch, merchantPage, page, size, sort]);

  return (
      <div className="row mt-3">
        <div className="col-sm-12">
          <div className="d-flex justify-content-between align-items-center mb-3">
            <h1 className="mb-0">All Merchants</h1>
            {
              merchants.length ?
                  <button className="btn btn-danger" onClick={handleDeleteAll}>
                    Delete All Merchants
                  </button> :
                  <button className="btn btn-primary" onClick={handleImport}>
                    Import Merchants
                  </button>
            }
          </div>
          <table className="table table-bordered table-striped table-hover">
            <thead className="bg-primary text-white">
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Description</th>
              <th>Email</th>
              <th>Status</th>
              <th>TotalSum</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            {merchants.map((merchant) => (
                <tr key={merchant.id}>
                  <td>
                    <Link to={`/merchants/view/${merchant.id}`}>{merchant.id}</Link>
                  </td>
                  <td>{merchant.name}</td>
                  <td>{merchant.description}</td>
                  <td>{merchant.email}</td>
                  <td>{merchant.status}</td>
                  <td>{merchant.totalTransactionSum}</td>
                  <td>
                    <button className="btn btn-sm btn-danger" onClick={() => handleDelete(merchant.id)}>
                      Delete
                    </button>
                  </td>
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

export default MerchantList;
