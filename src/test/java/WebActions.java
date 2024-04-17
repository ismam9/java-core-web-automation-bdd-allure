import config.Log;
import config.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class WebActions extends WebDriver {

    public static boolean access(String urlAddress){
        try {
            driver.get(urlAddress);
            return true;
        } catch (Exception e) {
            String errorMessage = "[ERROR] Failed to access URL: " + urlAddress + ". Exception: " + e.getMessage();
            Log.register(errorMessage);
            return false;
        }
    }

    /**
     * Método que envía una cadena de texto a un elemento.
     *
     * @param elm   Elemento al cual se enviará la cadena de texto. Generalmente un
     *              input o textarea.
     * @param texto Cadena de texto que se enviará al elemento.
     */
    public static void sendKeys(WebElement elm, String texto) {
        String msg;
        try {
            elm.clear();
            elm.sendKeys(texto);
            msg = "[OK] " + ": \"" + texto + "\" enviado correcto a: " + elm.toString();
        } catch (Exception ex) {
            msg = "[ERROR] " + ": No se ha podido enviar \"" + texto + "\" al elemento " + elm.toString();
        }
        Log.register(msg);
    }

    /**
     * Método que ejecuta un click sobre un elemento.
     *
     * @param elm Elemento al cual se quiere hacer click.
     */
    public static void click(WebElement elm) {
        String msg;
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(elm).click().perform();
            msg = "[OK] " + ": Click correcto en: " + elm.toString();
        } catch (Exception e) {
            msg = "[ERROR] " + ": Fallo al dar click: " + elm.toString();
        }
        Log.register(msg);
    }

    /**
     * Metodo que espera a que un elemento sea visible. Este metodo recibe un
     * WebElement. Se recomienda el uso de este metodo para elementos que existen en
     * el DOM, aunque no sean visibles. Si son elementos generados dinamicamente, se
     * recomienda utilizar los metodos que utilizan xpath o ID.
     *
     * @param elm Objeto WebElement sobre el cual se quiere esperar su visibilidad.
     */
    public static void waitForVisibility(WebElement elm) {
        String msg;
        int espera = 35;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(espera));
            wait.until(ExpectedConditions.visibilityOf(elm));
            msg = ("[OK] " + ": Espera OK. Elemento " + elm.toString() + " visible");
        } catch (Exception ex) {
            msg = ("[WARNING] " + ": Tiempo de espera superado. Elemento no visible.");
            msg = msg.replace("[WARNING]", "<span style=\"color:orange; font-weight: bold;\"\\>[WARNING]</span>");
        }
        Log.register(msg);
    }

    public static void waitTillPageIsLoaded(){
        String msg;
        int espera = 35;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(espera));
            ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            wait.until(expectation);
            msg = ("[OK] " + ": SE HA ESPERADO CORRECTAMENTE HASTA QUE LA WEB ESTA CARGADA");
            waitSecs(2);
        } catch (Exception ex) {
            msg = ("[WARNING] " + ": Tiempo de espera superado. Elemento no visible.");
            msg = msg.replace("[WARNING]", "<span style=\"color:orange; font-weight: bold;\"\\>[WARNING]</span>");
        }
        Log.register(msg);
    }

    /**
     * Metodo que espera a que un elemento sea clickable. Este metodo recibe un
     * WebElement. Se recomienda el uso de este metodo para elementos que existen en
     * el DOM, aunque no sean visibles. Si son elementos generados dinamicamente,
     * cuando este metodo busque ese WebElement, si no existe en el DOM, retornara
     * una NoSuchElementException.
     *
     * @param elm Objeto WebElement sobre el cual se quiere esperar para hacer click
     *            sobre el.
     */
    public static void waitAndClick(WebElement elm) {
        String msg;
        int espera = 35;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(espera));
            wait.until(ExpectedConditions.elementToBeClickable(elm));
            msg = ("[OK] " + ": Elemento clickable " + elm.toString() + " visible");
            waitSecs(3);
            elm.click();
        } catch (Exception ex) {

            msg = ("[WARNING] " + ": Tiempo de espera superado (" + espera + "seg.). Elemento se mantiene visible.");
            msg = msg.replace("[WARNING]", "<span style=\"color:orange; font-weight: bold;\"\\>[WARNING]</span>");
        }
        Log.register(msg);
    }

    /**
     * Metodo que espera a que un elemento sea clickable. Este metodo recibe un
     * xpath. Se recomienda el uso de este metodo para elementos generados
     * dinamicamente en el DOM.
     *
     * @param xpath Localizador del elemento sobre el cual se quiere esperar para
     *              hacer click.
     */
    public static void waitAndClick(String xpath) {
        String msg;
        int espera = 35;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(espera));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            msg = ("[OK] " + ": Elemento clickable " + xpath + " visible");
            waitSecs(3);
            driver.findElement(By.xpath(xpath)).click();
        } catch (Exception ex) {

            msg = ("[ERROR] " + ": Tiempo de espera superado. Elemento no clickable.");
        }
        Log.register(msg);
    }

    public static void waitSecs(int secs) {
        String msg;
        try {
            Thread.sleep(secs * 1000L);
            msg = ("[OK] " + ": Espera de " + secs + " seg. realizada.");

        } catch (Exception ex) {
            msg = ("[ERROR] " + ": Error al realizar la espera.");

        }
        Log.register(msg);
    }
    public static void waitSecsRandomize(int min, int max) {
        int random = (int) (Math.random() * (max - min) + min);
        String msg;
        try {
            Thread.sleep(random * 1000L);
            msg = ("[OK] " + ": Espera de " + random + " seg. realizada.");

        } catch (Exception ex) {
            msg = ("[ERROR] " + ": Error al realizar la espera.");

        }
        Log.register(msg);
    }

    /** SCROLLS */
    public static void scrollTop() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollTop)");
            Log.register("[OK][" + Thread.currentThread().getStackTrace()[1].getClassName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                    + ": Page was scrolled to the top");
        } catch (Exception e) {
            Log.register("[ERROR][" + Thread.currentThread().getStackTrace()[1].getClassName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                    + ": Page wasn't scrolled to the top: " + e.getMessage());
        }
    }

    /**
     * Metodo que ejecuta un desplazamiento al tope inferior de la pagina.
     */
    public static void scrollBottom() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
            Log.register("[OK][" + Thread.currentThread().getStackTrace()[1].getClassName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                    + ": Page was scrolled to the bottom");
        } catch (Exception e) {
            Log.register("[ERROR][" + Thread.currentThread().getStackTrace()[1].getClassName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                    + ": Page wasn't scrolled to the bottom: " + e.getMessage());
        }
    }

    /**
     * Metodo que ejecuta un desplazamiento de la pagina hasta la ubicacion del
     * elemento indicado.
     *
     * @param elm WebElement hacia el cual se quiere hacer el desplazamiento.
     * //@see https://developer.mozilla.org/es/docs/Web/API/Element/scrollIntoView
     */
    public static void scrollTo(WebElement elm) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'end', behavior:'smooth'});", elm);
            Log.register("[OK][" + Thread.currentThread().getStackTrace()[1].getClassName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                    + ": Page was scrolled to the element");
        } catch (Exception e) {
            Log.register("[ERROR][" + Thread.currentThread().getStackTrace()[1].getClassName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]" + "["
                    + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                    + ": Page wasn't scrolled to the element: " + e.getMessage());
        }
    }

    /**
     * Metodo que comprueba si un elemento existe en el DOM. Recibe un xpath para
     * obtener la cantidad de elementos con esas caracteristicas y con ello evaluar
     * su existencia.
     *
     * @param xpath Localizador del elemento.
     */
    public static boolean exists(String xpath) {
        String msg = "";
        try {
            int cant = driver.findElements(By.xpath(xpath)).size();

            if (cant == 0) {
                msg = ("[OK] " + ": Busqueda correcta. El elemento no existe.");
                return false;
            } else {
                msg = ("[OK] " + ": Busqueda correcta. El elemento existe (" + cant + ")");
                return true;
            }

        } catch (Exception ex) {
            msg = ("[ERROR] " + ": Error al buscar existencia del elemento");
            return false;
        } finally {
            Log.register(msg);
        }

    }

    /**
     * Extras de acciones con los elementos
     *
     * */

    public static void lookupElement(WebElement elm) throws Exception {
        try {
            assertTrue(elm.isDisplayed());
            Log.register("[OK][" + WebActions.class.getSimpleName() + "][El elemento " + elm.toString() + " existe]");
        } catch (Exception ex) {
            Log.register(
                    "[ERROR][" + WebActions.class.getSimpleName() + "][El elemento " + elm.toString() + " no existe");
            throw new Exception("Elemento no existe");
        }
    }
}
