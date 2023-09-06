import {combineReducers} from "redux";
import merchants from "./merchants";
import transactions from "./transactions";

export default combineReducers({
  merchants, transactions
});
