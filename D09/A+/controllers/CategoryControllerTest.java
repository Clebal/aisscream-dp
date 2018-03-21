// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
// import org.springframework.test.context.web.WebAppConfiguration;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
// import services.CategoryService;
// import controllers.administrator.CategoryAdministratorController;
//
// @ContextConfiguration(locations = {
// "classpath:spring/junit.xml"
// })
// @RunWith(SpringJUnit4ClassRunner.class)
// @WebAppConfiguration
// public class CategoryControllerTest {
//
// private MockMvc mockMvc;
//
// // Controller to test
// @Autowired
// private CategoryAdministratorController categoryAdminController;
//
// @Autowired
// private CategoryService categoryService;
//
//
// @Before
// public void beforeTest() {
// this.mockMvc = MockMvcBuilders.standaloneSetup(this.categoryAdminController).build();
// }
//
// @Test
// public void shouldReturnHttpCode200OnGet() throws Exception {
//
// final Integer parentCategories;
//
// // parentCategories = this.categoryService.getParentCategories().size();
// //
// // this.mockMvc
// // .perform(MockMvcRequestBuilders.get("/category/admin/list"))
// // .andExpect(MockMvcResultMatchers.status().isOk())
// // .andExpect(MockMvcResultMatchers.view().name("category/list"))
// // .andExpect(MockMvcResultMatchers.forwardedUrl("category/list"))
// // .andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasSize(parentCategories)))
// // .andExpect(MockMvcResultMatchers.model().attribute("categories", Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("name", Matchers.is("Selva")), Matchers.hasProperty("description", Matchers.is("En la selva hay muchos suspensos"))))))
// // .andExpect(
// // MockMvcResultMatchers.model().attribute("categories", Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("name", Matchers.is("Playa")), Matchers.hasProperty("description", Matchers.is("Es a donde no iremos si suspendemos DP. "))))));
// // }
// }
//
//}
