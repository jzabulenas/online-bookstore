import AlertMessage from "./AlertMessage";
import { useState } from "react";

export default function Delete({
  selectedCategoryName,
  selectedCategoryId,
  setEditBtnActive,
  setDeleteBtnActive,
  setDeleteCategoryActive,
  setSelectCategoryActive,
}) {
  const [message, setMessage] = useState({
    name: "",
    type: "",
  });

  async function handleDeleteYes() {
    try {
      const response = await fetch(
        `http://localhost:8080/categories/${selectedCategoryId}`,
        {
          method: "Delete",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.ok) {
        // Successful response (2xx status code)
        handleMessages("Category deleted!", "success");
        setTimeout(showCategoryList, 1200);
      } else if (response.status === 400) {
        const statusMessage = await response.text(); // Get the error message as plain text
        handleMessages(statusMessage, "danger");
      } else {
        // Handle errors (non-2xx status codes)
        const errorData = await response.json();
        alert(`Error: ${errorData.message}`);
      }
    } catch (error) {
      // Handle network errors or exceptions
      alert(`An error occurred: ${error.message}`);
    }
  }

  const handleMessages = (messageText, messageType) => {
    setMessage({
      ...message,
      name: messageText,
      type: messageType,
    });
  };

  const handleAlertClose = () => {
    setMessage({
      ...message,
      name: "",
      type: "",
    });
  };

  function handleDeleteNo() {
    showCategoryList();
  }

  function showCategoryList() {
    setEditBtnActive(false);
    setDeleteBtnActive(false);
    setDeleteCategoryActive(false);
    setSelectCategoryActive(true);
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
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            {/* Modal header */}
            <div className="modal-header text-bg-danger">
              <h1
                className="modal-title fs-5"
                id="staticBackdropLabel"
              >
                Do you want to delete?
              </h1>
              <div data-bs-theme="dark">
                <button
                  type="button"
                  className="btn-close "
                  data-bs-dismiss="modal"
                  aria-label="Close"
                  onClick={handleDeleteNo}
                ></button>
              </div>
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
                className="btn btn-primary btn-danger"
                data-bs-dismiss="modal"
                onClick={handleDeleteYes}
              >
                Yes
              </button>
              <button
                type="button"
                className="btn btn-secondary btn-light"
                data-bs-dismiss="modal"
                onClick={handleDeleteNo}
              >
                No
              </button>
            </div>
          </div>
        </div>
      </div>
      {message.name !== "" && (
        <AlertMessage
          message={message.name}
          type={message.type}
          handleAlertClose={handleAlertClose}
        />
      )}
    </div>
  );
}
