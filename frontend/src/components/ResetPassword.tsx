import { useForm } from "react-hook-form";
import { useNavigate, useSearchParams } from "react-router-dom";
import csrfToken from "../util/getCsrfToken";

type ResetPasswordFormData = {
  password: string;
  confirmPassword: string;
};

export default function ResetPassword() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token") ?? "";
  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
    watch,
  } = useForm<ResetPasswordFormData>();
  const navigate = useNavigate();

  if (!token) {
    return <p className="text-danger">Invalid password reset link.</p>;
  }

  const onSubmit = (data: ResetPasswordFormData) => {
    async function postData() {
      const url = `${import.meta.env.VITE_BACKEND_BASE_URL}/reset-password`;

      try {
        const response = await fetch(url, {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken(),
          },
          body: JSON.stringify({ token, password: data.password }),
        });

        // 200 OK has no body, so .json() would throw — fall back to empty object
        const body = await response.json().catch(() => ({}));

        // body.token — invalid/expired token (InvalidPasswordResetTokenException)
        // body.detail — compromised password (CompromisedPasswordException via ProblemDetail)
        // body.password — password too short/long (ResetPasswordRequestDTO @Size)
        if (response.status === 400 && body.token) {
          setError("root.tokenError", {
            type: response.status.toString(),
            message: body.token,
          });

          return;
        }

        if (response.status === 400 && body.detail) {
          setError("root.serverErrorPassword", {
            type: response.status.toString(),
            message: body.detail,
          });

          return;
        }

        if (response.status === 400 && body.password) {
          setError("root.serverErrorPassword", {
            type: response.status.toString(),
            message: body.password,
          });

          return;
        }

        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }

        navigate("/reset-password-success");
      } catch (error) {
        if (error instanceof Error) console.error(error.message);
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
              htmlFor="password"
              className="form-label"
            >
              New password:
            </label>
            <input
              type="password"
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

          <div className="mb-3">
            <label
              htmlFor="confirm-password"
              className="form-label"
            >
              Confirm new password:
            </label>
            <input
              type="password"
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

          {errors.root?.tokenError?.type === "400" && (
            <p className="text-danger">{errors.root.tokenError.message}</p>
          )}
          {errors.root?.serverErrorPassword?.type === "400" && (
            <p className="text-danger">
              {errors.root.serverErrorPassword.message}
            </p>
          )}

          <button
            type="submit"
            className="btn btn-primary"
          >
            Reset password
          </button>
        </form>
      </div>
    </div>
  );
}
