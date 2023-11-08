export default function Delete({ selectedCategoryName, selectedCategoryId }) {
  function handleDeleteYes() {
    fetch(`http://localhost:8080/categories/${selectedCategoryId}`, {
      method: "Delete",
      headers: {
        "Content-Type": "application/json",
      },
    });
  }
  return (
    <div>
      <div
        className="modal fade"
        id="staticBackdrop"
        data-bs-backdrop="static"
        data-bs-keyboard="false"
        tabIndex="-1"
        aria-labelledby="staticBackdropLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            {/* Modal header */}
            <div className="modal-header">
              <h1
                className="modal-title fs-5"
                id="staticBackdropLabel"
              >
                Do you want to delete?
              </h1>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>

            {/* Modal body */}
            <div className="modal-body">
              {/* Body content goes here */}
              <p>{selectedCategoryName}</p>
            </div>

            {/* Modal footer */}
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-primary"
                data-bs-dismiss="modal"
                onClick={handleDeleteYes}
              >
                Yes
              </button>
              <button
                type="button"
                className="btn btn-secondary"
                data-bs-dismiss="modal"
              >
                No
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
