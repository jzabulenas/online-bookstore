export default function BookCard({
  title,
  author,
  categories,
  description,
  pictureUrl,
  pages,
  isbn,
  publicationDate,
  language,
}) {
  return (
    <>
      <div className="col-md-6">
        <div className="row g-0 border rounded overflow-hidden flex-md-row mb-4 shadow-sm h-md-250 position-relative">
          <div className="col p-4 d-flex flex-column position-static">
            <strong className="d-inline-block mb-2 text-primary">
              {author}
            </strong>
            <h3 className="mb-2">{title}</h3>
            <p className="card-text mb-auto">
              {description.substring(0, 200) + "..."}
            </p>
            <p className="card-text mt-2 mb-auto">
              <span className="text-body-secondary">
                <strong>Pages: </strong>
              </span>
              {pages}
            </p>
            <p className="card-text mb-auto">
              <span className="text-body-secondary">
                <strong>ISBN: </strong>
              </span>
              {isbn}
            </p>
            <p className="card-text mb-auto">
              <span className="text-body-secondary">
                <strong>Publication date: </strong>
              </span>

              {publicationDate}
            </p>
            <p className="card-text mb-auto">
              <span className="text-body-secondary">
                <strong>Language: </strong>
              </span>
              {language}
            </p>
            <p className="card-text mb-auto">
              <span className="text-body-secondary">
                <strong>Categories: </strong>
              </span>

              {categories}
            </p>
            {/* <a
              href="https://www.google.lt"
              className="icon-link gap-1 icon-link-hover stretched-link"
            ></a> */}
          </div>

          <div className="col-auto d-none d-lg-block">
            <svg
              className="bd-placeholder-img"
              width="220"
              height="330"
              xmlns="http://www.w3.org/2000/svg"
              role="img"
              aria-label="Placeholder: Thumbnail"
              preserveAspectRatio="xMidYMid slice"
              focusable="false"
            >
              <title>Placeholder</title>
              <rect
                width="100%"
                height="100%"
                fill="#55595c"
              />
              <image
                href={pictureUrl}
                width="100%"
                height="100%"
                // preserveAspectRatio="xMidYMid slice"
                style={{ objectFit: "cover" }}
              />
            </svg>
          </div>
        </div>
      </div>
    </>
  );
}
