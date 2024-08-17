package hiperium.cities.commons.dto;

/**
 * The ErrorResponse class represents the error response returned by the system.
 * It contains information about the error, such as the error code, status, message, and details.
 * This class is a record, meaning that it is an immutable data class with
 * automatically generated constructor, accessor methods, and other utility methods.
 * <p>
 * The ErrorResponse class has the following parameters:
 * @param errorCode An integer representing the error code.
 * @param errorStatus A string representing the error status.
 * @param errorMessage A string representing the error message.
 */
public record ErrorResponse(
    Integer errorCode,
    String errorStatus,
    String errorMessage) {

    /**
     * The Builder class is responsible for creating instances of the ErrorResponse class.
     * It provides methods for setting the error code, status, message, and details.
     */
    public static class Builder {
        private Integer errorCode;
        private String errorStatus;
        private String errorMessage;

        /**
         * Creates a new instance of the Builder class.
         */
        public Builder() {
            // Default constructor
        }

        /**
         * Sets the error code.
         *
         * @param errorCode The error code.
         * @return The Builder instance.
         */
        public Builder errorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        /**
         * Sets the error status.
         *
         * @param errorStatus The error status.
         * @return The Builder instance.
         */
        public Builder errorStatus(String errorStatus) {
            this.errorStatus = errorStatus;
            return this;
        }

        /**
         * Sets the error message.
         *
         * @param errorMessage The error message.
         * @return The Builder instance.
         */
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        /**
         * Builds an instance of the ErrorResponse class.
         *
         * @return The ErrorResponse instance.
         */
        public ErrorResponse build() {
            return new ErrorResponse(errorCode, errorStatus, errorMessage);
        }
    }
}
