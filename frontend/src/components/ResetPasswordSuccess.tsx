import { Link } from "react-router-dom";

export default function ResetPasswordSuccess() {
  return (
    <p>
      Your password has been reset successfully. <Link to="/login">Log in</Link>
    </p>
  );
}
