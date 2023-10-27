export default function Categories() {
  return (
    <div className="container">
      <label htmlFor="category">Category:</label>
      <select
        className="form-select"
        name="category"
      >
        <option
          defaultValue="Select a category"
          disabled
        >
          Select a category
        </option>
        <option name="science-fiction">Science </option>
        <option name="fantasy">Fantasy</option>
        <option name="self-help">Self Help</option>
      </select>
    </div>
  );
}
