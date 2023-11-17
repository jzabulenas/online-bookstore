import { useEffect, useState } from "react";
import Add from "./Add";
import Edit from "./Edit";
import Delete from "./Delete";

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [addClicked, setAddClicked] = useState(false);
  const [editClicked, setEditClicked] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState();
  const [editBtnActive, setEditBtnActive] = useState(false);
  const [deleteBtnActive, setDeleteBtnActive] = useState(false);
  const [selectCategoryActive, setSelectCategoryActive] = useState(true);
  const [deleteCategoryActive, setDeleteCategoryActive] = useState(false);

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
    setDeleteBtnActive(true);
    setDeleteCategoryActive(true);
    const selectedCategory = e.target.value;
    categories.forEach((category) => {
      if (category.name === selectedCategory) {
        setSelectedCategory({
          ...selectedCategory,
          id: category.id,
          name: category.name,
        });
      }
    });
  };

  function handleAddClick() {
    setAddClicked(true);
    setSelectCategoryActive(false);
  }

  function handleEditClick() {
    setEditClicked(true);
    setSelectCategoryActive(false);
  }

  function handleDeleteClick() {
    setEditClicked(false);
    setSelectCategoryActive(false);
  }

  return (
    <div className="container  col-12 col-sm-8 col-lg-4 mt-3 mb-3">
      {selectCategoryActive && (
        <>
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
            className="btn btn-success mb-2 me-2"
            onClick={handleAddClick}
          >
            Add
          </button>
          {editBtnActive && (
            <button
              className="btn btn-info mb-2 me-2"
              onClick={handleEditClick}
            >
              Edit
            </button>
          )}
          {deleteBtnActive && (
            <button
              type="button"
              className="btn btn-danger mb-2"
              data-bs-toggle="modal"
              data-bs-target="#staticBackdrop"
              onClick={handleDeleteClick}
            >
              Delete
            </button>
          )}
        </>
      )}
      {addClicked && (
        <Add
          setAddClicked={setAddClicked}
          setSelectCategoryActive={setSelectCategoryActive}
          setEditBtnActive={setEditBtnActive}
          setDeleteBtnActive={setDeleteBtnActive}
        />
      )}
      {editClicked && (
        <Edit
          selectedCategoryId={selectedCategory.id}
          setEditClicked={setEditClicked}
          setSelectCategoryActive={setSelectCategoryActive}
          setEditBtnActive={setEditBtnActive}
          setDeleteBtnActive={setDeleteBtnActive}
        />
      )}
      {deleteCategoryActive && (
        <Delete
          selectedCategoryName={selectedCategory.name}
          selectedCategoryId={selectedCategory.id}
          setEditBtnActive={setEditBtnActive}
          setDeleteBtnActive={setDeleteBtnActive}
          setDeleteCategoryActive={setDeleteCategoryActive}
          setSelectCategoryActive={setSelectCategoryActive}
        />
      )}
    </div>
  );
}
