import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useParams} from "react-router";
import {Link} from "react-router-dom";
import {getMerchant} from '../../modules/merchants';
import {FaEdit} from "react-icons/fa";

const MerchantDetails = () => {
  const { merchantId } = useParams();
  const dispatch = useDispatch();
  const merchant = useSelector(state => state.merchants.currentMerchantDetails);

  useEffect(() => {
    dispatch(getMerchant(merchantId));
  }, [dispatch, merchantId]);

  return merchant ? (
      <div className="row">
        <div className="col-sm-12">
          <div className="d-flex justify-content-between align-items-center">
            <h1>Details for Merchant ID {merchant.id}</h1>
            <Link to={`/merchants/edit/${merchant.id}`} className="d-flex align-items-center">
              <FaEdit size={45} title="Edit Merchant" />
            </Link>
          </div>
          <hr/>
          <h3>Belongs To</h3>
          <p className="lead">{merchant.name}</p>
          <h3>Status</h3>
          <p className="lead">{merchant.description}</p>
          <h3>Reference ID</h3>
          <p className="lead">{merchant.email}</p>
          <h3>Merchant activity status</h3>
          <p className="lead">{merchant.status}</p>
          <h3>Transactionsum</h3>
          <p className="lead">{merchant.totalTransactionSum}</p>
          <hr/>
          <p>
            <Link to="/">&laquo; back to list</Link>
          </p>
        </div>
      </div>
  ) : null;
}

export default MerchantDetails;
