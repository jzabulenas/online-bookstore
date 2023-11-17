export default function Footer() {
  return (
    <footer className="mt-3 w-100 text-size 10px">
      <div
        id="footer"
        className="container-fluid"
      >
        <div className="row ms-3 ">
          <div className="div1 col-12 col-lg-3 d-flex justify-content-center ">
            {<p class="fs-6">textContact us: tel. 00370 223322223</p>}
          </div>
          <div className="div2 col-12 col-lg-3 d-flex justify-content-center">
            <a
              href="#"
              className="link-primary"
            >
              info@bookreservation.com
            </a>
          </div>
          <div className="div3 col-12 col-lg-3 d-flex justify-content-center">
            <p>&copy; Copyright 2023. All Rights Reserved.</p>
          </div>
          <div className="footer div4 col-12 col-lg-3 d-flex justify-content-center">
            <ul>
              <li>
                <a href="https://techin.lt/">Address</a>
              </li>
              <li>
                <a href="http://localhost:3000/about">About us</a>
              </li>
              <li>
                <img
                  src="data:image/png;base64,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"
                  className=""
                  alt="..."
                  width="30"
                  height="30"
                ></img>
                <a href="https://www.facebook.com/ateitiesprofesija/">
                  Follow us on Facebook
                </a>
              </li>
              <li>
                <img
                  src="data:image/jpeg;base64,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"
                  className=""
                  alt="..."
                  width="30"
                  height="30"
                ></img>
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
      </div>
    </footer>
  );
}
