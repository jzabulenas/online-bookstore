package lt.techin.bookreservationapp.services;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationService {

	public static String processFieldErrors(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			StringBuilder errorMessageBuilder = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();

			for (int i = 0; i < fieldErrors.size(); i++) {
				FieldError fieldError = fieldErrors.get(i);
				errorMessageBuilder.append(fieldError.getDefaultMessage());
				if (i < fieldErrors.size() - 1) {
					errorMessageBuilder.append(" | ");
				}
			}

			String errorMessage = errorMessageBuilder.toString();
			return errorMessage;
		}

		return null;
	}

}
