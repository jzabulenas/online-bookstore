import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-body-tertiary">
      <div className="col-md-6 p-lg-5 mx-auto my-5">
        <h1 className="display-3 fw-bold">Book recommendation app</h1>
        <h2 className="fw-normal text-muted mb-3">
          Easily find new books to read
        </h2>
        <div className="d-flex gap-3 justify-content-center lead fw-normal">
          <Link
            className="icon-link"
            to={"/about"}
          >
            Learn more
          </Link>
        </div>
      </div>
    </div>
  );
}
