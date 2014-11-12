$(function() {
    /**
     * Функция загружает информацию с сервера. Формирует запрос вида
     * имя_приложения/действие[?[[параметр=значение]&...]&[[имя_массива=значение]&...]]
     * @param name имя действия, для получения информации
     * @param params параметры типа {}. Может содержать простые поля и массивы
     * @param func функция типа function(data, status). Обрабатывает
     * полученную с сервера информацию
     */
    var load = function() {
        var body = $("body");
        var root = body.attr("data-root") + "/";
        var prefix = body.attr("data-prefix");
        var getParams = function(params) {
            var result = "";
            for (var p in params) {
                if(params.hasOwnProperty(p)) {
                    if (params[p].hasOwnProperty("length")) {
                        for (var i=0; i<params[p].length; i++) {
                            result += "&"+p+"="+params[p][i];
                        }
                    }
                    else {
                        result += "&"+p+"="+params[p];
                    }
                }
            }
            return result.substring(1,result.length);
        };
        return function (name, params, func) {
            params = getParams(params);
            var url = root + body.attr("data-" + name)+
                prefix + (params.length>0 ? "?" : "") + params;
            console.info("=========>"+name+"["+url+"]");
            $.getJSON(url, {}, func);
        }
    }();

    var Model = function() {
        var views = [];
        return {
            notifyAll : function() {
                for (var i=0; i<views.length; i++) {
                    if (views.hasOwnProperty(i)) {
                        console.info(this.toString()+ " ==> "+views[i].toString());
                        views[i].notify(this, i);
                    }
                }
            },
            addView : function(view) {
                views[views.length] = view;
            },
            removeView : function(i) {
                delete views[i];
            }
        };
    }

    var CityFieldModel = function(field) {
        var model = Model();
        model.notify = function() {
            model.notifyAll();
        }
        model.getValue = function() {
            return field();
        }
        model.toString = function() {
            return "CityFieldModel";
        }
        return model;
    };

    var CitySelectorModel = function(newIterator) {
        var model = Model();
        var city = {};
        var phi = CityFieldModel(function() {
            return city.latitude;
        });
        var lambda = CityFieldModel(function() {
            return city.longitude;
        });
        model.addView(lambda);
        model.addView(phi);
        model.notify = function(model) {
            var p = model.iterator(this);
            if (p.hasNext()) {
                p.next().select();
            }
        }
        model.setKey = function(key) {
            load("city", {"key" : key}, function(data, status) {
                city = data;
                model.notifyAll();
            });
        }
        model.getKey = function() {
            return city.key;
        }
        model.getPhi = function() {
            return phi;
        }
        model.getLambda = function() {
            return lambda;
        }
        model.toString = function() {
            return "CitySelectorModel";
        }
        return model;
    };

    var CityListModel = function() {
        var model = Model();
        var list = null;
        var cities = {};
        model.iterator = function(selector) {
            var i = 0;
            return {
                hasNext : function() {
                    return i<list.length;
                },
                next : function() {
                    var item = list[i++];
                    return {
                        getName : function() {
                            return item.name;
                        },
                        select : function() {
                            return selector.setKey(item.key);
                        }
                    }
                }
            }
        }
        model.newSelector = function(label) {
            var city = CitySelectorModel()
            model.addView(city);
            return cities[label] = city;
        }
        model.init = function() {
            load("city-list", {}, function(data, status) {
                list = data;
                model.notifyAll();
            });
        }
        model.getParams = function() {
            var result = {};
            for (var l in cities)
                if(cities.hasOwnProperty(l))
                    result[l] = cities[l].getKey();
            return result;
        }
        model.toString = function() {
            return "CityListModel";
        }
        return model;
    }

    var CalculatorListModel = function() {
        var model = Model();
        var list = [];
        var available = [];

        var Item = function(index, obj) {
            return {
                getName : function() {
                    return obj.name;
                },
                getShortName : function() {
                    return obj.shortName;
                },
                getIndex : function() {
                    return index;
                }
            };
        }

        var find = function(i) {
            for (;i<available.length; i++)
                if (available[i])
                    return i;
        };

        model.iterator = function(selector) {
            var index = find(0);
            var hasNext2 = function() {
                return index < available.length;
            }
            var next2 = function() {
                console.info(index);
                var old = index;
                index = find(index+1);
                return {
                    getName : function() {
                        return list[old].shortName;
                    },
                    select : function() {
                        available[old] = false;
                        available[selector.getIndex()] = true;
                        selector.setState(Item(old, list[old]));
                        console.info("select="+old);
                        model.notifyAll();
                    }
                };
            }
            var change = function(that, hasNext2, next2) {
                that.hasNext = hasNext2;
                that.next = next2;
            }
            return {
                hasNext : function() { return true; },
                next : function() {
                    change(this, hasNext2, next2);
                    return {
                        getName : function() {
                            return list[selector.getIndex()].shortName;
                        },
                        select : function() {}
                    }
                }
            };
        };
        model.newCalculator = function() {
            var index = find(0);
            if (index<list.length) {
                available[index] = false;
                var selector=CalculatorSelectorModel(Item(index, list[index]));
                model.notifyAll();
                return selector;
            }
            return null;
        }
        model.removeCalculator = function(calc) {
            available[calc.getIndex()] = true;
            calc.remove();
            model.notifyAll();
        }
        model.init = function() {
            load("calc-list", {}, function(data, status) {
                available=[];
                list = data;
                for (var i=0; i<list.length; i++) {
                    available[i] = true;
                }
                model.notifyAll();
            });
        }
        model.getParams = function() {
            var result = [];
            for (var i=0; i<available.length; i++)
                if(!available[i])
                    result[result.length] = list[i].name;
            return {names : result};
        }
        model.toString = function() {
            return "CalculatorListModel";
        }
        return model;
    };

    var CalculatorSelectorModel = function(item) {
        var model = Model();
        var result;
        var remove = false;
        model.setResult = function(r) {
            result = r;
            result.addView(this);
        }
        model.setState = function(i) {
            item = i;
            model.notifyAll();
        }
        model.notify = function(m, i) {
            if (remove) {
                m.removeView(i)
            }
            else {
                model.notifyAll();
            }
        }
        model.getIndex = function() {
            return item.getIndex();
        }
        model.getName = function() {
            return item.getName();
        }
        model.getShortName = function() {
            return item.getShortName();
        }
        model.getValue = function() {
            return result.getResult(item.getName());
        }
        model.toString = function() {
            return "CalculatorSelectorModel";
        }
        model.remove = function() {
            remove = true;
        }
        return model;
    }

    var ResultModel = function(list) {
        var model = Model();
        var result = {};
        model.getResult = function(name) {
            if (result.hasOwnProperty(name))
                return result[name];
            else
                return "";
        };
        model.load = function() {
            if (list.length>0) {
                var params = list[0].getParams();
                for (var i=1; i<list.length; i++) {
                    var next = list[i].getParams();
                    for (var p in next) {
                        if (next.hasOwnProperty(p))
                            params[p] = next[p];
                    }
                }
                load("result", params, function(data, status) {
                    result = data;
                    model.notifyAll();
                });
            }
        };
        model.toString = function() {
            return "ResultModel";
        }
        return model;
    }

    var SelectorView = function(select, selector, updateOnClick) {
        var option = $("option",select).clone();
        var list = [];
        var updateList = function(model) {
            var iterator = model.iterator(selector);
            select.children().remove();
            list = [];
            while(iterator.hasNext()) {
                var item = iterator.next();
                var next = option.clone();
                next.html(item.getName());
                list[list.length] = item;
                select.append(next);
            }
        }
        select.change(function() {
            list[select[0].selectedIndex].select();
        });
        var view = {
            toString : function() {
                return "SelectorView";
            }
        };
        if (updateOnClick) {
            var model = null;
            select.mousedown(function() {
                console.info("mouse model="+(model===null ? "null" : "!null"));
                if (model !== null) {
                    updateList(model);
                    model = null;
                }
            });
            view.notify = function(m) {
                model = m;
            }
            var next = option.clone();
            select.children().remove();
            next.html(selector.getShortName());
            select.append(next);
            return view;
        }
        else {
            view.notify = function(model) {
                updateList(model);
            }
            return view;
        }
    }

    var FieldView = function(field) {
        return {
            notify : function(model) {
                field.html(model.getValue());
            },
            toString : function() {
                return "FieldView";
            }
        }
    }

    var CalculatorListView = function(record, calcs, result) {
        var parent = record.parent();
        $("td", record).hide();
        var action = function(record) {
            var func = function() {
                console.info("append");
                var calc = calcs.newCalculator();
                if (calc != null) {
                    calc.setResult(result);
                    func = function() {
                        console.info("remove");
                        record.remove();
                        calcs.removeCalculator(calc);
                    };
                    var next = record.clone();

                    calc.addView(FieldView($(".result", record)));
                    calc.notifyAll();
                    var selector = SelectorView($("select", record),calc,true);
                    calcs.addView(selector);
                    selector.notify(calcs);
                    $("td", record).show();
                    $("input", record).attr("value", "-");
                    parent.append(next);
                    $("input", next).click(action(next));
                }
            }
            return function() { return func(); }
        };
        $("input", record).click(action(record));
    }

    var FormView = function(form, size) {
        form.hide();
        var count = 0;
        return {
            notify : function(model, i) {
                model.removeView(i);
                if (++count>=size) {
                    form.show();
                }
            },
            toString : function() {
                return "FormView";
            }
        }
    }

    var end = {
        notify : function() { },
        toString : function() { return "end"; }
    }

    var cities = CityListModel();

    var mainForm = FormView($("#main"), 2);

    $(".city").each(function() {
        var that = $(this);
        var label = that.attr("id");
        var selector = cities.newSelector(label);

        selector.getPhi().addView(FieldView($(".phi", that)));
        selector.getLambda().addView(FieldView($(".lambda", that)));
        selector.addView(mainForm);
        cities.addView(SelectorView($("select", that), selector, false));

        selector.addView(end);
    });
    cities.addView(end);

    var calcs = CalculatorListModel();
    calcs.addView(end);

    var result = ResultModel([cities, calcs]);

    CalculatorListView($(".calculator"), calcs, result);

    $("#calc").click(function() {
        result.load();
    })

    cities.init();
    calcs.init();

});