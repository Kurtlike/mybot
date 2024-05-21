require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        a: Начнём.

    state: Hello
        intent!: /привет
        a: Привет привет

    state: GetWeather
        intent!: /weather
        script:
            var city = $caila.inflect($parseTree._geo, ["nomn"]);
            openWeatherMapCurrent("metric", "ru", city).then(function (res) {
                if (res && res.weather) {
                    $reactions.answer("Сегодня в городе " + capitalize(city) + " " + res.weather[0].description + ", " + Math.round(res.main.temp) + "°C" );
                    if(res.weather[0].main == 'Rain' || res.weather[0].main == 'Drizzle') {
                        $reactions.answer("Советую захватить с собой зонтик!")
                    } else if (Math.round(res.main.temp) < 0) {
                        $reactions.answer("Бррррр ну и мороз")
                    }
                } else {
                    $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду.");
                }
            }).catch(function (err) {
                $reactions.answer("Что-то сервер барахлит. Не могу узнать погоду.");
            });
        
    state: Currency
        intent!: /currency
        a: Пока пока
        
    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}
