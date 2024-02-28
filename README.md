
# JVM, JIT, GC

### ✨Немного теории

🛠️ **JVM (Java Virtual Machine)**

_JVM (Java Virtual Machine)_ - это виртуальная машина, которая исполняет Java-байткод. Она является основным компонентом _Java Runtime Environment (JRE)_ и _Java Development Kit (JDK)_.

Основные задачи JVM:
- **исполнение Java-байткода**: `JVM` преобразует Java-байткод, который создаётся в процессе компиляции Java и хранится в файлах `.class`, в машинный код, который выполняется непосредственно на целевой платформе;
- **управление памятью**: `JVM` управляет выделением и освобождением памяти для Java-приложений. Она отслеживает объекты, на которые больше нет ссылок, и освобождает память с помощью механизма сборки мусора;
- **управление потоками**: `JVM` управляет потоками выполнения Java-приложений, позволяя им работать параллельно и координировать свою работу;
- **другие задачи**: `JVM` также выполняет другие задачи, такие как загрузка классов, проверка безопасности, динамическое связывание и т.д.

⚡ **JIT (Just-In-Time Compiler)**

_JIT (Just-In-Time Compiler)_ - это компилятор, который компилирует Java-байткод в машинный код непосредственно во время выполнения программы. Он является частью `JVM` и предназначен для улучшения производительности выполнения Java-приложений. 

Основные задачи JIT:
- **анализ Java-байткода**: `JIT` анализирует Java-байткод и определяет, какие участки кода можно скомпилировать в машинный код;
- **oптимизация кода**: `JIT` выполняет различные оптимизации над скомпилированным кодом, чтобы улучшить его производительность, такие как инлайнинг, удаление ненужных инструкций, развертывание циклов и т.д.;
- **компиляция в машинный код**: `JIT` компилирует оптимизированный код в машинный код, который может быть непосредственно выполнен процессором.

🚮 **GC (Garbage Collector)**

_GC (Garbage Collector)_ - это механизм в `JVM`, который автоматически освобождает память, занимаемую объектами, на которые больше нет ссылок. Он отслеживает объекты, которые больше недоступны для программы, и возвращает соответствующую память обратно в пул памяти для последующего использования. 

**Основные задачи GC**:
- **определение "мёртвых" объектов**: `GC` определяет, какие объекты больше недоступны для программы, путём анализа ссылок на них из других объектов;
- **освобождение памяти**: `GC` освобождает память, занимаемую мёртвыми объектами, и возвращает её обратно в пул памяти для последующего использования;
- **оптимизация производительности**: современные `GC` реализации выполняют оптимизации, такие как параллельное выполнение, инкрементальная сборка мусора и компактизация памяти, чтобы уменьшить негативное влияние сборки мусора на производительность Java-приложений.

###
Несколько видов сборщиков мусора:
1. **Serial GC (также известный как Serial Collector)**:
- простой сборщик мусора, который использует один поток для выполнения сборки мусора;
- подходит для небольших и однопоточных приложений, где основной приоритет - минимизировать задержки и упростить реализацию;
- характеризуется низкими накладными расходами на сборку мусора, но может вызывать заметные задержки при выполнении сборки мусора на больших объёмах данных.
2. **Parallel GC (также известный как Throughput Collector)**:
- использует несколько потоков для выполнения сборки мусора параллельно;
- ориентирован на максимизацию пропускной способности приложения и минимизацию общего времени простоя при выполнении сборки мусора;
- подходит для многопоточных приложений с высокой нагрузкой, где приоритет - обеспечение высокой производительности за счёт параллельной обработки.
3. **G1 GC (Garbage-First Garbage Collector)**:
- был введен в Java 7 и стал основным сборщиком мусора в Java 9;
- использует разбиение кучи на несколько регионов и собирает мусор в этих регионах независимо друг от друга, чтобы минимизировать временные задержки;
- ориентирован на предоставление предсказуемых временных характеристик сборки мусора и подходит для приложений с большим объёмом данных и жёсткими требованиями к временным задержкам;
- обладает некоторыми функциями, например, автоматическим управлением размером кучи и управлением временными задержками с помощью параметров командной строки.

### 🚀Практика

В данной работе представлена реализация заданий, связанных с вышеописанной темой:
1. **`JIT`**: анализ запуска приложения с различными параметрами
2. **`GC`**: анализ данных jvisualvm для оценки работы разных видов `GC`

## Задание 1 JIT

