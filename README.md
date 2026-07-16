# MaisonBackend

Spring Boot backend for the Maison hostel management system.

## Requirements

- Java 17
- PostgreSQL
- Maven, or the included Maven wrapper

## Run the Application

1. Start PostgreSQL and make sure a database named `maison_db` exists.

2. Check the database settings in `src/main/resources/application.properties`.

	```properties
	spring.datasource.url=jdbc:postgresql://localhost:5432/maison_db
	spring.datasource.username=postgres
	spring.datasource.password=yeezyszn
	```

	Update the username and password if your local PostgreSQL setup is different.

3. Start the application from the project root.

	On Windows:

	```powershell
	mvnw.cmd spring-boot:run
	```

	On macOS/Linux:

	```bash
	./mvnw spring-boot:run
	```

4. Open the backend in your browser or API client.

	- Swagger UI: `http://localhost:8080/swagger-ui.html`
	- OpenAPI docs: `http://localhost:8080/v3/api-docs`

## Build the Project

If you want to compile the project without running it:

```powershell
mvnw.cmd clean install
```

On macOS/Linux:

```bash
./mvnw clean install
```

## Notes

- JPA is configured with `ddl-auto=update`, so tables are created or updated automatically on startup.
- The project uses Spring Boot, Spring Data JPA, PostgreSQL, Lombok, and Springdoc OpenAPI.