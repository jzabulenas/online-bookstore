import { useState } from "react";
import AlertMessage from "./AlertMessage";

export default function Add({
  setAddClicked,
  setSelectCategoryActive,
  setEditBtnActive,
  setDeleteBtnActive,
}) {
  const [categoryField, setCategoryField] = useState({
    name: "",
  });
  const [message, setMessage] = useState({
    name: "",
    type: "",
  });

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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCategoryField({
      ...categoryField,
      name: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const dataToPost = {
      name: categoryField.name,
    };

    try {
      const response = await fetch("http://localhost:8080/categories", {
        method: "POST",
        body: JSON.stringify(dataToPost),
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        // Successful response (2xx status code)
        handleMessages("Category created!", "success");
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
  };

  function showCategoryList() {
    setAddClicked(false);
    setEditBtnActive(false);
    setDeleteBtnActive(false);
    setSelectCategoryActive(true);
  }

  function handleCancelBtn() {
    showCategoryList();
  }

  return (
    <form onSubmit={handleSubmit}>
      <label>Category title:</label>
      <input
        className="form-control mt-3"
        type="text"
        name="category"
        value={categoryField.name}
        onChange={handleChange}
      />
      {message.name !== "" && (
        <AlertMessage
          message={message.name}
          type={message.type}
          handleAlertClose={handleAlertClose}
        />
      )}
      <button
        className="btn btn-primary mt-3 mb-3 "
        type="submit"
      >
        Add
      </button>
      <button
        className="btn btn-warning ms-2"
        type="button"
        onClick={handleCancelBtn}
      >
        Cancel
      </button>
    </form>
  );
}
