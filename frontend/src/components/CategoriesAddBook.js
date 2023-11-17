import { useState, useEffect } from "react";

export default function CategoriesAddBook({
  handleChange,
  selectedCategories,
  index,
}) {
  const [categories, setCategories] = useState([]);

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

        setCategories(filteredCategories);
      }
    };

    fetchData();

    return () => {
      active = false;
    };
  }, [selectedCategories]);

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
