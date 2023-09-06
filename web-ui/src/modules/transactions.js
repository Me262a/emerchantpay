import {ERROR_SUFFIX, SUCCESS_SUFFIX} from "redux-axios-middleware";
import UserService from "../services/UserService";
import {MICROSERVICE_TRANSACTION} from "../AppConstants";

const GET_TRANSACTION = 'GET_TRANSACTION';
const LIST_TRANSACTIONS = 'LIST_TRANSACTIONS';
const ADD_TRANSACTION = 'ADD_TRANSACTION';

const initialState = {
  transactionPage: null,
  transactionList: null,
  currentTransactionDetails: null,
  serverErrors: null
};

const transactions = (state = initialState, action) => {
  if (action.type.endsWith(ERROR_SUFFIX)) {
    console.log(action.error.response.data);
    return {
      ...state,
      serverErrors: action.error.response.data
    };
  }

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
        url: `/ui/transactions/${uuid}`,
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
      url: `/ui/transactions?page=${page}&size=${size}&sort=${sort}`,
      method: 'GET',
    },
  },
});

export const addTransaction = (transaction) => {
  console.log(`${UserService.getUsername()} added the authorize transaction for customer ${transaction.email}`);
  return {
    type: ADD_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      request: {
        url: '/ui/transactions',
        method: 'POST',
        data: transaction,
      },
    },
  }
};

