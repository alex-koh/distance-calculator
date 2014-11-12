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
        data-calc-list="CalcList"
        data-city-list="CityList"
        data-city="City"
        data-result="Result"
        data-prefix="_JSON.action"
        >

<!-- Главная таблица, в которую средствами js помещаются дополнительные
    таблицы, каждая из которых отвечает за один из концов измеряемого
    расстояния -->
<table id="main">
    <tr>
        <th>Город отправления</th>
        <td>
            <table id="from" class="city">
                <tr>
                    <th>
                        <select>
                            <option value="empty">[empty]</option>
                        </select>
                    </th>
                    <td class="phi text">10</td>
                    <td class="dim">&deg; с.ш.</td>
                    <td class="lambda text">10</td>
                    <td class="dim">&deg; в.д.</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <th>Город прибытия</th>
        <td>
            <table id="to" class="city">
                <tr>
                    <th>
                        <select>
                            <option value="empty">[empty]</option>
                        </select>
                    </th>
                    <td class="phi text">10</td>
                    <td class="dim">&deg; с.ш.</td>
                    <td class="lambda text">10</td>
                    <td class="dim">&deg; в.д.</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <th>Вычислители</th>
        <td>
            <table>
                <tr class="calculator">
                    <th><input type="submit" value="+"></th>
                    <td>
                        <select>
                            <option value="empty">[empty]</option>
                        </select>
                    </td>
                    <td class="result text">result</td>
                    <td class="dim">км</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <th></th>
        <td><input id="calc" type="submit" value="Рассчитать"></td>
    </tr>
</table>
<a id="admin" href="<%=request.getContextPath()%>/Admin.action">Страница администратора</a>
</body>
</html>
