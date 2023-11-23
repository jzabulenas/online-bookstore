export default function About() {
  return (
    <div className="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-body-tertiary">
      <div className="col-md-6 p-lg-5 mx-auto my-5">
        <h3 className="fw-normal text-muted mb-3">Book Reservation App</h3>
        <ul className="list-unstyled">
          <li>Add, edit, and delete book categories.</li>
          <li>Add, edit, and delete a book with assigned categories.</li>
          <li>Search for a book and reserve it.</li>
          <li>Add a book to favorites.</li>
        </ul>
      </div>
    </div>
  );
}
