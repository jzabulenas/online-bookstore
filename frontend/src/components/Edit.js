import { useState } from "react";
import AlertMessage from "./AlertMessage";

export default function Edit() {
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

  return (
    <form onSubmit={handleSubmit}>
      <label>Category title:</label>
      <input
        className="form-control"
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
        className="btn btn-primary"
        type="submit"
      >
        Update
      </button>
    </form>
  );
}
