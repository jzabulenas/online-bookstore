import { useEffect, useState } from "react";
import Add from "./Add";

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [addClicked, setAddClicked] = useState(false);

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
      <button
        className="btn btn-success"
        onClick={() => setAddClicked(true)}
      >
        Add new category
      </button>
      {addClicked && <Add />}
    </div>
  );
}
