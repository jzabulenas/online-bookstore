import { useForm } from "react-hook-form";
import csrfToken from "../util/getCsrfToken";
import { useNavigate } from "react-router-dom";

export default function Signup({ setIsSignedUp }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    watch,
  } = useForm();
  const navigate = useNavigate();

  const onSubmit = (data) => {
    async function postData() {
      const url = `${import.meta.env.VITE_BACKEND_BASE_URL}/signup`;

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

        const body = await response.json();

        if (
          response.status === 400 &&
          body.email === "Such email address is already in use"
        ) {
          setError("root.serverErrorEmail", {
            type: response.status,
            message: body.email,
          });
        }

        if (
          response.status === 400 &&
          body.detail ===
            "The provided password is compromised and cannot be used. Use something more unique"
        ) {
          setError("root.serverErrorPassword", {
            type: response.status,
            message: body.detail,
          });
        }

        // This if statement is needed, because it prevents "navigate" from activating if any server error occurs,
        // notably the root.serverError above
        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }

        setIsSignedUp(true);
        navigate("/");
      } catch (error) {
        console.error(error.message);
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
              {...register("email", {
                required: true,
                minLength: 7,
                maxLength: 255,
              })}
            />
          </div>
          {errors.email && errors.email.type === "required" && (
            <p className="text-danger">This field is required.</p>
          )}
          {errors.email && errors.email.type === "minLength" && (
            <p className="text-danger">
              Email must be at least 7 characters long.
            </p>
          )}
          {errors.email && errors.email.type === "maxLength" && (
            <p className="text-danger">
              Email must be at most 255 characters long.
            </p>
          )}
          {errors.root?.serverErrorEmail?.type === 400 && (
            <p className="text-danger">
              {errors.root?.serverErrorEmail?.message}
            </p>
          )}

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
              {...register("password", {
                required: true,
                minLength: 14,
                maxLength: 64,
              })}
            />
          </div>
          {errors.password && errors.password.type === "required" && (
            <p className="text-danger">This field is required.</p>
          )}
          {errors.password && errors.password.type === "minLength" && (
            <p className="text-danger">
              Password must be at least 14 characters long.
            </p>
          )}
          {errors.password && errors.password.type === "maxLength" && (
            <p className="text-danger">
              Password must be at most 64 characters long.
            </p>
          )}
          {errors.root?.serverErrorPassword?.type === 400 && (
            <p className="text-danger">
              {errors.root?.serverErrorPassword?.message}
            </p>
          )}

          <div className="mb-3">
            <label
              htmlFor="confirm-password"
              className="form-label"
            >
              Confirm password:
            </label>
            <input
              type="password"
              name="confirm-password"
              id="confirm-password"
              className="form-control"
              {...register("confirmPassword", {
                required: true,
                validate: (value) => {
                  if (watch("password") != value) {
                    return "Passwords do not match.";
                  }
                },
              })}
            />
          </div>
          {errors.confirmPassword &&
            errors.confirmPassword.type === "required" && (
              <p className="text-danger">This field is required.</p>
            )}
          {errors.confirmPassword &&
            errors.confirmPassword.type === "validate" && (
              <p className="text-danger">{errors.confirmPassword.message}</p>
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
  );
}
