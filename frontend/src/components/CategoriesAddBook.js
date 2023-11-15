import { useState, useEffect } from "react";

export default function CategoriesAddBook({ handleChange }) {
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

  return (
    <select
      className="form-select"
      name="category"
      defaultValue="default"
      onChange={handleChange}
    >
      <option
        value="default"
        disabled
        hidden
      >
        Select a category
      </option>

      {categories.map((category) => {
        return (
          <option
            key={category.id}
            value={category.name}
          >
            {category.name}
          </option>
        );
      })}
    </select>
  );
}
