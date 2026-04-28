import { Route, Routes } from "react-router-dom";
import About from "./components/About";
import ErrorPage from "./components/ErrorPage";
import Footer from "./components/Footer";
import ForgotPassword from "./components/ForgotPassword";
import Home from "./components/Home";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import OAuth2RedirectHandler from "./components/OAuth2RedirectHandler";
import ResetPassword from "./components/ResetPassword";
import ResetPasswordSuccess from "./components/ResetPasswordSuccess";
import SavedBooks from "./components/SavedBooks";
import Signup from "./components/Signup";
import { useState } from "react";
import VerificationSuccess from "./components/VerificationSuccess";

function App() {
  const [isSignedUp, setIsSignedUp] = useState(false);

  return (
    <>
      <Navbar />
      <main className="container">
        <Routes>
          <Route
            path="/"
            element={
              <Home
                isSignedUp={isSignedUp}
                setIsSignedUp={setIsSignedUp}
              />
            }
          />
          <Route
            path="/about"
            element={<About />}
          />
          <Route
            path="/login"
            element={<Login />}
          />
          <Route
            path="/oauth2/redirect"
            element={<OAuth2RedirectHandler />}
          />
          <Route
            path="/saved-books"
            element={<SavedBooks />}
          />
          <Route
            path="/signup"
            element={<Signup setIsSignedUp={setIsSignedUp} />}
          />
          <Route
            path="/verification-success"
            element={<VerificationSuccess />}
          />
          <Route
            path="/forgot-password"
            element={<ForgotPassword />}
          />
          <Route
            path="/reset-password"
            element={<ResetPassword />}
          />
          <Route
            path="/reset-password-success"
            element={<ResetPasswordSuccess />}
          />
          <Route
            path="*"
            element={<ErrorPage />}
          />
        </Routes>
      </main>
      <Footer />
    </>
  );
}

export default App;
