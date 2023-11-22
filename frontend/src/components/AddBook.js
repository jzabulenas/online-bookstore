import { useState } from "react";
import CategoriesAddBook from "./CategoriesAddBook";
import AlertMessage from "./AlertMessage";

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
  const [activeMinusBtn, setActiveMinusBtn] = useState("");

  const [message, setMessage] = useState({
    name: "",
    type: "",
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setBookData({
      ...bookData,
      [name]: value,
    });

    setErrors({
      ...errors,
      [name]: "",
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
    setErrors({
      ...errors,
      category: "",
    });
  };

  const handlePLusBtn = () => {
    setBookData({
      ...bookData,
      categories: [...bookData.categories, ""],
    });
  };

  const handleMinusBtn = () => {
    const newCategories = [...bookData.categories];
    newCategories.pop();
    setBookData({
      ...bookData,
      categories: newCategories,
    });

    setSelectedCategories([...newCategories]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const newErrors = {};

    if (!/^[A-Z0-9][a-zA-Z0-9 .,:'"!?&()-]+$/.test(bookData.title)) {
      newErrors["title"] =
        "Book title must start with an uppercase " +
        "letter, that can be followed by a mix of alphanumeric characters, spaces, and certain punctuation marks!";
    }
    if (!/^[A-Z][a-z]+ [A-Z][a-z]+$/.test(bookData.author)) {
      newErrors["author"] =
        "Author's first and last name must start with " +
        "an uppercase letter, that can be followed by one or more lowercase letters!";
    }
    if (selectedCategories.length === 0) {
      newErrors["category"] = "Select one category at least!";
    }
    if (!/^[A-Z].{0,299}$/.test(bookData.description)) {
      newErrors["description"] =
        "Description should start with a capital letter " +
        "and is limited to a maximum of 300 characters!";
    }
    if (!/^(https?):\/\/[^\s$]+\.(jpg|png)$/.test(bookData.pictureUrl)) {
      newErrors["pictureUrl"] =
        'URl should start with either "http://" or "https://" and end with ".jpg" or ".png!';
    }
    if (bookData.pages < 1) {
      newErrors["pages"] = "Pages field must have a value greater than 0!";
    }
    if (
      !/((978[\--– ])?[0-9][0-9\--– ]{10}[\--– ][0-9xX])|((978)?[0-9]{9}[0-9Xx])/.test(
        bookData.isbn
      )
    ) {
      newErrors["isbn"] = "ISBN is incorrect";
    }
    if (!/(\d{4})-(\d{2})-(\d{2})/.test(bookData.publicationDate)) {
      newErrors["publicationDate"] = "Select a date, please!";
    }
    if (!/^[A-Z][a-z]+$/.test(bookData.language)) {
      newErrors["language"] =
        "Language must start with an uppercase " +
        "letter, that can be followed by one or more lowercase letters!";
    }

    if (Object.keys(newErrors).length !== 0) {
      // Proceed with submission
      setErrors(newErrors);
    } else {
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

      try {
        const response = await fetch("http://localhost:8080/books", {
          method: "POST",
          body: JSON.stringify(dataToPost),
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (response.ok) {
          handleMessages("Book created!", "success");
          setTimeout(handleAlertClose, 1200);
        } else if (response.status === 400 || response.status === 404) {
          const statusMessage = await response.text(); // Get the error message as plain text
          handleMessages(statusMessage, "danger");
        } else {
          // Handle errors (non-2xx status codes)
          const errorData = await response.json();
          alert(`Error: ${errorData.message}`);
        }
      } catch (error) {
        // Handle network errors or exceptions
        alert(`An error occurred: ${error.message}`);
      }
    }
  };

  const handleMessages = (messageText, messageType) => {
    setMessage({
      ...message,
      name: messageText,
      type: messageType,
    });
  };

  const handleAlertClose = (key) => {
    setMessage({
      ...message,
      name: "",
      type: "",
    });

    setErrors((prevErrors) => ({
      ...prevErrors,
      [key]: "",
    }));
  };

  return (
    <div className="container col-12 col-sm-8 col-lg-4 mt-3 mb-3">
      <h4>Add a new book:</h4>

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label
            className="form-label"
            htmlFor="title"
          >
            Title
          </label>
          <input
            className="form-control mb-2"
            type="text"
            name="title"
            id="title"
            placeholder="Enter the book title"
            value={bookData.title}
            onChange={handleChange}
          />

          {errors.title && (
            <AlertMessage
              message={errors.title}
              type="danger"
              handleAlertClose={() => handleAlertClose("title")}
            />
          )}
        </div>

        <div className="mb-3">
          <label
            className="form-label"
            htmlFor="author"
          >
            Author
          </label>
          <input
            className="form-control mb-2"
            type="text"
            name="author"
            id="author"
            placeholder="Enter the author's full name"
            value={bookData.author}
            onChange={handleChange}
          />

          {errors.author && (
            <AlertMessage
              message={errors.author}
              type="danger"
              handleAlertClose={() => handleAlertClose("author")}
            />
          )}
        </div>

        <div className="mb-3">
          <label
            className="form-label"
            htmlFor="category"
          >
            Select a category
          </label>

          {bookData.categories.map((category, index) => (
            <CategoriesAddBook
              key={index}
              handleChange={(e) => handleCategoryChange(index, e.target.value)}
              selectedCategories={selectedCategories}
              index={index}
              setActivePlusBtn={setActivePlusBtn}
              setActiveMinusBtn={setActiveMinusBtn}
            />
          ))}
          <button
            className={`btn btn-primary rounded-circle ${activePlusBtn} me-1`}
            type="button"
            onClick={handlePLusBtn}
          >
            +
          </button>

          <button
            className={`btn btn-danger rounded-circle ${activeMinusBtn}`}
            style={{ padding: "6px 15px" }}
            type="button"
            onClick={handleMinusBtn}
          >
            -
          </button>
          <div className="mt-2">
            {errors.category && (
              <AlertMessage
                message={errors.category}
                type="danger"
                handleAlertClose={() => handleAlertClose("category")}
              />
            )}
          </div>
        </div>

        <div className="mb-3">
          <label
            className="form-label"
            htmlFor="description"
          >
            Description
          </label>
          <textarea
            className="form-control mb-2"
            name="description"
            id="description"
            rows="3"
            placeholder="Enter a brief description of the book (max 300 characters)"
            value={bookData.description}
            onChange={handleChange}
          ></textarea>

          {errors.description && (
            <AlertMessage
              message={errors.description}
              type="danger"
              handleAlertClose={() => handleAlertClose("description")}
            />
          )}
        </div>

        <div className="mb-3">
          <label
            className="form-label"
            htmlFor="picture-url"
          >
            Picture Url
          </label>
          <input
            className="form-control mb-2"
            type="text"
            name="pictureUrl"
            id="picture-url"
            placeholder="Enter the URL of the book cover picture"
            value={bookData.pictureUrl}
            onChange={handleChange}
          />

          {errors.pictureUrl && (
            <AlertMessage
              message={errors.pictureUrl}
              type="danger"
              handleAlertClose={() => handleAlertClose("pictureUrl")}
            />
          )}
        </div>

        <div className="row">
          <div className="col-md-6 mb-2">
            <label
              className="form-label"
              htmlFor="pages"
            >
              Pages
            </label>
            <input
              className="form-control mb-2"
              type="number"
              name="pages"
              id="pages"
              placeholder="Enter the number of pages"
              value={bookData.pages}
              onChange={handleChange}
            />
            {errors.pages && (
              <AlertMessage
                message={errors.pages}
                type="danger"
                handleAlertClose={() => handleAlertClose("pages")}
              />
            )}
          </div>

          <div className="col-md-6 mb-2">
            <label
              className="form-label"
              htmlFor="isbn"
            >
              ISBN
            </label>
            <input
              className="form-control mb-2"
              type="text"
              name="isbn"
              id="isbn"
              placeholder="Enter the book's ISBN"
              value={bookData.isbn}
              onChange={handleChange}
            />
            {errors.isbn && (
              <AlertMessage
                message={errors.isbn}
                type="danger"
                handleAlertClose={() => handleAlertClose("isbn")}
              />
            )}
          </div>

          <div className="col-md-6 mb-3">
            <label
              className="form-label"
              htmlFor="publication-date"
            >
              Publication Date
            </label>
            <input
              className="form-control mb-2"
              type="date"
              name="publicationDate"
              id="publication-date"
              value={bookData.publicationDate}
              onChange={handleChange}
            />
            {errors.publicationDate && (
              <AlertMessage
                message={errors.publicationDate}
                type="danger"
                handleAlertClose={() => handleAlertClose("publicationDate")}
              />
            )}
          </div>

          <div className="col-md-6">
            <label
              className="form-label"
              htmlFor="language"
            >
              Language
            </label>
            <input
              className="form-control mb-2"
              type="text"
              name="language"
              id="language"
              placeholder="Enter the language of the book"
              value={bookData.language}
              onChange={handleChange}
            />
            {errors.language && (
              <AlertMessage
                message={errors.language}
                type="danger"
                handleAlertClose={() => handleAlertClose("language")}
              />
            )}
          </div>
        </div>

        {message.name !== "" && (
          <AlertMessage
            message={message.name}
            type={message.type}
            handleAlertClose={handleAlertClose}
          />
        )}

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
