# End to end backend tests

## UserBookControllerTest

- generateBooks
    - Happy path
        - generateBooks_whenBookIsGenerated_thenReturn200AndListOfBooks
        - generateBooks_whenUserBooksExist_thenNotSeeThemInNewlyGeneratedBooks
        - generateBooks_whenCalledTwiceWithSameInput_thenResultsShouldDiffer - disabled, flaky
    - Unhappy path
        - generateBooks_whenMessageIsNull_thenReturn400AndMessage
        - generateBooks_whenMessageIsTooShort_thenReturn400AndMessage
        - generateBooks_whenMessageIsTooLong_thenReturn400AndMessage
        - generateBooks_whenUnauthenticated_thenReturn401
        - generateBooks_whenAuthenticatedButNoCSRF_thenReturn403AndBody
        - generateBooks_whenCalledMoreThan6Times_thenReturn429AndBody

---

- saveUserBook
    - Happy path
        - saveUserBook_whenBookIsSaved_thenReturn201AndBody
        - saveUserBook_whenTitleAlreadyExistsForOtherUser_thenReturn201AndMessage
    - Unhappy path
        - saveUserBook_whenTitleAlreadyExistsForUser_thenReturn400AndMessage
        - saveUserBook_whenUnauthenticatedCalls_thenReturn401
        - saveUserBook_whenAuthenticatedButNoCSRF_thenReturn403AndBody
- getUserBooks
    - Happy path
        - getUserBooks_whenCalled_thenReturnBooksAnd200
        - getUserBooks_whenOneUserHasBooks_thenOtherUserHasNoneAnd200
    - Unhappy path
        - getUserBooks_whenListEmpty_thenReturnEmptyListAnd200
        - getUserBooks_whenUnauthenticated_thenReturn401AndNoBody

## UserControllerTest

- signup
    - Happy path
        - signup_whenUserSignsUp_thenReturn201AndBody
        - signup_whenUserSignsUpWithAsBigPasswordAsPossible_thenReturn201AndBody
    - Unhappy path
        - Email
            - signup_whenEmailIsNull_thenReturn400AndBody
            - signup_whenEmailIsTooShort_thenReturn400AndBody
            - signup_whenEmailLocalPartIsTooLong_thenReturn400AndBody
            - signup_whenEmailDomainPartIsTooLong_thenReturn400AndBody
            - signup_whenEmailLocalPartAndDomainPartIsTooLong_thenReturn400AndBody
            - signup_whenEmailAlreadyExists_thenReturn400AndBody
        - Password
            - signup_whenPasswordIsNull_shouldReturn400AndBody
            - signup_whenPasswordIsTooShort_shouldReturn400AndBody
            - signup_whenPasswordIsTooLong_shouldReturn400AndBody
            - signup_whenPasswordIsFoundToBeCompromised_thenReturn400AndBody