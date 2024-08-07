import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="text-center p-5">
      <h1 className="fw-bold text-break">Book recommendation app</h1>
      <h2 className="fs-5 text-black text-opacity-75">
        Easily find new books to read
      </h2>
      <Link to="/about">Learn more</Link>
    </div>
  );
}
