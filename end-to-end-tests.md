I guess it is more important to match frontend tests, because this is what the user actually uses.

The thing is, if you call repositories in your backend tests, it may not be on par to frontend tests. Because frontend tests always call endpoints.

## Homepage

| Backend    | Mobile                  | Desktop                 |
| ---------- | ----------------------- | ----------------------- |
| Not needed | should display homepage | should display homepage |

## Sign up

| Backend                                                         | Mobile                                                                                                              | Desktop                                                                                                             |
| --------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- |
| whenUserSignsUp_thenReturn201AndBody                            | should sign up                                                                                                      | should sign up                                                                                                      |
| whenUserSignsUpWithAsBigPasswordAsPossible_thenReturn201AndBody | should sign up with longest password possible                                                                       | should sign up with longest password possible                                                                       |
| whenEmailIsNull_thenReturn400AndBody                            | should display an error message when email is null                                                                  | should display an error message when email is null                                                                  |
| whenEmailIsTooShort_thenReturn400AndBody                        | should display an error message when email is too short                                                             | should display an error message when email is too short                                                             |
| whenEmailLocalPartIsTooLong_thenReturn400AndBody                | ⬇️ (nowhere near as thorough as backend, but that's all I have)                                                     | ⬇️ (nowhere near as thorough as backend, but that's all I have)                                                     |
| whenEmailDomainPartIsTooLong_thenReturn400AndBody               | should display an error message when email is too long (nowhere near as thorough as backend, but that's all I have) | should display an error message when email is too long (nowhere near as thorough as backend, but that's all I have) |
| whenEmailLocalPartAndDomainPartIsTooLong_thenReturn400AndBody   | ⬆️ (nowhere near as thorough as backend, but that's all I have)                                                     | ⬆️ (nowhere near as thorough as backend, but that's all I have)                                                     |
| whenEmailAlreadyExists_thenReturn400AndBody                     | should display an error message when email already exists                                                           | should display an error message when email already exists                                                           |
| whenPasswordIsNull_shouldReturn400AndBody                       | should display an error message when password is null                                                               | should display an error message when password is null                                                               |
| whenPasswordIsTooShort_shouldReturn400AndBody                   | should display an error message when password is too short                                                          | should display an error message when password is too short                                                          |
| whenPasswordIsTooLong_shouldReturn400AndBody                    | should display an error message when password is too long                                                           | should display an error message when password is too long                                                           |
| whenPasswordIsFoundToBeCompromised_thenReturn400AndBody         | should display an error message when password is compromised                                                        | should display an error message when password is compromised                                                        |
| Not needed                                                      | should display an error message when confirm password is null                                                       | should display an error message when confirm password is null                                                       |
| Not needed                                                      | should display an error message when confirm password does not match password                                       | should display an error message when confirm password does not match password                                       |

## Log in

| Backend | Mobile                                                              | Desktop                                                             |
| ------- | ------------------------------------------------------------------- | ------------------------------------------------------------------- |
| NEED    | should log in, when correct credentials are provided                | should log in, when correct credentials are provided                |
| NEED    | should display error message, when log in credentials are incorrect | should display error message, when log in credentials are incorrect |

## Generate books

| Backend                                                             | Mobile                                                                  | Desktop                                                                 |
| ------------------------------------------------------------------- | ----------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| whenBooksAreGenerated_thenReturn200AndListOfBooks                   | should generate 3 books to read when provided input                     | should generate 3 books to read when provided input                     |
| whenUserHasBooksTheyLikedBefore_thenNotSeeThemInNewlyGeneratedBooks | should not generate particular books if they were liked before          | should not generate particular books if they were liked before          |
| whenMessageIsNull_thenReturn400AndMessage                           | should display an error when book field input is null                   | should display an error when book field input is null                   |
| whenMessageIsTooShort_thenReturn400AndMessage                       | should display an error when book field input is too short              | should display an error when book field input is too short              |
| whenMessageIsTooLong_thenReturn400AndMessage                        | should display an error when book field input is too long               | should display an error when book field input is too long               |
| whenUnauthenticated_thenReturn401                                   | Can't replicate                                                         | Can't replicate                                                         |
| whenAuthenticatedButNoCSRF_thenReturn403AndBody                     | Can't replicate                                                         | Can't replicate                                                         |
| whenBookGenerationIsCalledMoreThan6Times_thenReturn429AndBody       | should display error message when books are generated more than 6 times | should display error message when books are generated more than 6 times |

## Like books (Calling it "Save books" would probably have been better, because "liking" actually just saves it for you, so you can later find it)

| Backend                                                                             | Mobile                                          | Desktop                                         |
| ----------------------------------------------------------------------------------- | ----------------------------------------------- | ----------------------------------------------- |
| whenOneBookIsLiked_thenReturn201AndBody                                             |                                                 |                                                 |
| whenTwoBooksAreLiked_thenReturn201AndBody                                           |                                                 |                                                 |
| whenThreeBooksAreLiked_thenReturn201AndBody                                         |                                                 |                                                 |
| whenBookIsAlreadyLikedByOtherUserAndILikeSameBookForNewUser_thenReturn201AndMessage | Can't replicate                                 | Can't replicate                                 |
| whenBookIsLikedForCurrentUserAndITryToLikeItAgain_thenReturn400AndMessage           | Can't replicate (clicking again shows no error) | Can't replicate (clicking again shows no error) |
| whenUnauthenticatedTriesCalling_thenReturn401                                       | Can't replicate                                 | Can't replicate                                 |
| whenAuthenticatedTriesCallingButNoCSRF_thenReturn403AndBody                         | Can't replicate                                 | Can't replicate                                 |

## Saved books

| Backend                                                                       | Mobile                                                                                           | Desktop                                                                                          |
| ----------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------ |
| whenOneUserHasBooksTheySaved_thenOtherUserHasNoneAnd200                       | should click like on generated books, and not see them displayed in 'saved books' for other user | should click like on generated books, and not see them displayed in 'saved books' for other user |
