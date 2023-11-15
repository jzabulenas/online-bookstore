export default function Footer() {
  return (
    <footer class="mt-3 w-100">
      <div
        id="footer"
        class="container-fluid"
      >
        <div class="border pt-3 pb-2 container-fluid ">
          <div class="row  col-3 col-sm-12 ms-3 ">
            <div class="div1 col-3">
              {<p>Contact us: tel. 00370 223322223</p>}
            </div>
            <div class="div2 col-3">
              <a
                href="#"
                class="link-primary"
              >
                info@bookreservation.com
              </a>
            </div>
            <div class="div3 col-3">
              <p>&copy; Copyright 2023. All Rights Reserved.</p>
            </div>
            <div class="footer div4 col-3">
              <ul>
                <li>
                  <a href="contact.html">Address</a>
                </li>
                <li>
                  <a href="contact.html">About us</a>
                </li>
                <li>
                  <a href="facebook.com/SheCodes">Follow us on Facebook</a>
                </li>
                <li>
                  <a href="twitter.com/SheCodes">Follow us on Twitter</a>
                </li>
                <li>
                  <a href="newsletter.html">Sign up for our newsletter</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}
