import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function SavedBooks() {
  const [books, setBooks] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    async function getBooks() {
      const url = "http://localhost:8080/books";

      try {
        const response = await fetch(url, {
          method: "GET",
          credentials: "include",
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

    getBooks();
  }, []);

  return (
    <>
      <h1>Books you have saved</h1>

      {books.length === 0 ? (
        <p>You do not have any saved books.</p>
      ) : (
        books.map((book, index) => {
          return <p key={index}>{book.title}</p>;
        })
      )}
    </>
  );
}
