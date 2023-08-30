import { SUCCESS_SUFFIX } from "redux-axios-middleware";
import UserService from "../services/UserService";
import {MICROSERVICE_TRANSACTION} from "../AppConstants";

const GET_TRANSACTION = 'GET_TRANSACTION';
const LIST_TRANSACTIONS = 'LIST_TRANSACTIONS';
const ADD_TRANSACTION = 'ADD_TRANSACTION';

const transactions = (state = [], action) => {
  switch (action.type) {
    case LIST_TRANSACTIONS + SUCCESS_SUFFIX:
      return action.payload.data;

    case ADD_TRANSACTION:
      return state.filter((transaction) => transaction.id !== action.payload.transaction.id);

    default:
      return state;
  }
};

export default transactions;

export const getTransaction = id => {
  console.log(`${UserService.getUsername()} gets the transaction ${id}`);
  return {
    type: GET_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      id,
      request: {
        url: `/demo/transactions/${id}`,
        method: 'GET',
      },
    },
  }
};

export const allTransactions = () => ({
  type: LIST_TRANSACTIONS,
  microservice: MICROSERVICE_TRANSACTION,
  payload: {
    request: {
      url: '/demo/transactions',
    },
  },
});

export const addAuthorizeTransaction = transaction => {
  console.log(`${UserService.getUsername()} added the authorize transaction ${transaction.title}`);
  return {
    type: ADD_TRANSACTION,
    microservice: MICROSERVICE_TRANSACTION,
    payload: {
      request: {
        url: '/demo/transactions',
        method: 'POST',
        data: transaction,
      },
    },
  }
};
