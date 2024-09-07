import { useState } from "react";
import { useForm } from "react-hook-form";
import { BsHandThumbsUp } from "react-icons/bs";
import csrfToken from "../util/getCsrfToken";

export default function GenerateBooks() {
  const { register, handleSubmit } = useForm();
  const [result, setResult] = useState(null);

  const onSubmit = (data) => {
    async function postData() {
      const url = "http://localhost:8080/generate-books";

      try {
        const response = await fetch(url, {
          method: "POST",
          credentials: "include",
          headers: {
            "X-XSRF-TOKEN": csrfToken(),
          },
          body: data.book,
        });

        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }

        const json = await response.json();
        setResult(json);
        console.log(json);
      } catch (error) {
        console.error(error.message);
      }
    }

    postData();
  };

  const clickThumbsUp = async (bookTitle) => {
    const url = "http://localhost:8080/save-book";

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
        <label htmlFor="book">Input your book:</label>
        <input
          type="text"
          id="book"
          {...register("book")}
        />

        <button>Submit</button>
      </form>

      {result &&
        result.map((book, index) => (
          <p key={index}>
            {book} <BsHandThumbsUp onClick={() => clickThumbsUp(book)} />
          </p>
        ))}
    </>
  );
}
