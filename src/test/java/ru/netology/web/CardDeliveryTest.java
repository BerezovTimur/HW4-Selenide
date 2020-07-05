package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDeliveryTest {

    String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    String wrongDate = LocalDate.now().plusDays(-3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));


    @Test
    void shouldSubmitCorrectForm() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(byText("Успешно!")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldSubmitEmptyForm() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("");
        $("[name='phone']").setValue("");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='city']").getText();
        assertEquals("Поле обязательно для заполнения", getText);
    }

    @Nested
    @DisplayName("Тесты по графе город")

    @Test
    void shouldGetMistakeIfWrongCity() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Гатчина");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='city']").getText();
        assertEquals("Доставка в выбранный город недоступна", getText);
    }

    @Test
    void shouldGetMistakeIfNoCity() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue("date");
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='city']").getText();
        assertEquals("Поле обязательно для заполнения", getText);
    }

    @Test
    void shouldGetMistakeIfLatinNameCity() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Saint-Petersburg");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='city']").getText();
        assertEquals("Доставка в выбранный город недоступна", getText);
    }

    @Nested
    @DisplayName("Тесты по дате")

    @Test
    void shouldGetMistakeIfWrongDate() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(wrongDate);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='date'] span.input__sub").getText();
        assertEquals("Заказ на выбранную дату невозможен", getText);
    }

    @Nested
    @DisplayName("Тесты по ФИО")

    @Test
    void shouldSubmitIfSurnameHaveDash() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин-Солдат");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(byText("Успешно!")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldGetMistakeIfLatinName() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Ivan Brovkin");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='name'] span.input__sub").getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", getText);
    }

    @Nested
    @DisplayName("Тесты по номеру телефона")

    @Test
    void shouldGetMistakeIfNoPhoneNumber() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='phone'] span.input__sub").getText();
        assertEquals("Поле обязательно для заполнения", getText);
    }

    @Test
    void shouldGetMistakeIfNoPlusInPhoneNumber() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='phone'] span.input__sub").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", getText);
    }

    @Test
    void shouldGetMistakeIfShortNumber() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+7911000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='phone'] span.input__sub").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", getText);
    }

    @Test
    void shouldGetMistakeIfLongNumber() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+791100000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='phone'] span.input__sub").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", getText);
    }

    @Test
    void shouldGetMistakeIfLetterInNumber() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("ASDF");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $("[data-test-id='phone'] span.input__sub").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", getText);
    }

    @Nested
    @DisplayName("Тесты по checkbox")

    @Test
    void shouldGetMistakeIfDontClickCheckbox() {
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[class='button__text']").click();
        String getText = $("[role='presentation']").getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", getText);
    }




}
