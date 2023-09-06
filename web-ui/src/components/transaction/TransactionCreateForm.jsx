import React from 'react';
import {useFormik} from "formik";
import * as Yup from "yup";
import {useDispatch} from "react-redux";
import {useNavigate} from "react-router";
import {addTransaction,} from "../../modules/transactions";
import {TransactionType} from "../../AppConstants";

const InputField = ({ formik, id, type, name, label, placeholder }) => (
    <div className="mb-3">
        <label htmlFor={id} className="form-label">{label}</label>
        <input
            type={type}
            className="form-control"
            id={id}
            name={name}
            placeholder={placeholder}
            value={formik.values[name]}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
        />
        {formik.touched[name] && formik.errors[name] ? (
            <div className="text-danger">{formik.errors[name]}</div>
        ) : null}
    </div>
);

const AuthorizeFields = ({ formik }) => (
    <>
        <InputField formik={formik} id="customerEmail" type="email" name="customerEmail"
                    label="Customer Email" placeholder="Customer Email" />
        <InputField formik={formik} id="customerPhone" type="tel" name="customerPhone"
                    label="Customer Phone" placeholder="Customer Phone" />
        <InputField formik={formik} id="amount" type="number" name="amount"
                    label="Amount" placeholder="Amount" />
    </>
);

const ChargeFields = ({ formik }) => (
    <>
        <InputField formik={formik} id="referenceId" type="text" name="referenceId"
                    label="Reference UUID" placeholder="UUID of Authorise transaction" />
        <InputField formik={formik} id="amount" type="number" name="amount"
                    label="Charge Amount" placeholder="Refund Amount" />
    </>
);

const RefundFields = ({ formik }) => (
    <>
        <InputField formik={formik} id="referenceId" type="text" name="referenceId"
                    label="Reference UUID" placeholder="UUID of Charge transaction" />
        <InputField formik={formik} id="amount" type="number" name="amount"
                    label="Refund Amount" placeholder="Refund Amount" />
    </>
);

const ReversalFields = ({ formik }) => (
    <InputField formik={formik} id="referenceId" type="text" name="referenceId"
                label="Reference UUID" placeholder="UUID of Authorise transaction" />
);


const TransactionCreateForm = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

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
                    action = addTransaction({ transactionType: TransactionType.AUTHORIZE, ...values  });
                    break;
                case 'Charge':
                    action = addTransaction({ transactionType: TransactionType.CHARGE, ...values });
                    break;
                case 'Refund':
                    action = addTransaction({ transactionType: TransactionType.REFUND,  ...values  });
                    break;
                case 'Reversal':
                    action = addTransaction({ transactionType: TransactionType.REVERSAL,  ...values  });
                    break;
                default:
                    break;
            }

            if (action) {
                dispatch(action).then((response) => {
                    if (response?.payload?.data) {
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
