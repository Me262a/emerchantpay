import {useDispatch, useSelector} from "react-redux";
import {importAdmins} from "../../modules/merchants";

const AdminImport = () => {
    const dispatch = useDispatch();
    const importedAdmins = useSelector(state => state.merchants.importedAdmins);

    const handleImport = () => {
        dispatch(importAdmins());
    };

    return (
        <div className="row">
            <div className="col-sm-6">
                <h1>Admin Actions:</h1>
                <button onClick={handleImport} className="btn btn-primary">Import Admins</button>
                {/* Display Imported Admins after import*/}
                {importedAdmins && importedAdmins.length > 0 && (
                    <div className="mt-4">
                        <h3>Imported Admins:</h3>
                        <ul>
                            {importedAdmins.map(admin => (
                                <li key={admin.id}>
                                    {admin.name} ({admin.email})
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
}

export default AdminImport;
