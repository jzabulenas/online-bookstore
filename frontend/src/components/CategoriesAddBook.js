import { useState, useEffect } from "react";

export default function CategoriesAddBook({
  handleChange,
  selectedCategories,
  index,
  setActivePlusBtn,
  setActiveMinusBtn,
}) {
  const [categories, setCategories] = useState([]);
  const [categoriesCount, setCategoriesCount] = useState([]);

  useEffect(() => {
    let active = true;

    const fetchData = async () => {
      const response = await fetch("http://localhost:8080/categories");
      const data = await response.json();

      if (active) {
        const filteredCategories = data.filter((category) => {
          const isCategorieNameInSelectedArray = selectedCategories.includes(
            category.name
          );
          const isLocalSelecetionSelected =
            category.name === selectedCategories[index];

          return !isCategorieNameInSelectedArray || isLocalSelecetionSelected;
        });

        setCategoriesCount(data.length);

        setCategories(filteredCategories);
      }
    };

    fetchData();

    togglePlusBtn();
    toggleMinusBtn();

    return () => {
      active = false;
    };
  }, [selectedCategories]);

  function togglePlusBtn() {
    const isCategorychosen =
      selectedCategories[index] === undefined ||
      selectedCategories[index] === "";
    const oneMoreCategoryList = index === 2 || categoriesCount - index <= 1;
    if (isCategorychosen || oneMoreCategoryList) {
      setActivePlusBtn("disabled");
    } else {
      setActivePlusBtn("");
    }
  }

  function toggleMinusBtn() {
    const categoryFieldIndex = index === 0;
    if (categoryFieldIndex) {
      setActiveMinusBtn("disabled");
    } else {
      setActiveMinusBtn("");
    }
  }

  return (
    <select
      className="form-select mb-2"
      name="category"
      id="category"
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
