package org.example.trainsystem.dto;

    public class UserDTO {
        private String username;
        private String password;
        private String confirmPassword;
        private String name;
        private String email;
        private String userType;

        public UserDTO() {
        }

        public UserDTO(String username, String password, String confirmPassword, String name, String email, String userType) {
            this.username = username;
            this.password = password;
            this.confirmPassword = confirmPassword;
            this.name = name;
            this.email = email;
            this.userType = userType;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
// Getters and setters
    }


