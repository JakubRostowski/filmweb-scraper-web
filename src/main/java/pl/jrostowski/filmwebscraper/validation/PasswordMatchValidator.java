package pl.jrostowski.filmwebscraper.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    private String password;
    private String confirmPassword;
    private String message;

    @Override
    public void initialize(final PasswordMatch constraintAnnotation) {
        password = constraintAnnotation.password();
        confirmPassword = constraintAnnotation.confirmPassword();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            final Object firstObj = new BeanWrapperImpl(value).getPropertyValue(password);
            final Object secondObj = new BeanWrapperImpl(value).getPropertyValue(confirmPassword);

            valid = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ignore) {}

        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(password)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}
