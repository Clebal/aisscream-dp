
package controllers;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.UserService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ListActorControllerTest extends AbstractTest {

	private MockMvc			mockMvc;

	@Autowired
	@Mock
	private UserService		userServiceMock;

	@Autowired
	@InjectMocks
	private ActorController	actorController;


	@Before
	public void beforeTest() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.actorController).build();
	}

	@Test
	public void listActorControllerTest() throws Exception {
		Integer tamUsers;

		super.authenticate(null);

		tamUsers = this.userServiceMock.findAllPaginated(1, 5).size();

		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/actor/list.do?page=1"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("actor/list"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("actor/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("users", Matchers.hasSize(tamUsers)))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"users",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("address", Matchers.is("Calle Sin Número, 123")), Matchers.hasProperty("email", Matchers.is("user1@acme.com")), Matchers.hasProperty("name", Matchers.is("Alejandro")),
						Matchers.hasProperty("phone", Matchers.is("+34618396001")), Matchers.hasProperty("surname", Matchers.is("Martínez ruiz"))))))
			.andExpect(
				MockMvcResultMatchers.model().attribute(
					"users",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("address", Matchers.is("Calle Falsa, 123")), Matchers.hasProperty("email", Matchers.is("user2@acme.com")), Matchers.hasProperty("name", Matchers.is("Luis")),
						Matchers.hasProperty("surname", Matchers.is("López González"))))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("users",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("email", Matchers.is("user3@acme.com")), Matchers.hasProperty("name", Matchers.is("María")), Matchers.hasProperty("surname", Matchers.is("García Trinidad"))))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("users",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("email", Matchers.is("user4@acme.com")), Matchers.hasProperty("name", Matchers.is("Sergio")), Matchers.hasProperty("surname", Matchers.is("Sánchez Ortiz"))))))
			.andExpect(
				MockMvcResultMatchers.model().attribute("users",
					Matchers.hasItem(Matchers.allOf(Matchers.hasProperty("email", Matchers.is("user5@acme.com")), Matchers.hasProperty("name", Matchers.is("Pepe")), Matchers.hasProperty("surname", Matchers.is("Casillas Martín"))))));

	}

}
