import { useState } from "react";
import { useForm } from "react-hook-form";
import csrfToken from "../util/getCsrfToken";

type ForgotPasswordFormData = {
  email: string;
};

export default function ForgotPassword() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ForgotPasswordFormData>();
  const [submitted, setSubmitted] = useState(false);
  const [rateLimited, setRateLimited] = useState(false);

  const onSubmit = (data: ForgotPasswordFormData) => {
    async function postData() {
      const url = `${import.meta.env.VITE_BACKEND_BASE_URL}/forgot-password`;

      try {
        const response = await fetch(url, {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            "X-XSRF-TOKEN": csrfToken(),
          },
          body: JSON.stringify({ email: data.email }),
        });

        if (response.status === 429) {
          setRateLimited(true);
          return;
        }

        setSubmitted(true);
      } catch (error) {
        if (error instanceof Error) console.error(error.message);
      }
    }

    postData();
  };

  if (submitted) {
    return (
      <p>
        If an account with that email exists, a password reset link has been
        sent. Please check your inbox.
      </p>
    );
  }

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
          {rateLimited && (
            <p className="text-danger">
              Too many requests. Please try again later.
            </p>
          )}

          <button
            type="submit"
            className="btn btn-primary"
          >
            Send reset link
          </button>
        </form>
      </div>
    </div>
  );
}