Сделать цикл на 100000 итераций, в цикле в предварительно созданную Map<Integer, String> сложить ключ - индекс, значение - "value" + индекс 
1) Запустить с опцией **`-XX:+PrintCompilation`**, проанализировать информацию в консоли
2) Запустить с опцией **`-XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining`**, проанализировать информацию в консоли

## 🤔 Описание результатов

Для реализации данного задания был создан класс [**`Task1`**](https://github.com/MironovNikita/sber-homework15/blob/main/src/main/java/org/application/Task1.java). В нём происходит добавление объектов в HashMap "map" и затем удаление данных.

```java
public class Task1 {
    public static void main(String[] args) {
        System.out.println("Задание 1 - JIT");
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            map.put(i, "value" + i);
        }

        for (int i = 0; i < 100_000; i++) {
            map.remove(i);
        }
    }
}
```
Программа запускалась из командной строки Windows. Запуск осуществлялся с двумя параметрами:
1. **`-XX:+PrintCompilation`**
2. **`-XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining`**
Полные логи вы можете найти [**`здесь`**](https://github.com/MironovNikita/sber-homework15/tree/main/res/task1)

#### 1. Анализ запуска **`-XX:+PrintCompilation`**

Обозначения представленных команд:
**`-XX:+PrintCompilation`**.
Эта опция позволяет выводить информацию о компиляции методов `JVM`. Когда виртуальная машина Java компилирует методы в машинный код, она выводит информацию о процессе компиляции, такую как номер метода, имя класса, количество байт кода и так далее.

Компиляция метода **`put`** начинается в 3-ем инлайне. Фрагмент лога выглядит следующим образом:

| **Временная метка, мс** | **Идентификатор компиляции** | **Атрибуты** | **Уровень компиляции** |            **Название метода**           | **Размер метода в байтах (bytes)** | **Индикатор деоптимизации** |
|:-----------------------:|:----------------------------:|--------------|:----------------------:|:----------------------------------------:|:----------------------------------:|-----------------------------|
|            48           |              79              |              |            3           |         java.util.HashMap::putVal        |                 300                |                             |
|            49           |              80              |              |            3           |          java.util.HashMap::hash         |                 20                 |                             |
|            49           |              93              |              |            3           | java.util.HashMap$HashIterator::nextNode |                 100                |                             |
|            49           |              86              |              |            3           |        java.util.HashMap::getNode        |                 150                |                             |
|            50           |              82              |              |            3           |      java.util.HashMap$Node::<init>      |                 26                 |                             |
|            50           |              81              |              |            3           |        java.util.HashMap::newNode        |                 13                 |                             |
|            50           |              90              |              |            3           |  java.util.HashMap$HashIterator::hasNext |                 13                 |                             |
|            51           |              83              |              |            3           |   java.util.HashMap::afterNodeInsertion  |                  1                 |                             |
|            51           |              108             |              |            4           |        java.lang.String::hashCode        |                 60                 |                             |
|            51           |              94              |              |            3           |          java.util.HashMap::get          |                 19                 |                             |

Здесь:
1. _**Временная метка**_ - в миллисекундах с момента запуска приложения.
2. _**ID компиляции**_ - добавочный идентификатор для каждого скомпилированного метода.
3. _**Атрибуты**_ - состояние компиляции с пятью возможными значениями:
- **%** - произошла замена в стеке;
- **s** - метод синхронизирован;
- **!** - метод содержит обработчик исключений;
- **b** - компиляция происходила в блокирующем режиме;
- **n** - компиляция преобразовала оболочку в собственный метод.
4. _**Уровень компиляции**_ - имеет значение от 0 до 4.
5. _**Название метода**_ - имя скомпилированного метода.
6. _**Размер метода в байтах**_.
7. _**Индикатор деоптимизации**_ - имеет два значения:
- **made not entrant** - деоптимизация C1 или оптимистичные предположения компилятора оказались неверными;
- **made zombie** - механизм очистки для сборщика мусора, позволяющий освободить место в кэше кода.

Далее можно заметить, что методы получают уровень компиляции 4:

| **Временная метка, мс** | **Добавочный идентификатор** | **Атрибуты** | **Уровень компиляции** |                  **Название метода**                  | **Размер метода в байтах (bytes)** | **Индикатор деоптимизации** |
|:-----------------------:|:----------------------------:|--------------|:----------------------:|:-----------------------------------------------------:|:----------------------------------:|-----------------------------|
|            56           |              119             |              |            4           |                java.util.HashMap::hash                |                 20                 |                             |
|            56           |              118             |              |            4           |         java.util.HashMap::afterNodeInsertion         |                  1                 |                             |
|            57           |              109             |              |            3           |     java.util.ImmutableCollections$SetN::contains     |                 26                 |                             |
|            57           |              126             |              |            3           |    java.util.concurrent.ConcurrentHashMap::casTabAt   |                 21                 |                             |
|            57           |              89              |              |            3           |    java.util.ImmutableCollections$Set12$1::hasNext    |                 13                 |                             |
|            58           |              87              |              |            1           | java.lang.module.ModuleDescriptor$Requires::modifiers |                  5                 |                             |
|            58           |              83              |              |            3           |         java.util.HashMap::afterNodeInsertion         |                  1                 |       made not entrant      |
|            58           |              80              |              |            3           |                java.util.HashMap::hash                |                 20                 |       made not entrant      |
|            59           |              132             |              |            4           |               java.util.HashMap::putVal               |                 300                |                             |
|            59           |              92              |              |            3           |                 java.util.HashSet::add                |                 20                 |                             |
|            59           |              99              |              |            3           |          java.util.HashMap$KeyIterator::next          |                  8                 |                             |
|            60           |              133             |              |            4           |               java.util.HashMap::newNode              |                 13                 |                             |

Компиляция методов, связанных с удалением значений происходит в конце программы:

| **Временная метка, мс** | **Добавочный идентификатор** | **Атрибуты** | **Уровень компиляции** |         **Название метода**         | **Размер метода в байтах (bytes)** | **Индикатор деоптимизации** |
|:-----------------------:|:----------------------------:|--------------|:----------------------:|:-----------------------------------:|:----------------------------------:|-----------------------------|
|           755           |             1447             |              |            4           |    java.util.HashMap::removeNode    |                 291                |                             |
|           755           |             1448             |              |            4           | java.util.HashMap::afterNodeRemoval |                  1                 |                             |
|           756           |             1449             |              |            3           |      java.util.HashMap::remove      |                 26                 |                             |
|           756           |             1450             |              |            3           |      java.lang.Integer::equals      |                 29                 |                             |
|           757           |             1451             |       %      |            3           |   org.application.Task1::main @ 49  |                 73                 |                             |
|           757           |             1452             |              |            1           | java.util.HashMap::afterNodeRemoval |                  1                 |                             |
|           758           |             1453             |              |            3           |    java.util.HashMap::removeNode    |                 291                |                             |
|           759           |             1454             |              |            3           |      java.util.HashMap::remove      |                 26                 |                             |
|           759           |             1455             |              |            4           |      java.lang.Integer::equals      |                 29                 |                             |
|           759           |             1448             |              |            3           | java.util.HashMap::afterNodeRemoval |                  1                 |       made not entrant      |
|           760           |             1450             |              |            3           |      java.lang.Integer::equals      |                 29                 |       made not entrant      |
|           761           |             1451             |       %      |            4           |   org.application.Task1::main @ 49  |                 73                 |       made not entrant      |

У метода **`org.application.Task1::main`** также указано смещение **`@ 49`**. Это означает, что эта конкретная часть кода находится на 49-м байте внутри метода **`main`**. Смещения в байт-коде используются для определения расположения различных инструкций внутри метода в `Java Virtual Machine (JVM)`. Это помогает `JVM` понимать, какие инструкции нужно выполнить и в каком порядке.

Подробный лог находится [**`здесь`**](https://github.com/MironovNikita/sber-homework15/blob/main/res/task1/-XX%2BPrintCompilation.txt)

#### 2. Анализ запуска **`-XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining`**

Обозначения представленных команд:
1. **`-XX:+PrintCompilation`**
Эта опция позволяет выводить информацию о компиляции методов `JVM`. Когда виртуальная машина Java компилирует методы в машинный код, она выводит информацию о процессе компиляции, такую как номер метода, имя класса, количество байт кода и так далее.

2. **`-XX:+UnlockDiagnosticVMOptions`**
Эта опция разрешает использование дополнительных диагностических опций в `JVM`. Она открывает доступ к некоторым дополнительным функциям и опциям, которые обычно не доступны в обычном режиме работы.

3. **`-XX:+PrintInlining`**
Эта опция используется для вывода информации о встраивании методов. Встроенные методы - это процесс, при котором `JVM` встраивает вызов метода непосредственно в вызывающий метод, минуя сам вызов метода. Это может улучшить производительность за счёт уменьшения накладных расходов на вызов методов.

Фрагмент лога выглядит следующим образом:

| **Временная метка, мс** | **Добавочный идентификатор** | **Атрибуты** | **Уровень компиляции** |                           **Название метода**                          | **Размер метода в байтах (bytes)** |    **Дополнительная информация**    |
|:-----------------------:|:----------------------------:|:------------:|:----------------------:|:----------------------------------------------------------------------:|:----------------------------------:|:-----------------------------------:|
|            74           |              75              |              |            2           |                        java.util.HashMap::putVal                       |                 300                |                                     |
|                         |                              |              |                        |                      @ 5 java.lang.String::<init>                      |                  7                 |                inline               |
|                         |                              |              |                        |                     @ 20 java.util.HashMap::resize                     |                 356                |         callee is too large         |
|                         |                              |              |                        |                      @ 3 java.lang.String::<init>                      |                 99                 |         callee is too large         |
|                         |                              |              |                        |                      @ 23 java.util.HashMap::hash                      |                 20                 |                inline               |
|                         |                              |              |                        | @ 28 java.lang.invoke.MethodHandleStatics::newIllegalArgumentException |                  9                 |                inline               |
|                         |                              |              |                        |                     @ 9 java.lang.Object::hashCode                     |                  0                 |          no static binding          |
|                         |                              |              |                        |             @ 5 java.lang.IllegalArgumentException::<init>             |                  6                 | don't inline Throwable constructors |
|                         |                              |              |                        |                     @ 56 java.util.HashMap::newNode                    |                 13                 |          no static binding          |
|            78           |              115             |              |            3           |             java.util.concurrent.ConcurrentHashMap::putVal             |                 432                |          no static binding          |
|                         |              85              |              |            3           |                 java.util.HashMap$HashIterator::hasNext                |                 13                 |                                     |
|                         |              125             |              |            4           |         java.util.ImmutableCollections$SetN$SetNIterator::next         |                 90                 |                                     |
|                         |                              |              |                        |                      @ 63 java.lang.Object::equals                     |                 11                 |          no static binding          |

Таким образом, благодаря применению данных опций, можно получить дополнительную информацию о компилируемых методах. В примере, указанном выше:
- **`no static binding`** - сообщение указывает на то, что для этого вызова не удалось выполнить статическое связывание, возможно, потому что метод вызывается через динамическое определение.
- **`inline`** - сообщение указывает на то, что метод был успешно встроен (инлайнен) в вызывающий метод. Инлайнинг - это процесс встраивания кода вызываемого метода непосредственно в код вызывающего метода.
- **`callee is too large`** - сообщение указывает на то, что метод, который вызывается внутри текущего метода (т.е. "callee"), слишком велик по размеру для инлайнинга. Это означает, что размер вызываемого метода превышает определенный порог, и поэтому компилятор решил не встраивать этот вызов.
- **`don't inline Throwable constructors`** - сообщение указывает на то, что конструкторы класса `Throwable` (например, исключения) не должны встраиваться в вызывающий метод. Это предостережение о том, что вызов конструктора исключения не должен быть встроен в другой метод, возможно, потому что это может вызвать неожиданное поведение или нарушить общую структуру обработки исключений.

Подробный лог находится [**`здесь`**](https://github.com/MironovNikita/sber-homework15/blob/main/res/task1/-XX%2BPrintCompilation%20-XX%2BUnlockDiagnosticVMOptions%20-XX%2BPrintInlining.txt)

## Задание 2

Из `%JAVA_HOME%\bin` запустить `jvisualvm`, установить через пункт меню `Tools\Plugins\Available Plugis` плагин: `Visual GC`.
Запустить приложение, создающее много объектов с разными `GC`, посмотреть в `jvisualvm`, как заполняются объекты в разных областях памяти(heap).

## 🤨 Описание результатов

Для реализации данного задания были проведены запуски для разных GC:
- **`Serial GC`**;
- **`Parallel GC`**;
- **`G1 GC`**.

Сама логика программы, с которой будут работать GC, описана в классе [**`Task2`**](https://github.com/MironovNikita/sber-homework15/blob/main/src/main/java/org/application/Task2.java).

```java
while (true) {
            printGCMenu();
            int command = checkIntInput(scanner);
            switch (command) {
                case 1 -> {
                    for (int i = 0; i <= 10_000_000; i++) {
                        longs.add(i);
                    }
                }
                case 2 -> {
                    longs = new ArrayList<>();
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Извините, такая команда отсутствует :с");
            }
        }
```

VisualVM имеет несколько графиков, позволяющих отслеживать использование памяти в приложении:
1. 🌿 **Eden Space (Eden)**:
- это область кучи, где создаются новые объекты Java;
- объекты создаются в Eden Space, и если они переживают один или несколько циклов сборки мусора, они могут быть перемещены в `Survivor Spaces` или непосредственно в `Old Generation`;
- визуализация этой области позволяет отслеживать создание новых объектов и их временные характеристики.
2. ⛺️ **Survivor Space (Survivor 0 и Survivor 1)``:
- это области кучи, используемые для перемещения объектов, которые пережили некоторое количество циклов сборки мусора в `Eden Space`;
- обычно есть два `Survivor Spaces` (называемых Survivor 0 и Survivor 1), между которыми происходит переключение во время сборки мусора;
- объекты, которые переживают один или несколько циклов сборки мусора в `Survivor Spaces`, могут быть перемещены в `Old Generation`.
3. 🏰 **Old Generation (Old Gen)**:
- это область кучи, где хранятся старые и долгоживущие объекты Java;
- cборка мусора в `Old Generation` обычно более интенсивная и происходит реже, чем в других областях кучи.
4. 🔍 **Metaspace**:
- это область памяти, используемая для хранения метаданных классов, методов и других структур данных `JVM`;
- в отличие от старого пространства (`PermGen`), которое использовалось в более ранних версиях `JVM`, `Metaspace` не имеет фиксированного размера и может динамически расширяться;
- визуализация `Metaspace` позволяет отслеживать использование памяти для классов, загруженных в вашем приложении, а также общее использование метаспейса `JVM`.

Для работы приложения с **`Serial GC`** была вызвана команда **`java -XX:+UseSerialGC Task2.java`**.

![serialGC](https://github.com/MironovNikita/sber-homework15/blob/main/res/task2/serialGC.gif)

При запуске приложения **`Serial GC`** разбивает кучу на три области: `Eden Space`, `Survivor Space` и `Old Generation`. `Eden Space` предназначен для новых объектов, а `Survivor Space` и `Old Generation` используются для долгоживущих объектов. **`Serial GC`** чаще всего используется на однопроцессорных или небольших системах, где производительность не является критическим фактором.

Для работы приложения с **`Parallel GC`** была вызвана команда **`java -XX:+UseParallelGC Task2.java`**.

![parallelGC](https://github.com/MironovNikita/sber-homework15/blob/main/res/task2/parallelGC.gif)

Как и **`Serial GC`**, **`Parallel GC`** разделяет кучу на три области: `Eden Space`, `Survivor Space` и `Old Generation`. `Eden Space` используется для новых объектов, а `Survivor Space` и `Old Generation` - для долгоживущих объектов. **`Parallel GC`** обычно используется в средних и крупных приложениях, где скорость и производительность имеют большое значение, и небольшие периоды простоя могут быть допустимы для выполнения сборки мусора.

Для работы приложения с **`G1 GC`** была вызвана команда **`java -XX:+UseG1GC Task2.java`**.

![G1GC](https://github.com/MironovNikita/sber-homework15/blob/main/res/task2/G1GC.gif)

Куча в **`G1 GC`** разделена на несколько равных по размеру регионов. Каждый регион может быть как `Eden`, `Survivor`, так и `Old Generation` в зависимости от потребностей сборки мусора. Он выбирает регионы с наибольшим количеством мусора для сборки в первую очередь. **`G1 GC`** автоматически адаптирует размеры регионов и частоту сборки мусора в зависимости от характеристик приложения и доступной памяти. Данный сборщик обеспечивает баланс между производительностью и предсказуемостью сборки мусора, что делает его хорошим выбором для средних и крупных Java-приложений, особенно тех, где важно минимизировать время простоя приложения.

### 💡 Примечание

В [**`pom.xml`**](https://github.com/MironovNikita/sber-homework15/blob/main/pom.xml) данного задания не добавлялись зависимости в блоке *properties /properties*:

Результат сборки текущего проекта:

```java
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.370 s
[INFO] Finished at: 2024-02-28T21:32:59+03:00
[INFO] ------------------------------------------------------------------------
```