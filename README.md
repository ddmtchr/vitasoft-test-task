### Технологии

- Spring Boot 2.7.18 (Java 11)
- Spring Data 2.7.18 (Hibernate)
- Авторизация по JWT (Spring Security)
- База данных PostgreSQL
- Docker

### Наполнение данными

Пользователи:
- username: `John`, password: `qwerty`
- username: `Paul`, password: `123456`
- username: `Andrew`, password: `asdfgh`

### Запуск в IDEA

- Установить переменную окружения `DATABASE_URL`
- ИЛИ раскомментировать строку в `application.properties` и закомментировать строку с переменной окружения

### Запуск в Docker

- Запустить Docker Daemon
- Выполнить команду `docker-compose up --build` из корня проекта
- Приложение доступно на `http://localhost:8080/`