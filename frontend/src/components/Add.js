export default function Add() {
  return (
    <form>
      <label>Category title:</label>
      <input
        className="form-control"
        type="text"
        name="category"
      />
      <button
        className="btn btn-primary"
        type="submit"
      >
        Add
      </button>
    </form>
  );
}
