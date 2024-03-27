import "./Footer.css";

export default function Footer() {
  return (
    <footer className="container text-font-size">
      <div className="row">
        <div className="col-12 col-sm-6 d-flex justify-content-center justify-content-sm-start">
          <ul className="list-unstyled">
            <li className="p-1">Phone: +370 600 60060</li>
            <li className="p-1">
              <a
                href="https://www.vtmc.lt/kontaktai/"
                className=""
              >
                info@bookreservation.com
              </a>
            </li>
            <li className="p-1">&copy; Copyright 2024</li>
          </ul>
        </div>

        <div className="col-12 col-sm-6 d-flex justify-content-center justify-content-sm-end">
          <ul className="list-unstyled">
            <li className="p-1">
              <a href="https://techin.lt/">Address</a>
            </li>

            <li className="p-1">
              <a href="http://localhost:3000/about">About us</a>
            </li>

            <li className="p-1">
              <a href="https://www.facebook.com/ateitiesprofesija/">
                Follow us on Facebook
              </a>
            </li>

            <li className="p-1">
              <a href="https://twitter.com/github">Follow us on Twitter</a>
            </li>

            <li className="p-1">
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
