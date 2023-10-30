import { useState } from "react";
import AlertMessage from "./AlertMessage";

export default function Add() {
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
        // You can also redirect the user or perform other actions on success
      } else {
        // Handle errors (non-2xx status codes)
        const errorData = await response.json();
        alert(`Error: ${errorData.message}`);
        // You may want to display a more user-friendly message
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
        />
      )}
      <button
        className="btn btn-primary"
        type="submit"
      >
        Add
      </button>
    </form>
  );
}
