import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate, useParams} from "react-router-dom";
import {getMerchant, updateMerchant} from '../../modules/merchants';

const MerchantEditForm = () => {
    const {merchantId} = useParams();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const merchant = useSelector(state => state.merchants.currentMerchantDetails);

    const [formData, setFormData] = useState({
        name: '',
        description: '',
        email: '',
        status: '',
    });

    useEffect(() => {
        if (merchant) {
            setFormData(merchant);
        } else {
            dispatch(getMerchant(merchantId));
        }
    }, [dispatch, merchant, merchantId]);

    const handleFormSubmit = (e) => {
        e.preventDefault();
        dispatch(updateMerchant(merchantId, formData))
            .then(() => navigate(`/merchants/view/${merchantId}`));
    };

    return (
        <div className="container mt-4">
            <div className="d-flex justify-content-end mb-4">
                <button onClick={() => navigate(`/merchants/view/${merchantId}`)} className="btn btn-secondary">
                    Back
                </button>
            </div>
            <form onSubmit={handleFormSubmit} className="mt-4">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-lg-4">
                            <div className="mb-3">
                                <label htmlFor="name" className="form-label">Name:</label>
                                <input
                                    type="text"
                                    id="name"
                                    className="form-control"
                                    value={formData.name}
                                    onChange={e => setFormData({...formData, name: e.target.value})}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="description" className="form-label">Description:</label>
                                <textarea
                                    id="description"
                                    className="form-control"
                                    value={formData.description}
                                    onChange={e => setFormData({...formData, description: e.target.value})}
                                    rows="3"
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="email" className="form-label">Email:</label>
                                <input
                                    type="email"
                                    id="email"
                                    className="form-control"
                                    value={formData.email}
                                    onChange={e => setFormData({...formData, email: e.target.value})}
                                />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="status" className="form-label">Status:</label>
                                <select
                                    id="status"
                                    className="form-select"
                                    value={formData.status}
                                    onChange={e => setFormData({...formData, status: e.target.value})}
                                >
                                    <option value="ACTIVE">ACTIVE</option>
                                    <option value="INACTIVE">INACTIVE</option>
                                </select>
                            </div>
                            <button type="submit" className="btn btn-primary">Save Changes</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    );
}

export default MerchantEditForm;
