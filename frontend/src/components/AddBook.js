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
  return (
    <div className="container  col-12 col-sm-8 col-lg-4 mt-3 mb-3">
      <h4>Add a new book:</h4>

      <form>
        <label className="form-label">Title</label>
        <input
          className="form-control"
          type="text"
          name="title"
          placeholder="Enter the book title"
        />

        <label className="form-label">Author</label>
        <input
          className="form-control"
          type="text"
          name="author"
          placeholder="Enter the author's full name"
        />

        <label className="form-label">Select a categorie</label>
        <CategoriesAddBook />

        <label className="form-label">Description</label>
        <textarea
          className="form-control"
          name="description"
          rows="3"
          placeholder="Enter a brief description of the book (max 300 characters)"
        ></textarea>

        <label className="form-label">Picture Url</label>
        <input
          className="form-control"
          type="text"
          name="pictureUrl"
          placeholder="Enter the URL of the book cover picture"
        />

        <label className="form-label">Pages</label>
        <input
          className="form-control"
          type="number"
          name="pages"
          placeholder="Enter the number of pages"
        />

        <label className="form-label">ISBN</label>
        <input
          className="form-control"
          type="text"
          name="isbn"
          placeholder="Enter the book's ISBN"
        />

        <label className="form-label">Publication Date</label>
        <input
          className="form-control"
          type="date"
          name="publicationDate"
        />

        <label className="form-label">Language</label>
        <input
          className="form-control"
          type="text"
          name="language"
          placeholder="Enter the language of the book"
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
