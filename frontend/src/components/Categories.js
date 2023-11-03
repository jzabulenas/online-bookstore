import { useEffect, useState } from "react";
import Add from "./Add";
import Edit from "./Edit";

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [addClicked, setAddClicked] = useState(false);
  const [editClicked, setEditClicked] = useState(false);
  const [selectedCategoryId, setSelectedCategoryId] = useState();

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

  const handleSelectedCategory = (e) => {
    const selectedCategory = e.target.value;
    categories.forEach((category) => {
      if (category.name === selectedCategory) {
        setSelectedCategoryId(category.id);
      }
    });
  };

  return (
    <div className="container">
      <label htmlFor="category">Current categories:</label>
      <select
        className="form-select"
        name="category"
        defaultValue="default"
        onChange={handleSelectedCategory}
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
      <button
        className="btn btn-success"
        onClick={() => setAddClicked(true)}
      >
        Add new category
      </button>
      <button
        className="btn btn-info"
        onClick={() => setEditClicked(true)}
      >
        Edit category
      </button>
      {addClicked && <Add />}
      {editClicked && <Edit selectedCategoryId={selectedCategoryId} />}
    </div>
  );
}
