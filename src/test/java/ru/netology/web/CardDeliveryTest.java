package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDeliveryTest {

    String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));


    @Test
    void shouldSubmitCorrectForm(){
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(byText("Успешно!")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldSubmitEmptyForm(){
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("");
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("");
        $("[name='phone']").setValue("");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        String getText = $();
        assertEquals("Поле обязательно для заполнения", getText);
        //$(byText("Успешно!")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldGetMistakeIfWrongCity(){
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Гатчина");
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(byText("Доставка в выбранный город недоступна")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldGetMistakeIfNoCity(){
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").setValue("");
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(byText("Поле обязательно для заполнения")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldGetMistakeIfNoDate(){
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").setValue(date);
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(byText("Неверно введена дата")).waitUntil(Condition.visible, 15000);
    }

}
