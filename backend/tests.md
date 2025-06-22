# Rest Assured end to end tests

## BookController

- generateBooks
  - Happy path
    - generateBooks_whenBookIsGenerated_thenReturn200AndListOfBooks
    - generateBooks_whenUserBooksExist_thenNotSeeThemInNewlyGeneratedBooks
    - generateBooks_whenCalledTwiceWithSameInput_thenResultsShouldDiffer
  - Unhappy path
    - generateBooks_whenMessageIsNull_thenReturn400AndMessage
    - generateBooks_whenMessageIsTooShort_thenReturn400AndMessage
    - generateBooks_whenMessageIsTooLong_thenReturn400AndMessage
    - generateBooks_whenUnauthenticated_thenReturn401
    - generateBooks_whenAuthenticatedButNoCSRF_thenReturn403AndBody
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
  - Unhappy path
    - getUserBooks_whenListEmpty_thenReturnEmptyListAnd200
    - getUserBooks_whenUnauthenticated_thenReturn401AndNoBody
