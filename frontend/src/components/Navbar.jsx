import { Link, NavLink, useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import useSessionStorage from "../hooks/useSessionStorage";
import csrfToken from "../util/getCsrfToken";
import "./Navbar.css";

export default function Navbar() {
  const roles = useSessionStorage("roles");
  const navigate = useNavigate();

  const logout = () => {
    sessionStorage.removeItem("email");
    sessionStorage.removeItem("roles");

    const callLogoutEndpoint = async () => {
      const response = await fetch("http://localhost:8080/logout", {
        method: "POST",
        credentials: "include",
        headers: {
          "X-XSRF-TOKEN": csrfToken(),
        },
      });
    };

    callLogoutEndpoint();
    handleLinkClick();
    window.dispatchEvent(new Event("storage")); // Trigger a storage event manually

    // This is done so the call becomes asynchronous,
    // because otherwise it does not navigate me
    setTimeout(() => {
      navigate("/");
    }, 0);
  };

  const handleLinkClick = () => {
    // Get the navbar collapse element
    const navbarCollapse = document.querySelector(".navbar-collapse");

    // Toggle the 'show' class on the navbar collapse element to hide it
    navbarCollapse.classList.remove("show");
  };

  return (
    <nav className="navbar navbar-expand-md bg-body-tertiary mb-3">
      <div className="container">
        <Link
          className="navbar-brand"
          to={"/"}
        >
          <img
            src={logo}
            alt="Book recommendation app logo"
            className="logo"
          />
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div
          className="collapse navbar-collapse"
          id="navbarSupportedContent"
        >
          <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <NavLink
                className="nav-link"
                to="/"
                onClick={handleLinkClick}
              >
                Home
              </NavLink>
            </li>

            {roles === null && (
              <>
                <li className="nav-item">
                  <NavLink
                    className="nav-link"
                    to={"/login"}
                    onClick={handleLinkClick}
                  >
                    Log in
                  </NavLink>
                </li>

                <li className="nav-item">
                  <NavLink
                    className="nav-link"
                    to={"/about"}
                    onClick={handleLinkClick}
                  >
                    About
                  </NavLink>
                </li>
              </>
            )}

            {(roles?.some((role) => role.authority === "ROLE_ADMIN") ||
              roles?.some((role) => role.authority === "ROLE_USER")) && (
              <li className="nav-item">
                <Link
                  className="nav-link"
                  onClick={logout}
                >
                  Log out
                </Link>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}
