package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @Test
    void shouldSubmitForm(){
        open("http://localhost:9999/");
        $("[type='text'][placeholder]").setValue("Санкт-Петербург");
        $("[class='input__control'][type='tel'][placeholder]").setValue("20.07.2020");
        $("[name='name']").setValue("Иван Бровкин");
        $("[name='phone']").setValue("+79110000000");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(byText("Успешно!")).waitUntil(Condition.visible, 15000);
    }
}
