export default function alertMessage({ message, type, handleAlertClose }) {
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
          onClick={handleAlertClose}
        ></button>
      </div>
    </div>
  );
}
