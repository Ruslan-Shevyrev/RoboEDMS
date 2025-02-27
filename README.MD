# RoboEDMS

ETL bot for transferring data from delo-web information system to EDMS68 Information System.

## Инструкция пользователя робота для выгрузки документов из ИС «ДЕЛО-WEB».

Установка производится копированием попки RoboEDMS в любое удобное место на жестком диске.

### Предварительная настройка в ОС Windows.

1. Открыть файл StartRobotEDMS.cmd любым доступным текстовым редактором (Блокнот, Notepad++).

**ВНИМАНИЕ не открывать текстовым процессором(Microsoft office Word, WordPad, LibreOffice).**

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

<table>
	<tr>
		<td valign="center" width="49%"><img src="https://github.com/Ruslan-Shevyrev/Ruslan-Shevyrev/blob/main/logoRS/logo_mini.gif" title="logo"></td>
		<td valign="center" width="49%"><img src="https://github.com/Ruslan-Shevyrev/Ruslan-Shevyrev/blob/main/logoRS/logoRS_FULL.png" title="RuslanShevyrev"></td>
	</tr>
</table>