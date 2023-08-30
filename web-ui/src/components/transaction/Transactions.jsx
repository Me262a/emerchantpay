import { Route, Routes } from "react-router-dom";
import TransactionDetails from "./TransactionDetails";
import TransactionCreateForm from "./TransactionCreateForm";
import TransactionList from "./TransactionList";
import Menu from "./Menu";
import NoMatch from "../common/NoMatch";

const Transactions = () => (
  <>
    <Menu/>
    <Routes>
      <Route exact path="/" element={<TransactionList/>}/>
      <Route path="/transactions/view/:transactionId" element={<TransactionDetails/>}/>
      <Route exact path="/transactions/new" element={<TransactionCreateForm/>}/>
      <Route path="*" element={<NoMatch/>}/>
    </Routes>
  </>
)

export default Transactions
