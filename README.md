# JavaFX Address Book Demo

JavaFX-застосунок для управління адресною книгою з підтримкою SQLite бази даних, статистики (гістограми, кругова діаграма) та курсів валют НБУ.

## Залежності

| Бібліотека | Версія | Призначення |
|---|---|---|
| JavaFX | 24.0.1 | GUI-фреймворк (controls, fxml, graphics, base) |
| SQLite JDBC | 3.49.1.0 | Зберігання даних у SQLite (addressbook.db) |
| Gson | 2.10.1 | Парсинг JSON-відповідей API НБУ |
| JAXB API | 2.3.1 | Серіалізація/десеріалізація XML (збереження у файл) |
| JAXB Runtime | 2.3.9 | Реалізація JAXB (вилучена з JDK 9+) |

## Вимоги

- **JDK 25** (OpenJDK або сумісний дистрибутив)
- Підключення до інтернету для першого запуску (завантаження Gradle 8.14.5 та залежностей)

## Збірка та запуск

### Збірка fat jar

```bash
./gradlew shadowJar
```

Результат: `build/libs/javafxdemo.jar`

### Запуск

```bash
java -jar build/libs/javafxdemo.jar
```

> **JDK 25:** під час запуску можуть з'явитися попередження `WARNING: A restricted method in java.lang.System has been called` від JavaFX та SQLite JDBC. Це нормально — застосунок працює коректно. Щоб прибрати попередження:
> ```bash
> java --enable-native-access=ALL-UNNAMED -jar build/libs/javafxdemo.jar
> ```

### Запуск через Gradle (dev-режим)

```bash
./gradlew run
```

### Повна збірка (компіляція + тести + jar)

```bash
./gradlew build
```

## Структура проекту

```
src/
  ua/org/nalabs/javalessons/javafx/
    Main.java                   # Точка входу JavaFX Application
    Launcher.java               # Bootstrap-клас для fat jar
    controller/                 # FXML-контролери
    model/                      # Моделі даних (Person, NBUCurrency)
    repository/                 # Репозиторій SQLite
    util/                       # Утиліти (дати, валюти, JAXB-адаптери)
    view/                       # FXML-файли та CSS
resources/
  images/                       # Іконки застосунку
```

## Авторизація

Логін за замовчуванням:
- **Login:** `root`
- **Password:** `toor`

## Ініціалізація БД

Якщо база даних порожня — у меню **File → Fill DB** заповнити тестовими даними.

## Нотатки щодо сумісності

Застосунок використовує `javax.xml.bind` (JAXB), який був вилучений з JDK 9+. Бібліотека підключається через Gradle-залежність `javax.xml.bind:jaxb-api:2.3.1`.

JavaFX bundled у fat jar через Shadow plugin. При запуску `java -jar` модульна система JDK не потребує додаткових прапорців `--module-path`.
