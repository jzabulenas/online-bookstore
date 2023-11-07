import { useEffect, useState } from "react";
import Add from "./Add";
import Edit from "./Edit";

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [addClicked, setAddClicked] = useState(false);
  const [editClicked, setEditClicked] = useState(false);
  const [selectedCategoryId, setSelectedCategoryId] = useState();
  const [editBtnActive, setEditBtnActive] = useState(false);

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

  const handleSelectedCategory = (e) => {
    setEditBtnActive(true);
    const selectedCategory = e.target.value;
    categories.forEach((category) => {
      if (category.name === selectedCategory) {
        setSelectedCategoryId(category.id);
      }
    });
  };

  return (
    <div className="container  col-12 col-sm-8 col-lg-4 mt-3 mb-3">
      <label htmlFor="category ">Current categories:</label>
      <select
        className="form-select mt-3 mb-3"
        name="category mt-3"
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
        className="btn btn-success  mb-3"
        onClick={() => setAddClicked(true)}
      >
        Add new category
      </button>
      {editBtnActive && (
        <button
          className="btn btn-info"
          onClick={() => setEditClicked(true)}
        >
          Edit category
        </button>
      )}
      {addClicked && <Add setAddClicked={setAddClicked} />}
      {editClicked && (
        <Edit
          selectedCategoryId={selectedCategoryId}
          setEditClicked={setEditClicked}
        />
      )}
    </div>
  );
}
