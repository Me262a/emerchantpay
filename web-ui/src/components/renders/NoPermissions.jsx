import UserService from "../../services/UserService";

const NoPermissions = () => {
  const isAuthenticated = UserService.isLoggedIn();

  if (isAuthenticated && !UserService.hasAnyRole(['admin', 'merchant'])) {
    return <div>You're logged in, but you don't have permissions to view content.</div>;
  }

  return null;
};

export default NoPermissions;
