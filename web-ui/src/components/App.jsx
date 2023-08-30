import {Provider} from "react-redux";
import {BrowserRouter} from "react-router-dom";
import Welcome from "./common/Welcome";
import RenderOnAuthenticated from "./renders/RenderOnAuthenticated";
import RenderOnAnonymous from "./renders/RenderOnAnonymous";
import Merchants from "./merchant/Merchants";
import Transactions from "./transaction/Transactions";
import NoPermissions from "./renders/NoPermissions";

const App = ({store}) => (
  <Provider store={store}>
    <BrowserRouter>
      <div className="container">
        <RenderOnAnonymous>
          <Welcome/>
        </RenderOnAnonymous>
        <RenderOnAuthenticated roles={['admin']}>
          <Merchants/>
        </RenderOnAuthenticated>
        <RenderOnAuthenticated roles={['merchant']}>
          <Transactions/>
        </RenderOnAuthenticated>
        <NoPermissions/>
      </div>
    </BrowserRouter>
  </Provider>
);

export default App;
