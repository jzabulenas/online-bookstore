import { useForm } from "react-hook-form";
import csrfToken from "../util/getCsrfToken";
import { useNavigate } from "react-router-dom";

export default function Signup() {
  const { register, handleSubmit } = useForm();
  const navigate = useNavigate();

  const onSubmit = (data) => {
    async function postData() {
      const url = "http://localhost:8080/signup";

      try {
        const response = await fetch(url, {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken(),
          },
          body: JSON.stringify({
            email: data.email,
            password: data.password,
            roles: [1],
          }),
        });

        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }

        // const json = await response.json();
        // setBooks(json);
        // console.log(json);
        console.log(response);
        navigate("/");
      } catch (error) {
        console.error(error.message);
        // sessionStorage.clear();
        // navigate("/login");
      }
    }

    postData();
  };

  return (
    <div className="row">
      <div className="col">
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

          <div className="mb-3">
            <label
              htmlFor="email"
              className="form-label"
            >
              Password:
            </label>
            <input
              type="password"
              name="password"
              id="password"
              className="form-control"
              {...register("password")}
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
  );
}
