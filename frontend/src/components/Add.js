import { useState } from "react";

export default function Add() {
  const [categoryField, setCategoryField] = useState({
    name: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCategoryField({
      ...categoryField,
      name: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const dataToPost = {
      name: categoryField.name,
    };

    fetch("http://localhost:8080/categories", {
      method: "POST",
      body: JSON.stringify(dataToPost),
      headers: {
        "Content-Type": "application/json",
      },
    });
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
      <button
        className="btn btn-primary"
        type="submit"
      >
        Add
      </button>
    </form>
  );
}
