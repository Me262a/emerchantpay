import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { toast } from 'react-toastify';

const ErrorHandler = ({ children }) => {
    const serverErrors = useSelector(state => state.transactions.serverErrors);

    useEffect(() => {
        if (serverErrors) {
            toast.error(serverErrors.toString());
        }
    }, [serverErrors]);

    return children;
};

export default ErrorHandler;