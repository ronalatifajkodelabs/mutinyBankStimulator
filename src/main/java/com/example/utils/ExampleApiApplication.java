package com.example.utils;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
            tags = {
                    @Tag(name="widget", description="Widget operations."),
                    @Tag(name="gasket", description="Operations related to gaskets")
            },
            info = @Info(
                    title="OpenAPI for Quarkus Bank Stimulator",
                    version = "1.0.1",
                    contact = @Contact(
                            name = "Postman request workspace",
                            url = "https://www.postman.com/ronalatifajkodelabs/workspace/bankstimulator/collection/34371957-223626db-3068-491c-9bfc-e19855024e83?action=share&creator=34371957"),
                    license = @License(
                            name = "Amazon Corretto 21.0.2")
            )
    )
    public class ExampleApiApplication extends Application {
    }
