package com.capstone.BnagFer.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApi() {
        Server server = new Server().url("/");

        return new OpenAPI()
                .info(getInfo())
                .components(authSetting())
                .addServersItem(server)
                .addSecurityItem(new SecurityRequirement().addList("access-token"));
    }

    private Info getInfo() {
        License license = new License();
        license.setName("{Application}");

        return new Info()
                .title("방구석 퍼거슨 API Document")
                .description("방구석 퍼거슨 API document 입니다.")
                .version("v0.0.1")
                .license(license);
    }

    private Components authSetting() {
        return new Components()
                .addSecuritySchemes(
                        "access-token", //변경사항
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}
