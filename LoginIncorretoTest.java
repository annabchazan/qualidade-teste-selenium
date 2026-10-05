import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginIncorretoTest {

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

	private String repetir(String texto, int vezes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < vezes; i++) {
			sb.append(texto);
		}
		return sb.toString();
	}

	private void loginIncorreto(String email, String senha) {
		assertEquals("Automation Exercise", driver.getTitle());
		clicar(By.cssSelector("a[href='/login']"));
		assertEquals("Login to your account", driver.findElement(By.cssSelector(".login-form h2")).getText());
		driver.findElement(By.cssSelector("input[data-qa='login-email']")).sendKeys(email);
		driver.findElement(By.cssSelector("input[data-qa='login-password']")).sendKeys(senha);
		clicar(By.cssSelector("button[data-qa='login-button']"));
		assertEquals("Your email or password is incorrect!",
				driver.findElement(By.cssSelector(".login-form p")).getText());
	}

	@Test
	public void emailValidoNaoCadastrado() {
		loginIncorreto("naocadastrado@teste.com", "senha123");
	}

	@Test
	public void emailMinimoSenhaMinima() {
		loginIncorreto("a@b.co", "1");
	}

	@Test
	public void emailLongoSenhaLonga() {
		loginIncorreto(repetir("a", 64) + "@" + repetir("b", 60) + ".com", repetir("s", 100));
	}

	@Test
	public void emailComMaiusculas() {
		loginIncorreto("NAOCADASTRADO@TESTE.COM", "SENHA123");
	}

	@Test
	public void emailComCaracteresEspeciais() {
		loginIncorreto("nome.sobrenome+teste@teste.com", "!@#$%^&*()");
	}

	@AfterEach
	public void quitDriver() {
		driver.quit();
	}

}
