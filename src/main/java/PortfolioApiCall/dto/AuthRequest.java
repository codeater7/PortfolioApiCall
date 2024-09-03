package PortfolioApiCall.dto;

import PortfolioApiCall.Config.EnvVariableReader;
import lombok.Getter;
import lombok.Lombok;

/*
 * As part of GraphQl learning.
 * Outside of requirement.
 */
public class AuthRequest {
        Lombok lombok;
        EnvVariableReader envVariableReader;


        @Getter
        private String username;
        @Getter
        private String password;

        /*
        private AuthRequest(String username, String password){
                this.username = username;
                this.password = username;
        }

        public String getPassword(){
                return this.password;
        }
        public String getUsername(){
                return this.username;
        }

         */

}
