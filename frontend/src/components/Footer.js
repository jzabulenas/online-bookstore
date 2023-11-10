export default function Footer() {
  return (
    <footer class="mt-3">
      <div class=" border container pt-3 pb-2">
        <div class="row">
          <div class="div1 col-4">
            {<p>Contact us: tel. 00370 223322223</p>}
          </div>
          <div class="div2 col-4">
            <a
              href="#"
              class="link-primary"
            >
              info@bookreservation.com
            </a>
          </div>
          <div class="div3 col-4">
            <p>&copy; Copyright 2023. All Rights Reserved.</p>
          </div>
          <div class="footer">
            <ul>
              <li>
                <a href="contact.html">Adress</a>
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
    </footer>
  );
}
