<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <script type="text/javascript" src="js/lib/jquery.min.2.1.1.js"></script>
    <script type="text/javascript" src="js/calculator.js"></script>
    <link rel="stylesheet" href="styles/style.css" type="text/css">
    <title>Calculator</title>
</head>
<body
        data-root = "<%=request.getContextPath()%>"
        data-calc-list="CalcList_JSON.action"
        data-city-list="CityList_JSON.action"
        data-city-select="City_JSON.action"
        data-result="Result_JSON.action">

<h1>Привет</h1>

<!-- Главная таблица, в которую средствами js помещаются дополнительные
    таблицы, каждая из которых отвечает за один из концов измеряемого
    расстояния -->
<table id="main">
    <tbody>
    <tr id="input">
        <td colspan="2"><div id="from" data-label="Откуда"></div></td>
        <td colspan="2"><div id="to" data-label="Куда"></div></td>
    </tr>
    <tr>
        <td class="calc" >
            <!-- Список вычислителей -->
            <div class="calc-record">
                <p><span class="calc-record-description"></span></p>
            </div>
        </td>
        <!-- Главная большая кнопка -->
        <td><div id="submit">Рассчитать</div></td>
        <td colspan="2">
            <!-- Таблица результатов -->
            <table>
                <thead>
                <tr>
                    <th>Имя вычислителя</th>
                    <th>Результат</th>
                </tr>
                </thead>
                <tbody class="result">
                <tr class="result-record">
                    <td><div class="result-record-name"></div></td>
                    <td><div class="result-record-value"></div></td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
<!-- Таблица, описывающая сторону интерфейса -->
<div class="side" id="blank">
    <table>
        <caption>Таблица</caption>
        <thead>
        <tr>
            <th>Список городов</th>
            <th>Выбранный город</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                <div class="city">
                    <div class="city-record">
                        <p><span class="city-record-name"></span></p>
                    </div>
                </div>
            </td>
            <td>
                <table class="city-select">
                    <tbody>
                    <tr>
                        <td>Номер</td>
                        <td><div class="city-select-key" ></div></td>
                    </tr>
                    <tr>
                        <td>Название</td>
                        <td><div class="city-select-name" ></div></td>
                    </tr>
                    <tr>
                        <td>Широта</td>
                        <td><div class="city-select-latitude"></div></td>
                    </tr>
                    <tr>
                        <td>Долгота</td>
                        <td><div class="city-select-longitude"></div></td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>
