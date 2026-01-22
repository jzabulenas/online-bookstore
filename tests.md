## Homepage

| Backend | Mobile                  | Desktop                 |
| ------- | ----------------------- | ----------------------- |
|         | should display homepage | should display homepage |

## Sign up

| Backend                                                                | Mobile                                                                        | Desktop                                                                       |
| ---------------------------------------------------------------------- | ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------- |
| signup_whenUserSignsUp_thenReturn201AndBody                            | should sign up                                                                | should sign up                                                                |
| signup_whenUserSignsUpWithAsBigPasswordAsPossible_thenReturn201AndBody | should sign up with longest password possible                                 | should sign up with longest password possible                                 |
| signup_whenEmailIsNull_thenReturn400AndBody                            | should display an error message when email is null                            | should display an error message when email is null                            |
| signup_whenEmailIsTooShort_thenReturn400AndBody                        | should display an error message when email is too short                       | should display an error message when email is too short                       |
| signup_whenEmailLocalPartIsTooLong_thenReturn400AndBody                | should display an error message when email is too long                        | should display an error message when email is too long                        |
| signup_whenEmailDomainPartIsTooLong_thenReturn400AndBody               |                                                                               |                                                                               |
| signup_whenEmailLocalPartAndDomainPartIsTooLong_thenReturn400AndBody   |                                                                               |                                                                               |
| signup_whenEmailAlreadyExists_thenReturn400AndBody                     | should display an error message when email already exists                     | should display an error message when email already exists                     |
| signup_whenPasswordIsNull_shouldReturn400AndBody                       | should display an error message when password is null                         | should display an error message when password is null                         |
| signup_whenPasswordIsTooShort_shouldReturn400AndBody                   | should display an error message when password is too short                    | should display an error message when password is too short                    |
| signup_whenPasswordIsTooLong_shouldReturn400AndBody                    | should display an error message when password is too long                     | should display an error message when password is too long                     |
| signup_whenPasswordIsFoundToBeCompromised_thenReturn400AndBody         | should display an error message when password is compromised                  | should display an error message when password is compromised                  |
|                                                                        | should display an error message when confirm password is null                 | should display an error message when confirm password is null                 |
|                                                                        | should display an error message when confirm password does not match password | should display an error message when confirm password does not match password |
