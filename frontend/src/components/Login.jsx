import { useForm } from "react-hook-form";
import csrfToken from "../util/getCsrfToken";

export default function Login() {
  const { register, handleSubmit } = useForm();

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
          // redirect: "follow",
        });

        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }

        // const json = await response.json();
        // setBooks(json);
        // console.log(json);
        // console.log(
        //   new URLSearchParams({
        //     username: data.email,
        //     password: data.password,
        //   }).toString()
        // );

        console.log(response);
      } catch (error) {
        console.error(error.message);
        // sessionStorage.clear();
        // navigate("/login");
      }
    }

    postData();
  };

  return (
    <>
      {/* <div className="row">
        <div className="col">
          <a href="http://localhost:8080/login">Log in</a>
        </div>
      </div> */}

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
