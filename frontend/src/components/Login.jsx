import { useForm } from "react-hook-form";

export default function Login() {
  const { register, handleSubmit } = useForm();

  const onSubmit = (data) => console.log(data);

  return (
    <>
      <div className="row">
        <div className="col">
          <a href="http://localhost:8080/login">Log in</a>
        </div>
      </div>

      <div className="row">
        <div className="col-4 mx-auto">
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="mb-3">
              <label
                htmlFor="email"
                className="form-label"
              >
                Email:
              </label>
              <input
                type="email"
                name="email"
                id="email"
                className="form-control"
                {...register("email")}
              />
            </div>

            <button
              type="submit"
              className="btn btn-primary"
            >
              Submit
            </button>
          </form>
        </div>
      </div>
    </>
  );
}
