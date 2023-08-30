import { Route, Routes } from "react-router-dom";
import TransactionDetails from "./TransactionDetails";
import TransactionAuthorizeForm from "./TransactionAuthorizeForm";
import TransactionList from "./TransactionList";
import Menu from "./Menu";
import NoMatch from "../common/NoMatch";

const Transactions = () => (
  <>
    <Menu/>
    <Routes>
      <Route exact path="/" element={<TransactionList/>}/>
      <Route exact path="/transactions/new" element={<TransactionAuthorizeForm/>}/>
      <Route path="/transactions/:transactionId" element={<TransactionDetails/>}/>
      <Route path="*" element={<NoMatch/>}/>
    </Routes>
  </>
)

export default Transactions
