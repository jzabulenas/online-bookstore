import { useState } from "react";
import { useForm } from "react-hook-form";
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
      } catch (error) {
        console.error(error.message);
      }
    }

    postData();
  };

  return (
    <>
      <form onSubmit={handleSubmit(onSubmit)}>
        <label htmlFor="book">Input your book:</label>
        <input
          type="text"
          id="book"
          {...register("book")}
        />

        <button>Submit</button>
      </form>

      <p>{result?.result}</p>
    </>
  );
}
