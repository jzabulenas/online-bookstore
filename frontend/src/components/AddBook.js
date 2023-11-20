import { useState } from "react";
import CategoriesAddBook from "./CategoriesAddBook";

export default function AddBook() {
  const [bookData, setBookData] = useState({
    title: "",
    author: "",
    categories: [""],
    description: "",
    pictureUrl: "",
    pages: "",
    isbn: "",
    publicationDate: "",
    language: "",
  });

  const [selectedCategories, setSelectedCategories] = useState([]);
  const [activePlusBtn, setActivePlusBtn] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setBookData({
      ...bookData,
      [name]: value,
    });
  };

  const handleCategoryChange = (index, value) => {
    const newCategories = [...bookData.categories];

    newCategories[index] = value;
    setBookData({
      ...bookData,
      categories: newCategories,
    });

    setSelectedCategories([...newCategories]);

    console.log(bookData);
  };

  const handlePLusBtn = () => {
    setBookData({
      ...bookData,
      categories: [...bookData.categories, ""],
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const dataToPost = {
      title: bookData.title,
      author: bookData.author,
      categories: bookData.categories.map((category) => ({
        name: category,
      })),

      description: bookData.description,
      pictureUrl: bookData.pictureUrl,
      pages: bookData.pages,
      isbn: bookData.isbn,
      publicationDate: bookData.publicationDate,
      language: bookData.language,
    };

    fetch("http://localhost:8080/books", {
      method: "POST",
      body: JSON.stringify(dataToPost),
      headers: {
        "Content-Type": "application/json",
      },
    });
  };

  return (
    <div className="container  col-12 col-sm-8 col-lg-4 mt-3 mb-3">
      <h4>Add a new book:</h4>

      <form onSubmit={handleSubmit}>
        <label className="form-label">Title</label>
        <input
          className="form-control"
          type="text"
          name="title"
          placeholder="Enter the book title"
          value={bookData.title}
          onChange={handleChange}
        />

        <label className="form-label">Author</label>
        <input
          className="form-control"
          type="text"
          name="author"
          placeholder="Enter the author's full name"
          value={bookData.author}
          onChange={handleChange}
        />

        <label className="form-label">Select a category</label>
        {bookData.categories.map((category, index) => (
          <CategoriesAddBook
            key={index}
            handleChange={(e) => handleCategoryChange(index, e.target.value)}
            selectedCategories={selectedCategories}
            index={index}
            setActivePlusBtn={setActivePlusBtn}
          />
        ))}

        <button
          className={`btn btn-primary rounded-circle ${activePlusBtn}`}
          type="button"
          onClick={handlePLusBtn}
        >
          +
        </button>
        <div>
          <label className="form-label">Description</label>
          <textarea
            className="form-control"
            name="description"
            rows="3"
            placeholder="Enter a brief description of the book (max 300 characters)"
            value={bookData.description}
            onChange={handleChange}
          ></textarea>
        </div>

        <label className="form-label">Picture Url</label>
        <input
          className="form-control"
          type="text"
          name="pictureUrl"
          placeholder="Enter the URL of the book cover picture"
          value={bookData.pictureUrl}
          onChange={handleChange}
        />

        <label className="form-label">Pages</label>
        <input
          className="form-control"
          type="number"
          name="pages"
          placeholder="Enter the number of pages"
          value={bookData.pages}
          onChange={handleChange}
        />

        <label className="form-label">ISBN</label>
        <input
          className="form-control"
          type="text"
          name="isbn"
          placeholder="Enter the book's ISBN"
          value={bookData.isbn}
          onChange={handleChange}
        />

        <label className="form-label">Publication Date</label>
        <input
          className="form-control"
          type="date"
          name="publicationDate"
          value={bookData.publicationDate}
          onChange={handleChange}
        />

        <label className="form-label">Language</label>
        <input
          className="form-control"
          type="text"
          name="language"
          placeholder="Enter the language of the book"
          value={bookData.language}
          onChange={handleChange}
        />

        <button
          className="btn btn-primary"
          type="submit"
        >
          Submit
        </button>
      </form>
    </div>
  );
}
