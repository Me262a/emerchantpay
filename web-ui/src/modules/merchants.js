import { SUCCESS_SUFFIX } from "redux-axios-middleware";
import UserService from "../services/UserService";
import MerchantPageView from "../domain/merchant/MerchantPageModel";
import {MICROSERVICE_MERCHANT} from "../AppConstants";
import AdminViewModel from "../domain/merchant/AdminViewModel";

const LIST_MERCHANTS = 'LIST_MERCHANTS';
const GET_MERCHANT = 'GET_MERCHANT';
const UPDATE_MERCHANT = 'UPDATE_MERCHANT';
const IMPORT_MERCHANTS = 'IMPORT_MERCHANTS';
const IMPORT_ADMINS = 'IMPORT_ADMINS';
const DELETE_MERCHANT = 'DELETE_MERCHANT';
const DELETE_ALL_MERCHANTS = 'DELETE_ALL_MERCHANTS';

const initialState = {
  merchantPage: null,
  currentMerchantDetails: null,
  importedAdmins: []
};

const merchants = (state = initialState, action) => {
  switch (action.type) {
    case LIST_MERCHANTS + SUCCESS_SUFFIX:
      const data = action.payload.data;
      const merchantPage = new MerchantPageView(data.content, data.totalPages, data.number);
      return {
        ...state,
        merchantPage: merchantPage.toPlainObject()
      };

    case GET_MERCHANT + SUCCESS_SUFFIX:
      return {
        ...state,
        currentMerchantDetails: action.payload.data
      };

    case UPDATE_MERCHANT + SUCCESS_SUFFIX:
      return {
        ...state,
        currentMerchantDetails: action.payload.data
      };

    case IMPORT_MERCHANTS + SUCCESS_SUFFIX:
      return {
        ...state,
        ...action.payload.data.reduce((acc, merchant) => {
          acc[merchant.id] = merchant;
          return acc;
        }, {})
      };

    case IMPORT_ADMINS + SUCCESS_SUFFIX:
      return {
        ...state,
        importedAdmins: action.payload.data.map(admin => new AdminViewModel(admin.id, admin.name, admin.description, admin.email).toPlainObject())
      };

    case DELETE_MERCHANT + SUCCESS_SUFFIX:
      return {
        ...state,
        merchantPage: null // Invalidate the cached page
      };

    case DELETE_ALL_MERCHANTS + SUCCESS_SUFFIX:
      return {};

    default:
      return state;
  }
};
export default merchants;

export const getMerchant = (id) => {
  console.log(`${UserService.getUsername()} gets the merchant with id ${id}`);
  return {
    type: GET_MERCHANT,
    microservice: MICROSERVICE_MERCHANT,
    payload: {
      request: {
        url: `/merchant/${id}`,
        method: 'GET',
      },
    },
  }
};

export const getMerchantsPage = (page, size, sort) => ({
  type: LIST_MERCHANTS,
  microservice: MICROSERVICE_MERCHANT,
  payload: {
    request: {
      url: `/merchant?page=${page}&size=${size}&sort=${sort}`,
      method: 'GET',
    },
  },
});

export const updateMerchant = (id, data) => {
  console.log(`${UserService.getUsername()} updated the merchant with ID ${id}`);
  return {
    type: UPDATE_MERCHANT,
    microservice: MICROSERVICE_MERCHANT,
    payload: {
      request: {
        url: `/merchant/${id}`,
        method: 'PUT',
        data: data,
      },
    },
  }
};


export const importMerchants = () => {
  console.log(`${UserService.getUsername()} imported merchants`);
  return {
    type: IMPORT_MERCHANTS,
    microservice: MICROSERVICE_MERCHANT,
    payload: {
      request: {
        url: '/merchant/import',
        method: 'POST',
      },
    },
  }
};

export const importAdmins = () => {
  console.log(`${UserService.getUsername()} imported admins`);
  return {
    type: IMPORT_ADMINS,
    microservice: MICROSERVICE_MERCHANT,
    payload: {
      request: {
        url: '/import',
        method: 'POST',
      },
    },
  }
};

export const deleteMerchant = (id) => {
  console.log(`${UserService.getUsername()} deleted the merchant with ID ${id}`);
  return {
    type: DELETE_MERCHANT,
    microservice: MICROSERVICE_MERCHANT,
    payload: {
      request: {
        url: `/merchant/${id}`,
        method: 'DELETE',
      },
    },
  }
};

export const deleteAllMerchants = () => {
  console.log(`${UserService.getUsername()} deleted all merchants`);
  return {
    type: DELETE_ALL_MERCHANTS,
    microservice: MICROSERVICE_MERCHANT,
    payload: {
      request: {
        url: '/merchant',
        method: 'DELETE',
      },
    },
  }
};