import {Route, Routes} from "react-router-dom";
import AdminImport from "./AdminImport";
import MerchantList from "./MerchantList";
import Menu from "./Menu";
import NoMatch from "../common/NoMatch";
import MerchantDetails from "./MerchantDetails";
import MerchantEditForm from "./MerchantEditForm";

const Merchants = () => (
  <>
    <Menu/>
    <Routes>
      <Route path="/" element={<MerchantList/>}/>
      <Route path="/merchants/view/:merchantId" element={<MerchantDetails/>}/>
      <Route path="/merchants/edit/:merchantId" element={<MerchantEditForm/>}/>
      <Route path="/merchants/admin/import" element={<AdminImport/>}/>
      <Route path="*" element={<NoMatch/>}/>
    </Routes>
  </>
)

export default Merchants
