import { useState, useEffect } from "react";
import AddBook from "./AddBook";
import BookCard from "./BookCard";
import AlertMessage from "./AlertMessage";

export default function Books() {
  const [addBookClicked, setAddBookClicked] = useState(false);
  const [books, setBooks] = useState([]);
  const [showBooks, setShowBooks] = useState(false);
  const [message, setMessage] = useState({
    name: "",
    type: "",
  });
  const [addBookSubmitted, setAddBookSubmitted] = useState(false);

  useEffect(() => {
    let active = true;

    const fetchData = async () => {
      try {
        const response = await fetch("http://localhost:8080/books");
        const data = await response.json();
        if (response.ok) {
          setBooks(data);
          handleshowBooks();
        } else if (response.status === 400 || response.status === 404) {
          const statusMessage = await response.text(); // Get the error message as plain text
          handleMessages(statusMessage, "danger");
          console.log("neradau");
        }
      } catch (error) {
        // Handle network errors or exceptions
        handleMessages("No books found!", "danger");
        // alert(`An error occurred: ${error.message}`);
      }
    };

    fetchData();

    const handleshowBooks = () => {
      if (books.length !== 0) {
        setShowBooks(true);
      }
    };

    return () => {
      active = false;
      setAddBookSubmitted(false);
    };
  }, [books, addBookSubmitted]);

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
  };

  const handleAddBook = () => {
    setAddBookClicked(!addBookClicked);
    handleAlertClose();
  };

  return (
    <>
      <div className="container mt-5 mb-3">
        <button
          type="button"
          className="btn btn-warning mb-2"
          onClick={handleAddBook}
        >
          Add Book
        </button>
        <div className="container col-12 col-sm-8 col-lg-4 mt-3 mb-3">
          {message.name !== "" && (
            <AlertMessage
              message={message.name}
              type={message.type}
              handleAlertClose={handleAlertClose}
            />
          )}
          {addBookClicked && (
            <AddBook
              setAddBookSubmitted={setAddBookSubmitted}
              handleAddBook={handleAddBook}
            />
          )}
        </div>
        <div className="row mt-2 mb-2">
          {showBooks &&
            books.map((book) => {
              return (
                <BookCard
                  key={book.id}
                  title={book.title}
                  author={book.author}
                  categories={book.categories.map(
                    (categorie) => " #" + categorie.name
                  )}
                  description={book.description}
                  pictureUrl={book.pictureUrl}
                  pages={book.pages}
                  isbn={book.isbn}
                  publicationDate={book.publicationDate}
                  language={book.language}
                />
              );
            })}
        </div>
      </div>
    </>
  );
}
