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
  }, [categories]);

  return (
    <div className="container  col-12 col-sm-8 col-lg-4 mt-3 mb-3">
      <label htmlFor="category ">Current categories:</label>
      <select
        className="form-select mt-3 mb-3"
        name="category mt-3"
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
        className="btn btn-success  mb-3"
        onClick={() => setAddClicked(true)}
      >
        Add new category
      </button>
      {addClicked && <Add setAddClicked={setAddClicked} />}
    </div>
  );
}
