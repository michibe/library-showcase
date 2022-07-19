package org.michibe.libraryshowcase.configuration.db;

import org.michibe.libraryshowcase.api.model.CreateBookReqPayload;
import org.michibe.libraryshowcase.api.model.CreateCategoryReqPayload;
import org.michibe.libraryshowcase.api.model.CreateUserReqPayload;
import org.michibe.libraryshowcase.api.model.UpdateBookReqPayload;
import org.michibe.libraryshowcase.modules.library.BookService;
import org.michibe.libraryshowcase.modules.library.CategoryService;
import org.michibe.libraryshowcase.modules.user.UserService;
import org.michibe.libraryshowcase.modules.user.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("!test")
public class PlaygroundDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaygroundDataProvider.class);
    private final UserService userService;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final String adminEmail;
    private final String adminPassword;
    private final boolean addPlaygroundData;

    public PlaygroundDataProvider(
            UserService userService,
            BookService bookService,
            CategoryService categoryService,
            @Value("${library-showcase.user.admin.email}")
            String adminEmail,
            @Value("${library-showcase.user.admin.password}")
            String adminPassword,
            @Value("${library-showcase.add-playground-data}")
            boolean addPlaygroundData) {
        this.userService = userService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.addPlaygroundData = addPlaygroundData;
    }

    @PostConstruct
    public void setupAdminUser() {
        final var adminUser = userService.getUserByEmail(adminEmail);

        if (adminUser.isEmpty()) {
            LOGGER.info("Creating admin user.");
            userService.createUser(new CreateUserReqPayload(
                    "Admin",
                    adminEmail,
                    adminPassword,
                    Role.CUSTOMER
            ));
        } else {
            LOGGER.info("Skip to create admin user, because admin already exist.");
        }
    }

    @PostConstruct
    public void setupPlaygroundData() {
        if (addPlaygroundData) {
            final var emailMax = "max-mustermann@gmail.com";
            final var playgroundUser = userService.getUserByEmail(emailMax);

            if (playgroundUser.isEmpty()) {

                userService.createUser(new CreateUserReqPayload(
                        "Max",
                        "max-mustermann@gmail.com",
                        "123",
                        Role.CUSTOMER
                ));

                userService.createUser(new CreateUserReqPayload(
                        "Inge",
                        "inge-mustermann@gmail.com",
                        "53453",
                        Role.CUSTOMER
                ));

                userService.createUser(new CreateUserReqPayload(
                        "Harry",
                        "harry-potter@gmail.com",
                        "777566",
                        Role.CUSTOMER
                ));


                categoryService.createCategory(new CreateCategoryReqPayload(
                        "Adventure",
                        "Very interesting"
                ));

                categoryService.createCategory(new CreateCategoryReqPayload(
                        "Horror",
                        "Very creepy"
                ));

                final var trainingCategory = categoryService.createCategory(new CreateCategoryReqPayload(
                        "Training",
                        "Very instructive"
                ));

                categoryService.createCategory(new CreateCategoryReqPayload(
                        "ScienceFiction",
                        "Very unreal"
                ));


                bookService.createBook(new CreateBookReqPayload(
                                "Clean Code",
                                "Robert C. Martin",
                                "Pearson Education",
                                2008,
                                trainingCategory.id()
                        )
                );

                bookService.createBook(new CreateBookReqPayload(
                                "Design Patterns mit Java",
                                "Florian Siebler",
                                "Hanser",
                                2001,
                                trainingCategory.id()
                        )
                );

                bookService.createBook(new CreateBookReqPayload(
                                "Harry Potter and the Philosopher's Stone",
                                "Joanne K. Rowling",
                                "Bloomsbury Publishing",
                                1997,
                                null
                        )
                );

                final var book = bookService.createBook(
                        new CreateBookReqPayload(
                                "Harry Potter and the Chamber of Secrets",
                                "Joanne K. Rowling",
                                "Bloomsbury Publishing",
                                2000,
                                null
                        )
                );
            }
        }
    }

}
