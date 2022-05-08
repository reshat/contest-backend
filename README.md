# contest-backend
Программа хранит данные в БД, предоставляет API для клиентской части приложения.
Основные функции программы:
Аутентификация на основе логина и пароля, в ответ система возращает JWT токен(дейвтсует 12 часов), который служит для авторизации пользователя.
В программе представлено две роли, студент и преподаватель.
Студент имеет возможность просматривать свои курсы, задания, отправлять на них решения, смотреть предыдущие попытки о оценки по заданиям.
Преподаватель может:
создавать курсы и задания,
создавать группы и добавлять в них студентов,
добавлять группы на курс, 
редактировать задания, 
оставлять комментарии к заданиям,
оценивать задания,
удалять комментарии.
Существует 3 типа заданий:
Тестовые задания с 1 или множественным ответом,
Задания на написание SQL кода, проверяемые автоматически,
Задания на написание SQL кода, проверяемые вручную
Для каждого типа задания существует отдельный запрос на добавление решения, причем у задания проверяемых автоматически есть возможность отправить решение не на оценку, а как попытку, тогда сервер вернет двумерный массив.
