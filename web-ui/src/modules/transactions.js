import { SUCCESS_SUFFIX, ERROR_SUFFIX  } from "redux-axios-middleware";
import UserService from "../services/UserService";
import {MICROSERVICE_TRANSACTION} from "../AppConstants";

const GET_TRANSACTION = 'GET_TRANSACTION';
const LIST_TRANSACTIONS = 'LIST_TRANSACTIONS';
const ADD_TRANSACTION = 'ADD_TRANSACTION';

const initialState = {
  transactionList: null,
  currentTransactionDetails: null,
  transactionPage: null,
  serverErrors: null
};

const transactions = (state = initialState, action) => {
  switch (action.type) {
    case LIST_TRANSACTIONS + SUCCESS_SUFFIX:
      const data = action.payload.data;
      const transactionPage = {
        content: data.content,
        totalPages: data.totalPages,
        currentPage: data.number,
      };
      return {
        ...state,
        transactionPage: transactionPage,
        transactionList: data.content
      };

    case GET_TRANSACTION + SUCCESS_SUFFIX:
      return {
        ...state,
        currentTransactionDetails: action.payload.data
      };

    case ADD_TRANSACTION + SUCCESS_SUFFIX:
      return {
        ...state,
        currentTransactionDetails: action.payload.data
      };

    case ADD_TRANSACTION + ERROR_SUFFIX:
      return {
        ...state,
        serverErrors: action.payload.response.data
      };

    default:
      return state;
  }
};

export default transactions;

export const getTransaction = (uuid) => {
  console.log(`${UserService.getUsername()} gets the transaction ${uuid}`);
  return {
    type: GET_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      request: {
        url: `/single/${uuid}`,
        method: 'GET',
      },
    },
  }
};

export const getTransactionsPage = (page, size, sort) => ({
  type: LIST_TRANSACTIONS,
  microservice: MICROSERVICE_TRANSACTION,
  payload: {
    request: {
      url: `all?page=${page}&size=${size}&sort=${sort}`,
      method: 'GET',
    },
  },
});

export const addAuthorizeTransaction = (transaction) => {
  console.log(`${UserService.getUsername()} added the authorize transaction for customer ${transaction.email}`);
  return {
    type: ADD_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      request: {
        url: '/authorize',
        method: 'POST',
        data: transaction,
      },
    },
  }
};

export const addChargeTransaction = (transaction) => {
  console.log(`${UserService.getUsername()} added the charge transaction refId=${transaction.referenceId}`);
  return {
    type: ADD_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      request: {
        url: '/charge',
        method: 'POST',
        data: transaction,
      },
    },
  }
};

export const addRefundTransaction = (transaction) => {
  console.log(`${UserService.getUsername()} added the refund transaction refId=${transaction.referenceId}`);
  return {
    type: ADD_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      request: {
        url: '/refund',
        method: 'POST',
        data: transaction,
      },
    },
  }
};

export const addReversalTransaction = (transaction) => {
  console.log(`${UserService.getUsername()} added the reversal transaction refId=${transaction.referenceId}`);
  return {
    type: ADD_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      request: {
        url: '/reversal',
        method: 'POST',
        data: transaction,
      },
    },
  }
};
