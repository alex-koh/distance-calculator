var Factory = function() {
    var that = {};
    /**
     * Функция выбирает данные из структуры, полученной с сервера и помещает
     * их в узлы html-элемента, помеченные специальными классами
     * (поля корневого узла).
     * @param record узел, поля которого следует заполнить
     * @param name имя свойства из структуры, возращаемой с сервера, в состав
     * которой входят целевые данные
     * @returns {{}}
     * @constructor
     */
    that.Setter = function(record, name) {
        var prefix = record.attr("class")+"-";
        console.info(prefix);
        var that = {};
        // В этой переменной хранятся ссылки на все заполняемы поля и действия,
        // которые необходимо провести для их заполнения
        var map={};
        var match = new RegExp("^(?:"+prefix+"([a-z]+))$");
        // Составление списка целевых полей
        // По умолчанию для всех узлов информация просто помещается в html
        record.find("[class^='"+prefix+"']").each(function() {
            var clazz = $(this).attr("class");
            map[match.exec(clazz)[1]] = {
                self : function(record) {
                    return record.find("."+clazz);
                },
                func : function(data) {
                    this.html(data);
                }
            };
        });
        /**
         * Добавление действия. Функцие действию в качестве параметра this будет
         * корневой узел, содержащий заполняемые поля.
         * @param key ключ, по которому ищется в информация в структуре,
         * полученной с сервера
         * @param func действие
         */
        that.setAction = function(key, func) {
            map[key] = {
                self : function(record) {
                    return record;
                },
                func : func
            };
        };
        /**
         * Функция содержит логику по заполнению заполнения полей корневого узла
         * @param record корневой узел (или его копия)
         * @param data целевая информация
         */
        that.setBlank = function(record, data) {
            var prop;
            for (var key in map) {
                if (map.hasOwnProperty(key) && data.hasOwnProperty(key)) {
                    prop = map[key];
                    prop.func.apply(prop.self(record), [data[key]]);
                }
            }
        };
        /**
         * Функция получает данные от загрузчика.
         * @param data JSON-объект, полученный с сервера
         */
        that.send = function(data) {
            if (data.hasOwnProperty(name)) {
                console.info(this.toString());
                this.setBlank(record, data[name]);
            }
            else {
                throw Error();
            }
        };
        that.toString = function() {
            return "calculator.Setter";
        };
        return that;
    };
    /**
     * Функция обрабатывает массивы полученные с сервера. Она создает новые
     * элементы списка и заполняет их данными из элементов массива.
     * @param out список (перед началом работы функция очищает список)
     * @param name имя поля в JSON-объекте, хранящего массив
     * @returns {{}}
     * @constructor
     */
    that.ListSetter = function(out, name) {
        var factory = this;
        console.info(out.attr("class"));
        var record = out.find("."+out.attr("class")+"-record");
        var that = factory.Setter(record.clone(true), name);
        var super_setBlank = that.setBlank;
        /**
         * Переопределяет функцию в классе Setter
         * @param record прототип записи
         * @param data данные от сервера
         */
        that.setBlank = function(record, data) {
            var aRecord;
            out.children().remove();
            for (var i = 0; i < data.length; i++) {
                aRecord = record.clone(true);
                super_setBlank(aRecord, data[i]);
                out.append(aRecord);
            }
        };
        that.toString=function() {
            return "calculator.ListSetter";
        };
        return that;
    };
    /**
     * Функция создает обработчик результатов, возвращаемых сервером
     * @param cfg Массив состоит из двух элементов для левого и правого списка
     * городов. Каждый элемент содержит загрузчик города для данного
     * списка и целевой узел, куда следует вывести список.
     * @param name имя свойства в структуре, возвращаемой с сервера
     * @returns {Function} функция, выполняющая обработку информации,
     *  полученной от сервера.
     * @constructor
     */
    that.ListHandler = function(cfg, name) {
        var out = [];
        var setter = [];
        var selected = {};
        var that = {};
        var factory = this;
        for (var i=0; i<cfg.length; i++) {
            var s = factory.ListSetter(cfg[i].out, name);
            s.setAction("key", function(loader, label) {
                return function(key) {
                    this.click(function() {
                        selected[label] = key;
                        loader({'key' : key});
                    });
                }
            }(cfg[i].loader, cfg[i].label));
            out.push(cfg[i].out);
            setter.push(s);
        }
        /**
         Функция принимает данные от сервера и создает список городов
         @param data данные получаемые от сервера
         */
        that.send = function(data) {
            for (var i=0; i<cfg.length; i++) {
                setter[i].send(data);
                out[i].children().first().click();
            }
        };
        that.getSelected = function() {
            return selected;
        };
        return that;
    };
    /**
     * Обработчик массива вычислителей, полученных с сервера. Создает
     * список и собирает информацию о выбранных вычислителях.
     * @param out список вычислителей
     * @param name имя свойства в JSON-объекте
     * @returns {{}}
     * @constructor
     */
    that.CalcList = function(out, name) {
        var factory = this;
        var that = factory.ListSetter(out, name);
        var map = {};
        that.setAction("name", function(name) {
            map[name] = false;
            this.click(function() {
                console.info("click name="+name);
                map[name] = !map[name];
            });
        });
        /**
         * Возвращает список выбранных вычислителей
         * @returns {Array}
         */
        that.getSelected = function() {
            var list = [];
            for(var key in map) {
                if (map.hasOwnProperty(key) && map[key])
                    list.push(key);
            }
            return list;
        };
        return that;
    };
    /**
     * Функция возвращает обработчик отвечающий за создание и
     * настройку правой или левой стороны интерфейса
     * @param blank обернутый набор исходного прототипа стороны интерфейса
     * @param visitor посетитель в котором сохраняется информация для
     * последующей загрузки списка городов
     * @returns {Function} обработчик
     * @constructor
     */
    that.BlankHandler = function (blank , visitor) {
        var blank0 = blank.clone(true);
        blank.remove();
        return function(i,e) {
            var that = $(this);
            var blank = blank0.clone(true);
            visitor.add(that.attr("id"),
                blank.find(".city-select"),
                blank.find(".city"));
            // Добавление результатов на страницу
            blank.removeAttr("id");
            blank.find("caption").html(that.attr("data-label"));
            that.append(blank);
        }
    };
    /**
     * Посетитель собирающий информацию необходимую для создания списка городов
     * @param cfg объект содержащий функцию загрузки внешних параметров и
     *  функцию загрузки данных с сервера
     *  @param name имя свойства в JSON-объекте
     * @returns {{}} обработчик
     * @constructor
     */
    that.Visitor = function (cfg, name) {
        var listConfig = [];
        var that = {};
        var cityUrl = cfg.getProperty("city-select");
        var factory = this;
        that.add = function(label, city, list) {
            var handler = factory.Setter(city, name);
            var record = {};
            record.label = label;
            record.loader = function (data) {
                cfg.loader(cityUrl, data, handler);
            };
            record.out = list;
            listConfig.push(record);
        };
        that.getResult = function() {
            return listConfig;
        };
        return that;
    };
    /**
     * Обработчик нажатия кнопки.
     * @param cfg системные функции
     * @param listHandler обработчик списков городов
     * @param calcList обработчик списка вычислителей
     * @param resultHandler обработчик списка результатов
     * @returns {Function}
     * @constructor
     */
    that.SubmitHandler=function(cfg, listHandler, calcList, resultHandler) {
        var url = cfg.getProperty("result");
        return function() {
            var data = listHandler.getSelected();
            var params = "";
            for(var key in data) {
                if (data.hasOwnProperty(key)) {
                    params += key + "=" + data[key] + "&";
                }
            }
            var names = calcList.getSelected();
            if (names.length>0) {
                params+="names="+names[0];
                for (var i = 1; i < names.length; i++) {
                    params += "&names=" + names[i];
                }
                cfg.loader(url, params, resultHandler);
            }
            console.info(params);
        }
    };
    that.toString=function() {
        return "calculator.Factory";
    };
    return that;
};
/**
 * Главная функция проекта
 */
$(function() {
    var factory = Factory();
    var cfg = {
        getProperty : function() {
            var body = $("body");
            var root = body.attr("data-root") + "/";
            return function (name) {
                return root + body.attr("data-" + name);
            }
        }(),
        loader : function(url, data, handler) {
            console.info(url);
            $.getJSON(url, data, function(data, status) {
                handler.send(data);
            });
        }
    };
    var blank = $("#blank");
    var visitor = factory.Visitor(cfg, "city");
    var handler = factory.BlankHandler(blank, visitor);
    $("#input").find("div").each(handler);

    var listHandler =factory.ListHandler(visitor.getResult(), "list");
    cfg.loader(cfg.getProperty("city-list"), {}, listHandler);

    var calcList = factory.CalcList($(".calc"), "list");
    cfg.loader(cfg.getProperty("calc-list"),{}, calcList);

    var resultHandler = factory.ListSetter($(".result"), "result");

    $("#submit").click(factory.SubmitHandler(cfg, listHandler, calcList, resultHandler));
});