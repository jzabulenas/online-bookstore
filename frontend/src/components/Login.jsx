import { useForm } from "react-hook-form";
import csrfToken from "../util/getCsrfToken";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm();
  const navigate = useNavigate();

  const onSubmit = (data) => {
    async function postData() {
      const url = "http://localhost:8080/login";

      try {
        const response = await fetch(url, {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "X-XSRF-TOKEN": csrfToken(),
          },
          body: new URLSearchParams({
            username: data.email,
            password: data.password,
          }).toString(),
        });

        if (response.status === 401) {
          setError("root.serverError", {
            type: response.status,
          });
        }

        // if (!response.ok) {
        //   throw new Error(`Response status: ${response.status}`);
        // }

        if (response.status === 200) {
          navigate("/oauth2/redirect");
        }
      } catch (error) {
        console.log("This is caught");
        console.error(error.message);
      }
    }

    postData();
  };

  return (
    <>
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
                htmlFor="password"
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

            {errors.root?.serverError?.type === 401 && (
              <p className="text-danger">Username or password is incorrect.</p>
            )}

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
