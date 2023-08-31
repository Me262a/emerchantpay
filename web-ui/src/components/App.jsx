import {Provider} from "react-redux";
import {BrowserRouter} from "react-router-dom";
import Welcome from "./common/Welcome";
import RenderOnAuthenticated from "./renders/RenderOnAuthenticated";
import RenderOnAnonymous from "./renders/RenderOnAnonymous";
import Merchants from "./merchant/Merchants";
import Transactions from "./transaction/Transactions";
import NoPermissions from "./renders/NoPermissions";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import ErrorHandler from "./ErrorHandler";

const App = ({store}) => (
    <Provider store={store}>
        <BrowserRouter>
            <ErrorHandler>
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
            </ErrorHandler>
            <ToastContainer position={toast.POSITION.BOTTOM_RIGHT}/>
        </BrowserRouter>
    </Provider>
);

export default App;
