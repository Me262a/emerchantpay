import UserService from "../../services/UserService";

const RenderOnAuthenticated = ({ children, roles }) => {
  if (UserService.isLoggedIn() && UserService.hasAnyRole(roles)) {
    return children;
  }

  return null;
};

export default RenderOnAuthenticated;
