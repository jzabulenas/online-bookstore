import { useEffect, useState } from "react";

export default function Categories() {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    let active = true;

    const fetchData = async () => {
      const response = await fetch("http://localhost:8080/categories");
      const data = await response.json();

      if (active) {
        setCategories(data);
      }
    };

    fetchData();

    return () => {
      active = false;
    };
  }, []);

  useEffect(() => {
    console.log("Kategorijos"); // Log the updated categories content
    console.log(categories); // Log the updated categories content
  }, [categories]);

  return (
    <div className="container">
      <label htmlFor="category">Category:</label>
      <select
        className="form-select"
        name="category"
      >
        <option
          defaultValue="Select a category"
          disabled
        >
          Select a category
        </option>
        <option name="science-fiction">Science </option>
        <option name="fantasy">Fantasy</option>
        <option name="self-help">Self Help</option>
      </select>
    </div>
  );
}
