import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

public class RegistrarUsuarioTest {

	protected WebDriver driver;

	@BeforeEach
	public void createDriver() {
		driver = WebDriverManager.chromedriver().create();
		driver.get("http://automationexercise.com");
	}

	private void clicar(By by) {
		WebElement elemento = driver.findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
	}

	private void preencher(String id, String valor) {
		driver.findElement(By.id(id)).sendKeys(valor);
	}

	private String repetir(String texto, int vezes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < vezes; i++) {
			sb.append(texto);
		}
		return sb.toString();
	}

	private void registrar(String nome) {
		String email = "aluno" + System.currentTimeMillis() + "@teste.com";

		assertEquals("Automation Exercise", driver.getTitle());
		clicar(By.cssSelector("a[href='/login']"));
		assertEquals("New User Signup!", driver.findElement(By.cssSelector(".signup-form h2")).getText());

		driver.findElement(By.cssSelector("input[data-qa='signup-name']")).sendKeys(nome);
		driver.findElement(By.cssSelector("input[data-qa='signup-email']")).sendKeys(email);
		clicar(By.cssSelector("button[data-qa='signup-button']"));
		assertEquals("ENTER ACCOUNT INFORMATION", driver.findElement(By.cssSelector(".login-form h2 b")).getText());

		clicar(By.id("id_gender1"));
		preencher("password", "Senha@123");
		new Select(driver.findElement(By.id("days"))).selectByValue("10");
		new Select(driver.findElement(By.id("months"))).selectByValue("5");
		new Select(driver.findElement(By.id("years"))).selectByValue("2000");
		clicar(By.id("newsletter"));
		clicar(By.id("optin"));

		preencher("first_name", "Maria");
		preencher("last_name", "Silva");
		preencher("company", "Faculdade");
		preencher("address1", "Rua A, 123");
		preencher("address2", "Apto 4");
		new Select(driver.findElement(By.id("country"))).selectByValue("Canada");
		preencher("state", "Ontario");
		preencher("city", "Toronto");
		preencher("zipcode", "12345");
		preencher("mobile_number", "11999999999");
		clicar(By.cssSelector("button[data-qa='create-account']"));

		assertEquals("ACCOUNT CREATED!", driver.findElement(By.cssSelector("h2[data-qa='account-created']")).getText());
		clicar(By.cssSelector("a[data-qa='continue-button']"));

		assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logged in as')]")).getText().contains(nome));
		clicar(By.cssSelector("a[href='/delete_account']"));

		assertEquals("ACCOUNT DELETED!", driver.findElement(By.cssSelector("h2[data-qa='account-deleted']")).getText());
		clicar(By.cssSelector("a[data-qa='continue-button']"));
	}

	@Test
	public void nomeNormal() {
		registrar("Maria");
	}

	@Test
	public void nomeMinimoUmCaractere() {
		registrar("M");
	}

	@Test
	public void nomeLongo() {
		registrar(repetir("a", 50));
	}

	@Test
	public void nomeComEspaco() {
		registrar("Maria da Silva");
	}

	@Test
	public void nomeComNumeros() {
		registrar("Maria123");
	}

	@AfterEach
	public void quitDriver() {
		driver.quit();
	}

}
