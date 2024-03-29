[![codecov](https://codecov.io/gh/ivanmaleev/book-shop/branch/master/graph/badge.svg?token=JKA76NIOUV)](https://codecov.io/gh/ivanmaleev/book-shop)
====================================================
Проект: Book-shop
----------------------------------------------------
Проект "Книжный магазин".
Используемые технологии: String Boot, String MVC, String Data, String Security, REST, JWT, 
Hibernate, Redis, Liquibase, Mapstruct, Swagger, JUnit, Maven, PostgreSQL, Thymeleaf, Lombok.
----------------------------------------------------
Проект представляет собой сайт по покупке книг.
1) На главной странице расположены три раздела "Рекомендуемое", "Новинки", "Популярное". 
![ScreenShot](images/Screenshot_1.jpg)
2) С главной страницы можно перейти по ссылкам, расположенным вверху, 
   1) "Жанры", тут расположены жанры книг. Отсюда можно перейти к списку книг по различным жанрам.
   ![ScreenShot](images/Screenshot_2.jpg)
   2) "Новинки", новые книги. Здесь можно отфильтровать книги по дате издания.
   ![ScreenShot](images/Screenshot_3.jpg)
   3) "Популярное". Список популярных книг.
   ![ScreenShot](images/Screenshot_4.jpg)
   4) "Авторы", тут расположен список авторов книг. По ссылкам можно перейти к страницам авторов.
   ![ScreenShot](images/Screenshot_5.jpg)
3) На сайте доступна авторизация.  
   ![ScreenShot](images/Screenshot_6.jpg)
4) Авторизованному пользователю доступны страницы:
   1) "Профиль", страница с информацией о пользователе, история транзакций
   ![ScreenShot](images/Screenshot_7.jpg)
   2) "Мои книги", список купленных книг.
   ![ScreenShot](images/Screenshot_8.jpg)
   3) "Архив", книги, помещённые в личный архив.
   ![ScreenShot](images/Screenshot_9.jpg)
5) При переходе по ссылке любой книги открывается страница профиля книги. Здесь можно Отложить, 
Купить (книга попадает в корзину), Скачать книгу. Есть возможность Оценить книгу, проставив
количество звёзд. Также внизу страницы имеется список отзывов, для авторизованных пользователей имеется
возможность оставить отзыв, а также оценить отзыв.
   ![ScreenShot](images/Screenshot_10.jpg)
6) ![ScreenShot](images/Screenshot_11.jpg)
