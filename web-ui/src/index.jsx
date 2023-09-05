import axios from "axios";
import { createRoot } from "react-dom/client";
import axiosMiddleware from "redux-axios-middleware";
import thunk from "redux-thunk";
import App from "./components/App";
import UserService from "./services/UserService";
import rootReducer from "./modules";
import { configureStore } from '@reduxjs/toolkit';
import { MICROSERVICE_MERCHANT, MICROSERVICE_TRANSACTION } from './AppConstants';
const MERCHANT_BASE_URL = `${import.meta.env.VITE_MERCHANT_URL}`;
const TRANSACTION_BASE_URL = `${import.meta.env.VITE_TRANSACTION_URL}/api/transaction`;

// HTTP
const _axios_merchant = axios.create({ baseURL: MERCHANT_BASE_URL });
const _axios_transaction = axios.create({ baseURL: TRANSACTION_BASE_URL });

const setupInterceptors = (axiosInstance) => {
  axiosInstance.interceptors.request.use((config) => {
    if (UserService.isLoggedIn()) {
      const cb = () => {
        config.headers.Authorization = `Bearer ${UserService.getToken()}`;
        return Promise.resolve(config);
      };
      return UserService.updateToken(cb);
    }
    return config;
  });
}

setupInterceptors(_axios_merchant);
setupInterceptors(_axios_transaction);

// Setup MiddleWare

const merchantMiddleware = store => next => action => {
  if (action.microservice === MICROSERVICE_MERCHANT) {
    return axiosMiddleware(_axios_merchant)(store)(next)(action);
  }
  return next(action);
};

const transactionMiddleware = store => next => action => {
  if (action.microservice === MICROSERVICE_TRANSACTION) {
    return axiosMiddleware(_axios_transaction)(store)(next)(action);
  }
  return next(action);
};

// REDUX store

const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(thunk, merchantMiddleware, transactionMiddleware)
});

// App

const renderApp = () => createRoot(document.getElementById("app")).render(<App store={store}/>);

UserService.initKeycloak(renderApp);
