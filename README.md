### Опыт и навыки:  
>Как такого опыта в промышленной разработки не было. Из сложных технических проектов могу выделить диплом.Задача заключалась в разработке Web-сайта научно-технической конференции, благодаря которому пользователь (1-ая роль) мог просмотреть всю необходимую ему информацию о конференции или подать статью на конференцию, а рецензент (2-ая роль) оценить подаваемые работы. <br><br> Проект состоял из более 20 HTML-страниц, каждая со своей стилизацией и функционалом и имел следующий стек: Java, JavaScript, Oracle Database, HTML, CSS,  Spring фреймворк. <br><br> Во время разработки возникали сложности с файлами (хранение и предоставление клиенту на скачивание/просмотр), а также некоторые моменты с интерфейсом страниц (которые можно было подглядеть в интернете :D) и изучаемым фреймворком Spring.<br><br>Помимо этого, большую часть времени занимался самоизучением: увлекался парсингом данных с сайтов, программировал различных Telegram-боты и разрабатывал информационно-поисковые системы.  

### Цель курса:  
>В рамках курса хотелось бы подтянуть знания в языке, углубиться в некоторые моменты (например с безопасностью данных, быстрыми алгоритмами) и при возможности пробиться в компанию на трудоустройство. 

### Какой бы курсовой проект хотел бы реализовать:  
  >Пока что нет мыслей.

### Основные команды git:  
1. git init - инициализация (создание) в директории нового репозиторация Git в виде директории .git. Позволяет преобразовать проект без управления версиями в репозиторий Git или инициализировать новый пустой репозиторий. Без инициализации репозитория большинство команд Git невозможно использовать;  
2. git clone [ссылка на удалённый репозиторий] - копирование существующего удалённого репозитория Git. В директории, откуда была запущена команда git clone, создаётся директория с именем репозитория, в которую копируется сам репозиторий, все его ветки и коммиты;  
3. git add [название файла] - добавление изменённого файла в индекс для последующего коммита. Для индексирования сразу всех изменённых файлов используется на месте названия файла символ '.' (точка);  
4. git commit [ключи] - создаёт новый коммит, содержащий текущее содержимое индекса и заданное сообщение журнала с описанием изменений. Можно выделить следующие ключи:
	+ git commit -a - совершит коммит, автоматически индексируя изменения в файлах проекта, при этом новые файлы проиндексированы не будут. Позволяет пропустить git add перед коммитом;  
	+ git commit -m [комментарий] - позволяет написать комментарий вместе с командой, не открывая редактор. Пример: git commit -m "Пофиксил баг";  
	+ git --amend - позволяет заменить последний коммит новым. Бывает полезно в случае неправильного ввода сообщения. Пример git commit --amend -m "Фича добавлена";  
5. git push [репозиторий] [ветка] - вносит изменения (коммиты) в удалённый репозиторий (удалённую ветку). Для отправки всех зафиксированных изменений в ветках используется ключ --all, а для отправки текущей ветки - HEAD. Пример: git push origin master - добавление изменений из локальной ветки мастер в удалённый репозиторий origin.
6. git pull [репозиторий] [ветка] - забирает новые изменения (коммитов) из удалённой ветки репозитория и сливает их с изменениями в текущей ветке, где находимся при выполнении команды. Данная команда это комбинация git fetch + git merge. Пример: git pull origin master - получит данные с удалённой ветки master репозитория origin и произведёт слияние с текущей веткой.  