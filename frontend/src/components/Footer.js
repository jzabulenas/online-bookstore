import "./Footer.css";

export default function Footer() {
  return (
    <footer className="container-fluid text-font-size">
      <div className="row">
        <div className="col-12 col-lg-3 d-flex justify-content-center">
          <p>Tel. 00370 223322223</p>
        </div>

        <div className="col-12 col-lg-3 d-flex justify-content-center">
          <a
            href="https://www.vtmc.lt/kontaktai/"
            className=""
          >
            info@bookreservation.com
          </a>
        </div>

        <div className="col-12 col-lg-3 d-flex justify-content-center">
          <p>&copy; Copyright 2023</p>
        </div>

        <div className="col-12 col-lg-3 d-flex justify-content-center">
          <ul className="list-unstyled">
            <li>
              <a href="https://techin.lt/">Address</a>
            </li>

            <li>
              <a href="http://localhost:3000/about">About us</a>
            </li>

            <li>
              <a href="https://www.facebook.com/ateitiesprofesija/">
                Follow us on Facebook
              </a>
            </li>

            <li>
              <a href="https://twitter.com/github">Follow us on Twitter</a>
            </li>

            <li>
              <a href="https://www.vtmc.lt/naujienos/">
                Sign up for our newsletter
              </a>
            </li>
          </ul>
        </div>
      </div>
    </footer>
  );
}
