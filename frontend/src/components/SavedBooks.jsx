import { useEffect, useState } from "react";

export default function SavedBooks() {
  const [books, setBooks] = useState([]);

  useEffect(() => {
    async function getBooks() {
      const url = "http://localhost:8080/saved-books";

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
        console.error(error.message);
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
          return <p key={index}>{book}</p>;
        })
      )}
    </>
  );
}
