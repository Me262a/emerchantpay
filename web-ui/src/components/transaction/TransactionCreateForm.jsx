import React, {useEffect} from 'react';
import {useFormik} from "formik";
import * as Yup from "yup";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router";
import {
    addAuthorizeTransaction,
    addChargeTransaction,
    addRefundTransaction,
    addReversalTransaction
} from "../../modules/transactions";
import {toast} from "react-toastify";

const AuthorizeFields = ({formik}) => (
    <>
        <div className="mb-3">
            <label htmlFor="customerEmail" className="form-label">Customer Email</label>
            <input
                type="email"
                className="form-control"
                id="customerEmail"
                name="customerEmail"
                placeholder="Customer Email"
                value={formik.values.customerEmail}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
            />
            {formik.touched.customerEmail && formik.errors.customerEmail ? (
                <div className="text-danger">{formik.errors.customerEmail}</div>
            ) : null}
        </div>
        <div className="mb-3">
            <label htmlFor="customerPhone" className="form-label">Customer Phone</label>
            <input
                type="tel"
                className="form-control"
                id="customerPhone"
                name="customerPhone"
                placeholder="Customer Phone"
                value={formik.values.customerPhone}
                onChange={formik.handleChange}
            />
        </div>
        <div className="mb-3">
            <label htmlFor="amount" className="form-label">Amount</label>
            <input
                type="number"
                className="form-control"
                id="amount"
                name="amount"
                placeholder="Amount"
                value={formik.values.amount}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
            />
            {formik.touched.amount && formik.errors.amount ? (
                <div className="text-danger">{formik.errors.amount}</div>
            ) : null}
        </div>
    </>
);

const ChargeFields = ({formik}) => (
    <>
        <div className="mb-3">
            <label htmlFor="referenceId" className="form-label">Reference ID for Reversal</label>
            <input
                type="text"
                className="form-control"
                id="referenceId"
                name="referenceId"
                placeholder="Reference ID"
                value={formik.values.referenceId}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
            />
            {formik.touched.referenceId && formik.errors.referenceId ? (
                <div className="text-danger">{formik.errors.referenceId}</div>
            ) : null}
        </div>
        <div className="mb-3">
            <label htmlFor="amount" className="form-label">Charge Amount</label>
            <input
                type="number"
                className="form-control"
                id="amount"
                name="amount"
                placeholder="Refund Amount"
                value={formik.values.amount}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
            />
            {formik.touched.amount && formik.errors.amount ? (
                <div className="text-danger">{formik.errors.amount}</div>
            ) : null}
        </div>
    </>
);

const RefundFields = ({formik}) => (
    <>
        <div className="mb-3">
            <label htmlFor="referenceId" className="form-label">Reference ID for Reversal</label>
            <input
                type="text"
                className="form-control"
                id="referenceId"
                name="referenceId"
                placeholder="Reference ID"
                value={formik.values.referenceId}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
            />
            {formik.touched.referenceId && formik.errors.referenceId ? (
                <div className="text-danger">{formik.errors.referenceId}</div>
            ) : null}
        </div>
        <div className="mb-3">
            <label htmlFor="amount" className="form-label">Refund Amount</label>
            <input
                type="number"
                className="form-control"
                id="amount"
                name="amount"
                placeholder="Refund Amount"
                value={formik.values.amount}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
            />
            {formik.touched.amount && formik.errors.amount ? (
                <div className="text-danger">{formik.errors.amount}</div>
            ) : null}
        </div>
    </>

);

const ReversalFields = ({formik}) => (
    <div className="mb-3">
        <label htmlFor="referenceId" className="form-label">Reference ID for Reversal</label>
        <input
            type="text"
            className="form-control"
            id="referenceId"
            name="referenceId"
            placeholder="Reference ID"
            value={formik.values.referenceId}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
        />
        {formik.touched.referenceId && formik.errors.referenceId ? (
            <div className="text-danger">{formik.errors.referenceId}</div>
        ) : null}
    </div>
);

const TransactionCreateForm = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    // const serverErrors = useSelector(state => state.transactions.serverErrors);
    //
    // useEffect(() => {
    //     serverErrors.forEach(error => {
    //         toast.error(error);
    //     });
    // }, [serverErrors]);

    const getValidationSchema = (activeTab) => {
        switch (activeTab) {
            case 'Authorize':
                return Yup.object().shape({
                    activeTab: Yup.string().required(),
                    customerEmail: Yup.string().email('Invalid email format').required('Email is required'),
                    amount: Yup.number().positive('Amount must be positive').required('Amount is required')
                });
            case 'Charge':
            case 'Refund':
                return Yup.object().shape({
                    activeTab: Yup.string().required(),
                    referenceId: Yup.string().uuid('Invalid UUID format').required('Reference ID is required'),
                    amount: Yup.number().positive('Amount must be positive').required('Amount is required')
                });
            case 'Reversal':
                return Yup.object().shape({
                    activeTab: Yup.string().required(),
                    referenceId: Yup.string().uuid('Invalid UUID format').required('Reference ID is required')
                });
            default:
                return Yup.object().shape({});
        }
    };

    const formik = useFormik({
        initialValues: {
            activeTab: 'Authorize',
            referenceId: '',
            amount: '',
            customerEmail: '',
            customerPhone: ''
        },
        validate: (values) => {
            let schema = getValidationSchema(values.activeTab);
            try {
                schema.validateSync(values, {abortEarly: false});
                return {};
            } catch (error) {
                return error.inner.reduce((errors, err) => {
                    errors[err.path] = err.message;
                    return errors;
                }, {});
            }
        },
        onSubmit: values => {
            let action;
            switch (values.activeTab) {
                case 'Authorize':
                    action = addAuthorizeTransaction(values);
                    break;
                case 'Charge':
                    action = addChargeTransaction(values);
                    break;
                case 'Refund':
                    action = addRefundTransaction(values);
                    break;
                case 'Reversal':
                    action = addReversalTransaction(values);
                    break;
                default:
                    break;
            }

            if (action) {
                dispatch(action).then((response) => {
                    if (response && response.payload.data) {
                        navigate(`/transactions/view/${response.payload.data.uuid}`);
                    }
                });
            }
        },
    });

    return (
        <div className="row">
            <div className="col-sm-12">
                <ul className="nav nav-tabs">
                    {['Authorize', 'Charge', 'Refund', 'Reversal'].map((tab) => (
                        <li className="nav-item" key={tab}>
                            <button
                                className={`nav-link ${formik.values.activeTab === tab ? 'active' : ''}`}
                                onClick={() => formik.setFieldValue("activeTab", tab)}
                            >
                                {tab}
                            </button>
                        </li>
                    ))}
                </ul>
            </div>
            <div className="col-sm-12 mt-2">
                <h3>Add a new <span style={{color: 'darkblue'}}>{formik.values.activeTab}</span> transaction:</h3>
            </div>
            <div className="col-sm-6">
                <form onSubmit={formik.handleSubmit}>
                    {formik.values.activeTab === 'Authorize' && <AuthorizeFields formik={formik}/>}
                    {formik.values.activeTab === 'Charge' && <ChargeFields formik={formik}/>}
                    {formik.values.activeTab === 'Refund' && <RefundFields formik={formik}/>}
                    {formik.values.activeTab === 'Reversal' && <ReversalFields formik={formik}/>}
                    <button type="submit" className="btn btn-primary">Add {formik.values.activeTab} Transaction</button>
                </form>
            </div>
        </div>
    );
}

export default TransactionCreateForm;
