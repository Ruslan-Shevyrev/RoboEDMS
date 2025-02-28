# RoboEDMS

ETL bot for transferring data from delo-web information system to EDMS68 Information System.

## Инструкция пользователя робота для выгрузки документов из ИС «ДЕЛО-WEB».

Установка производится копированием попки RoboEDMS в любое удобное место на жестком диске.

### Предварительная настройка в ОС Windows.

1. Открыть файл StartRobotEDMS.cmd любым доступным текстовым редактором (Блокнот, Notepad++).

***ВНИМАНИЕ не открывать текстовым процессором(Microsoft office Word, WordPad, LibreOffice).***

Внутренняя структура файла выглядит как показано ниже

```
@echo off
chcp 1251
SET pathToJar=%~dp0
SET edmsUser=
SET edmsPassword=
SET visibalityBrowser=false
SET isLogging=false
SET isDeleteMessage=false
java -jar %pathToJar%roboEDMS.jar "%edmsUser%" "%edmsPassword%" "%visibalityBrowser%" "%isLogging%" "%isDeleteMessage%"
pause
```

3. В строке **SET edmsPassword=** необходимо прописать пароль пользователя ИС «ДЕЛО-WEB» без кавычек
Например: **SET edmsPassword=1**

4. Строка **SET visibalityBrowser=false** предназначена для отображения браузера при работе. Эта строчка используется для диагностики ошибок и не рекомендуется к изменению.

5. Строка **SET isLogging=false** предназначена для ведения логов при работе. Эта строчка используется для диагностики ошибок и не рекомендуется к изменению.

6. Строка **isDeleteMessage=false** предназначена для удаления сообщений после обработки. Эта строчка используется для тестирования работы  и не рекомендуется к изменению.

***ВНИМАНИЕ  При параметре  isDeleteMessage=false кнопка  «ОТВЕТ» будет неактивна.***

### Запуск программы в ОС Windows.

Для запуска программы необходимо запустить файл **startWin.vbs**.
Появится окно авторизации, в которое необходимо ввести логин и пароль пользователя ИС «ЭДДО».
В случае удачной авторизации появится основное окно программы, в которой над кнопкой «СТАРТ» будет гореть сообщение о состояние подключения.
Если в сообщение написано - «Соединение установлено», то можно начинать работу. В противном случае необходимо перезайти в программу.



<table>
	<tr>
		<td valign="center" width="49%"><img src="https://github.com/Ruslan-Shevyrev/Ruslan-Shevyrev/blob/main/logoRS/logo_mini.gif" title="logo"></td>
		<td valign="center" width="49%"><img src="https://github.com/Ruslan-Shevyrev/Ruslan-Shevyrev/blob/main/logoRS/logoRS_FULL.png" title="RuslanShevyrev"></td>
	</tr>
</table>