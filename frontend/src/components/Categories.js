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

  return (
    <div className="container">
      <label htmlFor="category">Category:</label>
      <select
        className="form-select"
        name="category"
        defaultValue="default"
      >
        <option
          value="default"
          disabled
          hidden
        >
          Select a category
        </option>

        {categories.map((category, index) => {
          return (
            <option
              key={index}
              value={category.name}
            >
              {category.name}
            </option>
          );
        })}
      </select>
    </div>
  );
}
