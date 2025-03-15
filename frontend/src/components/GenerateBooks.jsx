import { useState } from "react";
import { useForm } from "react-hook-form";
import { BsHandThumbsUp, BsHandThumbsUpFill } from "react-icons/bs";
import { useNavigate } from "react-router-dom";
import csrfToken from "../util/getCsrfToken";

export default function GenerateBooks() {
  const { register, handleSubmit } = useForm();
  const [books, setBooks] = useState(null);
  const [likedBooks, setLikedBooks] = useState([]); // Track liked books
  const navigate = useNavigate();

  const onSubmit = (data) => {
    async function postData() {
      const url = "http://localhost:8080/generate-books";

      try {
        const response = await fetch(url, {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken(),
          },
          body: JSON.stringify({
            message: data.book,
          }),
        });

        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }

        const json = await response.json();
        setBooks(json);
        console.log(json);
      } catch (error) {
        // console.error(error.message);
        sessionStorage.clear();
        navigate("/login");
      }
    }

    postData();
  };

  const clickThumbsUp = async (bookTitle) => {
    const url = "http://localhost:8080/books";

    try {
      const response = await fetch(url, {
        method: "POST",
        credentials: "include",
        headers: {
          "X-XSRF-TOKEN": csrfToken(),
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ title: bookTitle }),
      });

      if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
      }

      // On successful save, update the liked state
      setLikedBooks((prev) => [...prev, bookTitle]);
    } catch (error) {
      console.error(error.message);
    }
  };

  return (
    <>
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="mb-3"
      >
        <div className="mb-3">
          <label
            htmlFor="book"
            className="form-label"
          >
            Input your book:
          </label>
          <input
            type="text"
            id="book"
            {...register("book")}
            className="form-control"
          />
        </div>

        <button className="btn btn-primary">Submit</button>
      </form>

      {books &&
        books.result.map((book, index) => (
          <p key={index}>
            {book}
            {likedBooks.includes(book) ? (
              <BsHandThumbsUpFill className="ms-1 fs-4" />
            ) : (
              <BsHandThumbsUp
                onClick={() => clickThumbsUp(book)}
                className="ms-1 fs-4"
              />
            )}
          </p>
        ))}
    </>
  );
}
