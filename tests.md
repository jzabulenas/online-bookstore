I guess it is more important to match frontend tests, because this is what the user actually uses.

The thing is, if you call repositories in your backend tests, it may not be on par to frontend tests. Because frontend tests always call endpoints.

## Homepage

| Backend    | Mobile                  | Desktop                 |
| ---------- | ----------------------- | ----------------------- |
| Not needed | should display homepage | should display homepage |

## Sign up

| Backend                                                                                       | Mobile                                                                        | Desktop                                                                       |
| --------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------- |
| signup_whenUserSignsUp_thenReturn201AndBody (no verify email step)                            | should sign up                                                                | should sign up                                                                |
| signup_whenUserSignsUpWithAsBigPasswordAsPossible_thenReturn201AndBody (no verify email step) | should sign up with longest password possible                                 | should sign up with longest password possible                                 |
| signup_whenEmailIsNull_thenReturn400AndBody                                                   | should display an error message when email is null                            | should display an error message when email is null                            |
| signup_whenEmailIsTooShort_thenReturn400AndBody                                               | should display an error message when email is too short                       | should display an error message when email is too short                       |
| signup_whenEmailLocalPartIsTooLong_thenReturn400AndBody                                       | should display an error message when email is too long                        | should display an error message when email is too long                        |
| signup_whenEmailDomainPartIsTooLong_thenReturn400AndBody                                      |                                                                               |                                                                               |
| signup_whenEmailLocalPartAndDomainPartIsTooLong_thenReturn400AndBody                          |                                                                               |                                                                               |
| signup_whenEmailAlreadyExists_thenReturn400AndBody                                            | should display an error message when email already exists                     | should display an error message when email already exists                     |
| signup_whenPasswordIsNull_shouldReturn400AndBody                                              | should display an error message when password is null                         | should display an error message when password is null                         |
| signup_whenPasswordIsTooShort_shouldReturn400AndBody                                          | should display an error message when password is too short                    | should display an error message when password is too short                    |
| signup_whenPasswordIsTooLong_shouldReturn400AndBody                                           | should display an error message when password is too long                     | should display an error message when password is too long                     |
| signup_whenPasswordIsFoundToBeCompromised_thenReturn400AndBody                                | should display an error message when password is compromised                  | should display an error message when password is compromised                  |
| Not needed                                                                                    | should display an error message when confirm password is null                 | should display an error message when confirm password is null                 |
| Not needed                                                                                    | should display an error message when confirm password does not match password | should display an error message when confirm password does not match password |

## Log in

| Backend | Mobile                                                              | Desktop                                                             |
| ------- | ------------------------------------------------------------------- | ------------------------------------------------------------------- |
|         | should log in, when correct credentials are provided                | should log in, when correct credentials are provided                |
|         | should display error message, when log in credentials are incorrect | should display error message, when log in credentials are incorrect |

## Book generation

| Backend                                                              | Mobile                                                                  | Desktop                                                                 |
| -------------------------------------------------------------------- | ----------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| generateBooks_whenBookIsGenerated_thenReturn200AndListOfBooks        | should generate 3 books to read when provided input                     | should generate 3 books to read when provided input                     |
| generateBooks_whenUserBooksExist_thenNotSeeThemInNewlyGeneratedBooks | should not generate particular books if they were liked before          | should not generate particular books if they were liked before          |
| generateBooks_whenMessageIsNull_thenReturn400AndMessage              | should display an error when book field input is null                   | should display an error when book field input is null                   |
| generateBooks_whenMessageIsTooShort_thenReturn400AndMessage          | should display an error when book field input is too short              | should display an error when book field input is too short              |
| generateBooks_whenMessageIsTooLong_thenReturn400AndMessage           | should display an error when book field input is too long               | should display an error when book field input is too long               |
| generateBooks_whenUnauthenticated_thenReturn401                      | Can't replicate                                                         |                                                                         |
| generateBooks_whenAuthenticatedButNoCSRF_thenReturn403AndBody        | Can't replicate                                                         |                                                                         |
| generateBooks_whenCalledMoreThan6Times_thenReturn429AndBody          | should display error message when books are generated more than 6 times | should display error message when books are generated more than 6 times |

## Saved books

| Backend                                                                 | Mobile                                                                                           | Desktop         |
| ----------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ | --------------- |
| saveUserBook_whenBookIsSaved_thenReturn201AndBody                       | Can't replicate                                                                                  | Can't replicate |
| saveUserBook_whenTitleAlreadyExistsForOtherUser_thenReturn201AndMessage | Can't replicate                                                                                  | Can't replicate |
| saveUserBook_whenTitleAlreadyExistsForUser_thenReturn400AndMessage      | Can't replicate                                                                                  | Can't replicate |
| saveUserBook_whenUnauthenticatedCalls_thenReturn401                     | Can't replicate                                                                                  | Can't replicate |
| saveUserBook_whenAuthenticatedButNoCSRF_thenReturn403AndBody            | Can't replicate                                                                                  | Can't replicate |
| getUserBooks_whenCalled_thenReturnBooksAnd200                           | Can't replicate                                                                                  | Can't replicate |
| getUserBooks_whenOneUserHasBooks_thenOtherUserHasNoneAnd200             | should click like on generated books, and not see them displayed in 'saved books' for other user |
| getUserBooks_whenListEmpty_thenReturnEmptyListAnd200                    |
| getUserBooks_whenUnauthenticated_thenReturn401AndNoBody                 |

| Backend | Mobile                                                                              | Desktop |
| ------- | ----------------------------------------------------------------------------------- | ------- |
|         | should click like on a single generated book, and see it displayed in 'saved books' |         |
|         | should click like on two generated books, and see them displayed in 'saved books'   |         |
|         | should click like on three generated books, and see them displayed in 'saved books' |         |
|         | should not see liked books if no books are liked after generating books             |         |
