# SELAB-Refactoring

## ریفکتور ها
برای شروع در راستای Separate Query From Modifier،دو متد getTemp و getDateAddress را به دو متد میشکنیم و هرجا متد اولی کال شده بود هردو متد رو باهم کال میکنیم.( دو مورد ریفکتور)
![image](https://github.com/mohammadhnz/SELAB-Refactoring/assets/59181719/211c24f1-3aad-49cd-8bdc-9cbad596cbf6)

سپس در راستای اینکه switch-case را از بین ببریم از Polymorphism استفاده میکنیم. به این صورت که سه نوع مختلف Action تعریف میکنیم.(یک مورد ریفکتور) در نتیجه داریم:
![image](https://github.com/mohammadhnz/SELAB-Refactoring/assets/59181719/1060d088-3be9-4847-a2d4-e14c03dea440)


سپس در قدم بعدی یک facade برای SemanticSymbol تعریف میکنیم تا symbolTable و کل پکیج را wrap کند تا از هیچ کجا، importای بغیر از این کلاس نداشته باشیم در این پکیچ.
![image](https://github.com/mohammadhnz/SELAB-Refactoring/assets/59181719/636378b3-aee0-4462-a7c1-ecba862cff15)

برای facade بعدی، برای کلاس CodeGenerator یک Facade تعریف میکنیم که وظیفه wrap کردن کلاس CodeGenerator را دارد.
![image](https://github.com/mohammadhnz/SELAB-Refactoring/assets/59181719/559566d4-2645-4f5c-af79-d83349a3ae83)

در قدم بعدی در parser، اکشن‌های مختلف پشت سر هم کال شده بودن داخل شرط if، در نتیجه اینارو میایم و extract method میکنیم:
![image](https://github.com/mohammadhnz/SELAB-Refactoring/assets/59181719/1eecf504-37c3-44ae-a948-a4622577921f)

همینطور یک کار دیگر در CodeGenerator این بود که پرینت ارور در همه‌جا انجام شده بود، یک method extract ساده انجام میدهیم تا این موضوع از کد جدا شود.
![image](https://github.com/mohammadhnz/SELAB-Refactoring/assets/59181719/24642dd6-a2fd-46bb-b6dc-d306d6b0afd2)

در قدم بعدی طبق اصل self-encapsulated-field برای متغییر های پارسر getter و setter تعریف میکنیم و سپس متد‌هایی که برای هندل actionها (reduce, shift, accept) وجود داشتند را hide delegate میکنیم و در نهایت هم یک کلاس ActionHandler ایجاد میکنیم:
![image](https://github.com/mohammadhnz/SELAB-Refactoring/assets/59181719/a355d0ff-c89a-45e0-8a9d-ef14a16ed598)

