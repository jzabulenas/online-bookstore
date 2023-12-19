package lt.techin.bookreservationapp.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lt.techin.bookreservationapp.controllers.CategoryController;
import lt.techin.bookreservationapp.entities.Category;

@Component
public class DatabaseInitializer implements CommandLineRunner {

	private final CategoryController categoryController;

	public DatabaseInitializer(CategoryController categoryController) {
		this.categoryController = categoryController;
	}

	@Override
	public void run(String... args) throws Exception {
		Category category = new Category();
		category.setName("Arts & Photography");
//		categoryController.addCategory(category);
	}

}
