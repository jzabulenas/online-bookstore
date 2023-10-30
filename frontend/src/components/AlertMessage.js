export default function alertMessage({ message, type }) {
  return (
    <div id="liveAlertPlaceholder">
      <div
        className={`alert alert-${type} alert-dismissible`}
        role="alert"
      >
        <div>{message}</div>
        <button
          type="button"
          className="btn-close"
          data-bs-dismiss="alert"
          aria-label="Close"
        ></button>
      </div>
    </div>
  );
}
